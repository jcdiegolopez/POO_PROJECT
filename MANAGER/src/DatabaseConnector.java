import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.PreparedStatement;

public class DatabaseConnector {
    private Connection connection;

    public DatabaseConnector() {
    
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://poo@5.161.118.98:33014/proyectopoo?user=poo&password=secret");
            System.out.println(connection);
            System.out.println("Connected to the database.");
        } catch ( Exception e) {
            System.out.println(e);
        }
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
                            result.add(new Maestro(id, nombre, user, password, apellido,mail, sede));
                            break;
                        case "ESTUDIANTE":
                            result.add(new Estudiante(id, nombre, user, password, apellido,mail, sede));
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

    public void registrarUsuario(String nombre, String usuario, String password, String apellido, String mail, String sede, String tipo) throws Exception {
       openConnection();
       if(connection != null){
            String query = "INSERT INTO `usuarios`(NOMBRE,USER,PASSWORD,APELLIDO,MAIL,SEDE,TIPO) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.PreparedStatement(query);
            // Establezca los valores para los parámetros de la consulta INSERT
            PreparedStatement.setString(1, nombre);
            PreparedStatement.setString(2, usuario);
            PreparedStatement.setString(3, password);
            PreparedStatement.setString(4, apellido);
            PreparedStatement.setString(5, mail);
            PreparedStatement.setString(6, sede);
            PreparedStatement.setString(7, tipo);
            // Ejecute la consulta INSERT
            PreparedStatement.executeUpdate();
            
            
       }else{
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
