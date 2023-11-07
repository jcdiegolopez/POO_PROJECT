import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class DatabaseConnector {
    private Connection connection;

    public DatabaseConnector() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://poo@5.161.118.98:33014/proyectopoo?user=poo&password=secret");
            System.out.println(connection);
            System.out.println("Connected to the database.");
        } catch (Exception e) {
            System.out.println(e);
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
                throw new Exception(e);
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
                    String nombre = resultSet.getString("nombre");
                    String descripcion = resultSet.getString("descripcion");
                    LocalDate fechaInicio = resultSet.getDate("fecha_inicio").toLocalDate();
                    LocalDate fechaFin = resultSet.getDate("fecha_fin").toLocalDate();
                    int idLiderProyecto = resultSet.getInt("id_lider_proyecto");
                    int idMaestroAsociado = resultSet.getInt("id_maestro_asociado");

                    // Aquí necesitarás recuperar el objeto Estudiante y Maestro usando sus IDs
                    Estudiante liderProyecto = getEstudianteById(idLiderProyecto);
                    Maestro maestroAsociado = getMaestroById(idMaestroAsociado);

                    Proyecto proyecto = new Proyecto(
                            nombre,
                            descripcion,
                            fechaInicio,
                            fechaFin,
                            liderProyecto,
                            maestroAsociado
                    );
                    projects.add(proyecto);
                }

                resultSet.close();
                statement.close();
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener la lista de proyectos: " + e.getMessage(), e);
        }
        return projects;
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
       } else {
        throw new Exception("No se pudo conectar con la base de datos");
       }
    }

    public void registrarProyecto(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, int idLider, int idMaestroAsociado) throws Exception {
        openConnection();
        if (connection != null) {
            String query = "INSERT INTO proyectos (nombre, descripcion, fecha_inicio, fecha_fin, id_lider, id_maestro_asociado) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nombre);
                preparedStatement.setString(2, descripcion);
                preparedStatement.setDate(3, java.sql.Date.valueOf(fechaInicio));
                preparedStatement.setDate(4, java.sql.Date.valueOf(fechaFin));
                preparedStatement.setInt(5, idLider);
                preparedStatement.setInt(6, idMaestroAsociado);

                int filasAfectadas = preparedStatement.executeUpdate();
                if (filasAfectadas > 0) {
                    System.out.println("Proyecto registrado con éxito.");
                } else {
                    throw new Exception("No se pudo registrar el proyecto.");
                }
            } catch (SQLException e) {
                throw new Exception("Error al registrar el proyecto: " + e.getMessage());
            }
        } else {
            throw new Exception("No se pudo conectar con la base de datos");
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
