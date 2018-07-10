package com.example.hirensamtani.pizzafeedback;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.example.hirensamtani.pizzafeedback.model.FeedBackContract;
import com.example.hirensamtani.pizzafeedback.utils.LocationUtils;

import java.util.ArrayList;
import java.util.List;

public class GetLocation extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, android.support.v4.app.LoaderManager.LoaderCallbacks {

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    EditText branchName;
    String locLattitude;
    String locLongitude;
    Context context=this;
    List cityList;

    private static RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager mLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        branchName = (EditText) findViewById(R.id.branchName);

        initGoogleApiClient();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_loc);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkEnableGPS()){
                findCurrentLocation();
                String[] locArray = {locLattitude,locLongitude};
                LocationUtils locationUtils = new LocationUtils(context,locArray,branchName,cityList);
                locationUtils.execute();
                }
                else{
                    showGPSIntent();
                }

            }
        });





        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_branchList);


        mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        getSupportLoaderManager().initLoader(1, null, this);




        // specify an adapter (see also next example)
        Resources res = getResources();
        String[] myDataset = res.getStringArray(R.array.branchList);
        cityList = new ArrayList();
        for(String city:myDataset){
            cityList.add(city);
        }

       // getSupportLoaderManager().initLoader(1, null, this);






        AdView mAdView = (AdView) findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."


        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(context.getString(R.string.device_id))
                .build();
        mAdView.loadAd(adRequest);

    }

    public boolean checkEnableGPS(){
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void showGPSIntent(){
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.gps_intent_title));  // GPS not found
            builder.setMessage(context.getString(R.string.gps_intent_mess)); // Want to enable?
            builder.setPositiveButton(context.getString(R.string.gps_intent_positive), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    //owner.
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            builder.setNegativeButton(context.getString(R.string.gps_intent_negative), null);
            builder.create().show();
            return;
        }

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    public void initGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

        }
    }


    public void findCurrentLocation(){

        final int REQUEST_LOCATION = 2;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
        }
        else{

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            locLattitude = String.valueOf(mLastLocation.getLatitude());
            locLongitude = String.valueOf(mLastLocation.getLongitude());

        }


        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
          findCurrentLocation();
        }

        @Override
        public void onConnectionSuspended (int i){
            mGoogleApiClient.connect();
        }

        @Override
        public void onConnectionFailed (ConnectionResult connectionResult){
            Log.i("Exception", "Connection failed: ConnectionResult.getErrorCode() = "
                    + connectionResult.getErrorCode());
        }

    @Override
    public android.support.v4.content.Loader onCreateLoader(int id, Bundle args) {
        Uri BRANCH_URI = FeedBackContract.BranchEntry.CONTENT_URI;
        CursorLoader cursorLoader = new CursorLoader(this, BRANCH_URI, null,
                null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader loader, Object data) {
        Cursor cursor = (Cursor)data;
        cursor.moveToFirst();

        mAdapter = new BranchCursorAdapter(cursor,context);
        mRecyclerView.setAdapter(mAdapter);
        //cursor.close();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader loader) {

    }


}
