import java.util.Scanner;


public class Driver {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Registro de usuario
        System.out.println("Registro de usuario:");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("Universidad: ");
        String universidad = scanner.nextLine();
        System.out.print("Correo electrónico: ");
        String correoElectronico = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();
        
        Usuario nuevoUsuario = new Usuario(nombre, apellido, universidad, correoElectronico, contrasena);
        System.out.println("Usuario registrado exitosamente!\n");
        
        // Creación de proyecto
        System.out.println("Creación de proyecto:");
        System.out.print("Título del proyecto: ");
        String titulo = scanner.nextLine();
        System.out.print("Descripción del proyecto: ");
        String descripcion = scanner.nextLine();
        System.out.print("Fecha de vencimiento (dd/mm/yyyy): ");
        String fechaVencimiento = scanner.nextLine();
        Project nuevoProyecto = new Project(titulo, descripcion, fechaVencimiento);
        System.out.println("Proyecto creado exitosamente!\n");
        
        // Asignación de tareas
        System.out.println("Asignación de tarea:");
        System.out.print("Título de la tarea: ");
        String tituloTarea = scanner.nextLine();
        System.out.print("Descripción de la tarea: ");
        String descripcionTarea = scanner.nextLine();
        System.out.print("Fecha de vencimiento de la tarea (dd/mm/yyyy): ");
        String fechaVencimientoTarea = scanner.nextLine();
        
        Tarea nuevaTarea = new Tarea(tituloTarea, descripcionTarea, fechaVencimientoTarea);
        nuevaTarea.setAsignadoA(nuevoUsuario);
        nuevoProyecto.getTareas().add(nuevaTarea);
        System.out.println("Tarea asignada exitosamente!\n");
        
        // Mostrar información
        System.out.println("Información del sistema:");
        System.out.println("Usuario: " + nuevoUsuario.getNombre() + " " + nuevoUsuario.getApellido());
        System.out.println("Proyecto: " + nuevoProyecto.getTitulo());
        System.out.println("Tareas asignadas al usuario:");
        for(Tarea tarea : nuevoProyecto.getTareas()) {
            if(tarea.getAsignadoA().equals(nuevoUsuario)) {
                System.out.println("  - " + tarea.getTitulo());
            }
        }
    }
}