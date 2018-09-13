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
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.util.ArrayList;
import java.util.List;

import ve.com.strikersfran.myfamily.data.MiFamiliaBD;
import ve.com.strikersfran.myfamily.data.StaticConfig;
import ve.com.strikersfran.myfamily.model.ListFamiliar;
import ve.com.strikersfran.myfamily.model.ListMiFamilia;


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
    private ListMiFamilia dataListMiFamilia = null;

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

        // Obtener el Recycler
        recycler = (RecyclerView) myFragmentView.findViewById(R.id.rv_mi_familia);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new GridLayoutManager(getContext(),2);
        recycler.setLayoutManager(lManager);

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

        //if (dataListMiFamilia == null) {
            dataListMiFamilia = MiFamiliaBD.getInstance(getContext()).getListMiFamilia();
            if (dataListMiFamilia.getListMiFamilia().size() > 0) {
                //listMiFamiliaID = new ArrayList<>();
                //for (MiFamilia familia : dataListMiFamilia.getListMiFamilia()) {
                //    listMiFamiliaID.add(familia.getUid());
                //}
                // Crear un nuevo adaptador
                adapter = new MiFamiliaAdapter(dataListMiFamilia.getListMiFamilia());
                recycler.setAdapter(adapter);
                //detectFriendOnline.start();
            }
            else{
                getFamiliar();
            }
        //}



        /*List items = new ArrayList();

        items.add(new MiFamilia("file:///android_asset/avatar1.jpg","Sara Lemus","Esposa",3,"On-Line"));
        items.add(new MiFamilia("file:///android_asset/avatar2.jpg","Angel David Carrión","Hijo",3,"On-Line"));
        items.add(new MiFamilia("file:///android_asset/avatar3.jpg","Yismar Carrión","Hermano",3,"Off-Line"));
        items.add(new MiFamilia("file:///android_asset/avatar4.jpg","William Carrión","Hermano",3,"Off-Line"));
        */


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
                .orderByChild("estatus").equalTo("aceptada")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null) {
                    Log.e("CARGAR FAMILIAR","No se encontraron registros");
                    mProgresDialog.dismiss();

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
                                    //listMiFamiliaID = new ArrayList<>();
                                    for (DataSnapshot dataSnapshot1 : list) {
                                        if (!dataSnapshot1.getKey().equals(uid)) {
                                            for(ListFamiliar familiar: familiarList){
                                                if(TextUtils.equals(familiar.getUid(),dataSnapshot1.getKey())){
                                                    MiFamilia mf = new MiFamilia();
                                                    mf.setUid(familiar.getUid());
                                                    mf.setAvatar(dataSnapshot1.child("avatar").getValue().toString());
                                                    mf.setNombre(dataSnapshot1.child("nombre").getValue().toString());
                                                    mf.setPrimerApellido(dataSnapshot1.child("primerApellido").getValue().toString());
                                                    mf.setSegundoApellido(dataSnapshot1.child("segundoApellido").getValue().toString());
                                                    mf.setEmail(dataSnapshot1.child("email").getValue().toString());
                                                    mf.setLastUpdate((Long) dataSnapshot1.child("lastUpdate").getValue());
                                                    mf.setEstatus(familiar.getEstatus());
                                                    mf.setParentesco("S/P");

                                                    //para guardar la lista de id de mis familiares
                                                    //listMiFamiliaID.add(familiar.getUid());
                                                    miFamiliaList.add(mf);

                                                    //para almacenar la lista de familiares en la db local
                                                    dataListMiFamilia.getListMiFamilia().add(mf);
                                                    MiFamiliaBD.getInstance(getContext()).addMiFamilia(mf);
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
                                    mProgresDialog.dismiss();
                                    new LovelyInfoDialog(context)
                                            .setTopColorRes(R.color.primary_color)
                                            .setTitle("Error")
                                            .setMessage("Algo no Salio mientras se cargaba tus familiares "+databaseError.getMessage())
                                            .show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mProgresDialog.dismiss();
            }
        });
    }

    //para actualizar los familiares nuevos y datos de los actuales
    public void updateFamiliar(){

        //obtener la ultima fecha de actualizacion de la tabla en la base de datos


        mDatabase.getReference().child("familiarByUsers").child(StaticConfig.UID)
                .orderByChild("lastUpdate").equalTo("aceptada")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue() == null) {
                            Log.e("CARGAR FAMILIAR","No se encontraron registros");
                            mProgresDialog.dismiss();

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
                                            //listMiFamiliaID = new ArrayList<>();
                                            for (DataSnapshot dataSnapshot1 : list) {
                                                if (!dataSnapshot1.getKey().equals(uid)) {
                                                    for(ListFamiliar familiar: familiarList){
                                                        if(TextUtils.equals(familiar.getUid(),dataSnapshot1.getKey())){
                                                            MiFamilia mf = new MiFamilia();
                                                            mf.setUid(familiar.getUid());
                                                            mf.setAvatar(dataSnapshot1.child("avatar").getValue().toString());
                                                            mf.setNombre(dataSnapshot1.child("nombre").getValue().toString());
                                                            mf.setPrimerApellido(dataSnapshot1.child("primerApellido").getValue().toString());
                                                            mf.setSegundoApellido(dataSnapshot1.child("segundoApellido").getValue().toString());
                                                            mf.setEmail(dataSnapshot1.child("email").getValue().toString());
                                                            mf.setLastUpdate((Long) dataSnapshot1.child("lastUpdate").getValue());
                                                            mf.setEstatus(familiar.getEstatus());
                                                            mf.setParentesco("S/P");

                                                            //para guardar la lista de id de mis familiares
                                                            //listMiFamiliaID.add(familiar.getUid());
                                                            miFamiliaList.add(mf);

                                                            //para almacenar la lista de familiares en la db local
                                                            dataListMiFamilia.getListMiFamilia().add(mf);
                                                            MiFamiliaBD.getInstance(getContext()).addMiFamilia(mf);
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
                                            mProgresDialog.dismiss();
                                            new LovelyInfoDialog(context)
                                                    .setTopColorRes(R.color.primary_color)
                                                    .setTitle("Error")
                                                    .setMessage("Algo no Salio mientras se cargaba tus familiares "+databaseError.getMessage())
                                                    .show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        mProgresDialog.dismiss();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.opcion_mi_familia);
    }

}
