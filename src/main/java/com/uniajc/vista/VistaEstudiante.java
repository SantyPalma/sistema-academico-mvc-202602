package com.uniajc.vista;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.uniajc.controlador.ControladorEstudiante;
import com.uniajc.modelo.Estudiante;

public class VistaEstudiante {

    private final ControladorEstudiante controlador;
    private final Scanner scanner;

    public VistaEstudiante() {
        this.controlador = new ControladorEstudiante();
        this.scanner = new Scanner(System.in);
    }

    public VistaEstudiante(ControladorEstudiante controlador, Scanner scanner) {
        this.controlador = controlador;
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        int opcion = -1;
        do {
            System.out.println("\n=== GESTIÓN DE ESTUDIANTES ===");
            System.out.println("1. Registrar estudiante");
            System.out.println("2. Listar todos los estudiantes");
            System.out.println("3. Buscar estudiante por ID");
            System.out.println("4. Actualizar estudiante");
            System.out.println("5. Eliminar estudiante");
            System.out.println("0. Volver / Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
                switch (opcion) {
                    case 1 -> registrarEstudiante();
                    case 2 -> listarEstudiantes();
                    case 3 -> buscarPorId();
                    case 4 -> actualizarEstudiante();
                    case 5 -> eliminarEstudiante();
                    case 0 -> System.out.println("Saliendo del módulo de estudiantes...");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido.");
            }
        } while (opcion != 0);
    }

    private void registrarEstudiante() {
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine().trim();
            System.out.print("Apellido: ");
            String apellido = scanner.nextLine().trim();
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();

            controlador.registrarEstudiante(nombre, apellido, email);
            System.out.println("Estudiante registrado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al registrar: " + e.getMessage());
        }
    }

    private void listarEstudiantes() {
        try {
            List<Estudiante> estudiantes = controlador.obtenerTodosLosEstudiantes();
            if (estudiantes.isEmpty()) {
                System.out.println("No hay estudiantes registrados.");
            } else {
                System.out.println("\n--- Lista de Estudiantes ---");
                for (Estudiante e : estudiantes) {
                    System.out.printf("ID: %d | Nombre: %s %s | Email: %s%n",
                            e.getId(), e.getNombre(), e.getApellido(), e.getEmail());
                }
            }
        } catch (Exception e) {
            System.out.println("Error al listar: " + e.getMessage());
        }
    }

    private void buscarPorId() {
        try {
            System.out.print("Ingrese el ID del estudiante: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            Optional<Estudiante> estudiante = controlador.buscarEstudiantePorId(id);
            if (estudiante.isPresent()) {
                Estudiante e = estudiante.get();
                System.out.printf("Encontrado -> ID: %d | Nombre: %s %s | Email: %s%n",
                        e.getId(), e.getNombre(), e.getApellido(), e.getEmail());
            } else {
                System.out.println("No se encontró ningún estudiante con ID: " + id);
            }
        } catch (Exception e) {
            System.out.println("Error al buscar: " + e.getMessage());
        }
    }

    private void actualizarEstudiante() {
        try {
            System.out.print("ID del estudiante a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Nuevo Nombre: ");
            String nombre = scanner.nextLine().trim();
            System.out.print("Nuevo Apellido: ");
            String apellido = scanner.nextLine().trim();
            System.out.print("Nuevo Email: ");
            String email = scanner.nextLine().trim();

            controlador.actualizarEstudiante(id, nombre, apellido, email);
            System.out.println("Estudiante actualizado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al actualizar: " + e.getMessage());
        }
    }

    private void eliminarEstudiante() {
        try {
            System.out.print("ID del estudiante a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            controlador.eliminarEstudiante(id);
            System.out.println("Estudiante eliminado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }
}
