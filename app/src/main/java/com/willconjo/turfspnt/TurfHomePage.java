package com.willconjo.turfspnt;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;

import java.io.File;

public class TurfHomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        if(in.getStringExtra("asdf") == null){
            Intent acctIntent = new Intent(TurfHomePage.this, TurfAccountPage.class);
            startActivity(acctIntent);
            return;
        }else {
            setContentView(R.layout.activity_turf_home_page_empty);

            FloatingActionButton addAccountButton = (FloatingActionButton) findViewById(R.id.addAccountButton);
            addAccountButton.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View view) {
                            Intent intent = new Intent(TurfHomePage.this, TurfAddAccountPage.class);
                            startActivity(intent);
                        }
                    }
            );
        }
    }

}
