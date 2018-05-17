package sss.spade.spadei.inspectorspade.FarmActivities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sss.spade.spadei.inspectorspade.DataHandler.DataHandler;
import sss.spade.spadei.inspectorspade.FarmActivities.VerFarmer.TaskListActivity;
import sss.spade.spadei.inspectorspade.FarmActivities.VerFarmer.VerifyFarmerReplyActivity;
import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.SharedPref.SharedPreferencesMethod;

public class FarmActionReplyActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    Toolbar mActionBarToolbar;
    Context context;
    ImageView imgView;
    Button mButton;
    String mprovider;
    private LocationManager mLocationManager;
    ConnectivityManager connectivityManager;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000;
    private LocationRequest mLocationRequest;
    private LocationManager locationManager;
    private static final String TAG = "MainActivity";
    boolean connected = false;
    String flagg = "";
    float distanceInMetersgpsc5 = 400;
    float distanceInMetersgpsc6 = 400;
    boolean isWithin5kmgpsc5=false;
    boolean isWithin5kmgpsc6=false;
    float[] results5;
    float[] results6;
    String from="";


    // EditText mEditText;
    String sTaskStatus;
    TextView allMessagesLink,taskstatusfarmaction;
    final String DATA_ID = "data_id";
    final String DATA_TASK_DATE = "data_task_date";
    final String FARM_DWORK_ID = "farm_dwork_num";
    final String DATA_TASK_STATUS = "task_done_status";
    final String INSPECTOR_REPLY = "txtComment";
    String fetch_id, description,farmerReply, chemical, chemical_qty, compulsary, activity, img_link, done, date_of_task;
    String REGISTER_URL = "http://spade.farm/app/index.php/farmCalendar/fetch_farm_day_data_by_id";
    String SAVE_INSPECTOR_RESPONSE_URL = "http://spade.farm/app/index.php/inspectorApp/verify_activity";
    String DATE="date";
    String formattedDate;
    final String KEY_TOKEN="token4";
    String ct1="";

    TextView tvFarmerReply, tvactivityName, tvactivityDescription, tvactivityDate, tvchemical, tvqtychemical, tvCompulsary;

    private String user_num;
    String farm_num;

    final String API_URL = "http://spade.farm/app/index.php/farmApp/save_audio_response";
    ProgressDialog progressDialog;
    private int flag = 0;
    File file;
    String gpsc1,gpsc2,gpsc3,gpsc4,gpsc5,gpsc6;
    String[] gpsc1arr,gpsc2arr,gpsc3arr,gpsc4arr,gpsc5arr,gpsc6arr;
    Boolean locationchck=false;
    String comp_gps_check="";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent=new Intent(context,ShowTaskViewPagerActivity.class);
        // intent.putExtra("farm_num",farm_num);
        startActivity(intent);
        finish();
       /* if(from!=null){
            if(from.equals("pager")){

            }else{
                Intent intent=new Intent(context,TaskListActivity.class);
                //intent.putExtra("farm_num",farm_num);
                startActivity(intent);
                finish();
            }
        }
       */ return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_action_reply);
        Log.e("check", "reached onCreate");
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        context = this;
        ct1=SharedPreferencesMethod.getString(context,"cctt");

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        comp_gps_check=SharedPreferencesMethod.getString(context,SharedPreferencesMethod.COMP_GPS);


        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText("Activity");
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);

        tvactivityName = (TextView) findViewById(R.id.reply_activity_name_value);
        tvactivityDescription = (TextView) findViewById(R.id.reply_activity_description_value);
        tvactivityDate = (TextView) findViewById(R.id.reply_activity_date_value);
        tvchemical = (TextView) findViewById(R.id.reply_activity_chemical_name_value);
        tvqtychemical = (TextView) findViewById(R.id.reply_activity_quantity_value);
        tvCompulsary = (TextView) findViewById(R.id.reply_activity_compulsary_value);
        imgView = (ImageView) findViewById(R.id.img_view);
        mButton = (Button) findViewById(R.id.button_submit);
        //mEditText = (EditText) findViewById(R.id.et_farmer_reply);
        tvFarmerReply = (TextView) findViewById(R.id.tv_farmer_reply);
        allMessagesLink=(TextView)findViewById(R.id.view_all_messages);
        taskstatusfarmaction=(TextView)findViewById(R.id.task_status_farm_action);
        gpsc1= SharedPreferencesMethod.getString(context,"GPSC1");
        gpsc2=SharedPreferencesMethod.getString(context,"GPSC2");
        gpsc3=SharedPreferencesMethod.getString(context,"GPSC3");
        gpsc4=SharedPreferencesMethod.getString(context,"GPSC4");
        gpsc5=SharedPreferencesMethod.getString(context,"GPSC5");
        gpsc6=SharedPreferencesMethod.getString(context,"GPSC6");

        gpsc1arr=gpsc1.split("_");
        gpsc2arr=gpsc2.split("_");
        gpsc3arr=gpsc3.split("_");
        gpsc4arr=gpsc4.split("_");
        if(!gpsc5.equals("0")){
            gpsc5arr=gpsc5.split("_");
        }
        if(!gpsc6.equals("0")){
        gpsc6arr=gpsc6.split("_");
        }

        //Toast.makeText(context, gpsc1, Toast.LENGTH_SHORT).show();
       //Toast.makeText(context, gpsc1arr[0]+" "+gpsc1arr[1], Toast.LENGTH_SHORT).show();



        //getSupportActionBar().setTitle("My Title");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        Bundle extras = getIntent().getExtras();
        final String id = extras.getString("id");
        final String task_date = extras.getString("task_date");
        from=extras.getString("fromactivity");

//
//        Toast.makeText(context, id+" "+task_date+" "+from, Toast.LENGTH_SHORT).show();

        if(from!=null){
        if(from.equals("pager")){
            mButton.setVisibility(View.GONE);
        }}

         farm_num=extras.getString("farm_num");
        farm_num= DataHandler.newInstance().getFarmnum();


      //  Toast.makeText(context, farm_num, Toast.LENGTH_SHORT).show();


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         formattedDate = df.format(c);



        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mprovider = mLocationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(mprovider, 2000, 1, this);
        //location = mLocationManager.getLastKnownLocation(mprovider);
        checkLocation();





        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jobject = null;
                            Log.e("check", "reached try block");
                            jobject = new JSONObject(response);
                            fetch_id = jobject.getString("farm_dwork_num");
                            activity = jobject.getString("activity");
                            compulsary = jobject.getString("is_compulsary");
                            img_link = jobject.getString("img_link");
                            chemical = jobject.getString("chemical_to_use");
                            chemical_qty = jobject.getString("quantity_of_chemical");
                            done = jobject.getString("is_done");
                            date_of_task = jobject.getString("date");
                            description = jobject.getString("activity_description");
                            farmerReply = jobject.getString("farmer_reply");
                            Log.e("Check", "id" + id + " " + "img_link" + " " + img_link);
                            tvactivityName.setText(activity);
                            tvactivityDescription.setText(description);
                            tvactivityDate.setText(date_of_task);
                            tvchemical.setText(chemical);
                            tvqtychemical.setText(chemical_qty + " lit/acre");
                            if (compulsary.equals("Y")) {
                                tvCompulsary.setText("Yes");
                            } else {
                                tvCompulsary.setText("No");
                            }
                            if(done.equals("Y")){
                                taskstatusfarmaction.setText("Done");
                            }else{
                                taskstatusfarmaction.setText("Not Done");
                            }
                            Uri uri = Uri.parse(img_link);
                            Picasso.with(context).load(uri).into(imgView);
                           tvFarmerReply.setText(farmerReply);

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (id != null) {
                    params.put(DATA_ID, id);
                    Log.e("check", "id passed" + " " + id);
                }
                if (task_date != null) {
                    params.put(DATA_TASK_DATE, task_date);
                    Log.e("check", "date passed" + " " + task_date);
                }
                if(ct1!=null){
                    params.put(KEY_TOKEN,ct1);
                }
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                               AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setMessage("Want to Verify this Activity?");
                AlertDialog.Builder builder = alertDialogBuilder.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                            //we are connected to a network
                            connected = true;
                        } else {
                            connected = false;
                        }

                        if (connected) {
                            if(comp_gps_check.equals("1")) {
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                startLocationUpdates();
                                mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                                flagg = "loc";

                                onLocationChanged(mLocation);
                            }else{
                                locationchck=true;
                            }
                        } else {
                            Toast.makeText(context, "Turn on the Network", Toast.LENGTH_SHORT).show();
                        }

                        if (locationchck) {
                            //Toast.makeText(FarmActionReplyActivity.this, "You are in location range", Toast.LENGTH_SHORT).show();
                            StringRequest stringRequestNew = new StringRequest(Request.Method.POST, SAVE_INSPECTOR_RESPONSE_URL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.e("check", "This is resp" + response);
                                            if (response.equals("true")) {
                                                Toast.makeText(context, "Activity Verified Successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(context, ShowTaskViewPagerActivity
                                                        .class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(FarmActionReplyActivity.this, "Unable to Verify activity at the moment Please try again later", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("checkArray", error.toString());
                                            NetworkResponse networkResponse = error.networkResponse;
                                            Log.e("checkArray", String.valueOf(networkResponse.statusCode));
                                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                                final int checkArray = Log.e("checkArray", "timeout error");

                                            } else if (error instanceof AuthFailureError) {
                                                Log.e("checkArray", "authfailure error");

                                            } else if (error instanceof ServerError) {
                                                Log.e("checkArray", "Server error");

                                            } else if (error instanceof NetworkError) {
                                                //TODO
                                                Log.e("checkArray", "network error");

                                            } else if (error instanceof ParseError) {
                                                Log.e("checkArray", "parse error");
                                                //TODO
                                            }
                                        }
                                    }) {

                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();

                                    Log.e("check", "Reached Params");

                                    if (fetch_id != null) {
                                        params.put(FARM_DWORK_ID, fetch_id);
                                    }
                                    if (formattedDate != null) {
                                        params.put(DATE, formattedDate);
                                    }
                                    if(ct1!=null){
                                        params.put(KEY_TOKEN,ct1);
                                    }
                                    return params;
                                }
                            };

                            RequestQueue requestQueueNew = Volley.newRequestQueue(context);
                            requestQueueNew.add(stringRequestNew);

                        } else {
                            Toast.makeText(FarmActionReplyActivity.this, "Please be at farm location then verify", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        finish();
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        allMessagesLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context,"Open Chat Box",Toast.LENGTH_LONG).show();
                Log.e("check","open chat box");
                Intent intent=new Intent(context,VerifyFarmerReplyActivity.class);
                intent.putExtra("fetchId",fetch_id);
                startActivity(intent);
            }
        });
       /* Uri uri = Uri.parse();
        Picasso.with(context).load(uri).into(imgView);*/
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(context,ShowTaskViewPagerActivity.class);
        // intent.putExtra("farm_num",farm_num);
        startActivity(intent);
        finish();
        /*if(from!=null){
            if(from.equals("pager")){
                Intent intent=new Intent(context,ShowTaskViewPagerActivity.class);
                intent.putExtra("farm_num",farm_num);
                startActivity(intent);
                finish();
            }else{
                Intent intent=new Intent(context,TaskListActivity.class);
                intent.putExtra("farm_num",farm_num);
                startActivity(intent);
                finish();
            }
        }*/

    }

    public boolean isResponseFilled(String text){
        boolean flag = true;
        if(text.isEmpty()){
            flag=false;
        }
        if(text.equals("null")){
            flag=false;
        }
        if(text.equals(" ")){
            flag=false;
        }
        return flag;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        locationManager.requestLocationUpdates(mprovider, 2000, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(FarmActionReplyActivity.this).build();
            mGoogleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(10 * 1000);
            locationRequest.setFastestInterval(2 * 1000);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can
                            // initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(FarmActionReplyActivity.this, 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }


    @Override
    public void onLocationChanged(Location location) {


        if(location!=null) {
            if (flagg.equals("loc")) {
                if (!Double.toString(location.getLongitude()).equals("")) {

                    float[] results1 = new float[1];
                    Location.distanceBetween(Double.parseDouble(gpsc1arr[0]), Double.parseDouble(gpsc1arr[1]), location.getLatitude(), location.getLongitude(), results1);
                    float[] results2 = new float[1];
                    Location.distanceBetween(Double.parseDouble(gpsc2arr[0]), Double.parseDouble(gpsc2arr[1]), location.getLatitude(), location.getLongitude(), results2);
                    float[] results3 = new float[1];
                    Location.distanceBetween(Double.parseDouble(gpsc3arr[0]), Double.parseDouble(gpsc3arr[1]), location.getLatitude(), location.getLongitude(), results3);
                    float[] results4 = new float[1];
                    Location.distanceBetween(Double.parseDouble(gpsc4arr[0]), Double.parseDouble(gpsc4arr[1]), location.getLatitude(), location.getLongitude(), results4);
                    if(gpsc5arr!=null) {
                        if (gpsc5arr[0] != null && gpsc5arr[1] != null) {
                             results5 = new float[1];
                            Location.distanceBetween(Double.parseDouble(gpsc5arr[0]), Double.parseDouble(gpsc5arr[1]), location.getLatitude(), location.getLongitude(), results5);
                        }
                    }
                    if(gpsc6arr!=null){
                    if(gpsc6arr[0]!=null&&gpsc6arr[1]!=null){
                              results6 = new float[1];
                        Location.distanceBetween(Double.parseDouble(gpsc6arr[0]), Double.parseDouble(gpsc6arr[1]), location.getLatitude(), location.getLongitude(), results6);
                    }
                    }
                    float distanceInMetersgpsc1 = results1[0];
                    float distanceInMetersgpsc2 = results2[0];
                    float distanceInMetersgpsc3 = results3[0];
                    float distanceInMetersgpsc4 = results4[0];
                    if(gpsc5arr!=null){
                    if(gpsc5arr[0]!=null&&gpsc5arr[1]!=null){
                     distanceInMetersgpsc5 = results5[0];}}
                     if(gpsc6arr!=null){
                    if(gpsc6arr[0]!=null&&gpsc6arr[1]!=null){
                     distanceInMetersgpsc6 = results6[0];}}

                    boolean isWithin5kmgpsc1 = distanceInMetersgpsc1 < 350;
                    boolean isWithin5kmgpsc2 = distanceInMetersgpsc2 < 350;
                    boolean isWithin5kmgpsc3 = distanceInMetersgpsc3 < 350;
                    boolean isWithin5kmgpsc4 = distanceInMetersgpsc4 < 350;
                    if(gpsc5arr!=null){
                        if(gpsc5arr[0]!=null&&gpsc5arr[1]!=null){
                         isWithin5kmgpsc5 = distanceInMetersgpsc5 < 350;}}
                    if(gpsc6arr!=null){
                        if(gpsc6arr[0]!=null&&gpsc6arr[1]!=null){
                         isWithin5kmgpsc6 = distanceInMetersgpsc6 < 350;
                    }
                    }

                    //Toast.makeText(context, distanceInMetersgpsc1+" "+distanceInMetersgpsc2+" "+distanceInMetersgpsc3+" "+distanceInMetersgpsc4+" "+distanceInMetersgpsc5+" "+distanceInMetersgpsc6, Toast.LENGTH_SHORT).show();
Log.e("distances",distanceInMetersgpsc1+" "+distanceInMetersgpsc2+" "+distanceInMetersgpsc3+" "+distanceInMetersgpsc4+" "+distanceInMetersgpsc5+" "+distanceInMetersgpsc6);

                    if(gpsc5arr!=null||gpsc6arr!=null){
                        if(gpsc5arr!=null && gpsc6arr!=null){
                            if(isWithin5kmgpsc1 ||isWithin5kmgpsc2||isWithin5kmgpsc3||isWithin5kmgpsc4||isWithin5kmgpsc5||isWithin5kmgpsc6){
                                locationchck=true;
                            }else{locationchck=false;}
                        }
                        else if(gpsc5arr!=null){
                        if(isWithin5kmgpsc1 ||isWithin5kmgpsc2||isWithin5kmgpsc3||isWithin5kmgpsc4||isWithin5kmgpsc5){
                            locationchck=true;
                        }
                        else{locationchck=false;}
                    }
                    else {
                        if(isWithin5kmgpsc1 ||isWithin5kmgpsc2||isWithin5kmgpsc3||isWithin5kmgpsc4||isWithin5kmgpsc6){
                            locationchck=true;
                        }
                        else{locationchck=false;}
                    }
                    }


                    else{
                        if(isWithin5kmgpsc1 ||isWithin5kmgpsc2||isWithin5kmgpsc3||isWithin5kmgpsc4){


                            locationchck=true;
                        }
                        else{
                            locationchck=false;

                        }
                    }
                   // Toast.makeText(context, location.getLatitude()+"  "+location.getLongitude(), Toast.LENGTH_LONG).show();
                    flagg = "";
                }
            }
        }
    }




    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation == null) {
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
           // Toast.makeText(this, "Location not Detected Please Turn on Gps", Toast.LENGTH_SHORT).show();
        }
    }


}
