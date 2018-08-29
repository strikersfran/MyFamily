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
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.util.HashMap;

import ve.com.strikersfran.myfamily.data.SharedPreferenceHelper;
import ve.com.strikersfran.myfamily.data.StaticConfig;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail,mContrasena;
    private FirebaseAuth auth;
    private LovelyProgressDialog mProgresDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProgresDialog = new LovelyProgressDialog(this).setCancelable(false);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);

        mEmail = (EditText) findViewById(R.id.lg_email);
        mContrasena = (EditText) findViewById(R.id.lg_clave);

        Button btnLogin = (Button) findViewById(R.id.btn_entrar);
        TextView txtResetPass = (TextView) findViewById(R.id.txt_reset_pass);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        txtResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ResetPasswordActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }

    public void login(){

        mEmail.setError(null);
        mContrasena.setError(null);

        String email = mEmail.getText().toString().trim();
        String contrasena = mContrasena.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(email)){
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
        }

        if(cancel){
            focusView.requestFocus();
        }
        else {
            mProgresDialog.setTitle("Iniciando sesión")
                    .setTopColorRes(R.color.primary_color)
                    .show();

            //authenticate user
            auth.signInWithEmailAndPassword(email, contrasena)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                mProgresDialog.dismiss();
                                try {
                                    throw task.getException();
                                } catch(FirebaseAuthInvalidCredentialsException e) {
                                    mContrasena.setError(getString(R.string.error_invalid_credenciales));
                                    mContrasena.requestFocus();
                                } catch (FirebaseAuthInvalidUserException e){
                                    mEmail.setError(getString(R.string.error_invalid_email));
                                    mEmail.requestFocus();
                                }catch (FirebaseNetworkException e) {
                                    new LovelyInfoDialog(LoginActivity.this) {
                                        @Override
                                        public LovelyInfoDialog setConfirmButtonText(String text) {
                                            findView(com.yarolegovich.lovelydialog.R.id.ld_btn_confirm).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dismiss();
                                                }
                                            });
                                            return super.setConfirmButtonText(text);
                                        }
                                    }.setTopColorRes(R.color.primary_color)
                                    //.setIcon(R.drawable.ic_info)
                                    .setTitle("Error al iniciar sesión")
                                    .setMessage("Problemas de conexión con internet, verifica e intente de nuevo")
                                    .setCancelable(false)
                                    .setConfirmButtonText("Ok")
                                    .show();
                                }catch(Exception e) {
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                saveUserInfo();
                            }
                        }
                    });
        }

    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    public void saveUserInfo() {

        FirebaseUser user = auth.getCurrentUser();
        StaticConfig.UID=user.getUid();

        FirebaseDatabase.getInstance().getReference("users").child(StaticConfig.UID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgresDialog.dismiss();
                HashMap hashUser = (HashMap) dataSnapshot.getValue();
                User userInfo = new User("","","","","");
                userInfo.setNombre((String) hashUser.get("nombre"));
                userInfo.setPrimerApellido((String) hashUser.get("primerApellido"));
                userInfo.setSegundoApellido((String) hashUser.get("segundoApellido"));
                userInfo.setEmail((String) hashUser.get("email"));
                userInfo.setAvatar((String) hashUser.get("avatar"));

                SharedPreferenceHelper.getInstance(LoginActivity.this).saveUserInfo(userInfo);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", databaseError.getMessage());
            }
        });
    }

}
