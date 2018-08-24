package ve.com.strikersfran.myfamily;

public class MiFamilia {

    private String foto;
    private String nombre;
    private String parentesco;
    private int rating;
    private String estatus;

    public MiFamilia(String foto, String nombre, String parentesco, int rating, String estatus) {
        this.foto = foto;
        this.nombre = nombre;
        this.parentesco = parentesco;
        this.rating = rating;
        this.estatus = estatus;
    }

    public String getFoto() {
        return foto;
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
}
