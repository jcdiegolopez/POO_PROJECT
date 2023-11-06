public class Estudiante extends Usuario {

    public Estudiante(int idusuario, String nombre, String usuario, String password, String apellido, String mail, String sede){
        super(idusuario,nombre,usuario,password, apellido, mail, sede);
    }
    
    @Override
    public String getTipo() {
        return "ESTUDIANTE";
    }

    @Override
    public String toString() {
        return super.toString() + " ,Tipo=" +getTipo();
    }
}
