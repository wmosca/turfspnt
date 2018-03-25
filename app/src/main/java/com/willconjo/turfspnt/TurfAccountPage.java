package com.willconjo.turfspnt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.ImageButton;

public class TurfAccountPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turf_account_page);


        // Pull the EditTexts and Button from the activity
        ImageButton accountButton = findViewById(R.id.accountButton);
        //EditText emailEditText = findViewById(R.id.emailField);
        //EditText passwordEditText = findViewById(R.id.passwordField);

        // Set the onClick listener for the button
        accountButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View view){
                        Intent intent = new Intent(TurfAccountPage.this, TurfAccountConfiguration.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
