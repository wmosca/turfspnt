package com.willconjo.turfspnt;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TurfAddAccountPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turf_add_account_page);

        Button createButton = findViewById(R.id.createButton);

        createButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View view){
                        Intent intent = new Intent(TurfAddAccountPage.this, TurfHomePage.class);
                        Intent intentForService = new Intent(TurfAddAccountPage.this, TurfWidgetService.class);

                        EditText rentEditText = findViewById(R.id.rentEditText);
                        EditText monthlyEditText = findViewById(R.id.monthlyEditText);

                        String rentString = rentEditText.getText().toString();
                        String monthlyString = monthlyEditText.getText().toString();

                        String[] rentStringArr = rentString.split("\\.");
                        int rentDollars = Integer.parseInt(rentStringArr[0]);
                        int rentCents = Integer.parseInt(rentStringArr[1]);
                        int monthlyDollars;
                        if(monthlyString.equals("0.00")){
                            monthlyDollars = 0;
                        }else{
                            monthlyDollars = Integer.parseInt(monthlyString);
                        }
                        int useThisForLimits = monthlyDollars - rentDollars;
                        String monthlyLimit = Integer.toString(useThisForLimits);
                        String weeklyLimit = Integer.toString((int) (useThisForLimits / 4));
                        String dailyLimit = Integer.toString((int) (useThisForLimits / 30));
                        String totalSpentDollars = "0";
                        String totalSpentCents = "00";
                        String dString = totalSpentDollars + " " + totalSpentCents + " " + dailyLimit;
                        String wString = totalSpentDollars + " " + totalSpentCents + " " + weeklyLimit;
                        String mString = totalSpentDollars + " " + totalSpentCents + " " + monthlyLimit;
                        // Create account file
                        FileOutputStream accountWriter;
                        try {
                            accountWriter = openFileOutput("accounts.txt", Context.MODE_PRIVATE);
                            accountWriter.write("demo 94153815323555238".getBytes());
                            accountWriter.close();
                            System.out.println("Writing new account file");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        updateWidgets(getApplicationContext(), dString, wString, mString);
                        intentForService.putExtra("monthlyLimit", monthlyLimit);
                        intentForService.putExtra("dailyLimit", dailyLimit);
                        intentForService.putExtra("weeklyLimit", weeklyLimit);
                        startService(intentForService);
                        startActivity(intent);
                    }
                }
        );
    }

    public static void updateWidgets(Context context, String dString, String wString, String mString) {
        String[] dParts = dString.split(" ");
        String[] wParts = wString.split(" ");
        String[] mParts = mString.split(" ");
        Intent dIntent = new Intent(context.getApplicationContext(), TurfWidgetDaily.class);
        Intent wIntent = new Intent(context.getApplicationContext(), TurfWidgetWeekly.class);
        Intent mIntent = new Intent(context.getApplicationContext(), TurfWidgetMonthly.class);
        System.out.println("dParts[0] = "+ dParts[0]);
        System.out.println("dParts[1] = "+ dParts[1]);
        System.out.println("dParts[2] = "+ dParts[2]);
        dIntent.putExtra("dollars", dParts[0]);
        dIntent.putExtra("cents", dParts[1]);
        dIntent.putExtra("total", dParts[2]);
        wIntent.putExtra("dollars", wParts[0]);
        wIntent.putExtra("cents", wParts[1]);
        wIntent.putExtra("total", wParts[2]);
        mIntent.putExtra("dollars", mParts[0]);
        mIntent.putExtra("cents", mParts[1]);
        mIntent.putExtra("total", mParts[2]);
        dIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        wIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        mIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        context.sendBroadcast(dIntent);
        context.sendBroadcast(wIntent);
        context.sendBroadcast(mIntent);
    }

}
