package ve.com.strikersfran.myfamily;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

import ve.com.strikersfran.myfamily.util.ImageUtils;

public class MiFamiliaAdapter extends RecyclerView.Adapter<MiFamiliaAdapter.MiFamiliaViewHolder> {

    private List<MiFamilia> items;
    private Context context;

    public MiFamiliaAdapter(List<MiFamilia> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public MiFamiliaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.card_familia,parent,false);

        return new MiFamiliaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MiFamiliaViewHolder holder, int position) {

        holder.toolbar.inflateMenu(R.menu.menu_mifamilia);
        holder.imagenFamilia.setImageBitmap(ImageUtils.setImageAvatar(context,items.get(position).getAvatar()));
        holder.nameFamilia.setText(items.get(position).getNombre());
        holder.parentescoFamilia.setText(items.get(position).getParentesco());
        holder.apellidosFamilia.setText(items.get(position).getPrimerApellido()+" "+items.get(position).getSegundoApellido());
        holder.estatusFamilia.setText(items.get(position).getEstatus());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MiFamiliaViewHolder extends RecyclerView.ViewHolder{

        public Toolbar toolbar;
        public ImageView imagenFamilia;
        public TextView nameFamilia;
        public TextView parentescoFamilia;
        public TextView apellidosFamilia;
        public TextView estatusFamilia;

        public MiFamiliaViewHolder(View itemView) {
            super(itemView);

            toolbar = (Toolbar) itemView.findViewById(R.id.tb_familia);
            imagenFamilia = (ImageView) itemView.findViewById(R.id.imagen_familia);
            nameFamilia = (TextView) itemView.findViewById(R.id.name_familia);
            parentescoFamilia = (TextView) itemView.findViewById(R.id.parentesco_familia);
            apellidosFamilia = (TextView) itemView.findViewById(R.id.apellidos_familiar);
            estatusFamilia = (TextView) itemView.findViewById(R.id.estatus_familia);

        }
    }
}
