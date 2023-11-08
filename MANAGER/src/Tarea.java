import java.time.LocalDate;

public class Tarea {
    private String nombre;
    private Estudiante estudianteAsignado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String descripcion;
    private int calificacion;
    private boolean finalizada;
    private int idProyecto; // ID del proyecto al que está asignada la tarea
    private int idUsuarioAsignado; // ID del usuario asignado a la tarea

    public Tarea(String nombre, Estudiante estudianteAsignado, LocalDate fechaInicio, LocalDate fechaFin, String descripcion, int idProyecto, int idUsuarioAsignado, boolean finalizada) {
        this.nombre = nombre;
        this.estudianteAsignado = estudianteAsignado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descripcion = descripcion;
        this.finalizada = finalizada; // Inicialmente la tarea no está finalizada
        this.idProyecto = idProyecto;
        this.idUsuarioAsignado = idUsuarioAsignado;
    }

    public Tarea(String nombre, Estudiante estudianteAsignado, LocalDate fechaInicio, String descripcion, int idProyecto, int idUsuarioAsignado) {
        this(nombre, estudianteAsignado, fechaInicio, null, descripcion, idProyecto, idUsuarioAsignado, false);
    }

    // Métodos getter y setter para los nuevos campos (idProyecto e idUsuarioAsignado)
    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public int getIdUsuarioAsignado() {
        return idUsuarioAsignado;
    }

    public void setIdUsuarioAsignado(int idUsuarioAsignado) {
        this.idUsuarioAsignado = idUsuarioAsignado;
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
