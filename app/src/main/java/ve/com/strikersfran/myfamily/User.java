package ve.com.strikersfran.myfamily;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String avatar;
    private String email;
    private String uid;
    private Long lastUpdate;

    public User() {
    }

    public User(String nombre, String primerApellido, String segundoApellido, String avatar, String email, String uid) {
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.avatar = avatar;
        this.email = email;
        this.uid = uid;
        this.lastUpdate = System.currentTimeMillis()/1000;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getEmail() {
        return email;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nombre", nombre);
        result.put("primerApellido", primerApellido);
        result.put("segundoApellido", segundoApellido);
        result.put("avatar",avatar);
        result.put("email",email);
        result.put("uid",uid);
        result.put("lastUpdate",lastUpdate);

        return result;
    }
}
