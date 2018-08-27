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
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText mEmail;
    private FirebaseAuth auth;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mEmail = (EditText) findViewById(R.id.rc_email);
        Button btnRecuperar = (Button) findViewById(R.id.btn_recuperar);
        TextView txtIniciar = (TextView) findViewById(R.id.txt_rc_iniciar);

        mProgressBar = (ProgressBar) findViewById(R.id.rc_progress_bar);

        auth = FirebaseAuth.getInstance();

        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                mEmail.setError(null);

                if(TextUtils.isEmpty(email)){

                    mEmail.setError(getString(R.string.requerido));
                    mEmail.requestFocus();
                    return;
                }

                mProgressBar.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Se ha enviado un correo con el link para resetear su contraseña",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Ocurrió un error al envir el correo",Toast.LENGTH_SHORT).show();
                        }
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

                /*Toast.makeText(getApplicationContext(),"Se ha enviado un correo con su contraseña",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ResetPasswordActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();*/
            }
        });

        txtIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(ResetPasswordActivity.this,LoginActivity.class);
                //startActivity(intent);
                finish();
            }
        });
    }
}
