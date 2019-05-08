package com.nci.graeme.smartdoormobileapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailField;
    private EditText passwordField;
    private Button mRegisterBtn;
    private TextView textViewSignin;
    private TextView registerHere;
    private Typeface typeFace;
    private ImageView mImageView;

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;



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
        mImageView = findViewById(R.id.imageViewer);
        getSupportActionBar().hide();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference islandRef = mStorageRef.child("images/Graeme.jpeg");

        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });









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

    @GlideModule
    public class MyAppGlideModule extends AppGlideModule {

        @Override
        public void registerComponents(Context context, Glide glide, Registry registry) {
            // Register FirebaseImageLoader to handle StorageReference
            registry.append(StorageReference.class, InputStream.class,
                    new FirebaseImageLoader.Factory());
        }
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
