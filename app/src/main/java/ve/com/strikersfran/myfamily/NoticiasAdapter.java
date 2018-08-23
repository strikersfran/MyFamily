package ve.com.strikersfran.myfamily;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Francisco Carrion on 23/08/2018.
 */

public class NoticiasAdapter extends RecyclerView.Adapter<NoticiasAdapter.NoticiasViewHolder>{

    private List<Noticias> items;
    private Context context;

    public NoticiasAdapter(List<Noticias> items){
        this.items = items;
    }
    @NonNull
    @Override
    public NoticiasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_noticias,parent,false);

        return new NoticiasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticiasViewHolder holder, int position) {
        holder.toolbar.inflateMenu(R.menu.menu_card_noticias);
        Picasso.with(context).load(items.get(position).getImagenUsuario()).into(holder.imagenUsuario);
        //holder.imagenUsuario.setImageResource(R.drawable.contacto);
        holder.nombreUsuario.setText(items.get(position).getNombreUsuario());
        holder.fechaNoticia.setText(items.get(position).getFecha());
        Picasso.with(context).load(items.get(position).getImagen()).into(holder.imagenNoticia);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class NoticiasViewHolder extends RecyclerView.ViewHolder{

        public Toolbar toolbar;
        public CircleImageView imagenUsuario;
        public TextView nombreUsuario;
        public TextView fechaNoticia;
        public ImageView imagenNoticia;
        public TextView textNoticia;

        public NoticiasViewHolder(View v){
            super(v);
            toolbar = (Toolbar) v.findViewById(R.id.toolbar_noticias);
            imagenUsuario = (CircleImageView) v.findViewById(R.id.image_user);
            nombreUsuario = (TextView) v.findViewById(R.id.name_user);
            fechaNoticia = (TextView) v.findViewById(R.id.fecha_noticias);
            imagenNoticia = (ImageView) v.findViewById(R.id.image_noticias);
        }
    }
}
