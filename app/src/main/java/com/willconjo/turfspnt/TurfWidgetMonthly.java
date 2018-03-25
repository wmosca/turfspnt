package com.willconjo.turfspnt;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class TurfWidgetMonthly extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.turf_widget_monthly);
        //views.setTextViewText(R.id.widgetMonthlyTitleText, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent){
        if(intent.getAction().equals("android.appwidget.action.APPWIDGET_UPDATE")) {
            if(intent.getStringExtra("dollars") != null) {
                String numerator = intent.getStringExtra("dollars");
                String numeratorCents = intent.getStringExtra("cents");
                String denominator = intent.getStringExtra("total");
                int progress = (int) (Double.parseDouble(numerator) / Integer.parseInt(denominator) * 100);
                String dollars = Integer.toString((int)(Integer.parseInt(denominator) - Double.parseDouble(numerator)));

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.turf_widget_monthly);
                rv.setTextViewText(R.id.widgetMonthlyBudgetDollarsText, "$"+dollars);
                rv.setTextViewText(R.id.widgetMonthlyBudgetCentsText, numeratorCents);
                rv.setProgressBar(R.id.monthlyProgressBar, 100, progress, false);

                if (progress >= 75) {
                    rv.setTextColor(R.id.widgetMonthlyBudgetDollarsText, context.getResources().getColor(R.color.widgetTextOver));
                    rv.setTextColor(R.id.widgetMonthlyBudgetCentsText, context.getResources().getColor(R.color.widgetTextOver));
                }else if (progress <= 25){
                    rv.setTextColor(R.id.widgetMonthlyBudgetDollarsText, context.getResources().getColor(R.color.widgetTextUnder));
                    rv.setTextColor(R.id.widgetMonthlyBudgetCentsText, context.getResources().getColor(R.color.widgetTextUnder));
                }

                AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
                int[] ids = widgetManager.getAppWidgetIds(new ComponentName(context, TurfWidgetMonthly.class));

                appWidgetManager.partiallyUpdateAppWidget(ids[0], rv);
            }
        }
    }
}

