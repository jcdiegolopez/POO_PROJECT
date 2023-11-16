import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class DatabaseConnector {
    private Connection connection = null;    
    
    public DatabaseConnector() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://poo@5.161.118.98:33014/proyectopoo?user=poo&password=secret");
            System.out.println(connection);
            System.out.println("Connected to the database.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void openConnection() throws Exception {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://poo@5.161.118.98:33014/proyectopoo?user=poo&password=secret");
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }
    }

    public ArrayList<Usuario> getAllUsuariosInfo() throws Exception {
       openConnection();
       ArrayList<Usuario> result = new ArrayList<Usuario>();
        try {
            if (connection != null) {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM usuarios";
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    int id = resultSet.getInt("ID_USUARIOS");
                    String nombre = resultSet.getString("NOMBRE");
                    String user = resultSet.getString("USER");
                    String password = resultSet.getString("PASSWORD");
                    String apellido = resultSet.getString("APELLIDO");
                    String mail = resultSet.getString("MAIL");
                    String sede = resultSet.getString("SEDE");
                    String tipo = resultSet.getString("TIPO");
                    switch (tipo) {
                        case "MAESTRO":
                            result.add(new Maestro(id, nombre, user, password, apellido, mail, sede));
                            break;
                        case "ESTUDIANTE":
                            result.add(new Estudiante(id, nombre, user, password, apellido, mail, sede));
                            break;
                        default:
                            break;
                    }
                }

                resultSet.close();
                statement.close();
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return result;
    }

    public ArrayList<Proyecto> getAllProjects() throws Exception {
        openConnection();
        ArrayList<Proyecto> projects = new ArrayList<>();
        try {
            if (connection != null) {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM proyectos";
                ResultSet resultSet = statement.executeQuery(query);
    
                while (resultSet.next()) {
                    int idProyecto = resultSet.getInt("id_proyecto");
                    String nombre = resultSet.getString("nombre");
                    String descripcion = resultSet.getString("descripcion");
                    LocalDate fechaInicio = resultSet.getDate("fecha_inicio").toLocalDate();
                    LocalDate fechaFin = resultSet.getDate("fecha_fin").toLocalDate();
                    int idLiderProyecto = resultSet.getInt("id_lider");
                    int idMaestroAsociado = resultSet.getInt("id_maestro_asociado");
    
                    // Recuperar el objeto Estudiante y Maestro usando sus IDs
                    Estudiante liderProyecto = getEstudianteById(idLiderProyecto);
                    Maestro maestroAsociado = getMaestroById(idMaestroAsociado);
    
                    // Crear el objeto Proyecto
                    Proyecto proyecto = new Proyecto(
                            idProyecto,
                            nombre,
                            descripcion,
                            fechaInicio,
                            fechaFin,
                            liderProyecto,
                            maestroAsociado
                    );
    
                    // Cargar las tareas del proyecto
                    ArrayList<Tarea> tareasDelProyecto = getTareasPorProyecto(idProyecto);
                    for (Tarea tarea : tareasDelProyecto) {
                        proyecto.agregarTarea(tarea);
                    }
    
                    // Añadir el proyecto a la lista
                    projects.add(proyecto);
                }
    
                resultSet.close();
                statement.close();
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener la lista de proyectos: " + e.getMessage(), e);
        } finally {
             // Asegúrate de cerrar la conexión después de usarla
        }
        return projects;
    }
    

    public Estudiante getEstudianteById(int idEstudiante) throws SQLException {
        Estudiante estudiante = null;
        String query = "SELECT * FROM usuarios WHERE ID_USUARIOS = ? AND TIPO = 'ESTUDIANTE'";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, idEstudiante);
        
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int id = resultSet.getInt("ID_USUARIOS");
            String nombre = resultSet.getString("NOMBRE");
            String user = resultSet.getString("USER");
            String password = resultSet.getString("PASSWORD");
            String apellido = resultSet.getString("APELLIDO");
            String mail = resultSet.getString("MAIL");
            String sede = resultSet.getString("SEDE");
            // Asumiendo que la clase Estudiante tiene un constructor adecuado
            estudiante = new Estudiante(id, nombre, user, password, apellido, mail, sede);
        }
        
        resultSet.close();
        preparedStatement.close();
        
        return estudiante;
    }

    public Maestro getMaestroById(int idMaestro) throws SQLException {
        Maestro maestro = null;
        String query = "SELECT * FROM usuarios WHERE ID_USUARIOS = ? AND TIPO = 'MAESTRO'";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, idMaestro);
        
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int id = resultSet.getInt("ID_USUARIOS");
            String nombre = resultSet.getString("NOMBRE");
            String user = resultSet.getString("USER");
            String password = resultSet.getString("PASSWORD");
            String apellido = resultSet.getString("APELLIDO");
            String mail = resultSet.getString("MAIL");
            String sede = resultSet.getString("SEDE");
            // Asumiendo que la clase Maestro tiene un constructor adecuado
            maestro = new Maestro(id, nombre, user, password, apellido, mail, sede);
        }
        
        resultSet.close();
        preparedStatement.close();
        
        return maestro;
    }

    public void registrarUsuario(String nombre, String usuario, String password, String apellido, String mail, String sede, String tipo) throws Exception {
       openConnection();
       if (connection != null) {
            String query = "INSERT INTO `usuarios`(NOMBRE,USER,PASSWORD,APELLIDO,MAIL,SEDE,TIPO) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, usuario);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, apellido);
            preparedStatement.setString(5, mail);
            preparedStatement.setString(6, sede);
            preparedStatement.setString(7, tipo);
            
            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Inserción exitosa.");
            } else {
                throw new Exception("La inserción no tuvo éxito.");
            }
            preparedStatement.close();
       } else {
        throw new Exception("No se pudo conectar con la base de datos");
       }
    }

    public void registrarProyecto(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, int idLider, int idMaestroAsociado) throws Exception {
        openConnection();
        if (connection != null) {
            String query = "INSERT INTO proyectos (nombre, descripcion, fecha_inicio, fecha_fin, id_lider, id_maestro_asociado) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, descripcion);
            preparedStatement.setDate(3, java.sql.Date.valueOf(fechaInicio));
            preparedStatement.setDate(4, (fechaFin != null) ? java.sql.Date.valueOf(fechaFin) : null);
            preparedStatement.setInt(5, idLider);
            preparedStatement.setInt(6, idMaestroAsociado);
    
            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Proyecto registrado con éxito.");
            } else {
                throw new Exception("No se pudo registrar el proyecto.");
            }
            preparedStatement.close();
        } else {
            throw new Exception("No se pudo conectar con la base de datos");
        }
    }
    

    public void insertarTarea(String nombre, String descripcion, LocalDate fechaInicio,  int idProyecto, int idUsuarioAsignado) throws Exception {
        openConnection();
        if (connection != null) {
            String insertQuery = "INSERT INTO tareas (NOMBRE, DESCRIPCION, FECHA_INICIO, FECHA_FIN, CALIFICACION, FINALIZADA, ID_PROYECTO, ID_USUARIOS) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                System.out.println(getConnection());
                PreparedStatement stmt = connection.prepareStatement(insertQuery);
                stmt.setString(1, nombre);
                stmt.setString(2, descripcion);
                stmt.setDate(3, java.sql.Date.valueOf(fechaInicio));
                stmt.setDate(4, null);
                stmt.setInt(5, -1);
                stmt.setBoolean(6, false);
                stmt.setInt(7, idProyecto); // Reemplaza esto con la forma correcta de obtener el ID del proyecto
                stmt.setInt(8, idUsuarioAsignado); // Reemplaza esto con la forma correcta de obtener el ID del usuario asignado
    
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Tarea insertada exitosamente en la base de datos.");
                } else {
                    System.out.println("No se pudo insertar la tarea en la base de datos.");
                }
                stmt.close();
        }else{
            throw new Exception("No se pudo conectar con la base de datos");
        }
    }
    
    public ArrayList<Tarea> getTareasPorProyecto(int idProyecto) throws SQLException {
        ArrayList<Tarea> tareas = new ArrayList<>();
        String query = "SELECT * FROM tareas WHERE id_proyecto = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, idProyecto);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int idTarea = resultSet.getInt("id_tarea");
            String nombre = resultSet.getString("nombre");
            LocalDate fechaInicio = resultSet.getDate("fecha_inicio").toLocalDate();
            LocalDate fechaFin = resultSet.getObject("fecha_fin") != null ? resultSet.getDate("fecha_fin").toLocalDate() : null;
            String descripcion = resultSet.getString("descripcion");
            int idUsuarioAsignado = resultSet.getInt("id_usuarios");
            boolean finalizada = resultSet.getBoolean("finalizada");
            int calificacion = resultSet.getInt("calificacion");

            // Aquí determinas si el usuario asignado es un estudiante o un maestro
            Estudiante estudianteAsignado = getEstudianteById(idUsuarioAsignado);


            // Asumiendo que la clase Tarea tiene un constructor que acepta un Usuario
            Tarea tarea = new Tarea(idTarea,nombre, estudianteAsignado, fechaInicio, fechaFin, descripcion, idProyecto, idUsuarioAsignado, finalizada);
            tarea.setCalificacion(calificacion);
            tarea.marcarComoFinalizada();
            tareas.add(tarea);
        }

        resultSet.close();
        preparedStatement.close();

        return tareas;
    }
    
    

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
