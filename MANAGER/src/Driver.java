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
        while(account == null){
            System.out.println("1. Login");
            System.out.println("2. Sign in");
            System.out.println("3. Salir: ");
            System.out.println("Ingrese la opcion que desea: ");
            int opt = scanner.nextInt();
            switch (opt) {
                case 1:
                    
                    try {
                        System.out.println("Ingrese su correo electronico");
                        String email = scanner.nextLine();
                        System.out.println("Ingrese su contraseña");
                        String password = scanner.nextLine();
                        account = loginUser(email, password);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 2:
                    System.out.println("==================== CREACION DE USUARIO =====================");
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
                        scanner.nextLine();
                        System.out.print("1.Estudiante ");
                        System.out.print("2.Maestro ");
                        System.out.print("Tipo: ");
                        int sel1 = scanner.nextInt();
                        String tipo = (sel1==1)? "ESTUDIANTE" : "MAESTRO";
                        
                        RegisterUser(nombre,usuario, password, apellido, mail, sede, tipo);
                        System.out.println("Usuario creado con exito!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 3: 
                    System.out.println("Saliendo del programa");
                    
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
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

    public static void reloadUsers() throws Exception{
        usuarios = db.getAllUsuariosInfo();
    }

    
}