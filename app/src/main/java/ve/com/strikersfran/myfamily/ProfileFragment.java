package ve.com.strikersfran.myfamily;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import ve.com.strikersfran.myfamily.data.SharedPreferenceHelper;
import ve.com.strikersfran.myfamily.data.StaticConfig;
import ve.com.strikersfran.myfamily.util.ImageUtils;
import ve.com.strikersfran.myfamily.util.ValidarInput;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private User myAccount;
    private CircleImageView avatar;
    private TextView nombres;
    private TextView papellido;
    private TextView sapellido;
    private TextView email;
    private static final int PICK_IMAGE = 1994;
    private Context context;
    private DatabaseReference userDB;
    private LovelyProgressDialog mProgresDialog;
    private DatabaseReference mDatabase;
    private LovelyCustomDialog mCustomDialog;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        userDB = FirebaseDatabase.getInstance().getReference().child("users").child(StaticConfig.UID);

        // Inflate the layout for this fragment
        View  myFragmentView = inflater.inflate(R.layout.fragment_profile, container, false);
        context = myFragmentView.getContext();

        mProgresDialog = new LovelyProgressDialog(context);

        avatar = (CircleImageView) myFragmentView.findViewById(R.id.fp_avatar);
        nombres = (TextView) myFragmentView.findViewById(R.id.fp_nombres);
        papellido = (TextView) myFragmentView.findViewById(R.id.fp_primer_apellido);
        sapellido = (TextView) myFragmentView.findViewById(R.id.fp_segundo_apellido);
        email = (TextView) myFragmentView.findViewById(R.id.fp_correo);

        ImageButton editAvatar = (ImageButton) myFragmentView.findViewById(R.id.fp_edit_foto);

        ImageButton editDatos = (ImageButton) myFragmentView.findViewById(R.id.fp_btn_edit);

        LinearLayout cambiarContrasena = (LinearLayout) myFragmentView.findViewById(R.id.fp_cambiar_contrasena);
        LinearLayout salir = (LinearLayout) myFragmentView.findViewById(R.id.fp_salir);

        SharedPreferenceHelper prefHelper = SharedPreferenceHelper.getInstance(context);
        myAccount = prefHelper.getUserInfo();

        setImageAvatar(context, myAccount.getAvatar());

        nombres.setText(myAccount.getNombre());
        papellido.setText(myAccount.getPrimerApellido());
        sapellido.setText(myAccount.getSegundoApellido());
        email.setText(myAccount.getEmail());

        //evento para el boton salir o logout
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
            }
        });

        //evento para editar el avatar
        editAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Imagen de Perfil")
                        .setMessage("Estas seguro que deseas cambiar la inmagen de perfil?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_PICK);
                                startActivityForResult(Intent.createChooser(intent, "Seleccionar Imagen"), PICK_IMAGE);
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });

        //evento para editar los datos

        editDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View dialogView = inflater.inflate(R.layout.edit_datos_profile, null);

                final EditText enombre = dialogView.findViewById(R.id.edp_nombre);
                final EditText epapellido = dialogView.findViewById(R.id.edp_primer_apellido);
                final EditText esapellido = dialogView.findViewById(R.id.edp_segundo_apellido);
                final EditText eemail = dialogView.findViewById(R.id.edp_email);

                enombre.setText(myAccount.getNombre());
                epapellido.setText(myAccount.getPrimerApellido());
                esapellido.setText(myAccount.getSegundoApellido());
                eemail.setText(myAccount.getEmail());

                mCustomDialog= new LovelyCustomDialog(context);

                mCustomDialog.setView(dialogView)
                        .setTopColorRes(R.color.primary_color)
                        .setTitle("Editar mis datos")
                        .setListener(R.id.edp_btn_aceptar, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            updateDatos(enombre,epapellido,esapellido,eemail);
                            }
                        })
                        .setListener(R.id.edp_btn_cancelar, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mCustomDialog.dismiss();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });


        return myFragmentView;
    }

    private void updateDatos(EditText n,EditText pa, EditText sa,EditText e){

        //validar datos
        boolean cancel = false;
        View focusView = null;

        //obtener valores de los campos
        final String nombre = n.getText().toString().trim();
        final String pApellido = pa.getText().toString().trim();
        final String sApellido = sa.getText().toString().trim();
        final String emails = e.getText().toString().trim();

        if(TextUtils.isEmpty(nombre)){
            n.setError(getString(R.string.requerido));
            focusView=n;
            cancel = true;
        }else if(TextUtils.isEmpty(pApellido)){
            pa.setError(getString(R.string.requerido));
            focusView = pa;
            cancel = true;
        }else if(TextUtils.isEmpty(sApellido)){
            sa.setError(getString(R.string.requerido));
            focusView = sa;
            cancel = true;
        }else if(TextUtils.isEmpty(emails)){
            e.setError(getString(R.string.requerido));
            focusView = e;
            cancel = true;
        }else if(!ValidarInput.isEmailValid(emails)){
            e.setError(getString(R.string.error_email));
            focusView = e;
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus();
        }
        else {

            mCustomDialog.dismiss();

            final User myAccountUpdate = new User(nombre,pApellido,sApellido,myAccount.getAvatar(),emails);

            mProgresDialog.setCancelable(false)
                    .setTitle("Actualizando datos....")
                    .setTopColorRes(R.color.primary_color)
                    .show();

            Map<String, Object> userValues = myAccountUpdate.toMap();
            Map<String, Object> childUpdates = new HashMap<>();

            childUpdates.put("/users/"+StaticConfig.UID,userValues);
            mDatabase.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        mProgresDialog.dismiss();
                        SharedPreferenceHelper preferenceHelper = SharedPreferenceHelper.getInstance(context);
                        preferenceHelper.saveUserInfo(myAccountUpdate);

                        //actualizar el objeto del user
                        myAccount.setNombre(nombre);
                        myAccount.setPrimerApellido(pApellido);
                        myAccount.setSegundoApellido(sApellido);
                        myAccount.setEmail(emails);

                        //actualizar los datos en el view del profile
                        nombres.setText(nombre);
                        papellido.setText(pApellido);
                        sapellido.setText(sApellido);
                        email.setText(emails);

                        new LovelyInfoDialog(context)
                                .setTopColorRes(R.color.primary_color)
                                .setTitle("Felicidades !!")
                                .setMessage("Los datos fueron actualizados correctamente!")
                                .show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mProgresDialog.dismiss();
                    Log.e("Actualizando Perfil", "Actualizacón fallida Error: "+e.getMessage());
                    new LovelyInfoDialog(context)
                            .setTopColorRes(R.color.primary_color)
                            .setTitle("Error")
                            .setMessage("Algo salio mal no se pudo actualizar los datos ")
                            .show();
                }
            });

        }


    }

    private void setImageAvatar(Context context, String imgBase64){
        try {
            Resources res = getResources();
            Bitmap src;
            if (imgBase64.equals("default")) {
                src = BitmapFactory.decodeResource(res, R.drawable.avatar_default);
            } else {
                byte[] decodedString = Base64.decode(imgBase64, Base64.DEFAULT);
                src = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            }

            avatar.setImageBitmap(src);
        }catch (Exception e){
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(context, "No seleccionaste una imagen", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(data.getData());

                Bitmap imgBitmap = BitmapFactory.decodeStream(inputStream);
                imgBitmap = ve.com.strikersfran.myfamily.util.ImageUtils.cropToSquare(imgBitmap);
                InputStream is = ImageUtils.convertBitmapToInputStream(imgBitmap);
                final Bitmap liteImage = ImageUtils.makeImageLite(is,
                        imgBitmap.getWidth(), imgBitmap.getHeight(),
                        ImageUtils.AVATAR_WIDTH, ImageUtils.AVATAR_HEIGHT);

                String imageBase64 = ImageUtils.encodeBase64(liteImage);
                myAccount.setAvatar(imageBase64);

                mProgresDialog.setCancelable(false)
                        .setTitle("Actualizando Perfil....")
                        .setTopColorRes(R.color.primary_color)
                        .show();

                userDB.child("avatar").setValue(imageBase64)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    mProgresDialog.dismiss();
                                    SharedPreferenceHelper preferenceHelper = SharedPreferenceHelper.getInstance(context);
                                    preferenceHelper.saveUserInfo(myAccount);
                                    avatar.setImageDrawable(ImageUtils.roundedImage(context, liteImage));

                                    new LovelyInfoDialog(context)
                                            .setTopColorRes(R.color.primary_color)
                                            .setTitle("Felicidades !!")
                                            .setMessage("Se ha actualizado la imagen correctamente!")
                                            .show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mProgresDialog.dismiss();
                                Log.d("Update Avatar", "failed");
                                new LovelyInfoDialog(context)
                                        .setTopColorRes(R.color.primary_color)
                                        .setTitle("Error")
                                        .setMessage("Algo salio mal no se pudo actualizar la imagen")
                                        .show();
                            }
                        });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
