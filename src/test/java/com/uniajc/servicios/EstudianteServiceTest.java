package com.uniajc.servicios;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uniajc.dao.EstudianteDao;
import com.uniajc.modelo.Estudiante;

class EstudianteServiceTest {

    private EstudianteService estudianteService;
    private FakeEstudianteDao fakeDao;

    static class FakeEstudianteDao extends EstudianteDao {
        private final List<Estudiante> storage = new ArrayList<>();
        private int idSeq = 1;

        @Override
        public void guardar(Estudiante estudiante) {
            estudiante.setId(idSeq++);
            storage.add(new Estudiante(estudiante.getId(), estudiante.getNombre(), estudiante.getApellido(), estudiante.getEmail()));
        }

        @Override
        public List<Estudiante> obtenerTodos() {
            return new ArrayList<>(storage);
        }

        @Override
        public Optional<Estudiante> buscarPorId(int id) {
            return storage.stream().filter(e -> e.getId() == id).findFirst();
        }

        @Override
        public Optional<Estudiante> buscarPorEmail(String email) {
            return storage.stream().filter(e -> e.getEmail().equalsIgnoreCase(email)).findFirst();
        }

        @Override
        public void actualizar(Estudiante estudiante) {
            for (int i = 0; i < storage.size(); i++) {
                if (storage.get(i).getId() == estudiante.getId()) {
                    storage.set(i, new Estudiante(estudiante.getId(), estudiante.getNombre(), estudiante.getApellido(), estudiante.getEmail()));
                    return;
                }
            }
            throw new RuntimeException("No encontrado");
        }

        @Override
        public void eliminar(int id) {
            storage.removeIf(e -> e.getId() == id);
        }
    }

    @BeforeEach
    void setUp() {
        fakeDao = new FakeEstudianteDao();
        estudianteService = new EstudianteService(fakeDao);
    }

    @Test
    void registrarEstudianteValido() {
        Estudiante est = new Estudiante("Juan", "Perez", "juan.perez@uniajc.edu.co");
        estudianteService.registrarEstudiante(est);

        List<Estudiante> todos = estudianteService.obtenerTodosLosEstudiantes();
        assertEquals(1, todos.size());
        assertEquals("Juan", todos.get(0).getNombre());
    }

    @Test
    void registrarEstudianteEmailInvalidoLanzaExcepcion() {
        Estudiante est = new Estudiante("Juan", "Perez", "correo-invalido");
        assertThrows(IllegalArgumentException.class, () -> estudianteService.registrarEstudiante(est));
    }

    @Test
    void registrarEstudianteEmailDuplicadoLanzaExcepcion() {
        estudianteService.registrarEstudiante(new Estudiante("Juan", "Perez", "juan@uniajc.edu.co"));
        Estudiante duplicado = new Estudiante("Pedro", "Gomez", "juan@uniajc.edu.co");
        assertThrows(IllegalArgumentException.class, () -> estudianteService.registrarEstudiante(duplicado));
    }

    @Test
    void actualizarYliminarEstudiante() {
        Estudiante est = new Estudiante("Ana", "Lopez", "ana@uniajc.edu.co");
        estudianteService.registrarEstudiante(est);

        Estudiante actualizado = new Estudiante(est.getId(), "Ana Maria", "Lopez", "ana@uniajc.edu.co");
        estudianteService.actualizarEstudiante(actualizado);

        Optional<Estudiante> buscado = estudianteService.buscarEstudiantePorId(est.getId());
        assertTrue(buscado.isPresent());
        assertEquals("Ana Maria", buscado.get().getNombre());

        estudianteService.eliminarEstudiante(est.getId());
        assertTrue(estudianteService.buscarEstudiantePorId(est.getId()).isEmpty());
    }
}
