import java.util.ArrayList;
import java.time.LocalDate;


public class Proyecto {
    private String nombre;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Estudiante liderProyecto;
    private Maestro MaestroAsociado;
    private ArrayList<Tarea> tareas;
    private ArrayList<Estudiante> estudiantes;

    public Proyecto(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, Estudiante liderProyecto, Maestro MaestroAsociado) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.liderProyecto = liderProyecto;
        this.MaestroAsociado = MaestroAsociado;
        this.tareas = new ArrayList<>();
        this.estudiantes = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public Estudiante getLiderProyecto() {
        return liderProyecto;
    }

    public void setLiderProyecto(Estudiante liderProyecto) {
        this.liderProyecto = liderProyecto;
    }

    public Maestro getMaestroAsociado() {
        return MaestroAsociado;
    }

    public void setMaestroAsociado(Maestro MaestroAsociado) {
        this.MaestroAsociado = MaestroAsociado;
    }

    public ArrayList<Tarea> getTareas() {
        return tareas;
    }

    public ArrayList<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public void agregarTarea(Tarea tarea) {
        tareas.add(tarea);
    }

    public void agregarEstudiante(Estudiante estudiante) {
        estudiantes.add(estudiante);
    }

    public void asignarEstudianteALider(Estudiante estudiante) {
        liderProyecto = estudiante;
    }

    public Estudiante obtenerLiderProyecto() {
        return liderProyecto;
    }

    public ArrayList<Estudiante> obtenerEstudiantes() {
        return estudiantes;
    }

    public Maestro obtenerMaestroAsociado() {
        return MaestroAsociado;
    }

}

