package ve.com.strikersfran.myfamily;


import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MiFamiliaFragment extends Fragment {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private GridLayoutManager lManager;

    public MiFamiliaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_mi_familia, container, false);

        List items = new ArrayList();

        items.add(new MiFamilia("file:///android_asset/avatar1.jpg","Sara Lemus","Esposa",3,"On-Line"));
        items.add(new MiFamilia("file:///android_asset/avatar2.jpg","Angel David Carrión","Hijo",3,"On-Line"));
        items.add(new MiFamilia("file:///android_asset/avatar3.jpg","Yismar Carrión","Hermano",3,"Off-Line"));
        items.add(new MiFamilia("file:///android_asset/avatar4.jpg","William Carrión","Hermano",3,"Off-Line"));

        // Obtener el Recycler
        recycler = (RecyclerView) myFragmentView.findViewById(R.id.rv_mi_familia);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new GridLayoutManager(getContext(),2);
        recycler.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        adapter = new MiFamiliaAdapter(items);
        recycler.setAdapter(adapter);

        return myFragmentView;
    }

}
