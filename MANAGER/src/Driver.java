import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Driver {    public static ArrayList<Usuario> usuarios = null;
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
                showStudentsProjects();
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
    scanner.close();
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
    
        ArrayList<Proyecto> proyectosActivos = new ArrayList<>();
        ArrayList<Proyecto> proyectosCerrados = new ArrayList<>();
        for (Proyecto proyecto : proyectosFiltrados) {
            if (proyecto.getFechaFin() == null) {
                proyectosActivos.add(proyecto);
            } else {
                proyectosCerrados.add(proyecto);
            }
        }
    
        System.out.println("Proyectos activos:");
        for (int i = 0; i < proyectosActivos.size(); i++) {
            System.out.println((i + 1) + ". " + proyectosActivos.get(i).getNombre());
        }
    
        System.out.println("Proyectos cerrados:");
        for (int i = 0; i < proyectosCerrados.size(); i++) {
            System.out.println((i + 1 + proyectosActivos.size()) + ". " + proyectosCerrados.get(i).getNombre());
        }
    
        System.out.print("Seleccione un proyecto (número): ");
        int projectChoice = scanner.nextInt();
    
        if (projectChoice >= 1 && projectChoice <= proyectosActivos.size()) {
            Proyecto selectedProject = proyectosActivos.get(projectChoice - 1);
            showProjectMenuStudents(selectedProject);
        } else if (projectChoice > proyectosActivos.size() && projectChoice <= (proyectosActivos.size() + proyectosCerrados.size())) {
            Proyecto selectedProject = proyectosCerrados.get(projectChoice - proyectosActivos.size() - 1);
            showClosedProjectMenuStudents(selectedProject);
            return;
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
                    newMember(proyecto);
                    break;
                case 4:
                    enterProjectChat(proyecto.getId());
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

    public static void enterProjectChat(int idProyecto) throws SQLException, Exception {
        try {
            while (true) {
                db.mostrarChat(idProyecto);
                System.out.println("1. Enviar mensaje");
                System.out.println("2. Salir del chat");
    
                System.out.print("Seleccione una opción: ");
                int chatOption = scanner.nextInt();
                scanner.nextLine();
    
                switch (chatOption) {
                    case 1:
                        System.out.println("Ingrese su mensaje:");
                        String mensaje = scanner.nextLine();
                        db.enviarMensaje(idProyecto, mensaje, account.getIdusuario());
                        break;
                    case 2:
                        System.out.println("Saliendo del chat del proyecto.");
                        return;
                    default:
                        System.out.println("Opción no válida.");
                        break;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al acceder al chat del proyecto: " + e.getMessage());
        } catch (Exception ex) {
            System.err.println("Error inesperado al acceder al chat del proyecto: " + ex.getMessage());
        }
    }
    

    public static void showClosedProjectMenuStudents(Proyecto proyecto) throws Exception {
        while (true) {
            System.out.println("\nProyecto: " + proyecto.getNombre());
            System.out.println("Menú del proyecto cerrado:");
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
                    enterProjectChat(proyecto.getId());
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
                            enterProjectChat(proyecto.getId());
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

    public static void gradeProject(Proyecto proyecto) throws Exception {
    
    System.out.print("Ingrese la calificación para el proyecto '" + proyecto.getNombre() + "': ");
    double calificacion = scanner.nextDouble();

    try {
        db.actualizarCalificacionProyecto(proyecto.getId(), calificacion);
        reloadProjects();
        System.out.println("Proyecto calificado exitosamente: " + proyecto.getNombre());
        proyecto.setCalificacion(calificacion);
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
    
        int idLider;
        int idMaestro = -1;
    
        if (account.getTipo().equals("ESTUDIANTE")) {
            System.out.print("Ingrese su ID de estudiante líder: ");
            idLider = scanner.nextInt();
            scanner.nextLine(); 
    
            System.out.println("Seleccione un maestro para el proyecto:");
            for (int i = 0; i < usuarios.size(); i++) {
                if (usuarios.get(i) instanceof Maestro) {
                    System.out.println((i + 1) + ". " + usuarios.get(i).getNombre());
                }
            }
            System.out.print("Ingrese el número del maestro: ");
            int maestroIndex = scanner.nextInt() - 1;
            scanner.nextLine();
            if (maestroIndex >= 0 && maestroIndex < usuarios.size()) {
                idMaestro = usuarios.get(maestroIndex).getIdusuario();
            } else {
                System.out.println("Número de maestro no válido.");
                return;
            }
        } else if (account.getTipo().equals("MAESTRO")) {
            idMaestro = account.getIdusuario();
            System.out.print("Ingrese el ID del estudiante líder del proyecto: ");
            idLider = scanner.nextInt();
            scanner.nextLine();
        } else {
            System.out.println("Tipo de cuenta no reconocido para la creación de proyectos.");
            return;
        }
    
        double calificacionInicial = -1.0;
    
        try {
            db.registrarProyecto(nombre, descripcion, fechaInicio, fechaFin, idLider, idMaestro, calificacionInicial);
            reloadProjects();
            System.out.println("Proyecto registrado con éxito");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void newMember(Proyecto proyecto)throws Exception{
        try {
           ArrayList<Usuario> usuarios = db.getAllUsuariosInfo();
            System.out.println("IDs de los estudiantes disponibles:");
            for (Usuario usuario : usuarios) {
                if (usuario instanceof Estudiante) {
                    System.out.println("ID: " + usuario.getIdusuario() + " - Nombre: " + usuario.getNombre());
                }
            }
            System.out.print("Ingrese el ID del estudiante para agregar a miembro: ");
            int estudianteId = scanner.nextInt();
            db.agregarMiembro(proyecto.getId(), estudianteId);
            reloadProjects();
            System.out.print("Agregado miembro con exito!");
            proyecto.agregarEstudiante(db.getEstudianteById(estudianteId));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
    }

    public static void createTask(int idProyecto) throws Exception {
        try {
            System.out.println("========== CREACIÓN DE TAREA ==========");
            System.out.print("Nombre de la tarea: ");
            String tareaNombre = scanner.nextLine();
            System.out.print("Descripción de la tarea: ");
            String tareaDescripcion = scanner.nextLine();
            Proyecto actual = null;
            for (Proyecto proyecto : proyectos) {
                if (proyecto.getId() == idProyecto) {
                    actual = proyecto;
                }
            }
    
            if (actual == null) {
                System.out.println("Proyecto no encontrado.");
                return;
            }
    
            ArrayList<Usuario> todosLosEstudiantes = db.getAllUsuariosInfo();
            
            System.out.println("IDs de los estudiantes disponibles:");
            for (Usuario usuario : todosLosEstudiantes) {
                if (usuario instanceof Estudiante && usuario.getIdusuario() != actual.getLiderProyecto().getIdusuario()) {
                    System.out.println("ID: " + usuario.getIdusuario() + " - Nombre: " + usuario.getNombre());
                }
            }
    
            System.out.print("Ingrese el ID del estudiante al que desea asignar la tarea: ");
            int estudianteId = scanner.nextInt();
            scanner.nextLine(); 
    
            Estudiante estudianteAsignado = findEstudianteById(estudianteId);
    
            if (estudianteAsignado != null) {
                LocalDate fechaInicio = LocalDate.now();
                db.insertarTarea(tareaNombre, tareaDescripcion, fechaInicio, idProyecto, estudianteId);
                reloadProjects();
                System.out.println("Tarea asignada con éxito.");
                actualizarTareas(actual);
            } else {
                System.out.println("Estudiante no encontrado con el ID proporcionado.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void reloadProjects()throws Exception{
        proyectos = db.getAllProjects();
    }


    public static void actualizarTareas(Proyecto proyecto) throws Exception{
        try {
            ArrayList<Tarea> nuevas = db.getTareasPorProyecto(proyecto.getId());
            proyecto.setTareas(nuevas);

        } catch (Exception e) {
            throw new Exception(e);
        }
      
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
                if (tarea.isFinalizada()) {
                    System.out.println("- " + tarea.getNombre() + " - " + tarea.getDescripcion());
                }
            }
    
            System.out.println("Tareas sin completar:");
            for (Tarea tarea : tareasDelProyecto) {
                if (!tarea.isFinalizada()) {
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
            if(!tarea.isFinalizada()){
                
                System.out.println((i + 1) + ". " + tarea.getNombre() + " - " + tarea.getDescripcion());
            }
            
        }

        System.out.print("Seleccione el número de la tarea que desea cerrar: ");
        
        int selectedTask = scanner.nextInt();

        if (selectedTask >= 1 && selectedTask <= tareasDelProyecto.size()) {
            Tarea tareaSeleccionada = tareasDelProyecto.get(selectedTask - 1);
            LocalDate fechaCierre = LocalDate.now();

            db.actualizarFechaCierreTarea(tareaSeleccionada.getId(), fechaCierre);
            reloadProjects();
            System.out.println("Tarea cerrada exitosamente: " + tareaSeleccionada.getNombre());
            tareaSeleccionada.marcarComoFinalizada();
            tareaSeleccionada.setFechaFin(fechaCierre);
            
        } else {
            System.out.println("Número de tarea inválido.");
        }
        
    }

    public static void closeProjects(DatabaseConnector dbConnector) {
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
            LocalDate fechaCierre = LocalDate.now();

            if (selectedProjectIndex >= 0 && selectedProjectIndex <= proyectosAsignados.size()) {
                List<Proyecto> proyectosACerrar = selectedProjectIndex == 0 ? proyectosAsignados
                                                                        : Collections.singletonList(proyectosAsignados.get(selectedProjectIndex - 1));

                for (Proyecto proyecto : proyectosACerrar) {
                    try {
                        dbConnector.cerrarProyecto(proyecto.getId(), fechaCierre);
                        // Actualizar las tareas del proyecto
                        for (Tarea tarea : proyecto.getTareas()) {
                            if (!tarea.isFinalizada()) {
                                dbConnector.actualizarFechaCierreTarea(tarea.getId(), fechaCierre);
                            }
                        }
                        reloadProjects();
                        proyecto.setFechaFin(fechaCierre);
                        System.out.println("Proyecto '" + proyecto.getNombre() + "' cerrado con éxito.");
                    } catch (Exception e) {
                        System.out.println("Error al cerrar el proyecto '" + proyecto.getNombre() + "': " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Número de proyecto inválido.");
            }
        }
    }
}