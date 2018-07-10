package com.example.hirensamtani.pizzafeedback;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hirensamtani.pizzafeedback.model.FeedBackContract;
import com.example.hirensamtani.pizzafeedback.utils.ScreenUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HirenS on 16-06-2016.
 */
public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {
    private String[] mDataset;
    private RecyclerView mRecyclerView;
    private Context context;
    private RatingBar[] ratingBars;
    private int[] ratings = new int[20];
    private String OrderID = "";
    private String branchName="";
    // Provide a suitable constructor (depends on the kind of dataset)
    public FeedbackAdapter(String[] myDataset, Context context,
                     RatingBar[] ratingBars,
                     String OrderID,String branchName) {
        mDataset = myDataset;
//        this.mRecyclerView = mRecyclerView;
        this.context=context;
        this.ratingBars = ratingBars;
        this.OrderID = OrderID;
        this.branchName = branchName;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public RatingBar ratingBar;
        public ImageView queImage;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.info_text);
            ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
            queImage = (ImageView) v.findViewById(R.id.queImage);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FeedbackAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_feedback, parent, false);
        // set the view's size, margins, paddings and layout parameters
      //  ...
        ViewHolder vh = new ViewHolder(v);
        vh.setIsRecyclable(false);
        return vh;
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTextView.setText(mDataset[position]);
        holder.ratingBar.setRating(ratings[position]);
        holder.queImage.setImageDrawable(null);

        Resources resources = context.getResources();
        final int image_id = resources.getIdentifier("q"+(position+1), "drawable",
                context.getPackageName());
        ScreenUtils screenUtils = new ScreenUtils();
        screenUtils.loadImage(context,image_id,holder.queImage);
        final int pos=position;
        ratingBars[position] = holder.ratingBar;



        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //View nextView;

                ratings[position] = (int) rating;
                if(pos<getItemCount()-1){
                    //issue due to piccasso
                    //mRecyclerView.smoothScrollToPosition(pos+1);
                    mRecyclerView.scrollToPosition(pos+1);
                }

                if(pos==getItemCount()-1) {

                    boolean allQuestionsFilled = true;
                    for (int i = 0; i < getItemCount(); i++) {
                        if (ratings[i] == 0) {
                            Toast.makeText(context, context.getString(R.string.validation_fill_feedback), Toast.LENGTH_SHORT).show();
                            mRecyclerView.smoothScrollToPosition(i);
                            allQuestionsFilled = false;
                        }
                    }

                    if (allQuestionsFilled == true) {

                        String sOrderIDFilterSelection =
                                FeedBackContract.FeedbackEntry.TABLE_NAME +
                                        "." + FeedBackContract.FeedbackEntry.FEEDBACK_MASTER_COLUMN_ORDER_ID + " = " + OrderID;

                        Cursor curFeedBackForOrderID = context.getContentResolver().query
                                (FeedBackContract.FeedbackEntry.CONTENT_URI, null, sOrderIDFilterSelection, null, null);

                        if (curFeedBackForOrderID.getCount() == 0) {

                            ContentValues contentvalues = new ContentValues();
                            int ques_id = 1;
                            String feedbackString = ques_id + "-" + ratings[0];


                            for (int i = 1; i < getItemCount(); i++) {
                                ques_id = i + 1;
                                feedbackString = feedbackString + "," + ques_id + "-" + ratings[i];
                            }

                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date dt = new Date();

                            String entryDate = df.format(dt);

                            contentvalues.put(FeedBackContract.FeedbackEntry.FEEDBACK_MASTER_COLUMN_ORDER_ID, OrderID);
                            contentvalues.put(FeedBackContract.FeedbackEntry.FEEDBACK_MASTER_COLUMN_CUST_FEED, feedbackString);
                            contentvalues.put(FeedBackContract.FeedbackEntry.FEEDBACK_MASTER_COLUMN_DATE, entryDate);
                            contentvalues.put(FeedBackContract.FeedbackEntry.FEEDBACK_MASTER_COLUMN_BRANCH, branchName);
                            contentvalues.put(FeedBackContract.FeedbackEntry.FEEDBACK_MASTER_COLUMN_IS_SYNCED, "N");


                            context.getContentResolver().insert(FeedBackContract.FeedbackEntry.CONTENT_URI, contentvalues);


                            Toast.makeText(context, context.getString(R.string.feedback_success), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent();
                            intent.setAction("com.example.hirensamtani.pizzafeedback.FeedbackForTodayWidget.REFRESH");
                            context.sendBroadcast(intent);



                        }
                        else{
                            Toast.makeText(context,context.getString(R.string.feedback_exist), Toast.LENGTH_SHORT).show();
                        }

                            Intent intent = new Intent(context, OrderActivity.class);
                            intent.putExtra("branchName", branchName);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                            context.startActivity(intent);



                    }
                }
            }
        });
    }




    @Override
    public int getItemCount() {
        return mDataset.length;

    }



    public int[] getRatings(){
        return this.ratings;
    }
}
