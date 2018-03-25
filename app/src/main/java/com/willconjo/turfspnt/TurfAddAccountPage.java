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
                        if(monthlyString.equals("0.0")){
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
                        // Create WTFs
                        FileOutputStream dOutputStream;
                        FileOutputStream wOutputStream;
                        FileOutputStream mOutputStream;
                        try {
                            dOutputStream = openFileOutput("dailyWTF.txt", Context.MODE_PRIVATE);
                            wOutputStream = openFileOutput("weeklyWTF.txt", Context.MODE_PRIVATE);
                            mOutputStream = openFileOutput("monthlyWTF.txt", Context.MODE_PRIVATE);
                            dOutputStream.write(dString.getBytes());
                            wOutputStream.write(wString.getBytes());
                            mOutputStream.write(mString.getBytes());
                            dOutputStream.close();
                            wOutputStream.close();
                            mOutputStream.close();
                            System.out.println("Wrote the three WTFs");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        startService(intentForService);
                        startActivity(intent);
                        updateWidgets(getApplicationContext());
                    }
                }
        );
    }

    public static void updateWidgets(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), TurfWidgetDaily.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        int[] ids = widgetManager.getAppWidgetIds(new ComponentName(context, TurfWidgetDaily.class));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            widgetManager.notifyAppWidgetViewDataChanged(ids, android.R.id.list);

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

}
