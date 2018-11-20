package com.nci.graeme.smartdoormobileapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
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
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailField;
    private EditText passwordField;
    private Button mRegisterBtn;
    private TextView textViewSignin;
    private TextView registerHere;
    private Typeface typeFace;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailField = findViewById(R.id.editTextEmail);
        passwordField = findViewById(R.id.editTextPassword);
        mRegisterBtn = findViewById(R.id.buttonRegister);
        textViewSignin = findViewById(R.id.textViewSignIn);
        registerHere = findViewById(R.id.textViewRegisterHere);
        typeFace = Typeface.createFromAsset(this.getAssets(), "fonts/dodger3.ttf");
        registerHere.setTypeface(typeFace);



        mRegisterBtn.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            // Start Profile Activity
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }
      //  bringToRegisterPage();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void registerUser(){
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter A Valid Email !", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter A Password !", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                           // Toast.makeText(MainActivity.this, "Registered Succesfully!", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }else{
                            Toast.makeText(MainActivity.this, "Failed To Register. Please Try Again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

//    private void bringToRegisterPage(){
//        TextView textViewSignIn =  findViewById(R.id.textViewSignIn);
//        textViewSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
//            }
//        });
//    }

    @Override
    public void onClick(View v) {

        if(v == mRegisterBtn){
            registerUser();
        }
        if(v == textViewSignin){
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        }
    }
}
