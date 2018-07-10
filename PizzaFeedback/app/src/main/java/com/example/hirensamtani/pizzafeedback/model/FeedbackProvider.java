package com.example.hirensamtani.pizzafeedback.model;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.hirensamtani.pizzafeedback.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HirenS on 17-06-2016.
 */
public class FeedbackProvider extends ContentProvider{

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FeedBackDBHelper mFeedBackDBHelper;
    static final int FEEDBACK = 100;
    static final int FEEDBACK_WITH_ID = 101;
    static final int BRANCH = 102;
    static final int FEEDBACK_FOR_TODAY = 103;
    static DateFormat df =  new SimpleDateFormat("yyyy-MM-dd");
    static Date dt = new Date();

    static String entryDate = df.format(dt);

    private static final SQLiteQueryBuilder sInsertedFeedbackQueryBuilder;


    private static final SQLiteQueryBuilder sBranchQueryBuilder;

    static{
        sInsertedFeedbackQueryBuilder = new SQLiteQueryBuilder();
        sBranchQueryBuilder = new SQLiteQueryBuilder();

        sInsertedFeedbackQueryBuilder.setTables(
                FeedBackContract.FeedbackEntry.TABLE_NAME + " INNER JOIN " +
                        FeedBackContract.BranchEntry.TABLE_NAME +
                        " ON " + FeedBackContract.FeedbackEntry.TABLE_NAME +
                        "." + FeedBackContract.FeedbackEntry.FEEDBACK_MASTER_COLUMN_BRANCH +
                        " = " + FeedBackContract.BranchEntry.TABLE_NAME +
                        "." + FeedBackContract.BranchEntry.BRANCH_MASTER_COLUMN_BRANCH_NAME);

        sBranchQueryBuilder.setTables(
                FeedBackContract.BranchEntry.TABLE_NAME
        );


    }


    private static final String sDateFilterSelection =
            FeedBackContract.FeedbackEntry.TABLE_NAME+
                    "." + FeedBackContract.FeedbackEntry.FEEDBACK_MASTER_COLUMN_DATE + " like '%"+entryDate+"%' ";

    @Override
    public boolean onCreate() {
        mFeedBackDBHelper = new FeedBackDBHelper(getContext());
        insertDeleteCities();
        return true;
    }

    public void insertDeleteCities(){


        Resources res = getContext().getResources();
        String[] myDataset = res.getStringArray(R.array.branchList);

        SQLiteDatabase db = mFeedBackDBHelper.getWritableDatabase();
        ContentValues contentValues;

        //Delete cities
        db.delete(FeedBackContract.PATH_BRANCH,"1=1",null);

        //Insert Cities. To be handled later by syncadapter in future development
        for(String branchName:myDataset){
            contentValues = new ContentValues();
            contentValues.put(FeedBackContract.BranchEntry.BRANCH_MASTER_COLUMN_BRANCH_NAME,branchName);
            db.insert(FeedBackContract.PATH_BRANCH, null, contentValues);
        }



    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {
            case FEEDBACK:
            {
                retCursor = getFeedBackList(uri, projection, selection,sortOrder);
                break;
            }

            case BRANCH:
            {
                retCursor = getBranchList(uri, null, null);
                break;
            }
            case FEEDBACK_FOR_TODAY:{
                retCursor = getTodaysFeedBackList(uri,null,null);
            }


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);


        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        switch (sUriMatcher.match(uri)) {
            case FEEDBACK:
                return FeedBackContract.FeedbackEntry.CONTENT_TYPE;
            case BRANCH:
                return FeedBackContract.BranchEntry.CONTENT_ITEM_TYPE;
            case FEEDBACK_FOR_TODAY:
                return FeedBackContract.FeedbackEntry.CONTENT_TYPE;

            default:
                return null;

        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mFeedBackDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FEEDBACK: {
                long _id = db.insert(FeedBackContract.FeedbackEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FeedBackContract.FeedbackEntry.buildFeedackUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case BRANCH: {
                long _id = db.insert(FeedBackContract.BranchEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FeedBackContract.BranchEntry.buildBranchUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Context context= this.getContext();
        context.getContentResolver().notifyChange(uri, null);



        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mFeedBackDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if ( null == selection ) selection = "1";
        switch (match) {
            case FEEDBACK:
                rowsDeleted = db.delete(
                        FeedBackContract.FeedbackEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BRANCH:
                rowsDeleted = db.delete(
                        FeedBackContract.BranchEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mFeedBackDBHelper.getWritableDatabase();
        int rowsUpdated;
        final int match = sUriMatcher.match(uri);
        int returnVal;

        switch (match) {
            case FEEDBACK: {
                rowsUpdated = db.update(FeedBackContract.FeedbackEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Context context= getContext();
        context.getContentResolver().notifyChange(uri, null);

        if (rowsUpdated != 0) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FeedBackContract.CONTENT_AUTHORITY;


        matcher.addURI(authority, FeedBackContract.PATH_FEEDBACK, FEEDBACK);

        matcher.addURI(authority, FeedBackContract.PATH_FEEDBACK + "/*", FEEDBACK);
        matcher.addURI(authority, FeedBackContract.PATH_FEEDBACK + "/*/#", FEEDBACK);

        matcher.addURI(authority, FeedBackContract.PATH_FEEDBACK + "/*", FEEDBACK_FOR_TODAY);
        matcher.addURI(authority, FeedBackContract.PATH_FEEDBACK + "/*/#", FEEDBACK_FOR_TODAY);

        matcher.addURI(authority, FeedBackContract.PATH_BRANCH, BRANCH);

        matcher.addURI(authority, FeedBackContract.PATH_BRANCH + "/*", BRANCH);
        matcher.addURI(authority, FeedBackContract.PATH_BRANCH + "/*/#", BRANCH);


        return matcher;
    }


    private Cursor getFeedBackList(
            Uri uri, String[] projection,String selection,String sortOrder) {
        return sInsertedFeedbackQueryBuilder.query(mFeedBackDBHelper.getReadableDatabase(),
                projection,
                selection,
                null,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getTodaysFeedBackList(
            Uri uri, String[] projection, String sortOrder) {


        String selection = sDateFilterSelection;


        return sInsertedFeedbackQueryBuilder.query(mFeedBackDBHelper.getReadableDatabase(),
                projection,
                selection,
                null,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getBranchList(
            Uri uri, String[] projection, String sortOrder) {
        return sBranchQueryBuilder.query(mFeedBackDBHelper.getReadableDatabase(),
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }



    @Override
    @TargetApi(11)
    public void shutdown() {
        mFeedBackDBHelper.close();
        super.shutdown();
    }
}
