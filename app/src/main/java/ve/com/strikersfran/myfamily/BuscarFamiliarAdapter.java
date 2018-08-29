package ve.com.strikersfran.myfamily;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ve.com.strikersfran.myfamily.util.ImageUtils;

public class BuscarFamiliarAdapter extends RecyclerView.Adapter<BuscarFamiliarAdapter.BuscarFamiliarViewHolder>{

    private List<User> items;
    private Context context;

    public BuscarFamiliarAdapter(List<User> items){
        this.items = items;
    }
    @NonNull
    @Override
    public BuscarFamiliarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_search_familiar,parent,false);

        return new BuscarFamiliarViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BuscarFamiliarViewHolder holder, int position) {

        //Picasso.with(context).load(items.get(position).get).into(holder.imagen);
        holder.imagen.setImageBitmap(ImageUtils.setImageAvatar(context,items.get(position).getAvatar()));
        holder.nombre.setText(items.get(position).getNombre());
        holder.apellidos.setText(items.get(position).getPrimerApellido()+" "+items.get(position).getSegundoApellido());

        holder.btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Boton Agregar",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class BuscarFamiliarViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView imagen;
        public TextView nombre;
        public TextView apellidos;
        public ImageButton btnAgregar;

        public BuscarFamiliarViewHolder(View v){
            super(v);

            imagen = (CircleImageView) v.findViewById(R.id.ls_imagen_user);
            nombre = (TextView) v.findViewById(R.id.ls_nombre_user);
            apellidos = (TextView) v.findViewById(R.id.ls_apellidos_user);
            btnAgregar = (ImageButton) v.findViewById(R.id.ls_btn_agregar);
        }
    }
}
