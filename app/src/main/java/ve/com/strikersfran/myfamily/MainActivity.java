package ve.com.strikersfran.myfamily;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    private ActionBarDrawerToggle mDrawerToggle;
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

        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menu);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                        setDrawerState(false);

                        switch (menuItem.getItemId()) {
                            /*case R.id.menu_opcion_noticias:
                                mFragment = new NoticiasFragment();
                                //setDrawerState(true);
                                //fragmentTransaction = true;
                                break;*/
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
                            case R.id.menu_opcion_solicitudes:
                                mFragment = new SolicitudesFragment();
                                break;
                        }

                        //menuItem.setChecked(false);
                        getSupportActionBar().setTitle(menuItem.getTitle());

                        mDrawerLayout.closeDrawers();
                        return false;
                    }
                }
        );

        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.drawer_open, R.string.drawer_close){

            public void onDrawerOpened(@NonNull View drawerView) {
                super.onDrawerOpened(drawerView);

            }


            public void onDrawerClosed(@NonNull View drawerView) {
                super.onDrawerClosed(drawerView);
                if(mFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, mFragment)
                            .addToBackStack(null)
                            .commit();
                    mFragment = null;
                }
            }

            public void onDrawerStateChanged(int newState) {
                User userInfo = prefHelper.getUserInfo();
                mUserName.setText(userInfo.getNombre());
                mUserApellidos.setText(userInfo.getPrimerApellido()+" "+userInfo.getSegundoApellido());
                mUserEmail.setText(userInfo.getEmail());
                mUserImagen.setImageBitmap(ImageUtils.setImageAvatar(MainActivity.this,userInfo.getAvatar()));
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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
                    mUserImagen.setImageBitmap(ImageUtils.setImageAvatar(MainActivity.this,userInfo.getAvatar()));
                }
            }
        };
    }

    /*Esta funcion permite tomar la desicision de que boton mostrar en la barra cuando se cambia de fragmento*/
    public void setDrawerState(boolean isEnabled) {
        if ( isEnabled ) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerToggle.syncState();
            //getSupportActionBar().setHomeButtonEnabled(true);
        }
        else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            mDrawerToggle.syncState();
            //getSupportActionBar().setHomeButtonEnabled(false);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (/*mDrawerToggle.isDrawerIndicatorEnabled() &&*/ mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch(item.getItemId()) {
            case R.id.accion_salir:
                auth.signOut();
                break;
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

    /*Para detectar los eventos en le icono back de la toolbar*/
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    //este metodo es para detectar el ultimo fagmento y poder colocar el icono amburguesa en el toolbar
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Log.e("BackPressed","BackPressed activo");
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(getSupportFragmentManager().getBackStackEntryCount() > 1){
            Log.e("BackPressed","entro en cuenta > 1");
            getSupportFragmentManager().popBackStack();
        }else {
            Log.e("BackPressed","entro en cuenta < 1");
            setDrawerState(true);
            super.onBackPressed();
        }
    }

}
