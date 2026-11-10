package com.uniajc.controlador;

import java.util.List;
import java.util.Optional;

import com.uniajc.dao.EstudianteDao;
import com.uniajc.modelo.Estudiante;
import com.uniajc.servicios.EstudianteService;

public class ControladorEstudiante {

    private final EstudianteService estudianteService;

    public ControladorEstudiante() {
        this.estudianteService = new EstudianteService(new EstudianteDao());
    }

    public ControladorEstudiante(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    public void registrarEstudiante(String nombre, String apellido, String email) {
        Estudiante estudiante = new Estudiante(nombre, apellido, email);
        estudianteService.registrarEstudiante(estudiante);
    }

    public void registrarEstudiante(Estudiante estudiante) {
        estudianteService.registrarEstudiante(estudiante);
    }

    public List<Estudiante> obtenerTodosLosEstudiantes() {
        return estudianteService.obtenerTodosLosEstudiantes();
    }

    public Optional<Estudiante> buscarEstudiantePorId(int id) {
        return estudianteService.buscarEstudiantePorId(id);
    }

    public void actualizarEstudiante(int id, String nombre, String apellido, String email) {
        Estudiante estudiante = new Estudiante(id, nombre, apellido, email);
        estudianteService.actualizarEstudiante(estudiante);
    }

    public void actualizarEstudiante(Estudiante estudiante) {
        estudianteService.actualizarEstudiante(estudiante);
    }

    public void eliminarEstudiante(int id) {
        estudianteService.eliminarEstudiante(id);
    }
}
