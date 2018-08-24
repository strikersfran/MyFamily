package ve.com.strikersfran.myfamily;

/**
 * Created by Francisco Carrion on 24/08/2018.
 */

public class Chats {

    private String imagenUser;
    private String nombreUser;
    private String fecha;
    private String ultimoMensaje;

    public Chats(String imagenUser, String nombreUser, String fecha, String ultimoMensaje) {
        this.imagenUser = imagenUser;
        this.nombreUser = nombreUser;
        this.fecha = fecha;
        this.ultimoMensaje = ultimoMensaje;
    }

    public String getImagenUser() {
        return imagenUser;
    }

    public String getNombreUser() {
        return nombreUser;
    }

    public String getFecha() {
        return fecha;
    }

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }
}
