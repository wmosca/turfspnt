package com.willconjo.turfspnt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;

public class TurfHomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turf_home_page_empty);
        Intent intent = new Intent(TurfHomePage.this, TurfWidgetService.class);
        startService(intent);
    }

}
