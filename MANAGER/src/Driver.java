import java.util.ArrayList;



public class Driver {
    public static ArrayList<Usuario> usuarios = null;

    public static void main(String[] args) throws Exception {
        DatabaseConnector db = new DatabaseConnector();
        usuarios = db.getAllUsuariosInfo();

        
    }

    public Usuario loginUser(String email, String password){
        for (Usuario usuario : usuarios) {
            if (usuario.getMail().equals(email) && usuario.getPassword().equals(password)) {
                return usuario;
            }
        }
        return null;
    }

    
}