package com.example.hirensamtani.pizzafeedback.model;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

import com.example.hirensamtani.pizzafeedback.R;

/**
 * Created by hirensamtani on 25/6/16.
 */
public class FeedbackProviderObserver extends ContentObserver {

    private AppWidgetManager widgetManager;
    private ComponentName componentName;


    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */


    public FeedbackProviderObserver(AppWidgetManager widgetManager,
                                    ComponentName componentName,Handler handler) {
        super(handler);
        this.widgetManager = widgetManager;
        this.componentName = componentName;

    }



    @Override
    public void onChange(boolean selfChange) {


        //widgetManager.notifyAppWidgetViewDataChanged(widgetManager.getAppWidgetIds(componentName), R.id.list_view);
    }


}
