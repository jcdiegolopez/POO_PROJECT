import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class DatabaseConnector {
    private Connection connection;

    public DatabaseConnector() {
    
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/proyecto_poo", "root", "admin123");
            System.out.println(connection);
            System.out.println("Connected to the database.");
        } catch ( Exception e) {
            System.out.println(e);
        }
    }

    public void openConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/proyecto_poo", "root", "admin123");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public ArrayList<Usuario> getAllUsuariosInfo() {
       openConnection();
       ArrayList<Usuario> result = new ArrayList<Usuario>();
        try {
            if (connection != null) {
                
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM USUARIOS";
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
                    System.out.println(tipo);
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
            e.printStackTrace();
        }
        return result;
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
