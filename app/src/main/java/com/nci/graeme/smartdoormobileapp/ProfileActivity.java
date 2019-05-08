package com.nci.graeme.smartdoormobileapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView textViewUserEmail;
    private Typeface typeFace;
    private Button logoutButton;
    private WebView webView;
    private Button openDoorButton;
    private Button create;
    private ImageView imageView;
    private EditText username;
    private TextToSpeech ts;
    private DatabaseReference smartdatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//Create the text to speech object called ts
        ts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    ts.setLanguage(Locale.US);
                }
            }
        });
// Reference to Firebase Database called smartDatabase
        smartdatabase = FirebaseDatabase.getInstance().getReference();
//Reference to Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
//If there is no current user, show register activity
        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        }
        FirebaseUser user = mAuth.getCurrentUser();  //Get current user
        logoutButton = findViewById(R.id.logoutButton); // Connect to logout button
        textViewUserEmail = findViewById(R.id.textViewUserEmail); //Connect to user email view to display user email
        typeFace = Typeface.createFromAsset(this.getAssets(), "fonts/dodger3.ttf"); //Font for user email
        textViewUserEmail.setTypeface(typeFace);
        textViewUserEmail.setText("Welcome "+user.getEmail());
        openDoorButton = findViewById(R.id.openDoorButton); //Connect open door button
        username = findViewById(R.id.getName); // Get user name for adding to the data set
        create = findViewById(R.id.createDataset); // Connect to the create button
        imageView = findViewById(R.id.imageViewer); // Image View to display images from front door camera

//Using Picasso to pass and display the current image in Firebase Storage in the image view
        String url = "https://firebasestorage.googleapis.com/v0/b/smartdoormobileapp.appspot.com/o/live%2FVisitor.jpg?alt=media&token=e154696c-f452-49bc-965e-2f047deae342";
        Picasso.get().load(url).into(imageView);


// Set onclick listeners to the 3 buttons (Open, Logout, Create)
        logoutButton.setOnClickListener(this);
        openDoorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDoor();
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDataSet();
            }
        });

       //  new RefreshData().execute();
    }

    private void createDataSet(){
//        Toast.makeText(this.getBaseContext(), "Stand in front of door", Toast.LENGTH_LONG).show();
        AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
        alertDialog.setTitle("EZ-Entry");
        alertDialog.setMessage("Please stand in front of the smart door");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_blue_bright);


        String instructions = "Please stand in front of the door and stare in to the camera";
        ts.speak(instructions, TextToSpeech.QUEUE_FLUSH, null);
        String create = "Create";
        String name = username.getText().toString();
        smartdatabase.child("Data").setValue(create);
        smartdatabase.child("Name").setValue(name);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
          String empty = "null";
            smartdatabase.child("Data").setValue(empty);
        }

    }

    private void openDoor(){
        String instructions = "Door is unlocked";
        ts.speak(instructions, TextToSpeech.QUEUE_FLUSH, null);
        String door = "Open";
//        smartdatabase.child("Door").setValue(door);
        new SendDoorRequest().execute(door);
//        Toast.makeText(this.getBaseContext(), "Door Unlocked", Toast.LENGTH_LONG).show();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            String instructions2 = "Door has been locked";
            ts.speak(instructions2, TextToSpeech.QUEUE_FLUSH, null);
             door = "Closed";
//            smartdatabase.child("Door").setValue(door);
            new SendDoorRequest().execute(door);
//            Toast.makeText(this.getBaseContext(), "Door Locked", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onClick(View v) {
        if(v == logoutButton){
            mAuth.signOut();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private class SendDoorRequest extends AsyncTask<String, Void, Object> {
        @Override
        protected Void doInBackground(String... params) {
           // Log.d("RefreshData", "Entered onPostExecute");
            String param = params[0];
            if(param != null && (param.equalsIgnoreCase("open") || param.equalsIgnoreCase("closed"))){
                try{
                    smartdatabase.child("Door").setValue(param);
                }
                catch (Exception e) {
                    Log.d("SendDoorRequest", e.getMessage());
                }
            }
      //  Log.d("RefreshData", "request created....");
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
              Log.d("SendDoorRequest", "Entered onPostExecute");
        }
    }
//    private class RefreshData extends AsyncTask<Void, Void, Object> {
//        @Override
//        protected Void doInBackground(Void... voids) {
//           // Log.d("RefreshData", "Entered onPostExecute");
//            try{
//                Thread.sleep(3000);
//            }
//            catch (InterruptedException e) {
//                Log.d("RefreshData", e.getMessage());
//            }
//      //  Log.d("RefreshData", "request created....");
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object result) {
//              Log.d("RefreshData", "Entered onPostExecute");
//              try {
//                  String url = "https://firebasestorage.googleapis.com/v0/b/smartdoormobileapp.appspot.com/o/live%2FVisitor.jpg?alt=media&token=e154696c-f452-49bc-965e-2f047deae342";
//                  Picasso.get().load(url).into(imageView);
//              }catch(Exception e){
//                  Log.d("RefreshData", e.getMessage());
//                  Toast.makeText(getApplicationContext(), "Failed to retrieve latest image", Toast.LENGTH_SHORT).show();
//              }
//            new RefreshData().execute();
//        }
//    }



}  //end of class
