package ve.com.strikersfran.myfamily;

/**
 * Created by Francisco Carrion on 23/08/2018.
 */

public class Noticias {

    private String imagenUsuario;
    private String nombreUsuario;
    private String fecha;
    private String imagen;
    private String text;
    private int cantMeGusta;
    private int cantComentarios;
    private int cantCompartido;


    public Noticias(String iu, String nu, String fn, String in, String tn, int cm, int cc, int cp){

        this.imagenUsuario = iu;
        this.nombreUsuario = nu;
        this.fecha = fn;
        this.imagen = in;
        this.text = tn;
        this.cantMeGusta = cm;
        this.cantComentarios = cc;
        this.cantCompartido = cp;
    }

    public String getImagenUsuario() {
        return imagenUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getFecha() {
        return fecha;
    }

    public String getImagen() {
        return imagen;
    }

    public String getText() {
        return text;
    }

    public int getCantMeGusta() {
        return cantMeGusta;
    }

    public int getCantComentarios() {
        return cantComentarios;
    }

    public int getCantCompartido() {
        return cantCompartido;
    }
}
