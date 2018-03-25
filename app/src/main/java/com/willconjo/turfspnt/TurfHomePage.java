package com.willconjo.turfspnt;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;

public class TurfHomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turf_home_page_empty);

        FloatingActionButton addAccountButton = (FloatingActionButton)findViewById(R.id.addAccountButton);
        addAccountButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View view){
                        Intent intent = new Intent(TurfHomePage.this, TurfAddAccountPage.class);
                        startActivity(intent);
                    }
                }
        );
    }

}
