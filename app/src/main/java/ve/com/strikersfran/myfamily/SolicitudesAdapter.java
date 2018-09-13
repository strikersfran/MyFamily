package ve.com.strikersfran.myfamily;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ve.com.strikersfran.myfamily.util.ImageUtils;

public class SolicitudesAdapter extends RecyclerView.Adapter<SolicitudesAdapter.SolicitudesViewHolder>{

    private List<MiFamilia> items;
    private Context context;

    public SolicitudesAdapter(List<MiFamilia> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public SolicitudesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.card_solicitudes,parent,false);

        return new SolicitudesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitudesViewHolder holder, int position) {

        holder.imagenFamilia.setImageBitmap(ImageUtils.setImageAvatar(context,items.get(position).getAvatar()));
        holder.nameFamilia.setText(items.get(position).getNombre());
        holder.apellidosFamilia.setText(items.get(position).getPrimerApellido()+" "+items.get(position).getSegundoApellido());

        holder.aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class SolicitudesViewHolder extends RecyclerView.ViewHolder{

        public ImageView imagenFamilia;
        public TextView nameFamilia;
        public TextView apellidosFamilia;
        public Button aceptar;
        public Button eliminar;

        public SolicitudesViewHolder(View itemView) {
            super(itemView);

            imagenFamilia = (ImageView) itemView.findViewById(R.id.cs_imagen_familia);
            nameFamilia = (TextView) itemView.findViewById(R.id.cs_nombres);
            apellidosFamilia = (TextView) itemView.findViewById(R.id.cs_apellidos);
            aceptar = (Button) itemView.findViewById(R.id.cs_btn_aceptar);
            eliminar = (Button) itemView.findViewById(R.id.cs_btn_eliminar);

        }
    }
}
