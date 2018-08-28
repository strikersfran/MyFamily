package ve.com.strikersfran.myfamily;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BuscarFamiliarFragment extends Fragment {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private ProgressBar mProgressBar;
    private FirebaseDatabase mDatabase;
    private List mItems;

    public BuscarFamiliarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View myFragmentView = inflater.inflate(R.layout.fragment_buscar_familiar, container, false);

        ImageButton btnBuscar = (ImageButton) myFragmentView.findViewById(R.id.fb_btn_buscar);
        final EditText txtFiltro = (EditText) myFragmentView.findViewById(R.id.fb_filtro);
        mProgressBar = (ProgressBar) myFragmentView.findViewById(R.id.fb_progress_bar) ;

        mDatabase = FirebaseDatabase.getInstance();

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filtro = txtFiltro.getText().toString().trim();

                if(TextUtils.isEmpty(filtro)){
                    txtFiltro.setError(getString(R.string.requerido));
                    txtFiltro.requestFocus();
                }
                else{
                    mProgressBar.setVisibility(View.VISIBLE);
                    mDatabase.getReference().child("users").orderByChild("nombre")
                            .startAt(filtro).endAt(filtro+"\\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            mProgressBar.setVisibility(View.GONE);

                            if (dataSnapshot.getValue() == null) {
                                Log.e("BUSCAR FAMILIAR","No se encontraron registros");

                            } else {
                                mItems = new ArrayList();
                                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                    String nombre = singleSnapshot.child("nombre").getValue(String.class);
                                    String pApellido = singleSnapshot.child("primerApellido").getValue(String.class);
                                    String sApellido = singleSnapshot.child("segundoApellido").getValue(String.class);
                                    mItems.add(new User(nombre,pApellido,sApellido));
                                }

                                // Obtener el Recycler
                                recycler = (RecyclerView) myFragmentView.findViewById(R.id.rv_buscar_familiar);
                                recycler.setHasFixedSize(true);

                                // Usar un administrador para LinearLayout
                                lManager = new LinearLayoutManager(getContext());
                                recycler.setLayoutManager(lManager);

                                // Crear un nuevo adaptador
                                adapter = new BuscarFamiliarAdapter(mItems);
                                recycler.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }
        });



        return myFragmentView;
    }

}
