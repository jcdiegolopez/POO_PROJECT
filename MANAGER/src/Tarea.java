import java.time.LocalDate;

public class Tarea {
    private String nombre;
    private Estudiante estudianteAsignado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String descripcion;
    private int calificacion;
    private boolean finalizada;
    private Usuario encargado;

    public Tarea(String nombre, Estudiante estudianteAsignado, LocalDate fechaInicio, LocalDate fechaFin, String descripcion, Usuario encargado) {
        this.nombre = nombre;
        this.estudianteAsignado = estudianteAsignado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descripcion = descripcion;
        this.finalizada = false; // Inicialmente la tarea no está finalizada
        this.encargado = encargado;
    }

    public int getCalificacion(){
        return this.calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Estudiante getEstudianteAsignado() {
        return estudianteAsignado;
    }

    public void setEstudianteAsignado(Estudiante estudianteAsignado) {
        this.estudianteAsignado = estudianteAsignado;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void marcarComoFinalizada() {
        finalizada = true;
    }

}
