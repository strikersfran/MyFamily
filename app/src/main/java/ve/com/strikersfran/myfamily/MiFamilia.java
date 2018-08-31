package ve.com.strikersfran.myfamily;

public class MiFamilia {

    private String avatar;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String parentesco;
    private int rating;
    private String estatus;
    private String uid;
    private Long lastUpdate;
    private String email;

    public MiFamilia() {
    }

    public MiFamilia(String avatar, String nombre, String primerApellido, String segundoApellido,
                     String parentesco, int rating, String estatus, String uid, Long lastUpdate,
                     String email) {
        this.avatar = avatar;
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.parentesco = parentesco;
        this.rating = rating;
        this.estatus = estatus;
        this.uid = uid;
        this.lastUpdate = lastUpdate;
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getNombre() {
        return nombre;
    }

    public String getParentesco() {
        return parentesco;
    }

    public int getRating() {
        return rating;
    }

    public String getEstatus() {
        return estatus;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public String getUid() {
        return uid;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public String getEmail() {
        return email;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
