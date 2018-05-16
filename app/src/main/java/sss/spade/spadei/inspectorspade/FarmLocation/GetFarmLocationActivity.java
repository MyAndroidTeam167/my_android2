package sss.spade.spadei.inspectorspade.FarmLocation;

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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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

import java.util.HashMap;
import java.util.Map;

import sss.spade.spadei.inspectorspade.DataHandler.DataHandler;
import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.SharedPref.SharedPreferencesMethod;
import sss.spade.spadei.inspectorspade.VerifyFarm.VerifyFarmActivity;
import sss.spade.spadei.inspectorspade.VerifyFarm.VerifyaFarm.VerifySingleFarmActivity;

public class GetFarmLocationActivity extends AppCompatActivity implements View.OnClickListener, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final String TAG = "MainActivity";
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private GoogleApiClient mGoogleApiClient;
    String mprovider;
    private Location mLocation,location;
    private LocationManager mLocationManager;
    ProgressDialog progressDialog;
    ConnectivityManager connectivityManager;
    boolean connected = false;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    String flag = "";
    Context context;
    TextView gpslat1, gpslat2, gpslat3, gpslat4, gpslat5, gpslat6, gpslong1, gpslong2, gpslong3, gpslong4, gpslong5, gpslong6, gpsccheck;
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6;
    ImageView btn1, btn2, btn3, btn4, btn5, btn6;
    Button btncheck,btnsubmit;
    String farm_num="";
    private static final String REGISTER_URL = "http://spade.farm/app/index.php/inspectorApp/save_gps_coordinates";
    public static final String KEY_FARM_NUM = "farm_num";
    public static final String KEY_GPSC1 = "gps1";
    public static final String KEY_GPSC2 = "gps2";
    public static final String KEY_GPSC3 = "gps3";
    public static final String KEY_GPSC4 = "gps4";
    public static final String KEY_GPSC5 = "gps5";
    public static final String KEY_GPSC6 = "gps6";
    String gps1,gps2,gps3,gps4,gps5,gps6;
    Toolbar mActionBarToolbar;
    final String KEY_TOKEN="token1";
    String ct1="";



    @Override
    public void onBackPressed() {

        Intent intent =new Intent(context, VerifyFarmActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent =new Intent(context, VerifyFarmActivity.class);
        startActivity(intent);
        finish();
        return super.onOptionsItemSelected(item);
    }

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_farm_location);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        context = this;

        TextView title=(TextView)findViewById(R.id.tittle);
        title.setText("Get Farm Location");
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);

        ct1= SharedPreferencesMethod.getString(context,"cctt");

        //getSupportActionBar().setTitle("My Title");

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

       /* Bundle extras = getIntent().getExtras();
        if (extras != null) {
            farm_num = extras.getString("farm_num");
            Toast.makeText(context, farm_num, Toast.LENGTH_SHORT).show();
        }*/
       farm_num= DataHandler.newInstance().getFarmnum();

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
        location = mLocationManager.getLastKnownLocation(mprovider);


        checkLocation();
        init();//check whether location service is enable or not in your  phone
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
            Toast.makeText(this, "Location not Detected Please Turn on Gps", Toast.LENGTH_SHORT).show();
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
    public void onLocationChanged(Location location) {

        /*String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        mLatitudeTextView.setText(String.valueOf(location.getLatitude()));
        mLongitudeTextView.setText(String.valueOf(location.getLongitude()));
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());*/
if(location!=null) {
    if (flag.equals("cor1")) {
        if (!Double.toString(location.getLongitude()).equals("")) {
            gpslong1.setText("Long: " + location.getLongitude());
            gpslat1.setText("Lat : " + location.getLatitude());
            gps1=location.getLatitude()+"_"+location.getLongitude();
            checkBox1.setVisibility(View.VISIBLE);
            checkBox1.setClickable(true);
            checkBox1.setChecked(true);
            checkBox1.setText("Re-Capture");
            btn1.setImageResource(R.drawable.loconclick64);
            //btn1.setBackgroundColor(Color.GREEN);
            btn1.setEnabled(false);
            flag = "";
        }
    } else if (flag.equals("cor2")) {
        if (!Double.toString(location.getLongitude()).equals("")) {
            // arrayList.add(Double.toString(location.getLatitude())+Double.toString(location.getLongitude()));
            gpslong2.setText("Long: " + location.getLongitude());
            gpslat2.setText("Lat : " + location.getLatitude());
            gps2=location.getLatitude()+"_"+location.getLongitude();
            checkBox2.setVisibility(View.VISIBLE);
            checkBox2.setChecked(true);
            checkBox2.setText("Re-Capture");
            checkBox2.setClickable(true);
            btn2.setImageResource(R.drawable.loconclick64);
            //btn1.setBackgroundColor(Color.GREEN);
            btn2.setEnabled(false);
            flag = "";

            // progressDialog.dismiss();

        }
    } else if (flag.equals("cor3")) {
        if (!Double.toString(location.getLongitude()).equals("")) {
            // arrayList.add(Double.toString(location.getLatitude())+Double.toString(location.getLongitude()));
            gpslong3.setText("Long: " + location.getLongitude());
            gpslat3.setText("Lat : " + location.getLatitude());
            gps3=location.getLatitude()+"_"+location.getLongitude();
            checkBox3.setVisibility(View.VISIBLE);
            checkBox3.setChecked(true);
            checkBox3.setText("Re-Capture");
            checkBox3.setClickable(true);

            btn3.setImageResource(R.drawable.loconclick64);
            //btn1.setBackgroundColor(Color.GREEN);
            btn3.setEnabled(false);
            flag = "";

            //  progressDialog.dismiss();
        }

    } else if (flag.equals("cor4")) {
        if (!Double.toString(location.getLongitude()).equals("")) {
            // arrayList.add(Double.toString(location.getLatitude())+Double.toString(location.getLongitude()));
            gpslong4.setText("Long: " + location.getLongitude());
            gpslat4.setText("Lat : " + location.getLatitude());
            gps4=location.getLatitude()+"_"+location.getLongitude();
            checkBox4.setVisibility(View.VISIBLE);
            checkBox4.setChecked(true);
            checkBox4.setText("Re-Capture");
            checkBox4.setClickable(true);

            btn4.setImageResource(R.drawable.loconclick64);
            //btn1.setBackgroundColor(Color.GREEN);
            btn4.setEnabled(false);
            flag = "";


            // progressDialog.dismiss();
        }

    } else if (flag.equals("cor5")) {
        if (!Double.toString(location.getLongitude()).equals("")) {
            // arrayList.add(Double.toString(location.getLatitude())+Double.toString(location.getLongitude()));
            gpslong5.setText("Long: " + location.getLongitude());
            gpslat5.setText("Lat : " + location.getLatitude());
            gps5=location.getLatitude()+"_"+location.getLongitude();
            checkBox5.setVisibility(View.VISIBLE);
            checkBox5.setChecked(true);
            checkBox5.setText("Re-Capture");
            checkBox5.setClickable(true);

            btn5.setImageResource(R.drawable.loconclick64);
            //btn1.setBackgroundColor(Color.GREEN);
            btn5.setEnabled(false);
            flag = "";

            //  progressDialog.dismiss();
        }

    } else if (flag.equals("cor6")) {
        if (!Double.toString(location.getLongitude()).equals("")) {
            // arrayList.add(Double.toString(location.getLatitude())+Double.toString(location.getLongitude()));
            gpslong6.setText("Long: " + location.getLongitude());
            gpslat6.setText("Lat : " + location.getLatitude());
            gps6=location.getLatitude()+"_"+location.getLongitude();
            checkBox6.setVisibility(View.VISIBLE);
            checkBox6.setChecked(true);
            checkBox6.setText("Re-Capture");
            checkBox6.setClickable(true);

            btn6.setImageResource(R.drawable.loconclick64);
            //btn1.setBackgroundColor(Color.GREEN);
            btn6.setEnabled(false);
            flag = "";

            // progressDialog.dismiss();
        }
    }
}else{
    Toast.makeText(this, "Unable to Fetch Location at the moment Please Check location Settings", Toast.LENGTH_LONG).show();
}
    }


    public void checkbox1_clicked(View v) {
        if (checkBox1.isChecked()) {
            btn1.setEnabled(false);
            btn1.setImageResource(R.drawable.loconclick64);
            checkBox1.setText("Re-Capture");

        } else {
            gpslat1.setText("Lat:");
            gpslong1.setText("Long:");
            checkBox1.setText("");
            //checkBox1.setEnabled(false);
            checkBox1.setClickable(false);
            btn1.setEnabled(true);
            btn1.setImageResource(R.drawable.loc64);
            //Toast.makeText(context, "mera bhi chala", Toast.LENGTH_SHORT).show();
        }


    }

    public void checkbox2_clicked(View v) {
        if (checkBox2.isChecked()) {
            btn2.setEnabled(false);
            btn2.setImageResource(R.drawable.loconclick64);
            checkBox2.setText("Re-Capture");


        } else {
            gpslat2.setText("Lat:");
            gpslong2.setText("Long:");
            checkBox2.setText("");
            checkBox2.setClickable(false);
            btn2.setEnabled(true);
            btn2.setImageResource(R.drawable.loc64);


        }

    }

    public void checkbox3_clicked(View v) {
        if (checkBox3.isChecked()) {
            btn3.setEnabled(false);
            btn3.setImageResource(R.drawable.loconclick64);
            checkBox3.setText("Re-Capture");


        } else {
            gpslat3.setText("Lat:");
            gpslong3.setText("Long:");
            checkBox3.setText("");
            btn3.setEnabled(true);
            btn3.setImageResource(R.drawable.loc64);
            checkBox3.setClickable(false);


        }

    }

    public void checkbox4_clicked(View v) {
        if (checkBox4.isChecked()) {
            btn4.setEnabled(false);
            btn4.setImageResource(R.drawable.loconclick64);
            checkBox4.setText("Re-Capture");


        } else {
            gpslat4.setText("Lat:");
            gpslong4.setText("Long:");
            checkBox4.setText("");
            btn4.setEnabled(true);
            btn4.setImageResource(R.drawable.loc64);
            checkBox4.setClickable(false);


        }

    }

    public void checkbox5_clicked(View v) {

        if (checkBox5.isChecked()) {
            btn5.setEnabled(false);
            btn5.setImageResource(R.drawable.loconclick64);
            checkBox5.setText("Re-Capture");


        } else {
            gpslat5.setText("Lat:");
            gpslong5.setText("Long:");
            checkBox5.setText("");
            btn5.setEnabled(true);
            btn5.setImageResource(R.drawable.loc64);
            checkBox5.setClickable(false);


        }

    }

    public void checkbox6_clicked(View v) {

        if (checkBox6.isChecked()) {
            btn6.setEnabled(false);
            btn6.setImageResource(R.drawable.loconclick64);
            checkBox6.setText("Re-Capture");


        } else {
            gpslat6.setText("Lat:");
            gpslong6.setText("Long:");
            checkBox6.setText("");
            btn6.setEnabled(true);
            btn6.setImageResource(R.drawable.loc64);
            checkBox6.setClickable(false);


        }
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
                    .addOnConnectionFailedListener(GetFarmLocationActivity.this).build();
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
                                status.startResolutionForResult(GetFarmLocationActivity.this, 1000);
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


    private void init() {
        btn1 = (ImageView) findViewById(R.id.gpsbut1);
        btn2 = (ImageView) findViewById(R.id.gpsbut2);
        btn3 = (ImageView) findViewById(R.id.gpsbut3);
        btn4 = (ImageView) findViewById(R.id.gpsbut4);
        btn5 = (ImageView) findViewById(R.id.gpsbut5);
        btn6 = (ImageView) findViewById(R.id.gpsbut6);
        btncheck = (Button) findViewById(R.id.checklocation);
        btnsubmit=(Button)findViewById(R.id.submit_gpsc);
        //showlog=(ImageView)findViewById(R.id.showlog);
        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
        checkBox5 = (CheckBox) findViewById(R.id.checkBox5);
        checkBox6 = (CheckBox) findViewById(R.id.checkBox6);
      /*  mLatitudeTextView = (TextView) findViewById((R.id.latitude_textview));
        mLongitudeTextView = (TextView) findViewById((R.id.longitude_textview));
        mLatitudeTextView.setVisibility(View.GONE);
        mLongitudeTextView.setVisibility(View.GONE);*/
        btncheck.setVisibility(View.GONE);
        checkBox1.setVisibility(View.GONE);
        checkBox2.setVisibility(View.GONE);
        checkBox3.setVisibility(View.GONE);
        checkBox4.setVisibility(View.GONE);
        checkBox5.setVisibility(View.GONE);
        checkBox6.setVisibility(View.GONE);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btncheck.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);
        //showlog.setOnClickListener(this);
        gpslat1 = (TextView) findViewById(R.id.tvlat1);
        gpslat2 = (TextView) findViewById(R.id.tvlat2);
        gpslat3 = (TextView) findViewById(R.id.tvlat3);
        gpslat4 = (TextView) findViewById(R.id.tvlat4);
        gpslat5 = (TextView) findViewById(R.id.tvlat5);
        gpslat6 = (TextView) findViewById(R.id.tvlat6);
        gpslong1 = (TextView) findViewById(R.id.tvlong1);
        gpslong2 = (TextView) findViewById(R.id.tvlong2);
        gpslong3 = (TextView) findViewById(R.id.tvlong3);
        gpslong4 = (TextView) findViewById(R.id.tvlong4);
        gpslong5 = (TextView) findViewById(R.id.tvlong5);
        gpslong6 = (TextView) findViewById(R.id.tvlong6);
        gpsccheck = (TextView) findViewById(R.id.tvcheckloc);
        gpsccheck.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gpsbut1: {
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    connected = true;
                } else {
                    connected = false;
                }


                if (connected) {
                    /*progressDialog = ProgressDialog.show(GetFarmLocationActivity.this,
                            getString(R.string.dialog_please_wait), "");*/
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
                    flag = "cor1";
                    onLocationChanged(mLocation);

                  } else {
                    Toast.makeText(context, "Turn on the Network", Toast.LENGTH_SHORT).show();
                }

                break;
            }
            case R.id.gpsbut2: {
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                } else {
                    connected = false;
                }


                if (connected) {
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
                    flag = "cor2";
                    onLocationChanged(mLocation);
                } else {
                    Toast.makeText(context, "Turn on the Network", Toast.LENGTH_SHORT).show();
                }


                break;
            }
            case R.id.gpsbut3: {

                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                } else {
                    connected = false;
                }


                if (connected) {
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
                    flag = "cor3";

                    onLocationChanged(mLocation);
                } else {
                    Toast.makeText(context, "Turn on the Network", Toast.LENGTH_SHORT).show();
                }

                break;
            }
            case R.id.gpsbut4: {

                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                } else {
                    connected = false;
                }


                if (connected) {
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
                    flag = "cor4";

                    onLocationChanged(mLocation);
                }
                   else {
                    Toast.makeText(context, "Turn on the Network", Toast.LENGTH_SHORT).show();
                }

                break;
            }
            case R.id.gpsbut5: {

                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                } else {
                    connected = false;
                }

                if (connected) {
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
                    flag = "cor5";

                    onLocationChanged(mLocation);
                } else {
                    Toast.makeText(context, "Turn on the Network", Toast.LENGTH_SHORT).show();
                }

                break;
            }

            case R.id.gpsbut6: {
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                } else {
                    connected = false;
                }
                if (connected) {
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
                    flag = "cor6";

                    onLocationChanged(mLocation);
                    }  else {
                    Toast.makeText(context, "Turn on the Network", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.submit_gpsc:{

                              if(checkBox1.isChecked() && checkBox2.isChecked() && checkBox3.isChecked() && checkBox4.isChecked())
                {
                    progressDialog = ProgressDialog.show(GetFarmLocationActivity.this,
                            getString(R.string.dialog_please_wait), "");

                    callVolley();

                }else{
                    Toast.makeText(context, "Please Take all coordinates", Toast.LENGTH_SHORT).show();
                }

                break;
            }

        }
    }

    private void callVolley() {

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,

                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("\"Co-ordinates Saved Successfully\"")) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(context, VerifySingleFarmActivity.class);
                                //intent.putExtra("farm_num",farm_num);
                                Toast.makeText(context, "Farm Location Captured Successfully", Toast.LENGTH_SHORT).show();
                                intent.putExtra("from","getfarm");
                                startActivity(intent);
                                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(GetFarmLocationActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    if(farm_num!=null) {
                        params.put(KEY_FARM_NUM, farm_num);
                    }
                    if(ct1!=null){
                        params.put(KEY_TOKEN,ct1);
                    }
                    if(gps1!=null){
                        params.put(KEY_GPSC1, gps1);
                    }
                    if(gps2!=null){                        params.put(KEY_GPSC2, gps2);
                    }
                    if(gps3!=null){                        params.put(KEY_GPSC3, gps3);
                    }
                    if(gps4!=null){                        params.put(KEY_GPSC4, gps4);
                    }
                    if(gps5!=null){                        params.put(KEY_GPSC5, gps5);
                    }
                    if(gps6!=null){                        params.put(KEY_GPSC6, gps6);
                    }


                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }catch (Exception e){}
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

}