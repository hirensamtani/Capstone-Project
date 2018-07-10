package com.example.hirensamtani.pizzafeedback.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hirensamtani.pizzafeedback.OrderActivity;
import com.example.hirensamtani.pizzafeedback.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HirenS on 22-06-2016.
 */
public class LocationUtils extends AsyncTask<Void,Void,Void> {
    private ProgressDialog dialog;
    private Context context;
    private boolean emptyString=true;
    String[] location = new String[2];
    String cityName="";
    EditText editText;
    List cityList = new ArrayList();

    public LocationUtils(Context context, String[] location,EditText editText,List cityList){
        this.context=context;
        this.location = location;
        this.editText = editText;
        this.cityList = cityList;
    }

    public LocationUtils(String[] location) {
        this.context=context;
        this.location = location;
        this.editText = editText;
    }


    public String getLocationBranch() {
        String branchCityName="";
        String retJsonList = null;


        HttpURLConnection connection = null;
        Uri.Builder dataUri = new Uri.Builder();



        dataUri.scheme("http")
                .authority("maps.googleapis.com")
                .appendPath("maps")
                .appendPath("api")
                .appendPath("geocode")
                .appendPath("json")
                .appendQueryParameter("latlng",location[0]+","+location[1])
                .appendQueryParameter("sensor","false");


        URL dataUrl = null;
        try {
            dataUrl = new URL(dataUri.toString());
            connection = (HttpURLConnection) dataUrl.openConnection();
            connection.connect();
            connection.setConnectTimeout(10000);
            int status = connection.getResponseCode();

            if(status!=HttpURLConnection.HTTP_OK){
                return null;
            }



            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String responseString;
            StringBuilder sb = new StringBuilder();
            while ((responseString = reader.readLine()) != null) {
                sb = sb.append(responseString);
            }
            retJsonList = sb.toString();




            JSONObject data = new JSONObject(retJsonList);
            JSONArray resultArray = data.optJSONArray("results");

            JSONObject results_init = resultArray.getJSONObject(0);

            JSONArray addressComponentsArray = results_init.optJSONArray("address_components");


            for(int i = 0; i < addressComponentsArray.length(); i++) {
                JSONObject addressComponentObj=(JSONObject) addressComponentsArray.get(i);

                JSONArray addressComponentType = addressComponentObj.optJSONArray("types");

                if(!addressComponentType.getString(0).equals("locality")){
                    continue;
                }
                else{
                    branchCityName = addressComponentObj.getString("long_name");
                    break;
                }
            }



        } catch (IOException e) {
            emptyString=false;
            Log.e("Exception:- ", Log.getStackTraceString(e));
        } catch (JSONException e) {
            Log.e("Exception:- ", Log.getStackTraceString(e));
        }

        return branchCityName;

    }

    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(this.context);
        dialog.setMessage(context.getString(R.string.loading_mess));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        this.cityName = getLocationBranch();
        return null;
    }

    protected void onPostExecute(Void result) {
        // dismiss progress dialog and update ui
        super.onPostExecute(result);
        if(cityName!=null&&cityName.trim().length()>0) {
            if(cityList.contains(cityName)){
                editText.setText(cityName);
                Intent intent = new Intent(context, OrderActivity.class);
                intent.putExtra("branchName",cityName);
                context.startActivity(intent);
            }
            else{
                Toast.makeText(context, context.getString(R.string.invalid_location_1)+cityName
                        +context.getString(R.string.invalid_location_2), Toast.LENGTH_SHORT).show();
            }

        }


        else{
            int duration = Toast.LENGTH_SHORT;
            String emptyToastMessage= context.getString(R.string.conn_err);
            if(emptyString){
                emptyToastMessage = context.getString(R.string.invalid_location);
            }

            Toast toast = Toast.makeText(context,emptyToastMessage , duration);
            toast.show();
        }


        try{
            if(dialog.isShowing())
                dialog.dismiss();

        }catch (IllegalArgumentException ie){
            Log.e("Exception:- ", Log.getStackTraceString(ie));
        }
    }
}
