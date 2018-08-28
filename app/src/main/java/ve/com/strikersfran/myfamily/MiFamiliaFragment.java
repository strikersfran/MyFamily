package ve.com.strikersfran.myfamily;


import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
    private FloatingActionButton mAddFamilia;

    public MiFamiliaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View myFragmentView = inflater.inflate(R.layout.fragment_mi_familia, container, false);

        mAddFamilia = (FloatingActionButton) myFragmentView.findViewById(R.id.fab_add_familia);

        mAddFamilia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment mFragment = new BuscarFamiliarFragment();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, mFragment)
                        .commit();
                //getSupportActionBar().setTitle("Buscar Familiar");
            }
        });

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

    public void findFamiliar(){

    }

}
