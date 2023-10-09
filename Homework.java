public class Tarea {
    private String titulo;
    private String descripcion;
    private String fechaVencimiento;
    private Usuario asignadoA;

    public Tarea(String titulo, String descripcion, String fechaVencimiento) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaVencimiento = fechaVencimiento;
    }