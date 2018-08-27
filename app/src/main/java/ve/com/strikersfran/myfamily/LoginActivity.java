package ve.com.strikersfran.myfamily;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail,mContrasena;
    private FirebaseAuth auth;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);

        mEmail = (EditText) findViewById(R.id.lg_email);
        mContrasena = (EditText) findViewById(R.id.lg_clave);
        mProgressBar = (ProgressBar) findViewById(R.id.lg_progress_bar);

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
            mProgressBar.setVisibility(View.VISIBLE);

            //authenticate user
            auth.signInWithEmailAndPassword(email, contrasena)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            mProgressBar.setVisibility(View.GONE);
                            if (!task.isSuccessful()) {
                                /*Toast.makeText(LoginActivity.this, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();*/
                                // there was an error
                                try {
                                    throw task.getException();
                                } catch(FirebaseAuthWeakPasswordException e) {
                                    mContrasena.setError(getString(R.string.error_weak_password));
                                    mContrasena.requestFocus();
                                } catch(FirebaseAuthInvalidCredentialsException e) {
                                    mEmail.setError(getString(R.string.error_invalid_credenciales));
                                    mEmail.requestFocus();
                                } catch(Exception e) {
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
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
    @Override
    protected void onResume() {
        super.onResume();
        mProgressBar.setVisibility(View.GONE);
    }

}
