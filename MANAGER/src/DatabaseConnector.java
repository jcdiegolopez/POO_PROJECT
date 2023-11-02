import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;


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

    public void getAllUsuariosInfo() {
       openConnection();
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

                    System.out.println("ID: " + id);
                    System.out.println("Nombre: " + nombre);
                    System.out.println("User: " + user);
                    System.out.println("Password: " + password);
                    System.out.println("-----------");
                }

                resultSet.close();
                statement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loginUser(String  username, String password){
        try {
            openConnection();
            if (connection != null) {
                
                Statement statement = connection.createStatement();
                String query = "SELECT ID_USUARIOS FROM USUARIOS WHERE USER = "+username ;
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    int id = resultSet.getInt("ID_USUARIOS");
                    

                }

                resultSet.close();
                statement.close();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
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
