package ve.com.strikersfran.myfamily;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Fragment mFragment = null;
    private NavigationView mNavView;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private TextView mUserName;
    private TextView mUserEmail;
    private CircleImageView mUserImagen;
    private DatabaseReference mUserRef;

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

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView mNavView = (NavigationView) findViewById(R.id.navview);

        View header=mNavView.getHeaderView(0);

        mUserName = (TextView) header.findViewById(R.id.m_user_name);
        mUserEmail = (TextView) header.findViewById(R.id.m_user_email);
        mUserImagen = (CircleImageView) header.findViewById(R.id.m_image_user);

        mUserEmail.setText(user.getEmail().toString());

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

            }
        });

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
                else{
                    StaticConfig.UID=user.getUid();
                    mUserRef = database.getReference("users").child(user.getUid());
                    mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //cambiar los datos en el menu
                            String nombre = dataSnapshot.child("nombre").getValue().toString();
                            String papellido = dataSnapshot.child("primerApellido").getValue().toString();
                            String sapellido = dataSnapshot.child("segundoApellido").getValue().toString();
                            String email = dataSnapshot.child("email").getValue().toString();
                            String avatar = dataSnapshot.child("avatar").getValue().toString();

                            User userInfo = new User(nombre,papellido,sapellido,avatar,email);

                            SharedPreferenceHelper.getInstance(MainActivity.this).saveUserInfo(userInfo);

                            mUserName.setText(nombre +" "+papellido);
                            mUserEmail.setText(email);
                            if(avatar.contentEquals("default")){
                                mUserImagen.setImageResource(R.drawable.avatar_default);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("ERROR", databaseError.getMessage());
                        }
                    });
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
}
