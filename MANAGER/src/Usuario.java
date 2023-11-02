public abstract class Usuario {
    protected int idusuario;
    protected String nombre;
    protected String usuario;
    protected String password;
    protected String apellido;
    protected String mail;
    protected String sede;

    public Usuario(int idusuario, String nombre, String usuario, String password, String apellido, String mail, String sede) {
        this.idusuario = idusuario;
        this.nombre = nombre;
        this.usuario = usuario;
        this.password = password;
        this.apellido = apellido;
        this.mail = mail;
        this.sede = sede;
    }

    // Getter para idusuario
    public int getIdusuario() {
        return idusuario;
    }

    // Setter para idusuario
    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    // Getter para nombre
    public String getNombre() {
        return nombre;
    }

    // Setter para nombre
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getter para usuario
    public String getUsuario() {
        return usuario;
    }

    // Setter para usuario
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    // Getter para password
    public String getPassword() {
        return password;
    }

    // Setter para password
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter para apellido
    public String getApellido() {
        return apellido;
    }

    // Setter para apellido
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    // Getter para mail
    public String getMail() {
        return mail;
    }

    // Setter para mail
    public void setMail(String mail) {
        this.mail = mail;
    }

    // Getter para sede
    public String getSede() {
        return sede;
    }

    // Setter para sede
    public void setSede(String sede) {
        this.sede = sede;
    }

    protected abstract String getTipo();

    public String toString() {
        return "idusuario=" + idusuario + ", nombre=" + nombre + ", usuario=" + usuario + ", password=" + password
                + ", apellido=" + apellido + ", mail=" + mail + ", sede=" + sede;
    }
    


}
