package ve.com.strikersfran.myfamily;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class BienvenidaActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(BienvenidaActivity.this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_bienvenida);

        Button btnIniciar = (Button) findViewById(R.id.btn_iniciar);
        TextView btnRegistrar = (TextView) findViewById(R.id.btn_registrar);

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BienvenidaActivity.this,LoginActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BienvenidaActivity.this,RegistrarActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }
}
