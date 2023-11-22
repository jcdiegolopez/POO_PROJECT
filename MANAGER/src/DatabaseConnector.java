import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.sql.Timestamp;

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
                    
                    // Manejar fecha_fin que podría ser null en la base de datos
                    LocalDate fechaFin = null;
                    java.sql.Date fechaFinSQL = resultSet.getDate("fecha_fin");
                    if (fechaFinSQL != null) {
                        fechaFin = fechaFinSQL.toLocalDate();
                    }
                    
                    int idLiderProyecto = resultSet.getInt("id_lider");
                    int idMaestroAsociado = resultSet.getInt("id_maestro_asociado");
                    double calificacion = resultSet.getDouble("calificacion");
    
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
                            maestroAsociado,
                            calificacion
                    );
    
                    // Cargar las tareas del proyecto
                    ArrayList<Tarea> tareasDelProyecto = getTareasPorProyecto(idProyecto);
                    for (Tarea tarea : tareasDelProyecto) {
                        proyecto.agregarTarea(tarea);
                    }

                    // Cargar las tareas del proyecto
                    ArrayList<Estudiante> miembrosdelproyecto = getMiembrosPorProyecto(idProyecto);
                    for (Estudiante estudiante : miembrosdelproyecto) {
                        proyecto.agregarEstudiante(estudiante);
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

    public void registrarProyecto(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, int idLider, int idMaestroAsociado, Double calificacion) throws Exception {
        openConnection();
        if (connection != null) {
            String query = "INSERT INTO proyectos (nombre, descripcion, fecha_inicio, fecha_fin, id_lider, id_maestro_asociado, calificacion) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, descripcion);
            preparedStatement.setDate(3, java.sql.Date.valueOf(fechaInicio));
            preparedStatement.setDate(4, (fechaFin != null) ? java.sql.Date.valueOf(fechaFin) : null);
            preparedStatement.setInt(5, idLider);
            preparedStatement.setInt(6, idMaestroAsociado);
    
            if (calificacion != null) {
                preparedStatement.setDouble(7, calificacion);
            } else {
                preparedStatement.setNull(7, java.sql.Types.DOUBLE);
            }
    
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
    
    
    public void actualizarCalificacionProyecto(int idProyecto, Double calificacion) throws SQLException {
        String query = "UPDATE proyectos SET CALIFICACION = ? WHERE ID_PROYECTO = ?";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            if (calificacion != null) {
                preparedStatement.setDouble(1, calificacion);
            } else {
                preparedStatement.setNull(1, java.sql.Types.DOUBLE);
            }
            preparedStatement.setInt(2, idProyecto);
            
            preparedStatement.executeUpdate();
        }
    }
    
    public void insertarTarea(String nombre, String descripcion, LocalDate fechaInicio, int idProyecto, int idUsuarioAsignado) throws Exception {
        openConnection();
        if (connection != null) {
            // Nota: Se ha eliminado la columna 'CALIFICACION' de la consulta
            String insertQuery = "INSERT INTO tareas (NOMBRE, DESCRIPCION, FECHA_INICIO, FECHA_FIN, FINALIZADA, ID_PROYECTO, ID_USUARIOS) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(insertQuery);
            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.setDate(3, java.sql.Date.valueOf(fechaInicio));
            stmt.setDate(4, null); // FECHA_FIN se inicia como null
            stmt.setBoolean(5, false); // FINALIZADA se inicia como false
            stmt.setInt(6, idProyecto);
            stmt.setInt(7, idUsuarioAsignado);
    
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Tarea insertada exitosamente en la base de datos.");
            } else {
                System.out.println("No se pudo insertar la tarea en la base de datos.");
            }
            stmt.close();
        } else {
            throw new Exception("No se pudo conectar con la base de datos");
        }
    }    

    public void agregarMiembro(int idProyecto, int idUsuarioAsignado) throws Exception {
        openConnection();
        if (connection != null) {
            String insertQuery = "INSERT INTO EstudiantesProyectos (ID_USUARIOS, ID_PROYECTO) " +
                        "VALUES (?, ?)";
                PreparedStatement stmt = connection.prepareStatement(insertQuery);
                stmt.setInt(1, idUsuarioAsignado); 
                stmt.setInt(2, idProyecto); 
    
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Miembro agregado exitosamente.");
                } else {
                    stmt.close();
                    throw new Exception("No se pudo insertar la tarea en la base de datos.");
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
           
            Estudiante estudianteAsignado = getEstudianteById(idUsuarioAsignado);

            Tarea tarea = new Tarea(idTarea,nombre, estudianteAsignado, fechaInicio, fechaFin, descripcion, idProyecto, idUsuarioAsignado, finalizada);
            tareas.add(tarea);
        }

        resultSet.close();
        preparedStatement.close();

        return tareas;
    }

    public ArrayList<Estudiante> getMiembrosPorProyecto(int idProyecto) throws SQLException {
        ArrayList<Estudiante> estudiantes = new ArrayList<>();
        String query = "SELECT * FROM EstudiantesProyectos WHERE id_proyecto = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, idProyecto);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int idUsuario = resultSet.getInt("id_usuarios");
            Estudiante estudiante = getEstudianteById(idUsuario);
            estudiantes.add(estudiante);
        }

        resultSet.close();
        preparedStatement.close();

        return estudiantes;
    }
    
    public void actualizarFechaCierreTarea(int tareaId, LocalDate fechaCierre) throws SQLException, Exception {
        try {
            openConnection();
            if (connection != null) {
                String updateQuery = "UPDATE tareas SET FECHA_FIN = ?, FINALIZADA = ? WHERE id_tarea = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setDate(1, java.sql.Date.valueOf(fechaCierre));
                preparedStatement.setBoolean(2, true);
                preparedStatement.setInt(3, tareaId);
    
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Fecha de cierre de tarea actualizada con éxito en la base de datos.");
                } else {
                    System.out.println("No se pudo actualizar la fecha de cierre de la tarea en la base de datos.");
                }
                preparedStatement.close();
            } else {
                throw new SQLException("No se pudo conectar con la base de datos.");
            }
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar la fecha de cierre de la tarea: " + e.getMessage());
        } catch (Exception ex) {
            throw new Exception("Error inesperado: " + ex.getMessage());
        }
    }

    public void cerrarProyecto(int idProyecto, LocalDate fechaCierre) throws Exception {
        try {
            openConnection();
            if (connection != null) {
                String checkOpenQuery = "SELECT fecha_fin FROM proyectos WHERE id_proyecto = ?";
                PreparedStatement checkOpenStatement = connection.prepareStatement(checkOpenQuery);
                checkOpenStatement.setInt(1, idProyecto);
                ResultSet resultSet = checkOpenStatement.executeQuery();
    
                LocalDate fechaFin = null;
                if (resultSet.next()) {
                    fechaFin = resultSet.getDate("fecha_fin") != null ?
                                resultSet.getDate("fecha_fin").toLocalDate() : null;
                }
    
                resultSet.close();
                checkOpenStatement.close();
    
                if (fechaFin != null) {
                    System.out.println("El proyecto ya está cerrado.");
                    return;
                }
    
                String updateQuery = "UPDATE proyectos SET fecha_fin = ? WHERE id_proyecto = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setDate(1, java.sql.Date.valueOf(fechaCierre));
                preparedStatement.setInt(2, idProyecto);
    
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Fecha de cierre del proyecto actualizada con éxito en la base de datos.");
                } else {
                    System.out.println("No se pudo actualizar la fecha de cierre del proyecto en la base de datos.");
                }
    
                preparedStatement.close();
            } else {
                throw new SQLException("No se pudo conectar con la base de datos.");
            }
        } catch (SQLException e) {
            throw new SQLException("Error al cerrar el proyecto: " + e.getMessage());
        }
    }



public void mostrarChat(int idProyecto) throws SQLException, Exception {
    try {
        openConnection();
        if (connection != null) {
            String selectQuery = "SELECT * FROM mensajes WHERE ID_PROYECTO = ? ORDER BY FECHA_ENVIO";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, idProyecto);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String contenido = resultSet.getString("CONTENIDO");
                Timestamp timestamp = resultSet.getTimestamp("FECHA_ENVIO");
                LocalDateTime fechaEnvio = timestamp.toLocalDateTime();
                int idUsuarioEmisor = resultSet.getInt("ID_USUARIO_EMISOR");
                Estudiante emisor = getEstudianteById(idUsuarioEmisor);
                System.out.println("[" + fechaEnvio + "]  " + emisor.getNombre() + ": " + contenido);
            }

            resultSet.close();
            preparedStatement.close();
        } else {
            throw new SQLException("No se pudo conectar con la base de datos.");
        }
    } catch (SQLException e) {
        throw new SQLException("Error al mostrar el chat: " + e.getMessage());
    } catch (Exception ex) {
        throw new Exception("Error inesperado: " + ex.getMessage());
    }
}

public void enviarMensaje(int idProyecto, String contenido, int idUsuarioEmisor) throws SQLException, Exception {
    try {
        openConnection();
        if (connection != null) {
            String insertQuery = "INSERT INTO mensajes (CONTENIDO, FECHA_ENVIO, ID_USUARIO_EMISOR, ID_PROYECTO) VALUES (?, NOW(), ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, contenido);
            preparedStatement.setInt(2, idUsuarioEmisor);
            preparedStatement.setInt(3, idProyecto);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } else {
            throw new SQLException("No se pudo conectar con la base de datos.");
        }
    } catch (SQLException e) {
        throw new SQLException("Error al enviar mensaje: " + e.getMessage());
    } catch (Exception ex) {
        throw new Exception("Error inesperado al enviar mensaje: " + ex.getMessage());
    }
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
