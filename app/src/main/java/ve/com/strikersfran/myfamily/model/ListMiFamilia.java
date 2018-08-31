package ve.com.strikersfran.myfamily.model;

import java.util.ArrayList;

import ve.com.strikersfran.myfamily.MiFamilia;

public class ListMiFamilia {

    private ArrayList<MiFamilia> listMiFamilia;

    public ArrayList<MiFamilia> getListMiFamilia() {
        return listMiFamilia;
    }

    public ListMiFamilia(){
        listMiFamilia = new ArrayList<>();
    }

    public String getAvataById(String id){
        for(MiFamilia familia: listMiFamilia){
            if(id.equals(familia.getUid())){
                return familia.getAvatar();
            }
        }
        return "";
    }

    public void setListMiFamilia(ArrayList<MiFamilia> listMiFamilia) {
        this.listMiFamilia = listMiFamilia;
    }
}
