package ve.com.strikersfran.myfamily;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrarActivity extends AppCompatActivity {

    private EditText mNombre,mPrimerApellido,mSegundoApellido;
    private EditText mEmail,mContrasena,mRepiteContrasena;
    private ProgressBar mProgressBar;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button btnRegistrar = (Button) findViewById(R.id.btn_registrar);
        TextView txtIniciar = (TextView) findViewById(R.id.txt_iniciar);

        mNombre = (EditText) findViewById(R.id.rg_nombre);
        mPrimerApellido = (EditText) findViewById(R.id.rg_primer_apellido);
        mSegundoApellido = (EditText) findViewById(R.id.rg_segundo_apellido);

        mEmail = (EditText) findViewById(R.id.rg_email);
        mContrasena = (EditText) findViewById(R.id.rg_contrasena);
        mRepiteContrasena = (EditText) findViewById(R.id.rg_repite_contrasena);

        mProgressBar = (ProgressBar) findViewById(R.id.rg_progress_bar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
                /*Intent intent = new Intent(RegistrarActivity.this,MainActivity.class);
                startActivity(intent);
                finish();*/
            }
        });

        txtIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrarActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void registrar(){

        resetError();

        //obtner valores de los campos
        final String nombre = mNombre.getText().toString().trim();
        final String pApellido = mPrimerApellido.getText().toString().trim();
        final String sApellido = mSegundoApellido.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String contrasena = mContrasena.getText().toString().trim();
        String rContrasena = mRepiteContrasena.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(nombre)){
            mNombre.setError(getString(R.string.requerido));
            focusView=mNombre;
            cancel = true;
        }else if(TextUtils.isEmpty(pApellido)){
            mPrimerApellido.setError(getString(R.string.requerido));
            focusView = mPrimerApellido;
            cancel = true;
        }else if(TextUtils.isEmpty(sApellido)){
            mSegundoApellido.setError(getString(R.string.requerido));
            focusView = mSegundoApellido;
            cancel = true;
        }else if(TextUtils.isEmpty(email)){
            mEmail.setError(getString(R.string.requerido));
            focusView = mEmail;
            cancel = true;
        }else if(!isEmailValid(email)){
            mEmail.setError(getString(R.string.error_email));
            focusView = mEmail;
            cancel = true;
        }else if(TextUtils.isEmpty(contrasena)){
            mContrasena.setError(getString(R.string.requerido));
            focusView = mContrasena;
            cancel = true;
        }else if(TextUtils.isEmpty(rContrasena)){
            mRepiteContrasena.setError(getString(R.string.requerido));
            focusView = mRepiteContrasena;
            cancel = true;
        }else if(!TextUtils.equals(contrasena,rContrasena)){
            mRepiteContrasena.setError(getString(R.string.no_coinciden));
            focusView = mRepiteContrasena;
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus();
        }
        else{
            mProgressBar.setVisibility(View.VISIBLE);

            //create user
            auth.createUserWithEmailAndPassword(email, contrasena)
                    .addOnCompleteListener(RegistrarActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Toast.makeText(RegistrarActivity.this, "Usuario creado con éxito:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                mProgressBar.setVisibility(View.GONE);
                                try {
                                    throw task.getException();
                                }  catch(FirebaseAuthUserCollisionException e) {
                                    mEmail.setError(getString(R.string.error_user_exists));
                                    mEmail.requestFocus();
                                } catch(Exception e) {
                                    Toast.makeText(RegistrarActivity.this, "Autenticación fallida." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                                //String uid = fUser.getUid();
                                User user = new User(nombre,pApellido,sApellido);

                                mDatabase.child("users").child(fUser.getUid()).setValue(user);
                                mProgressBar.setVisibility(View.GONE);
                                startActivity(new Intent(RegistrarActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    });
        }

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    public void resetError(){
        mNombre.setError(null);
        mPrimerApellido.setError(null);
        mSegundoApellido.setError(null);
        mEmail.setError(null);
        mContrasena.setError(null);
        mRepiteContrasena.setError(null);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mProgressBar.setVisibility(View.GONE);
    }
}
