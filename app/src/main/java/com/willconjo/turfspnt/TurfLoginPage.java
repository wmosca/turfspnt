package com.willconjo.turfspnt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.widget.EditText;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.view.View;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

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

        // Write a test file
        /*try {
            PrintWriter writer = new PrintWriter("testFile.txt", "UTF-8");
            writer.println("The first line");
            writer.println("The second line");
            writer.close();
        }catch(Exception e){
            System.out.println(e);
        }*/
        String everything = "";
        try(BufferedReader br = new BufferedReader(new FileReader("fileFile.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(" ");
                line = br.readLine();
            }
            everything = sb.toString();
        }catch(Exception e){
            System.out.println("asdf");
        }
        Log.d("DICKSANDBALLS","FILE CONTENTS: " + everything);
    }
}
