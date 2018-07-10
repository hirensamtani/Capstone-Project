package com.example.hirensamtani.pizzafeedback;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;

public class Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        setEmailBackgroundTrigger();

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                Intent i = new Intent(Splash.this, GetLocation.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);


    }


    public void setEmailBackgroundTrigger(){
        Calendar cal = Calendar.getInstance();

        Intent intent = new Intent(this, EmailFeedbackService.class);
        PendingIntent pintent = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        int alarm_interval_secs = 5*60*1000;
        //5 mins
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), alarm_interval_secs, pintent);
    }
}
