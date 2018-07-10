package com.example.hirensamtani.pizzafeedback;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;

import com.example.hirensamtani.EmailBean;
import com.example.hirensamtani.pizzafeedback.model.FeedBackContract;
import com.example.hirensamtani.pizzafeedback.utils.ManageEmailInternal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hirensamtani on 23/6/16.
 */
public class EmailFeedbackService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        DateFormat df =  new SimpleDateFormat("yyyy-MM-dd");
        Date dt = new Date();

        String entryDate = df.format(dt);
        String sDateFilterSelection =
                FeedBackContract.FeedbackEntry.TABLE_NAME+
                        "." + FeedBackContract.FeedbackEntry.FEEDBACK_MASTER_COLUMN_DATE + " like '%"+entryDate+"%' "+
                " AND "+FeedBackContract.FeedbackEntry.FEEDBACK_MASTER_COLUMN_IS_SYNCED+" = 'N'";

        Cursor curFeedBackForToday = context.getContentResolver().query
                (FeedBackContract.FeedbackEntry.CONTENT_URI,null,sDateFilterSelection,null,null);

        while(curFeedBackForToday.moveToNext()){


            EmailBean emailBean = new EmailBean();
            emailBean.setFromEmailAddress(context.getString(R.string.fromEmail));
            emailBean.setFromEmailPassword(context.getString(R.string.fromEmailPassword));
            emailBean.setToEmail(context.getString(R.string.toEmail));
            emailBean.setHost(context.getString(R.string.hostEmail));

            String orderID = curFeedBackForToday.getString(curFeedBackForToday.getColumnIndex(
                    FeedBackContract.FeedbackEntry.FEEDBACK_MASTER_COLUMN_ORDER_ID
            ));
            String branch = curFeedBackForToday.getString(curFeedBackForToday.getColumnIndex(
                    FeedBackContract.FeedbackEntry.FEEDBACK_MASTER_COLUMN_BRANCH
            ));

            emailBean.setSubject(context.getString(R.string.email_sub)
                    +orderID
                    +" branch "
                    +branch);

            String [] ratingSplit = curFeedBackForToday.getString(curFeedBackForToday.getColumnIndex(
                    FeedBackContract.FeedbackEntry.FEEDBACK_MASTER_COLUMN_CUST_FEED
            )).split(",");


            Resources resources = context.getResources();


            String[] questionList = resources.getStringArray(R.array.questionList);



            String message = context.getString(R.string.email_mess);

            for(int i=0;i<questionList.length;i++){
                message+="\n";
                message+=questionList[i];
                message+="\n";
                String[] feedbackSplit = ratingSplit[i].split("-");
                message+=feedbackSplit[1]+"/"+"5";
                message+="\n";
            }

            emailBean.setMessage(message);

            ManageEmailInternal manageEmail = new ManageEmailInternal(emailBean,context,orderID);
            manageEmail.execute();



        }
        curFeedBackForToday.close();

    }
}
