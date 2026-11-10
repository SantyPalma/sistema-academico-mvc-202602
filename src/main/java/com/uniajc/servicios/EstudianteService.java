package com.uniajc.servicios;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.uniajc.dao.EstudianteDao;
import com.uniajc.modelo.Estudiante;

public class EstudianteService {

    private static final Pattern NOMBRE_PATTERN = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,100}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final EstudianteDao estudianteDao;

    public EstudianteService(EstudianteDao estudianteDao) {
        this.estudianteDao = estudianteDao;
    }

    public void registrarEstudiante(Estudiante estudiante) {
        validarEstudiante(estudiante);

        Optional<Estudiante> existente = estudianteDao.buscarPorEmail(estudiante.getEmail());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un estudiante registrado con ese email.");
        }

        estudianteDao.guardar(estudiante);
    }

    public List<Estudiante> obtenerTodosLosEstudiantes() {
        return estudianteDao.obtenerTodos();
    }

    public Optional<Estudiante> buscarEstudiantePorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0.");
        }
        return estudianteDao.buscarPorId(id);
    }

    public Optional<Estudiante> buscarEstudiantePorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }
        return estudianteDao.buscarPorEmail(email.trim());
    }

    public void actualizarEstudiante(Estudiante estudiante) {
        if (estudiante == null || estudiante.getId() <= 0) {
            throw new IllegalArgumentException("Estudiante inválido.");
        }

        Optional<Estudiante> existente = estudianteDao.buscarPorId(estudiante.getId());
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Estudiante no encontrado.");
        }

        validarEstudiante(estudiante);

        Optional<Estudiante> porEmail = estudianteDao.buscarPorEmail(estudiante.getEmail());
        if (porEmail.isPresent() && porEmail.get().getId() != estudiante.getId()) {
            throw new IllegalArgumentException("El email ya está en uso por otro estudiante.");
        }

        estudianteDao.actualizar(estudiante);
    }

    public void eliminarEstudiante(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }

        Optional<Estudiante> existente = estudianteDao.buscarPorId(id);
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Estudiante no encontrado.");
        }

        estudianteDao.eliminar(id);
    }

    private void validarEstudiante(Estudiante estudiante) {
        if (estudiante == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo.");
        }
        if (estudiante.getNombre() == null || estudiante.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (!NOMBRE_PATTERN.matcher(estudiante.getNombre().trim()).matches()) {
            throw new IllegalArgumentException("El formato del nombre es inválido.");
        }
        if (estudiante.getApellido() == null || estudiante.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es obligatorio.");
        }
        if (!NOMBRE_PATTERN.matcher(estudiante.getApellido().trim()).matches()) {
            throw new IllegalArgumentException("El formato del apellido es inválido.");
        }
        if (estudiante.getEmail() == null || estudiante.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }
        if (!EMAIL_PATTERN.matcher(estudiante.getEmail().trim()).matches()) {
            throw new IllegalArgumentException("El formato del email es inválido.");
        }
    }
}
