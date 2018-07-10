package com.example.hirensamtani.pizzafeedback.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by HirenS on 17-06-2016.
 */
public class FeedBackContract {

    public static final String CONTENT_AUTHORITY = "com.example.hirensamtani.pizzafeedback";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);



    public static final String PATH_FEEDBACK = "FeedBackMaster";
    public static final String PATH_BRANCH = "BranchMaster";


    public static final class FeedbackEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FEEDBACK).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FEEDBACK;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FEEDBACK;

        public static final String TABLE_NAME = "FeedBackMaster";

        public static final String FEEDBACK_MASTER_COLUMN_ORDER_ID = "OrderID";
        public static final String FEEDBACK_MASTER_COLUMN_CUST_FEED = "CustFeed";
        public static final String FEEDBACK_MASTER_COLUMN_DATE = "date";
        public static final String FEEDBACK_MASTER_COLUMN_BRANCH = "branch";
        public static final String FEEDBACK_MASTER_COLUMN_IS_SYNCED = "isSynced";

        public static Uri buildFeedackUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }




    public static final class BranchEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BRANCH).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BRANCH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BRANCH;

        public static final String TABLE_NAME = "BranchMaster";

        public static final String BRANCH_MASTER_COLUMN_BRANCH_NAME = "BranchName";

        public static Uri buildBranchUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }


}
