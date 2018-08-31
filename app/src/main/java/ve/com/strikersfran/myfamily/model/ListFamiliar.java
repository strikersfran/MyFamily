package ve.com.strikersfran.myfamily.model;

public class ListFamiliar {

    private String uid;
    private String estatus;

    public ListFamiliar() {
    }

    public ListFamiliar(String uid, String estatus) {
        this.uid = uid;
        this.estatus = estatus;
    }

    public String getUid() {
        return uid;
    }

    public String getEstatus() {
        return estatus;
    }
}
