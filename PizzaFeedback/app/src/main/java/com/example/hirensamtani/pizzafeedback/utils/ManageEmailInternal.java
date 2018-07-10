package com.example.hirensamtani.pizzafeedback.utils;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.example.hirensamtani.EmailBean;
import com.example.hirensamtani.ManageEmail;
import com.example.hirensamtani.pizzafeedback.model.FeedBackContract;

public class ManageEmailInternal extends AsyncTask<Void,Void,Void> {
    private EmailBean emailBean;
    private Context context;
    private String orderID;
    private int resultEmail;

    public ManageEmailInternal(EmailBean emailBean,Context context,String orderID){
        this.emailBean = emailBean;
        this.context = context;
        this.orderID = orderID;
    }
    @Override
    protected Void doInBackground(Void... params) {
        ManageEmail manageEmail = new ManageEmail(emailBean);
        resultEmail=manageEmail.sendEmail(this.emailBean);
        return null;
    }


    protected void onPostExecute(Void result) {
        // dismiss progress dialog and update ui
        super.onPostExecute(result);
        if(resultEmail>1){
        ContentValues contentValues = new ContentValues();
    contentValues.put(FeedBackContract.FeedbackEntry.FEEDBACK_MASTER_COLUMN_IS_SYNCED,"Y");

    String selectionString = FeedBackContract.FeedbackEntry.FEEDBACK_MASTER_COLUMN_ORDER_ID+
            " = "+orderID;
    context.getContentResolver().update(FeedBackContract.FeedbackEntry.CONTENT_URI,contentValues,selectionString,null);
        }
    }





}
