package ve.com.strikersfran.myfamily;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class NoticiasFragment extends Fragment {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    public NoticiasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myFragmentView = inflater.inflate(R.layout.fragment_noticias, container, false);

        // Inicializar Animes
        List items = new ArrayList();

        items.add(new Noticias("file:///android_asset/avatar1.jpg","Sara Lemus","02 diciembre 2017",
                "file:///android_asset/noticia1.jpg","",0,1,6));
        items.add(new Noticias("file:///android_asset/avatar3.jpg","Francisco Carrión","02 febrero 2018",
                "file:///android_asset/noticia2.jpg","",0,1,6));
        items.add(new Noticias("file:///android_asset/avatar2.jpg","Angel David Carrión","15 diciembre 2017",
                "file:///android_asset/noticia3.jpg","",0,1,6));

        // Obtener el Recycler
        recycler = (RecyclerView) myFragmentView.findViewById(R.id.rv_noticias);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        adapter = new NoticiasAdapter(items);
        recycler.setAdapter(adapter);

        /*Toolbar tbCard = (Toolbar) myFragmentView.findViewById(R.id.toolbar_noticias);

        CircleImageView imageView = (CircleImageView) myFragmentView.findViewById(R.id.image_user);
        Picasso.with(getContext()).load(R.drawable.contacto).into(imageView);

        tbCard.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.opn_guardar:
                                Log.i("Toolbar noticias", "Acción Noticias Guardar");
                                break;
                            case R.id.opn_ver:
                                Log.i("Toolbar noticias", "Acción Noticias Ver Publicacion");
                                break;
                        }

                        return true;
                    }
                });

        tbCard.inflateMenu(R.menu.menu_card_noticias);*/


        // Inflate the layout for this fragment
        return myFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
    }

}
