package ve.com.strikersfran.myfamily;


import android.content.Context;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.util.ArrayList;
import java.util.List;

import ve.com.strikersfran.myfamily.data.StaticConfig;
import ve.com.strikersfran.myfamily.model.ListFamiliar;


/**
 * A simple {@link Fragment} subclass.
 */
public class MiFamiliaFragment extends Fragment {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private GridLayoutManager lManager;
    private FloatingActionButton mAddFamilia;
    private FirebaseDatabase mDatabase;
    private LovelyProgressDialog mProgresDialog;
    private Context context;
    private List mItems;

    public MiFamiliaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View myFragmentView = inflater.inflate(R.layout.fragment_mi_familia, container, false);
        context = myFragmentView.getContext();

        mAddFamilia = (FloatingActionButton) myFragmentView.findViewById(R.id.fab_add_familia);

        mDatabase = FirebaseDatabase.getInstance();
        mProgresDialog = new LovelyProgressDialog(context);

        mAddFamilia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment mFragment = new BuscarFamiliarFragment();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, mFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        getFamiliar();

        /*List items = new ArrayList();

        items.add(new MiFamilia("file:///android_asset/avatar1.jpg","Sara Lemus","Esposa",3,"On-Line"));
        items.add(new MiFamilia("file:///android_asset/avatar2.jpg","Angel David Carrión","Hijo",3,"On-Line"));
        items.add(new MiFamilia("file:///android_asset/avatar3.jpg","Yismar Carrión","Hermano",3,"Off-Line"));
        items.add(new MiFamilia("file:///android_asset/avatar4.jpg","William Carrión","Hermano",3,"Off-Line"));
        */
        // Obtener el Recycler
        recycler = (RecyclerView) myFragmentView.findViewById(R.id.rv_mi_familia);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new GridLayoutManager(getContext(),2);
        recycler.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        //adapter = new MiFamiliaAdapter(items);
        //recycler.setAdapter(adapter);

        return myFragmentView;
    }

    public void getFamiliar(){
        mProgresDialog.setCancelable(false)
                .setTitle("Cargando Familiares....")
                .setTopColorRes(R.color.primary_color)
                .show();

        mDatabase.getReference().child("familiarByUsers").child(StaticConfig.UID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null) {
                    Log.e("CARGAR FAMILIAR","No se encontraron registros");

                } else {
                    mItems = new ArrayList();
                    final List<ListFamiliar> familiarList = new ArrayList<>();

                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        //String uid = singleSnapshot.child("uid").getValue(String.class);
                        //String estatus = singleSnapshot.child("estatus").getValue(String.class);
                        familiarList.add(singleSnapshot.getValue(ListFamiliar.class)/*new ListFamiliar(uid,estatus)*/);

                    }

                    mDatabase.getReference().child("users")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Iterable<DataSnapshot> list = dataSnapshot.getChildren();

                                    // Getting current user Id
                                    String uid = StaticConfig.UID;

                                    // Filtrar solo mis familiares
                                    List<MiFamilia> miFamiliaList = new ArrayList<>();
                                    for (DataSnapshot dataSnapshot1 : list) {
                                        if (!dataSnapshot1.getKey().equals(uid)) {
                                            for(ListFamiliar familiar: familiarList){
                                                if(TextUtils.equals(familiar.getUid(),dataSnapshot1.getKey())){
                                                    MiFamilia mf = new MiFamilia();
                                                    mf.setFoto(dataSnapshot1.child("avatar").getValue().toString());
                                                    mf.setNombre(dataSnapshot1.child("nombre").getValue().toString());
                                                    mf.setEstatus(familiar.getEstatus());
                                                    mf.setParentesco("S/P");
                                                    mf.setRating(3);

                                                    miFamiliaList.add(mf);
                                                }
                                            }
                                        }

                                    }

                                    mProgresDialog.dismiss();

                                    // Crear un nuevo adaptador
                                    adapter = new MiFamiliaAdapter(miFamiliaList);
                                    recycler.setAdapter(adapter);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.opcion_mi_familia);
    }

}
