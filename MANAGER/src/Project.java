import java.util.ArrayList;
import java.util.List;

public class Project {
    private String titulo;
    private String descripcion;
    private String fechaVencimiento;
    private List<Tarea> tareas;

    public Project(String titulo, String descripcion, String fechaVencimiento) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaVencimiento = fechaVencimiento;
        this.tareas = new ArrayList<>();
    }

    // Getters y setters para los atributos

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

    public List<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
    }

    public void crearTarea(String titulo, String descripcion, String fechaVencimiento) {
        Tarea nuevaTarea = new Tarea(titulo, descripcion, fechaVencimiento);
        tareas.add(nuevaTarea);
    }

    public void asignarTarea(Tarea tarea, Usuario usuario) {
        //pendiente
    }

    public void generarReporte() {
        // pendite
    }
}