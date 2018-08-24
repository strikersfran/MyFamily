package ve.com.strikersfran.myfamily;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_chat, container, false);

        List items = new ArrayList();

        items.add(new Chats("file:///android_asset/avatar1.jpg","Sara Lemus","01/08/2018","Hola Cariño"));
        items.add(new Chats("file:///android_asset/avatar2.jpg","Angel David Carrión","05/08/2018","Papi te quiero mucho"));
        items.add(new Chats("file:///android_asset/avatar3.jpg","Yismar Carrión","10/08/2018","Hola mano como estan las cosas por alla"));
        items.add(new Chats("file:///android_asset/avatar4.jpg","William Carrión","20/08/2018","Que hubo men vamos a beber hoy??"));

        // Obtener el Recycler
        recycler = (RecyclerView) myFragmentView.findViewById(R.id.rv_chats);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        adapter = new ChatsAdapter(items);
        recycler.setAdapter(adapter);

        return myFragmentView;
    }

}
