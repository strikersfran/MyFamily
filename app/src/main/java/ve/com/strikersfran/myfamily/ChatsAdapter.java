package ve.com.strikersfran.myfamily;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Francisco Carrion on 24/08/2018.
 */

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>{

    private List<Chats> items;
    private Context context;
    private ImageView showImage;

    public ChatsAdapter(List<Chats> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_chat,parent,false);

        return new ChatsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsViewHolder holder, int position) {

        Picasso.with(context).load(items.get(position).getImagenUser()).into(holder.imagenUser);
        holder.nombreUser.setText(items.get(position).getNombreUser());
        holder.fecha.setText(items.get(position).getFecha());
        holder.mensaje.setText(items.get(position).getUltimoMensaje());

        showImage = holder.imagenUser;

        showImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = LayoutInflater.from(context).inflate(R.layout.imagen_show
                        , null);
                ImageView image = (ImageView) v.findViewById(R.id.imagen_show);

                //Picasso.with(context).load(view).into(image);
                Dialog settingsDialog = new Dialog(context);
                settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                settingsDialog.setContentView(v);
                settingsDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView imagenUser;
        public TextView nombreUser;
        public TextView fecha;
        public TextView mensaje;

        public ChatsViewHolder(View itemView) {
            super(itemView);

            imagenUser = (CircleImageView) itemView.findViewById(R.id.lc_imagen_user);
            nombreUser = (TextView) itemView.findViewById(R.id.lc_nombre_user);
            fecha = (TextView) itemView.findViewById(R.id.lc_fecha);
            mensaje = (TextView) itemView.findViewById(R.id.lc_mensaje);

        }
    }
}
