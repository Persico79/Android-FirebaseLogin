package com.persico.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private static final String TAG2 = "Verifica";
    private static final String LOGIN_NAME = "Login_Name";
    private static final String NOME_KEY = "username";
//TODO verifica password immessa


    private EditText _passwordText, _nameText, _emailText;
    private Button _signupButton;
    private ImageView  backbtn;
    private String emailst,passst,namest;
    private TextView titletxt, desctxt;
    private LinearLayout reg_ll, _loginLink, namell, emailll, passwordll;

    private FirebaseAuth mAuth;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mAuth = FirebaseAuth.getInstance();

        _passwordText =  findViewById(R.id.input_password);
        _nameText =  findViewById(R.id.input_name);
        _emailText =  findViewById(R.id.input_email);
        _signupButton = findViewById(R.id.btn_signup);
        _loginLink = findViewById(R.id.link_login);
        namell =  findViewById(R.id.namell);
        emailll =  findViewById(R.id.emailll);
        passwordll =  findViewById(R.id.passwordll);
        titletxt = findViewById(R.id.titletxt);
        desctxt = findViewById(R.id.desctxt);
        reg_ll = findViewById(R.id.reg_ll);

        mAuth.useAppLanguage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namest = _nameText.getText().toString();
                emailst = _emailText.getText().toString();
                passst = _passwordText.getText().toString();

                validate();

                createAccount(emailst, passst);
            }
        });
    }

    private void createAccount(String email, String password){
        Log.d(TAG, "createAccount:" + email);
        if (!validate()) {
            return;
        }

        emailst = _emailText.getText().toString();
        passst = _passwordText.getText().toString();

        mAuth.createUserWithEmailAndPassword(emailst, passst)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.i(TAG, "qui stiamo dentro");
                                    if(task.isSuccessful()){
                                        Toast.makeText(SignUpActivity.this, "Registrated successfully. Please check your email for verification", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "createUserWithEmail:success");

                                        saveName(); //Shared preferences
                                        //L'intent ridireziona alla LoginActivity dopo la registrazione
                                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {

                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void saveName(){ //Shared preferences save the user name
        namest = _nameText.getText().toString();
        SharedPreferences prefs = getSharedPreferences(LOGIN_NAME, 0);
        prefs.edit().putString(NOME_KEY, namest).apply();
    }


    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();


        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("At least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Enter a valid email address");
            _emailText.setFocusable(true);
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            _passwordText.setError("The password should be at least of 4 characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
