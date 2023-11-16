
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.NoSuchElementException;

public class Driver {
    public static ArrayList<Usuario> usuarios = null;
    public static Usuario account = null;
    public static Scanner scanner;
    public static DatabaseConnector db;
    public static ArrayList<Proyecto> proyectos = null;

    public static void main(String[] args) throws Exception {
        db = new DatabaseConnector();
        reloadUsers();
        reloadProjects();
        scanner = new Scanner(System.in);
        boolean loginCicle = true;

        while (loginCicle) {
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Salir");
            System.out.println("Ingrese la opción que desea: ");
            int opt = scanner.nextInt();

            switch (opt) {
                case 1:
                    try {
                        scanner.nextLine();
                        System.out.println("Ingrese su correo electrónico");
                        String email = scanner.nextLine();
                        System.out.println("Ingrese su contraseña");
                        String password = scanner.nextLine();
                        account = loginUser(email, password);
                        if (account != null) {
                            if (account.getTipo().equals("ESTUDIANTE")) {
                                studenstMenu();
                            } else if (account.getTipo().equals("MAESTRO")) {
                                profesorsMenu();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    System.out.println("Saliendo del programa");
                    loginCicle = false;
                    break;
                default:
                    System.out.println("Opción no válida");
                    break;
            }
        }
    }
    
    public static void studenstMenu() throws Exception {
        boolean continuar = true;

        while (continuar) {
            System.out.println("===================== MENU ESTUDIANTE =====================");
            System.out.println("1. Ver proyectos");
            System.out.println("2. Crear Proyecto");
            System.out.println("3. Cerrar proyecto");
            System.out.println("4. Salir");
            System.out.print("Elija una opción: ");
            int opt = scanner.nextInt();

            switch (opt) {
                case 1:
                    ArrayList<Proyecto> filtrados = filtrarProyectosPorUsuarioLogueado();
                    int count = filtrados.size();
                    if (count > 0) {
                        if (count == 1) {
                            Proyecto selectedProject = filtrados.get(0);
                            showProjectMenuStudents(selectedProject); 
                        } else {
                            for (int i = 0; i < count; i++) {
                                System.out.println(i + ". " + filtrados.get(i).getNombre());
                            }
                            
                            System.out.print("Seleccione un proyecto (0-" + (count - 1) + "): ");
                            int projectChoice = scanner.nextInt();
                    
                            if (projectChoice >= 0 && projectChoice < count) {
                                Proyecto selectedProject = filtrados.get(projectChoice);
                                showProjectMenuStudents(selectedProject);
                            } else {
                                System.out.println("Selección no válida.");
                            }
                        }
                    } else {
                        System.out.println("No hay proyectos disponibles.");
                    }
                    break;
                case 2:
                    createProject();
                    break;
                case 3:
                    closeProjects(db);                    
                    break;
                case 4:
                    System.out.println("Saliendo del menú estudiante.");
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción no válida");
                    break;
            }
        }
    }



public static void profesorsMenu() {
        boolean continuar = true;
    
        while (continuar) {
            System.out.println("================================= MENU MAESTRO =================================");
            System.out.println("1. Ver Proyectos");
            System.out.println("2. Crear Proyectos");
            System.out.println("3. Cerrar proyecto");
            System.out.println("4. Crear nuevo usuario");
            System.out.println("5. Salir");
            System.out.print("Elija una opción: ");
            int opt = scanner.nextInt();
    
            switch (opt) {
                case 1:
                    ArrayList<Proyecto> filtrados = filtrarProyectosPorUsuarioLogueado();
                    int count = filtrados.size();
                    if (count > 0) {
                        if (count == 1) {
                            Proyecto selectedProject = filtrados.get(0);
                            showProjectMenuProfesor(selectedProject); 
                        } else {
                            for (int i = 0; i < count; i++) {
                                System.out.println(i + ". " + filtrados.get(i).getNombre());
                            }
                            
                            System.out.print("Seleccione un proyecto (0-" + (count - 1) + "): ");
                            int projectChoice = scanner.nextInt();
                    
                            if (projectChoice >= 0 && projectChoice < count) {
                                Proyecto selectedProject = filtrados.get(projectChoice);
                                showProjectMenuProfesor(selectedProject);
                            } else {
                                System.out.println("Selección no válida.");
                            }
                        }
                    } else {
                        System.out.println("No hay proyectos disponibles.");
                    }
                    break;
            
                case 2:
                    createProject();
                    break;
                case 3:
                    closeProjects(db);                    
                    break;
                case 4:
                    System.out.println("==================== CREACIÓN DE USUARIO =====================");
                    try {
                        scanner.nextLine();
                        System.out.print("Nombre: ");
                        String nombre = scanner.nextLine();
                        System.out.print("Usuario: ");
                        String usuario = scanner.nextLine();
                        System.out.print("Contraseña: ");
                        String password = scanner.nextLine();
                        System.out.print("Apellido: ");
                        String apellido = scanner.nextLine();
                        System.out.print("Correo Electrónico: ");
                        String mail = scanner.nextLine();
                        System.out.print("Sede: ");
                        String sede = scanner.nextLine();
                        System.out.println("1.Estudiante ");
                        System.out.println("2.Maestro ");
                        System.out.print("Tipo: ");
                        int sel1 = scanner.nextInt();
                        String tipo = (sel1 == 1) ? "ESTUDIANTE" : "MAESTRO";

                        RegisterUser(nombre, usuario, password, apellido, mail, sede, tipo);
                        System.out.println("Usuario creado con éxito!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    System.out.println("Saliendo del menú Maestro.");
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción no válida");
                    break;
            }
        }
    }
    
    public static void showProjectsProfesor() {
        System.out.println("Proyectos como Maestro:");
        ArrayList<Proyecto> proyectosFiltrados = filtrarProyectosPorUsuarioLogueado();
    
        if (proyectosFiltrados.isEmpty()) {
            System.out.println("No tienes proyectos como maestro.");
            return;
        }
    
        for (int i = 0; i < proyectosFiltrados.size(); i++) {
            System.out.println((i + 1) + ". " + proyectosFiltrados.get(i).getNombre());
        }
    
        System.out.print("Seleccione un proyecto (número): ");
        int projectChoice = scanner.nextInt();
        scanner.nextLine();
    
        if (projectChoice >= 1 && projectChoice <= proyectosFiltrados.size()) {
            Proyecto selectedProject = proyectosFiltrados.get(projectChoice - 1);
            showProjectMenuProfesor(selectedProject);
        } else {
            System.out.println("Selección no válida.");
        }
    }
    
    public static void showStudentsProjects() throws Exception {
        System.out.println("Proyectos en los que está inscrito el estudiante:");
        ArrayList<Proyecto> proyectosFiltrados = filtrarProyectosPorUsuarioLogueado();
    
        if (proyectosFiltrados.isEmpty()) {
            System.out.println("No estás inscrito en ningún proyecto.");
            return;
        }
    
        for (int i = 0; i < proyectosFiltrados.size(); i++) {
            System.out.println((i + 1) + ". " + proyectosFiltrados.get(i).getNombre());
        }
    
        System.out.print("Seleccione un proyecto (número): ");
        int projectChoice = scanner.nextInt();
        scanner.nextLine();
    
        if (projectChoice >= 1 && projectChoice <= proyectosFiltrados.size()) {
            Proyecto selectedProject = proyectosFiltrados.get(projectChoice - 1);
            showProjectMenuStudents(selectedProject);
        } else {
            System.out.println("Selección no válida.");
        }
    }
    
    public static void showProjectMenuStudents(Proyecto proyecto) throws Exception {
        while (true) {
            System.out.println("\nProyecto: " + proyecto.getNombre());
            System.out.println("Menú del proyecto:");
            System.out.println("1. Ver tareas");
            System.out.println("2. Crear tareas");
            System.out.println("3. Agregar miembros al proyecto");
            System.out.println("4. Chat del proyecto");
            System.out.println("5. Cerrar tarea");
            System.out.println("6. Regresar al menú anterior");
    
            System.out.print("Seleccione una opción: ");
            int option = scanner.nextInt();
            scanner.nextLine();
    
            switch (option) {
                case 1:
                    showTasks(proyecto);
                    break;
                case 2:
                    createTask(proyecto.getId());                    
                    break;
                case 3:
                    // Agregar miembro
                    //newMember(proyecto);
                    break;
                case 4:
                    // Acceder al chat del proyecto
                    //accessProjectChat(proyecto);
                    break;
                case 5:
                    closeTasks(proyecto);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Selección no válida.");
                    break;
            }
        }
    }

    public static void showClosedProjectMenuStudents(Proyecto proyecto) throws Exception {
        while (true) {
            System.out.println("\nProyecto: " + proyecto.getNombre());
            System.out.println("Menú del proyecto:");
            System.out.println("1. Ver tareas");
            System.out.println("2. Chat del proyecto");
            System.out.println("3. Regresar al menú anterior");
            System.out.print("Seleccione una opción: ");
            int option = scanner.nextInt();
            scanner.nextLine();
    
            switch (option) {
                case 1:
                    showTasks(proyecto);
                    break;
                case 2:
                    // Acceder al chat del proyecto
                    //accessProjectChat(proyecto);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Selección no válida.");
                    break;
            }
        }
    }


    public static void showProjectMenuProfesor(Proyecto proyecto) {
        try {
            while (true) {
                System.out.println("\nProyecto: " + proyecto.getNombre());
                System.out.println("Menú del proyecto:");
                System.out.println("1. Ver tareas");
                System.out.println("2. Chat del proyecto");
                System.out.println("3. Calificar proyecto");
                System.out.println("4. Cerrar proyecto");
                System.out.println("5. Regresar al menú anterior");
    
                System.out.print("Seleccione una opción: ");
                int option = scanner.nextInt();
                scanner.nextLine();
    
                switch (option) {
                    case 1:
                        try {
                            showTasks(proyecto);
                        } catch (Exception e) {
                            System.err.println("Error al mostrar las tareas: " + e.getMessage());
                        }
                        break;
                    case 2:
                        try {
                            // Acceder al chat del proyecto
                            //accessProjectChat(proyecto);
                        } catch (Exception e) {
                            System.err.println("Error al acceder al chat del proyecto: " + e.getMessage());
                        }
                        break;
                    case 3:
                        try {
                            gradeProject(proyecto);
                        } catch (Exception e) {
                            System.err.println("Error al calificar el proyecto: " + e.getMessage());
                        }
                        break;
                    case 4:
                        try {
                            closeProjects(db);
                        } catch (Exception e) {
                            System.err.println("Error al cerrar el proyecto: " + e.getMessage());
                        }
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Selección no válida.");
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error general en el menú del proyecto: " + e.getMessage());
        } finally {
            
        }
    }

    public static void gradeProject(Proyecto proyecto) {
    

    System.out.print("Ingrese la calificación para el proyecto '" + proyecto.getNombre() + "': ");
    double calificacion = scanner.nextDouble();

    try {
        db.actualizarCalificacionProyecto(proyecto.getId(), calificacion);
        proyecto.setCalificacion(calificacion); // Actualizar la calificación en el objeto Proyecto
        System.out.println("Proyecto calificado exitosamente: " + proyecto.getNombre());
    } catch (SQLException e) {
        System.err.println("Error al actualizar la calificación del proyecto: " + e.getMessage());
    } finally {
        
        }
    }

    public static Usuario loginUser(String email, String password) throws Exception {
        for (Usuario usuario : usuarios) {
            if (usuario.getMail().equals(email) && usuario.getPassword().equals(password)) {
                return usuario;
            }
        }
        throw new Exception("Usuario con estas credenciales no encontrado");
    }

    public static void RegisterUser(String nombre, String usuario, String password, String apellido, String mail, String sede, String tipo) throws Exception {
        db.registrarUsuario(nombre, usuario, password, apellido, mail, sede, tipo);
        reloadUsers();
    }

    public static void reloadUsers() throws Exception {
        usuarios = db.getAllUsuariosInfo();
    }

    public static void createProject() {
        scanner.nextLine();
        System.out.println("========== CREACIÓN DE PROYECTO ==========");
        System.out.print("Nombre del proyecto: ");
        String nombre = scanner.nextLine();
        System.out.print("Descripción del proyecto: ");
        String descripcion = scanner.nextLine();
    
        LocalDate fechaInicio = LocalDate.now();
    
        LocalDate fechaFin = null;
    
        int idLider = (account.getTipo().equals("ESTUDIANTE")) ? account.getIdusuario() : -1;
        int idMaestro = -1;
    
        if (account.getTipo().equals("ESTUDIANTE")) {
            idLider = account.getIdusuario();
            System.out.println("Seleccione un maestro para el proyecto:");
    
            try {
                for (int i = 0; i < usuarios.size(); i++) {
                    if (usuarios.get(i) instanceof Maestro) {
                        System.out.println((i + 1) + ". " + usuarios.get(i).getNombre());
                    }
                }
                System.out.print("Ingrese el número del maestro: ");
                int maestroIndex = scanner.nextInt() - 1;
    
                if (maestroIndex >= 0) {
                    idMaestro = usuarios.get(maestroIndex).getIdusuario();
                } else {
                    System.out.println("Número de maestro no válido.");
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        } else if (account.getTipo().equals("MAESTRO")) {
            idMaestro = account.getIdusuario();
        }
    
        try {
            db.registrarProyecto(nombre, descripcion, fechaInicio, fechaFin, idLider, idMaestro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTask(int idProyecto) throws Exception {
        
        try {
            System.out.println("========== CREACIÓN DE TAREA ==========");
            System.out.print("Nombre de la tarea: ");
            String tareaNombre = scanner.nextLine();
            System.out.print("Descripción de la tarea: ");
            String tareaDescripcion = scanner.nextLine();
    
            ArrayList<Usuario> usuarios = db.getAllUsuariosInfo();
            System.out.println("IDs de los estudiantes disponibles:");
            for (Usuario usuario : usuarios) {
                if (usuario instanceof Estudiante) {
                    System.out.println("ID: " + usuario.getIdusuario() + " - Nombre: " + usuario.getNombre());
                }
            }
    
            System.out.print("Ingrese el ID del estudiante al que desea asignar la tarea: ");
            int estudianteId = scanner.nextInt();
    
            Estudiante estudianteAsignado = findEstudianteById(estudianteId);
    
            if (estudianteAsignado != null) {
                LocalDate fechaInicio = LocalDate.now();
                db.insertarTarea(tareaNombre, tareaDescripcion, fechaInicio, idProyecto, estudianteId);
                System.out.println("Tarea asignada con éxito.");
                reloadProjects();
            } else {
                System.out.println("Estudiante no encontrado con el ID proporcionado.");
            }
        } catch (NoSuchElementException e) {
            System.err.println("Entrada no válida. Asegúrate de ingresar valores numéricos válidos para el ID del estudiante.");
        } finally {
            
        }
    }
    

    public static void reloadProjects()throws Exception{
        proyectos = db.getAllProjects();
    }

    public static Estudiante findEstudianteById(int estudianteId) {
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Estudiante && usuario.getIdusuario() == estudianteId) {
                return (Estudiante) usuario;
            }
        }
        return null;
    }
    

    public static ArrayList<Proyecto> filtrarProyectosPorUsuarioLogueado() {
        ArrayList<Proyecto> proyectosFiltrados = new ArrayList<>();

        for (Proyecto proyecto : proyectos) {
            boolean esLider = proyecto.getLiderProyecto().getIdusuario() == account.getIdusuario();
            boolean esMaestroAsociado = proyecto.getMaestroAsociado().getIdusuario() == account.getIdusuario();
            boolean esMiembro = proyecto.getEstudiantes().stream().anyMatch(estudiante -> estudiante.getIdusuario() == account.getIdusuario());

            if (esLider || esMaestroAsociado || esMiembro) {
                proyectosFiltrados.add(proyecto);
            }
        }

        return proyectosFiltrados;
    }


    public static void showTasks(Proyecto proyecto) throws Exception {
        ArrayList<Tarea> tareasDelProyecto = proyecto.getTareas();
    
        if (tareasDelProyecto.isEmpty()) {
            System.out.println("No hay tareas asignadas a este proyecto.");
        } else {
            System.out.println("Listado de tareas para el proyecto: " + proyecto.getNombre());
    
            System.out.println("Tareas completadas:");
            for (Tarea tarea : tareasDelProyecto) {
                if (tarea.getFechaFin() != null) {
                    System.out.println("- " + tarea.getNombre() + " - " + tarea.getDescripcion());
                }
            }
    
            System.out.println("Tareas sin completar:");
            for (Tarea tarea : tareasDelProyecto) {
                if (tarea.getFechaFin() == null) {
                    System.out.println("- " + tarea.getNombre() + " - " + tarea.getDescripcion());
                }
            }
        }
    }
    

    public static void closeTasks(Proyecto proyecto) throws Exception {
        ArrayList<Tarea> tareasDelProyecto = proyecto.getTareas();

        if (tareasDelProyecto.isEmpty()) {
            System.out.println("No hay tareas asignadas a este proyecto.");
            return;
        }

        System.out.println("Lista de tareas en el proyecto " + proyecto.getNombre() + ":");
        for (int i = 0; i < tareasDelProyecto.size(); i++) {
            Tarea tarea = tareasDelProyecto.get(i);
            System.out.println((i + 1) + ". " + tarea.getNombre() + " - " + tarea.getDescripcion());
        }

        System.out.print("Seleccione el número de la tarea que desea cerrar: ");
        
        int selectedTask = scanner.nextInt();

        if (selectedTask >= 1 && selectedTask <= tareasDelProyecto.size()) {
            Tarea tareaSeleccionada = tareasDelProyecto.get(selectedTask - 1);
            LocalDate fechaCierre = LocalDate.now();

            db.actualizarFechaCierreTarea(tareaSeleccionada.getId(), fechaCierre);

            System.out.println("Tarea cerrada exitosamente: " + tareaSeleccionada.getNombre());
        } else {
            System.out.println("Número de tarea inválido.");
        }
        
    }

    public static void closeProjects(DatabaseConnector dbConnector) {
        // Lógica para obtener los proyectos asignados al usuario actualmente logueado
        ArrayList<Proyecto> proyectosAsignados = filtrarProyectosPorUsuarioLogueado();
        
        
        
        if (proyectosAsignados.isEmpty()) {
            System.out.println("No hay proyectos asignados para cerrar.");
        } else {
            System.out.println("Lista de proyectos asignados:");
            for (int i = 0; i < proyectosAsignados.size(); i++) {
                System.out.println((i + 1) + ". " + proyectosAsignados.get(i).getNombre());
            }
    
            System.out.print("Seleccione el número del proyecto que desea cerrar (0 para cerrar todos): ");
            int selectedProjectIndex = scanner.nextInt();
    
            if (selectedProjectIndex >= 0 && selectedProjectIndex <= proyectosAsignados.size()) {
                if (selectedProjectIndex == 0) {
                    // Cerrar todos los proyectos asignados
                    for (Proyecto proyecto : proyectosAsignados) {
                        try {
                            dbConnector.cerrarProyecto(proyecto.getId());
                            System.out.println("Proyecto '" + proyecto.getNombre() + "' cerrado con éxito.");
                        } catch (Exception e) {
                            System.out.println("Error al cerrar el proyecto '" + proyecto.getNombre() + "': " + e.getMessage());
                        }
                    }
                } else {
                    // Cerrar proyecto seleccionado
                    Proyecto proyectoSeleccionado = proyectosAsignados.get(selectedProjectIndex - 1);
                    try {
                        dbConnector.cerrarProyecto(proyectoSeleccionado.getId());
                        System.out.println("Proyecto '" + proyectoSeleccionado.getNombre() + "' cerrado con éxito.");
                    } catch (Exception e) {
                        System.out.println("Error al cerrar el proyecto: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Número de proyecto inválido.");
            }
        }
    
        scanner.close();
    }
}