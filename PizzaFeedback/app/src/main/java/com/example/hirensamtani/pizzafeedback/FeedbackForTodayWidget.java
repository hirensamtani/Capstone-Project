package com.example.hirensamtani.pizzafeedback;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.example.hirensamtani.pizzafeedback.model.FeedBackContract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class FeedbackForTodayWidget extends AppWidgetProvider {

    public static String REFRESH_ACTION = "com.example.hirensamtani.pizzafeedback.FeedbackForTodayWidget.REFRESH";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.feedback_for_today_widget);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.feedback_for_today_widget);


            DateFormat df =  new SimpleDateFormat("yyyy-MM-dd");
            Date dt = new Date();

            String entryDate = df.format(dt);
            String sDateFilterSelection =
                    FeedBackContract.FeedbackEntry.TABLE_NAME+
                            "." + FeedBackContract.FeedbackEntry.FEEDBACK_MASTER_COLUMN_DATE + " like '%"+entryDate+"%' ";


            String[] proj ={"count(*)"};

            Cursor cursor = context.getContentResolver().query
                    (FeedBackContract.FeedbackEntry.CONTENT_URI,proj,sDateFilterSelection,null,null);

            cursor.moveToNext();

            String feedBackCount = cursor.getString(0);


            CharSequence widgetText = context.getString(R.string.appwidget_text)+": "+feedBackCount;

            cursor.close();
            views.setTextViewText(R.id.feedbackCount, widgetText);

            //updateAppWidget(context, appWidgetManager, appWidgetId);
            Intent intent = new Intent(context, Splash.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, intent,0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
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
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        RemoteViews views =  new RemoteViews(context.getPackageName(), R.layout.feedback_for_today_widget);
        if (action.equals(REFRESH_ACTION)) {
            DateFormat df =  new SimpleDateFormat("yyyy-MM-dd");
            Date dt = new Date();

            String entryDate = df.format(dt);
            String sDateFilterSelection =
                    FeedBackContract.FeedbackEntry.TABLE_NAME+
                            "." + FeedBackContract.FeedbackEntry.FEEDBACK_MASTER_COLUMN_DATE + " like '%"+entryDate+"%' ";


            String[] proj ={"count(*)"};

            Cursor cursor = context.getContentResolver().query
                    (FeedBackContract.FeedbackEntry.CONTENT_URI,proj,sDateFilterSelection,null,null);

            cursor.moveToNext();

            String feedBackCount = cursor.getString(0);


            CharSequence widgetText = context.getString(R.string.appwidget_text)+": "+feedBackCount;

            cursor.close();
            views.setTextViewText(R.id.feedbackCount, widgetText);

        }
        else{
            super.onReceive(context, intent);
        }
        ComponentName componentName = new ComponentName(context, FeedbackForTodayWidget.class);
        AppWidgetManager.getInstance(context).updateAppWidget(componentName, views);
    }

}

