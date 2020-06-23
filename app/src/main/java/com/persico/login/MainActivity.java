package com.persico.login;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 5000;
    ImageView logo;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView logo = findViewById(R.id.logoView);

        mAuth = FirebaseAuth.getInstance();

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animazione);
        logo.startAnimation(anim);

        View sp = findViewById(R.id.splashProgress);
        ObjectAnimator.ofInt(sp, "progress", 100)
                .setDuration(3000)
                .start();

        new Handler().postDelayed (new Runnable() { //delay between splash and Login
            @Override
            public void run() {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user==null){
                    Intent i = new Intent (getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                    finish ();
                } else {
                    Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);

    }

}
