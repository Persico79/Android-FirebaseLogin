package com.persico.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class FirstActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private AccessTokenTracker accessTokenTracker;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private ImageView mPhoto;
    private TextView mNomeUtente;
    private TextView mEmail;

    @Override
    public void onStart() {
        super.onStart();
        updateUI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);



        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_toggle);

        navigationView.setNavigationItemSelectedListener(this);

        //Prende i dati dell'utente e li mette nell'header del navigation menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.user_navigation_text);
        TextView navEmail = (TextView) headerView.findViewById(R.id.mail_navigation_text);
        ImageView navPhoto = (ImageView) headerView.findViewById(R.id.photo_navigation_id);
        if (firebaseUser != null) {
            navUsername.setText(firebaseUser.getDisplayName());
            navEmail.setText(firebaseUser.getEmail());
            if(firebaseUser.getPhotoUrl()!=null){
                String photoUrl = firebaseUser.getPhotoUrl().toString();
                Picasso.get().load(photoUrl).into(navPhoto);
            }
        }



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_closed){
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };


    }

/*    private void loadUserInformations() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            *//*if(user.getPhotoUrl() != null){
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(mPhoto);
            }*//*
            if(user.getDisplayName()!=null){
                mNomeUtente.setText(user.getDisplayName());
            }
        }
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.layout_menu_logout, menu);
        //loadUserInformations();
        return true;
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mNomeUtente.setText(currentUser.getDisplayName());
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.logout_id){
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            updateUI();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){ //If there isn't a current user it goes to LoginActivity
            Intent iToLogin = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(iToLogin);
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
