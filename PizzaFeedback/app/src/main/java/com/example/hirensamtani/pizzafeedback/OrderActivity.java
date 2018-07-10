package com.example.hirensamtani.pizzafeedback;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hirensamtani.pizzafeedback.model.FeedBackContract;

public class OrderActivity extends AppCompatActivity {
    String branchName="";
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Intent intent = getIntent();

        branchName = intent.getStringExtra("branchName");
        TextView branchNameOrder = (TextView) findViewById(R.id.branchNameOrder);
        branchNameOrder.setText(branchName);

        Button nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNext(v);
            }
        });


    }

    public void onClickNext(View view){
        EditText OrderID = (EditText) findViewById(R.id.orderID);
        EditText OrderIDReEntered = (EditText) findViewById(R.id.orderIDReEntered);

        String OrderIDStr = OrderID.getText().toString();
        String OrderIDReEnteredStr = OrderIDReEntered.getText().toString();

        String mess = context.getString(R.string.validation_order_id);

        if(OrderIDStr.equals("")){
            Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
        }
        else if(!OrderIDStr.equals(OrderIDReEnteredStr)){
            mess = context.getString(R.string.validation_order_id_mismatch);
            Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(this,FeedbackActivity.class);
            intent.putExtra("OrderID",OrderIDStr);
            intent.putExtra("branchName",branchName);
            startActivity(intent);
        }

    }
}
