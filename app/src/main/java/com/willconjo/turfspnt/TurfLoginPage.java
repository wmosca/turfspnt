package com.willconjo.turfspnt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.view.View;

public class TurfLoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turf_login_page);

        // Pull the EditTexts and Button from the activity
        Button loginButton = findViewById(R.id.submitButton);
        //EditText emailEditText = findViewById(R.id.emailField);
        //EditText passwordEditText = findViewById(R.id.passwordField);

        // Set the onClick listener for the button
        loginButton.setOnClickListener(
            new View.OnClickListener(){
                public void onClick(View view){
                    Intent intent = new Intent(TurfLoginPage.this, TurfHomePage.class);
                    startActivity(intent);
                }
            }
        );
    }
}
