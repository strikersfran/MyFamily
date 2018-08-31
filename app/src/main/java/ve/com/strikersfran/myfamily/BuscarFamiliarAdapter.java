package ve.com.strikersfran.myfamily;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ve.com.strikersfran.myfamily.data.StaticConfig;
import ve.com.strikersfran.myfamily.model.ListFamiliar;
import ve.com.strikersfran.myfamily.util.ImageUtils;

public class BuscarFamiliarAdapter extends RecyclerView.Adapter<BuscarFamiliarAdapter.BuscarFamiliarViewHolder>{

    private List<User> items;
    private Context context;
    private LovelyProgressDialog mProgresDialog;

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
    public void onBindViewHolder(@NonNull final BuscarFamiliarViewHolder holder, int position) {

        mProgresDialog = new LovelyProgressDialog(context);
        //Picasso.with(context).load(items.get(position).get).into(holder.imagen);
        holder.imagen.setImageBitmap(ImageUtils.setImageAvatar(context,items.get(position).getAvatar()));
        holder.nombre.setText(items.get(position).getNombre());
        holder.apellidos.setText(items.get(position).getPrimerApellido()+" "+items.get(position).getSegundoApellido());
        holder.uid.setText(items.get(position).getUid());

        holder.btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgresDialog.setCancelable(false)
                        .setTitle("Agregando Familiar....")
                        .setTopColorRes(R.color.primary_color)
                        .show();
                //agregar el usaurio como familiar
                String uid = holder.uid.getText().toString();
                addFamiliar(uid,true);
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
        public EditText uid;

        public BuscarFamiliarViewHolder(View v){
            super(v);

            imagen = (CircleImageView) v.findViewById(R.id.ls_imagen_user);
            nombre = (TextView) v.findViewById(R.id.ls_nombre_user);
            apellidos = (TextView) v.findViewById(R.id.ls_apellidos_user);
            btnAgregar = (ImageButton) v.findViewById(R.id.ls_btn_agregar);
            uid = (EditText) v.findViewById(R.id.lsf_uid_user);
        }
    }
    /*Esta funcion permite agregar un usuario como familiar de forma cruzada*/
    private void addFamiliar(final String idFamiliar, boolean isIdFriend) {
        if (idFamiliar != null) {
            if (isIdFriend) {
                ListFamiliar familiar = new ListFamiliar(idFamiliar,"invitado");
                FirebaseDatabase.getInstance().getReference().child("familiarByUsers/" + StaticConfig.UID)
                        //.push().setValue(StaticConfig.UID)
                        //.child(idFamiliar).setValue("false")
                        .push().setValue(familiar)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    addFamiliar(idFamiliar, false);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mProgresDialog.dismiss();
                                new LovelyInfoDialog(context)
                                        .setTopColorRes(R.color.primary_color)
                                        .setTitle("Error")
                                        .setMessage("Algo salio mal al agregar el familiar "+e.getMessage())
                                        .show();
                            }
                        });
            } else {
                ListFamiliar familiar = new ListFamiliar(StaticConfig.UID,"invitado");
                FirebaseDatabase.getInstance().getReference().child("familiarByUsers/" + idFamiliar)
                        //.push().setValue(StaticConfig.UID)
                        //.child(StaticConfig.UID).setValue("false")
                        .push().setValue(familiar)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            addFamiliar(null, false);
                        }
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mProgresDialog.dismiss();
                                new LovelyInfoDialog(context)
                                        .setTopColorRes(R.color.primary_color)
                                        .setTitle("Error")
                                        .setMessage("Algo salio mal al agregar el familiar "+e.getMessage())
                                        .show();
                            }
                        });
            }
        } else {
            mProgresDialog.dismiss();
            new LovelyInfoDialog(context)
                    .setTopColorRes(R.color.primary_color)
                    .setTitle("Felicidades!!")
                    .setMessage("Se agrego correctamente el familiar")
                    .show();
        }
    }
}
