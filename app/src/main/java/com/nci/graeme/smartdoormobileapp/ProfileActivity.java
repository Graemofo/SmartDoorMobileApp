package com.nci.graeme.smartdoormobileapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView textViewUserEmail;
    private Typeface typeFace;
    private Button logoutButton;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth =FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        }
        FirebaseUser user = mAuth.getCurrentUser();
        logoutButton = findViewById(R.id.logoutButton);
        textViewUserEmail = findViewById(R.id.textViewUserEmail);
        typeFace = Typeface.createFromAsset(this.getAssets(), "fonts/dodger3.ttf");
        textViewUserEmail.setTypeface(typeFace);
        textViewUserEmail.setText("Welcome "+user.getEmail());

        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.google.com");

        logoutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == logoutButton){
            mAuth.signOut();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
