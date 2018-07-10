package com.example.hirensamtani.pizzafeedback;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.RatingBar;

public class FeedbackActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Context context = this;
    String feedBackString;
    int mRecyclerViewSize=0;
    RatingBar[] ratingBars;
    String branchName="";
    String OrderID="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Intent intent = getIntent();
        branchName = intent.getStringExtra("branchName");
        OrderID = intent.getStringExtra("OrderID");

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);


        mRecyclerView.setHasFixedSize(true);




        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        Resources res = context.getResources();
        String[] myDataset = res.getStringArray(R.array.questionList);





        ratingBars = new RatingBar[myDataset.length];
        mAdapter = new FeedbackAdapter(myDataset,context,ratingBars,OrderID,branchName);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerViewSize = myDataset.length;

        //mRecyclerView.setNestedScrollingEnabled(false);


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    //[...]
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        //Remove swiped item from list and notify the RecyclerView

                        int position = viewHolder.getAdapterPosition();
                        //RatingBar ratingBar = viewHolder.getAdapterPosition();
                        if(swipeDir== ItemTouchHelper.LEFT){
                            swipeLeft(position);
                            //swipeLeft(viewHolder);
                        }

                        if(swipeDir== ItemTouchHelper.RIGHT){
                            swipeRight(position);
                        }

                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);


    }

    public void swipeLeft(int position){
        ratingBars[position].setRating(0);
        ratingBars[position].setRating(1);
        //mRecyclerView.smoothScrollToPosition(position+1);

    }

    public void swipeRight(int position){
        ratingBars[position].setRating(0);
        ratingBars[position].setRating(5);
        //mRecyclerView.smoothScrollToPosition(position+1);

    }





}
