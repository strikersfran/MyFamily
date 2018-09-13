package ve.com.strikersfran.myfamily;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.util.ArrayList;
import java.util.List;

import ve.com.strikersfran.myfamily.data.StaticConfig;
import ve.com.strikersfran.myfamily.model.ListFamiliar;


/**
 * A simple {@link Fragment} subclass.
 */
public class SolicitudesFragment extends Fragment {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private FirebaseDatabase mDatabase;
    private LovelyProgressDialog mProgresDialog;
    private Context context;
    private List mItems;

    public SolicitudesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View myFragmentView = inflater.inflate(R.layout.fragment_solicitudes, container, false);
        context = myFragmentView.getContext();

        mDatabase = FirebaseDatabase.getInstance();
        mProgresDialog = new LovelyProgressDialog(context);

        // Obtener el Recycler
        recycler = (RecyclerView) myFragmentView.findViewById(R.id.rv_solicitudes);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(context);
        recycler.setLayoutManager(lManager);

        getSolicitudes();

        return myFragmentView;
    }

    public void getSolicitudes(){
        mProgresDialog.setCancelable(false)
                .setTitle("Cargando Solicitudes....")
                .setTopColorRes(R.color.primary_color)
                .show();

        mDatabase.getReference().child("familiarByUsers").child(StaticConfig.UID)
                .orderByChild("estatus").equalTo("recibida")
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
                                familiarList.add(singleSnapshot.getValue(ListFamiliar.class));

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
                                                            //dataListMiFamilia.getListMiFamilia().add(mf);
                                                            ///MiFamiliaBD.getInstance(getContext()).addMiFamilia(mf);
                                                        }
                                                    }
                                                }

                                            }

                                            mProgresDialog.dismiss();

                                            // Crear un nuevo adaptador
                                            adapter = new SolicitudesAdapter(miFamiliaList);
                                            recycler.setAdapter(adapter);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            mProgresDialog.dismiss();
                                            new LovelyInfoDialog(context)
                                                    .setTopColorRes(R.color.primary_color)
                                                    .setTitle("Error")
                                                    .setMessage("Algo no Salio mientras se cargaba tus solicitudes "+databaseError.getMessage())
                                                    .show();
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.opcion_solicitudes);
    }

}
