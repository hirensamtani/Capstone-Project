package com.example.hirensamtani.pizzafeedback.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HirenS on 07-06-2016.
 */
public class FeedBackDBHelper extends SQLiteOpenHelper {
    public Context context;
    public static final String DATABASE_NAME = "FeedbackDBName.db";

    public static final String FEEDBACK_MASTER_TABLE_NAME = "FeedBackMaster";
    public static final String FEEDBACK_MASTER_COLUMN_ORDER_ID = "OrderID";
    public static final String FEEDBACK_MASTER_COLUMN_CUST_FEED = "CustFeed";
    public static final String FEEDBACK_MASTER_COLUMN_DATE = "date";
    public static final String FEEDBACK_MASTER_COLUMN_BRANCH = "branch";
    public static final String FEEDBACK_MASTER_COLUMN_IS_SYNCED = "isSynced";

    public static final String BRANCH_MASTER_TABLE_NAME = "BranchMaster";
    public static final String BRANCH_MASTER_COLUMN_BRANCH_NAME = "BranchName";


    public FeedBackDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + FEEDBACK_MASTER_TABLE_NAME +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                FEEDBACK_MASTER_COLUMN_ORDER_ID + " TEXT UNIQUE NOT NULL, " +
                FEEDBACK_MASTER_COLUMN_CUST_FEED + " TEXT NOT NULL, " +
                FEEDBACK_MASTER_COLUMN_DATE + " TEXT NOT NULL, " +
                FEEDBACK_MASTER_COLUMN_BRANCH + " TEXT NOT NULL, " +
                FEEDBACK_MASTER_COLUMN_IS_SYNCED + " TEXT NOT NULL" +
                ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + BRANCH_MASTER_TABLE_NAME +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                BRANCH_MASTER_COLUMN_BRANCH_NAME + " TEXT UNIQUE NOT NULL "+
                ")");




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FEEDBACK_MASTER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BRANCH_MASTER_COLUMN_BRANCH_NAME);
        onCreate(db);
    }









    public int checkFeedbackCount(String CRFNo) {
        int feedBackCount = 0;
        String[] CRFNoArgs = {CRFNo};


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select COUNT(" + FEEDBACK_MASTER_COLUMN_ORDER_ID + ") from "
                + FEEDBACK_MASTER_TABLE_NAME
                + " where " + FEEDBACK_MASTER_COLUMN_ORDER_ID + " LIKE ?", CRFNoArgs);
        res.moveToFirst();
        feedBackCount = res.getInt(0);


        return feedBackCount;
    }


}