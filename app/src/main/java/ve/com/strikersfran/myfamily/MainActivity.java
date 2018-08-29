package ve.com.strikersfran.myfamily;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import ve.com.strikersfran.myfamily.data.SharedPreferenceHelper;
import ve.com.strikersfran.myfamily.data.StaticConfig;
import ve.com.strikersfran.myfamily.util.ImageUtils;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Fragment mFragment = null;
    private NavigationView mNavView;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private TextView mUserName;
    private TextView mUserApellidos;
    private TextView mUserEmail;
    private CircleImageView mUserImagen;
    private DatabaseReference mUserRef;
    private SharedPreferenceHelper prefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        Toolbar appbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(appbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prefHelper = SharedPreferenceHelper.getInstance(MainActivity.this);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView mNavView = (NavigationView) findViewById(R.id.navview);

        View header=mNavView.getHeaderView(0);

        mUserName = (TextView) header.findViewById(R.id.m_user_name);
        mUserApellidos = (TextView) header.findViewById(R.id.m_user_apellidos);
        mUserEmail = (TextView) header.findViewById(R.id.m_user_email);
        mUserImagen = (CircleImageView) header.findViewById(R.id.m_image_user);

        mUserEmail.setText(user.getEmail().toString());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new NoticiasFragment())
                .commit();

        mNavView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {


                        switch (menuItem.getItemId()) {
                            case R.id.menu_opcion_noticias:
                                mFragment = new NoticiasFragment();
                                //fragmentTransaction = true;
                                break;
                            case R.id.menu_opcion_chat:
                                mFragment = new ChatsFragment();
                                //fragmentTransaction = true;
                                break;
                            case R.id.menu_opcion_mi_familia:
                                mFragment = new MiFamiliaFragment();
                                //fragmentTransaction = true;
                                break;
                            case R.id.menu_opcion_mi_perfil:
                                mFragment = new ProfileFragment();
                                //fragmentTransaction = true;
                                break;
                        }

                        menuItem.setChecked(true);
                        getSupportActionBar().setTitle(menuItem.getTitle());

                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                }
        );

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                if(mFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, mFragment)
                            .commit();
                    mFragment = null;
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                User userInfo = prefHelper.getUserInfo();
                mUserName.setText(userInfo.getNombre());
                mUserApellidos.setText(userInfo.getPrimerApellido()+" "+userInfo.getSegundoApellido());
                mUserEmail.setText(userInfo.getEmail());
                setImageAvatar(MainActivity.this,userInfo.getAvatar());
            }
        });

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
                else{
                    StaticConfig.UID=user.getUid();
                    User userInfo = prefHelper.getUserInfo();
                    mUserName.setText(userInfo.getNombre());
                    mUserApellidos.setText(userInfo.getPrimerApellido()+" "+userInfo.getSegundoApellido());
                    mUserEmail.setText(userInfo.getEmail());
                    setImageAvatar(MainActivity.this,userInfo.getAvatar());
                }
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.accion_salir:
                auth.signOut();
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
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

            mUserImagen.setImageBitmap(src);
        }catch (Exception e){
        }
    }

}
