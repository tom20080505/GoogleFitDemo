package com.soft.fitdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 10;
    private static final int REQUEST_LOCATION = 1;

    private TextView    tvMsg;

    FitnessOptions fitnessOptions;
    private static OnDataPointListener mGoogleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w(TAG, "onCreate(), ");

        initView();
        //initControl();
    }

    @Override
    protected void onStart() {
        initControl();
        super.onStart();
        //mClient.connect();


        Log.w(TAG, "onStart(), ");
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Log.w(TAG, "onActivityResult(), requestCode: " + requestCode +
                        ", resultCode: " + resultCode +
                        "\n ,data: " + data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
                accessGoogleFit();
                //testBloodpressure();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.w(TAG, "onRequestPermissionsResult(), requestCode: " + requestCode +
                        ", permissions: " + Arrays.toString(permissions) +
                        ", grantResults: " + Arrays.toString(grantResults));
        //if (requestCode == REQUEST_LOCATION) {
        //    if(grantResults.length == 1
        //            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        //        // We can now safely use the API we requested access to
        //        //Location myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        //    } else {
        //        // Permission was denied or request was cancelled
        //    }
        //}

        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {}
                break;

            case GOOGLE_FIT_PERMISSIONS_REQUEST_CODE:
                //initialize(this);
                //permissionCheck();
                break;
        }

    }

    @Override
    protected void onStop() {
        Log.w(TAG, "onStop(), ");
        super.onStop();
        //if (mClient.isConnected())
        {
        ///    mClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        Log.w(TAG, "onDestroy(), ");
        super.onDestroy();
    }



    //------------------- User define function -----------------------//
    private void initView()
    {
        Log.w(TAG, " initView(), ");
        tvMsg = findViewById(R.id.tv_MSG1);
    }

    private void initControl()
    {
        Log.w(TAG, " initControl(), ");
        tvMsg.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvMsg.append("\n\t Start App.");

        //permissionCheck();
        //initialize(this);

        fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_HEIGHT, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_WEIGHT_SUMMARY, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_HEIGHT_SUMMARY, FitnessOptions.ACCESS_READ)
                .build();
        //tvMsg.append("\n\t fitnessOptions:" + fitnessOptions.getGoogleSignInAccount() + "\n, " +
        //        fitnessOptions.getImpliedScopes());

        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this, // your activity
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(this),
                    fitnessOptions);
        } else {
            //initialize(this);
            accessGoogleFit();
        }


        //DataSource bloodPressureSource = new DataSource.Builder()
        //        .setDataType(HealthDataTypes.TYPE_BLOOD_PRESSURE)
        //        .setAppPackageName(this.getBaseContext())
        //        .setStreamName(TAG + " - blood pressure")
        //        .setType(DataSource.TYPE_RAW)
        //        .build();
        //
        //DataPoint bloodPressure = (DataPoint) DataPoint.create(bloodPressureSource);
        //bloodPressure.setTimestamp(SystemClock.currentThreadTimeMillis(), MILLISECONDS);
        //bloodPressure.getValue(FIELD_BLOOD_PRESSURE_SYSTOLIC).setFloat(120.0f);
        //bloodPressure.getValue(FIELD_BLOOD_PRESSURE_DIASTOLIC).setFloat(80.0f);
        //bloodPressure.getValue(FIELD_BODY_POSITION).setInt(BODY_POSITION_SITTING);
        //bloodPressure.getValue(FIELD_BLOOD_PRESSURE_MEASUREMENT_LOCATION)
        //        .setInt(BLOOD_PRESSURE_MEASUREMENT_LOCATION_LEFT_UPPER_ARM);


        //tvMsg.append("\n\t bloodPressure:" + bloodPressure.toString() );

    }


    //private void permissionCheck()
    //{
    //    Log.w(TAG, " permissionCheck(), ");
    //    if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
    //        GoogleSignIn.requestPermissions(
    //                this, // your activity
    //                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
    //                GoogleSignIn.getLastSignedInAccount(this),
    //                fitnessOptions);
    //    } else {
    //        //initialize(this);
    //        accessGoogleFit();
    //    }
    //}

    GoogleApiClient mClient = null;
    private void accessGoogleFit() {
        Log.w(TAG, " accessGoogleFit(), ");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.YEAR, -1);
        long startTime = cal.getTimeInMillis();


        final DataReadRequest readRequest = new DataReadRequest.Builder()
                //.read(DataType.TYPE_WEIGHT)
                //.setTimeRange(1, cal.getTimeInMillis(), MILLISECONDS)
                //.setLimit(1)
                //.aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .aggregate(DataType.TYPE_WEIGHT, DataType.AGGREGATE_WEIGHT_SUMMARY)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .bucketByTime(1, TimeUnit.DAYS)
                .build();

        //GoogleApiClient mClient = new GoogleApiClient.Builder(this)
        mClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.HISTORY_API)
                //.addApi(Fitness.SESSIONS_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                .addScope(new Scope(Scopes.FITNESS_BODY_READ))
                .addScope(new Scope(Scopes.FITNESS_LOCATION_READ))
                .addScope(new Scope(Scopes.FITNESS_NUTRITION_READ))
                //.addScope(new Scope(Scopes.FITNESS_BODY_READ_WRITE))
                //.addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.w(TAG, "Connected !!!");
                        //new InsertAndVerifyDataTask().exec
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(this)
                .build();

        //final DataReadResult dataReadResult = Fitness.HistoryApi.readData(mClient, readRequest).await();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DataReadResult dataReadResult = Fitness.HistoryApi.readData(mClient, readRequest).await();
                tvMsg.append("\n, 2 dataReadResult: " + Arrays.toString(new String[]{dataReadResult.getBuckets().toArray().toString()}));
            }
        });

        Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .readData(readRequest)
                .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(final DataReadResponse dataReadResponse) {
                        Log.w(TAG, "onSuccess(), dataReadResponse: " + Arrays.toString(dataReadResponse.getBuckets().toArray()));

                        //runOnUiThread(new Runnable() {
                        //    @Override
                        //    public void run() {
                        //        tvMsg.append("\n, 2 dataReadResponse: " + Arrays.toString(dataReadResponse.getBuckets().toArray()));
                        //    }
                        //});
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure()", e);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DataReadResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<DataReadResponse> task) {
                        Log.w(TAG, "onComplete()");
                    }
                });




        //mlc add
        //testBloodpressure();
    }


    //private void testBloodpressure()
    //{
    //    DataSource bloodPressureSource = new DataSource.Builder()
    //            .setDataType(HealthDataTypes.TYPE_BLOOD_PRESSURE)
    //            .setAppPackageName(this.getBaseContext())
    //            .setStreamName(TAG + " - blood pressure")
    //            .setType(DataSource.TYPE_RAW)
    //            .build();
    //
    //    DataPoint bloodPressure = (DataPoint) DataPoint.create(bloodPressureSource);
    //    bloodPressure.setTimestamp(SystemClock.currentThreadTimeMillis(), MILLISECONDS);
    //    bloodPressure.getValue(FIELD_BLOOD_PRESSURE_SYSTOLIC).setFloat(120.0f);
    //    bloodPressure.getValue(FIELD_BLOOD_PRESSURE_DIASTOLIC).setFloat(80.0f);
    //    bloodPressure.getValue(FIELD_BODY_POSITION).setInt(BODY_POSITION_SITTING);
    //    bloodPressure.getValue(FIELD_BLOOD_PRESSURE_MEASUREMENT_LOCATION)
    //            .setInt(BLOOD_PRESSURE_MEASUREMENT_LOCATION_LEFT_UPPER_ARM);
    //
    //    tvMsg.append("\n\t bloodPressure:" + bloodPressure.toString() );
    //}


    public static void initialize(final FragmentActivity activity){
        Log.w(TAG, " initialize(), ");
        // Setup Callback listener
        GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                Log.i(TAG, "Connected! ");
                // Now you can make calls to the Fitness APIs.
                //subscribe();
            }

            @Override
            public void onConnectionSuspended(int i) {
                // If your connection to the sensor gets lost at some point,
                // you'll be able to determine the reason and react to it here.
                if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                    Log.i(TAG, "1 Connection lost.  Cause: Network Lost.");
                } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                    Log.i(TAG, "2 Connection lost.  Reason: Service Disconnected");
                }
            }
        };

        // Handle Failed connection
        GoogleApiClient.OnConnectionFailedListener connectionFailed = new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult result) {

                Log.i(TAG, "3 Google Play services connection failed. Cause: " + result.toString());

                Toast.makeText(activity, "4 Exception while connecting to Google Play services: " +
                        result.getErrorMessage() + ":" + result.getErrorCode(), Toast.LENGTH_SHORT).show();

            }
        };


        // Create Google Api Client
        mGoogleApiClient = (OnDataPointListener) new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(connectionCallbacks)
                .enableAutoManage(activity, connectionFailed)
                .addScope(new Scope(Scopes.FITNESS_BODY_READ_WRITE))
                .addApi(Fitness.HISTORY_API)
                .build();
        //mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class ResultTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    };

    //private class InsertAndVerifyDataTask extends AsyncTask<Void, Void, Void>
    //{
    //
    //    @Override
    //    protected Void doInBackground(Void... params)
    //    {
    //        /*
    //        DataSet dataSet = insertFit
    //        return null;
    //        */
    //    }
    //};

}

