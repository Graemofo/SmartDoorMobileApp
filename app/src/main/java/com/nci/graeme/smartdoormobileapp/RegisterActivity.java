package com.nci.graeme.smartdoormobileapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
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
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignUp;
    private FirebaseAuth mAuth;
    private ProgressDialog progress;
    private TextView textViewLoginHere;
    private Typeface typeFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            // Start Profile Activity
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewSignUp = findViewById(R.id.textViewSignUp);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        progress = new ProgressDialog(this);
        textViewLoginHere = findViewById(R.id.textViewLoginHere);
        typeFace = Typeface.createFromAsset(this.getAssets(), "fonts/dodger3.ttf");
        textViewLoginHere.setTypeface(typeFace);



        buttonSignIn.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter A Valid Email !", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter A Password !", Toast.LENGTH_SHORT).show();
            return;
        }
        progress.setMessage("Loading....");
        progress.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progress.dismiss();
                if(task.isSuccessful()){
                    //Start profile activity
                    finish();
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        if(v == buttonSignIn){
            userLogin();
        }
        if(v == textViewSignUp){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
