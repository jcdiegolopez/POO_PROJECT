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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Usuario getAsignadoA() {
        return asignadoA;
    }

    public void setAsignadoA(Usuario asignadoA) {
        this.asignadoA = asignadoA;
    }
}