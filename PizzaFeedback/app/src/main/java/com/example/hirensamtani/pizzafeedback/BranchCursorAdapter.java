package com.example.hirensamtani.pizzafeedback;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by HirenS on 16-06-2016.
 */
public class BranchCursorAdapter extends RecyclerView.Adapter<BranchCursorAdapter.ViewHolder> {
    //private String[] mDataset;

    Cursor mCursor;
    private RecyclerView mRecyclerView;
    static Context context;


    public BranchCursorAdapter(Cursor mCursor, Context context) {
        this.mCursor = mCursor;
        this.context=context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mTextView=null;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.branchCityName);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText =  (EditText)v.getRootView().findViewById(R.id.branchName);
                    editText.setText(mTextView.getText());
                    Intent intent = new Intent(context, OrderActivity.class);
                    intent.putExtra("branchName",mTextView.getText());
                    context.startActivity(intent);
                }
            });
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BranchCursorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_location_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        mCursor.moveToPosition(position);
        String branchName = mCursor.getString(mCursor.getColumnIndex("BranchName"));
        holder.mTextView.setText(branchName);
        holder.mTextView.setContentDescription(branchName);

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();

    }

}
