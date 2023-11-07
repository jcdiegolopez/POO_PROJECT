import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {
    public static ArrayList<Usuario> usuarios = null;
    public static Usuario account = null;
    public static Scanner scanner;
    public static DatabaseConnector db;

    public static void main(String[] args) throws Exception {
        db = new DatabaseConnector();
        reloadUsers();
        scanner = new Scanner(System.in);
        boolean loginCicle = true;

        while (loginCicle) {
            System.out.println("1. Login");
            System.out.println("2. Sign in");
            System.out.println("3. Salir");
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
                case 3:
                    System.out.println("Saliendo del programa");
                    loginCicle = false;
                    break;
                default:
                    System.out.println("Opción no válida");
                    break;
            }
        }
    }

    public static void studenstMenu() {
        boolean continuar = true;

        while (continuar) {
            System.out.println("===================== MENU ESTUDIANTE =====================");
            System.out.println("1. Proyectos");
            System.out.println("2. Crear Proyecto");
            System.out.println("3. Salir");
            System.out.print("Elija una opción: ");
            int opt = scanner.nextInt();

            switch (opt) {
                case 1:
                    //Agregar funcion de ver proyectos creados.
                    break;
                case 2:
                    createProject();
                    break;
                case 3:
                    System.out.println("Saliendo del menú estudiante.");
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción no válida");
                    break;
            }
        }
    }

    public static void showStudentsProjects() {
        System.out.println("Proyectos en los que está inscrito el estudiante:");

        try {
            String query = "SELECT p.nombre FROM proyectos AS p " +
                    "INNER JOIN estudiantes_proyectos AS ep ON p.id = ep.id_proyecto " +
                    "WHERE ep.id_estudiante = ?";

            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, account.getIdusuario()); 

            ResultSet resultSet = preparedStatement.executeQuery();

            int proyectoCount = 0;
            while (resultSet.next()) {
                String nombreProyecto = resultSet.getString("nombre");
                System.out.println((proyectoCount + 1) + ". " + nombreProyecto);
                proyectoCount++;
            }

            if (proyectoCount == 0) {
                System.out.println("El estudiante no está inscrito en ningún proyecto.");
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void profesorsMenu() {
        boolean continuar = true;
    
        while (continuar) {
            System.out.println("================================= MENU MAESTRO =================================");
            System.out.println("1. Ver Proyectos");
            System.out.println("2. Crear Proyectos");
            System.out.println("3. Salir");
            System.out.print("Elija una opción: ");
            int opt = scanner.nextInt();
    
            switch (opt) {
                case 1:
                    //Agregar funcion de ver proyectos creados.
                    break;
                case 2:
                    createProject();
                    break;
                case 3:
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
    
        try {

            String query = "SELECT nombre FROM proyectos WHERE id_maestro = ?";
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, account.getIdusuario());
    
            ResultSet resultSet = preparedStatement.executeQuery();
    
            int proyectoCount = 0;
            while (resultSet.next()) {
                String nombreProyecto = resultSet.getString("nombre");
                System.out.println((proyectoCount + 1) + ". " + nombreProyecto);
                proyectoCount++;
            }
    
            if (proyectoCount == 0) {
                System.out.println("El Maestro no tiene proyectos registrados.");
            }
    
            resultSet.close();
            preparedStatement.close();
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        System.out.println("Presione Enter para volver al menú Maestro.");
        scanner.nextLine();
        scanner.nextLine();
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
    scanner.nextLine(); // Limpiar el buffer del scanner
    System.out.println("========== CREACIÓN DE PROYECTO ==========");
    System.out.print("Nombre del proyecto: ");
    String nombre = scanner.nextLine();
    System.out.print("Descripción del proyecto: ");
    String descripcion = scanner.nextLine();
    System.out.print("Fecha de inicio (YYYY-MM-DD): ");
    String fechaInicioStr = scanner.nextLine();
    LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
    System.out.print("Fecha de fin (YYYY-MM-DD): ");
    String fechaFinStr = scanner.nextLine();
    LocalDate fechaFin = LocalDate.parse(fechaFinStr);
    
    // Aquí asumimos que el usuario que crea el proyecto será el líder si es estudiante,
    // o el maestro asociado si es maestro. Ajusta según la lógica de tu aplicación.
    int idLider = (account.getTipo().equals("ESTUDIANTE")) ? account.getIdusuario() : -1; // -1 o algún valor por defecto si no es estudiante
    int idMaestro = -1;
    if (account.getTipo().equals("ESTUDIANTE")) {
        idLider = account.getIdusuario();
        System.out.println("Seleccione un maestro para el proyecto:");
        try {
            for (int i = 0; i < usuarios.size(); i++) {
                if(usuarios.get(i) instanceof Maestro)
            {System.out.println((i + 1) + ". " + usuarios.get(i).getNombre());}
            }
            System.out.print("Ingrese el número del maestro: ");
            int maestroIndex = scanner.nextInt() - 1;
            if (maestroIndex >= 0) {
                idMaestro = usuarios.get(maestroIndex).getIdusuario();
            } else {
                System.out.println("Número de maestro no válido.");
                return; // Salir del método si no se selecciona un maestro válido
            }
        } catch (Exception e) {
            e.printStackTrace();
            return; // Salir del método si hay un error al recuperar los maestros
        }
    } else if (account.getTipo().equals("MAESTRO")) {
        idMaestro = account.getIdusuario();
        // Aquí podrías permitir que el maestro seleccione un líder estudiante si es necesario
    }
    
    try {
        db.registrarProyecto(nombre, descripcion, fechaInicio, fechaFin, idLider, idMaestro);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

}