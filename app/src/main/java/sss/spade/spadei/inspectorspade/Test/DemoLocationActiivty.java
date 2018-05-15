package sss.spade.spadei.inspectorspade.Test;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;

import sss.spade.spadei.inspectorspade.R;

public class DemoLocationActiivty extends AppCompatActivity implements View.OnClickListener, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    LocationManager locationManager;
    String mprovider;
    GoogleApiClient googleApiClient;
    TextView gpslat1, gpslat2, gpslat3, gpslat4, gpslat5, gpslat6, gpslong1, gpslong2, gpslong3, gpslong4, gpslong5, gpslong6,gpsccheck;
    Context context;
    Location location, templocation;
    ProgressDialog progressDialog;
    boolean connected = false;
    ArrayList<String> arrayList = new ArrayList<String>();
    ImageView btn1, btn2, btn3, btn4, btn5, btn6;
    Button btncheck;
    ConnectivityManager connectivityManager;
    String flag = "";
    CheckBox checkBox1,checkBox2,checkBox3,checkBox4,checkBox5,checkBox6;
    String temp="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_location_actiivty);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        context = this;

        //showlog=(Button)findViewById(R.id.showlog);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mprovider = locationManager.getBestProvider(criteria, false);
        //gpslocation=(TextView)findViewById(R.id.gps_location);
        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            location = locationManager.getLastKnownLocation(mprovider);
            locationManager.requestLocationUpdates(mprovider, 2000, 1, this);

        }

        if(location!=null)

        {

            onLocationChanged(location);

        }
        else{

            Toast.makeText(getApplicationContext(),"location not found", Toast.LENGTH_LONG ).show();

        }
        init();

            /*gpslocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


        });
*/

       /* showlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("String Array: ",""+arrayList);
            }
        });*/


    }

    private void init() {
        btn1 = (ImageView) findViewById(R.id.gpsbut1);
        btn2 = (ImageView) findViewById(R.id.gpsbut2);
        btn3 = (ImageView) findViewById(R.id.gpsbut3);
        btn4 = (ImageView) findViewById(R.id.gpsbut4);
        btn5 = (ImageView) findViewById(R.id.gpsbut5);
        btn6 = (ImageView) findViewById(R.id.gpsbut6);
        btncheck=(Button)findViewById(R.id.checklocation);
        //showlog=(ImageView)findViewById(R.id.showlog);
        checkBox1 = (CheckBox)findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox)findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox)findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox)findViewById(R.id.checkBox4);
        checkBox5 = (CheckBox)findViewById(R.id.checkBox5);
        checkBox6 = (CheckBox)findViewById(R.id.checkBox6);

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
        gpsccheck=(TextView)findViewById(R.id.tvcheckloc);
    }

    public void checkbox1_clicked(View v)
    {
        if(checkBox1.isChecked())
        {
            btn1.setEnabled(false);
            btn1.setImageResource(R.drawable.loconclick64);
            checkBox1.setText("Re-Capture");

        }
        else
        {
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
        if(checkBox2.isChecked())
        {
            btn2.setEnabled(false);
            btn2.setImageResource(R.drawable.loconclick64);
            checkBox2.setText("Re-Capture");


        }
        else
        {
            gpslat2.setText("Lat:");
            gpslong2.setText("Long:");
            checkBox2.setText("");
            checkBox2.setClickable(false);
            btn2.setEnabled(true);
            btn2.setImageResource(R.drawable.loc64);


        }

    }

    public void checkbox3_clicked(View v) {
        if(checkBox3.isChecked())
        {
            btn3.setEnabled(false);
            btn3.setImageResource(R.drawable.loconclick64);
            checkBox3.setText("Re-Capture");


        }
        else
        {
            gpslat3.setText("Lat:");
            gpslong3.setText("Long:");
            checkBox3.setText("");
            btn3.setEnabled(true);
            btn3.setImageResource(R.drawable.loc64);
            checkBox3.setClickable(false);


        }

    }

    public void checkbox4_clicked(View v) {
        if(checkBox4.isChecked())
        {
            btn4.setEnabled(false);
            btn4.setImageResource(R.drawable.loconclick64);
            checkBox4.setText("Re-Capture");


        }
        else
        {
            gpslat4.setText("Lat:");
            gpslong4.setText("Long:");
            checkBox4.setText("");
            btn4.setEnabled(true);
            btn4.setImageResource(R.drawable.loc64);
            checkBox4.setClickable(false);


        }

    }

    public void checkbox5_clicked(View v) {

        if(checkBox5.isChecked())
        {
            btn5.setEnabled(false);
            btn5.setImageResource(R.drawable.loconclick64);
            checkBox5.setText("Re-Capture");


        }
        else
        {
            gpslat5.setText("Lat:");
            gpslong5.setText("Long:");
            checkBox5.setText("");
            btn5.setEnabled(true);
            btn5.setImageResource(R.drawable.loc64);
            checkBox5.setClickable(false);



        }

    }

    public void checkbox6_clicked(View v) {

        if(checkBox6.isChecked())
        {
            btn6.setEnabled(false);
            btn6.setImageResource(R.drawable.loconclick64);
            checkBox6.setText("Re-Capture");


        }
        else
        {
            gpslat6.setText("Lat:");
            gpslong6.setText("Long:");
            checkBox6.setText("");
            btn6.setEnabled(true);
            btn6.setImageResource(R.drawable.loc64);
            checkBox6.setClickable(false);



        }
    }
    @Override
    public void onLocationChanged(Location location) {
       /* if (flag.equals("cor1")) {
            if (!Double.toString(location.getLongitude()).equals("")) {
                gpslong1.setText("Long: " + location.getLongitude());
                gpslat1.setText("Lat : " + location.getLatitude());
                checkBox1.setVisibility(View.VISIBLE);
                checkBox1.setClickable(true);
                checkBox1.setChecked(true);
                checkBox1.setText("Re-Capture");
                btn1.setImageResource(R.drawable.loconclick64);
                //btn1.setBackgroundColor(Color.GREEN);
                btn1.setEnabled(false);
                flag = "";


                progressDialog.dismiss();
            }
        } else if (flag.equals("cor2")) {
            if (!Double.toString(location.getLongitude()).equals("")) {
                // arrayList.add(Double.toString(location.getLatitude())+Double.toString(location.getLongitude()));
                gpslong2.setText("Long: " + location.getLongitude());
                gpslat2.setText("Lat : " + location.getLatitude());
                checkBox2.setVisibility(View.VISIBLE);
                checkBox2.setChecked(true);
                checkBox2.setText("Re-Capture");
                checkBox2.setClickable(true);

                btn2.setImageResource(R.drawable.loconclick64);
                //btn1.setBackgroundColor(Color.GREEN);
                btn2.setEnabled(false);
                flag = "";

                progressDialog.dismiss();

            }
        } else if (flag.equals("cor3")) {
            if (!Double.toString(location.getLongitude()).equals("")) {
                // arrayList.add(Double.toString(location.getLatitude())+Double.toString(location.getLongitude()));
                gpslong3.setText("Long: " + location.getLongitude());
                gpslat3.setText("Lat : " + location.getLatitude());
                checkBox3.setVisibility(View.VISIBLE);
                checkBox3.setChecked(true);
                checkBox3.setText("Re-Capture");
                checkBox3.setClickable(true);

                btn3.setImageResource(R.drawable.loconclick64);
                //btn1.setBackgroundColor(Color.GREEN);
                btn3.setEnabled(false);
                flag = "";

                progressDialog.dismiss();
            }

        } else if (flag.equals("cor4")) {
            if (!Double.toString(location.getLongitude()).equals("")) {
                // arrayList.add(Double.toString(location.getLatitude())+Double.toString(location.getLongitude()));
                gpslong4.setText("Long: " + location.getLongitude());
                gpslat4.setText("Lat : " + location.getLatitude());
                checkBox4.setVisibility(View.VISIBLE);
                checkBox4.setChecked(true);
                checkBox4.setText("Re-Capture");
                checkBox4.setClickable(true);

                btn4.setImageResource(R.drawable.loconclick64);
                //btn1.setBackgroundColor(Color.GREEN);
                btn4.setEnabled(false);
                flag = "";


                progressDialog.dismiss();
            }

        } else if (flag.equals("cor5")) {
            if (!Double.toString(location.getLongitude()).equals("")) {
                // arrayList.add(Double.toString(location.getLatitude())+Double.toString(location.getLongitude()));
                gpslong5.setText("Long: " + location.getLongitude());
                gpslat5.setText("Lat : " + location.getLatitude());
                checkBox5.setVisibility(View.VISIBLE);
                checkBox5.setChecked(true);
                checkBox5.setText("Re-Capture");
                checkBox5.setClickable(true);

                btn5.setImageResource(R.drawable.loconclick64);
                //btn1.setBackgroundColor(Color.GREEN);
                btn5.setEnabled(false);
                flag = "";

                progressDialog.dismiss();
            }

        } else if (flag.equals("cor6")) {
            if (!Double.toString(location.getLongitude()).equals("")) {
                // arrayList.add(Double.toString(location.getLatitude())+Double.toString(location.getLongitude()));
                gpslong6.setText("Long: " + location.getLongitude());
                gpslat6.setText("Lat : " + location.getLatitude());
                checkBox6.setVisibility(View.VISIBLE);
                checkBox6.setChecked(true);
                checkBox6.setText("Re-Capture");
                checkBox6.setClickable(true);

                btn6.setImageResource(R.drawable.loconclick64);
                //btn1.setBackgroundColor(Color.GREEN);
                btn6.setEnabled(false);
                flag = "";

                progressDialog.dismiss();
            }
        }else if(temp.equals("check")){*/
            //if (!Double.toString(location.getLongitude()).equals("")) {
                // arrayList.add(Double.toString(location.getLatitude())+Double.toString(location.getLongitude()));
                gpsccheck.setText("Lat :"+location.getLatitude()+" Long :"+location.getLongitude());
        Toast.makeText(context, location.getLongitude()+" "+location.getLatitude(), Toast.LENGTH_SHORT).show();
               // progressDialog.dismiss();
            //}
        //}
    }

    /* @Override
        public void onLocationChanged(Location location) {
            TextView longitude = (TextView) findViewById(R.id.textView);
            TextView latitude = (TextView) findViewById(R.id.textView1);

            if(!Double.toString(location.getLongitude()).equals("")) {
                arrayList.add(Double.toString(location.getLatitude())+Double.toString(location.getLongitude()));
                longitude.setText("Current Longitude:" + location.getLongitude());
                latitude.setText("Current Latitude:" + location.getLatitude());
                progressDialog.dismiss();
            }
        }
    */
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(this, "Enabled new provider " + s,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(DemoLocationActiivty.this).build();
            googleApiClient.connect();
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
                    .checkLocationSettings(googleApiClient, builder.build());
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
                                status.startResolutionForResult(DemoLocationActiivty.this, 1000);
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

/*        Toast.makeText(this, "Disabled provider " + s,
                Toast.LENGTH_SHORT).show();*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gpsbut1: {
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                } else {
                    connected = false;
                }


                if (connected) {
                    progressDialog = ProgressDialog.show(DemoLocationActiivty.this,
                            getString(R.string.dialog_please_wait), "");
                    flag = "cor1";

                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Criteria criteria = new Criteria();
                    mprovider = locationManager.getBestProvider(criteria, false);
                    //gpslocation=(TextView)findViewById(R.id.gps_location);
                    if (mprovider != null && !mprovider.equals("")) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        location = locationManager.getLastKnownLocation(mprovider);
                        //locationManager.requestLocationUpdates(mprovider, 400, 1, this);
                    }
                    if (location != null) {
                        onLocationChanged(location);
                      //  flag = "";


                    /*    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        templocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                        locationchanged(templocation, flag);*/
                    } else
                        Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
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
                    progressDialog = ProgressDialog.show(DemoLocationActiivty.this,
                            getString(R.string.dialog_please_wait), "");
                    flag = "cor2";

                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Criteria criteria = new Criteria();
                    mprovider = locationManager.getBestProvider(criteria, false);
                    //gpslocation=(TextView)findViewById(R.id.gps_location);
                    if (mprovider != null && !mprovider.equals("")) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        location = locationManager.getLastKnownLocation(mprovider);
                        //locationManager.requestLocationUpdates(mprovider, 400, 1, this);
                    }
                    if (location != null) {
                        onLocationChanged(location);
                       // flag = "";


                        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        templocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                        locationchanged(templocation, flag);*/
                    } else
                        Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
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
                    progressDialog = ProgressDialog.show(DemoLocationActiivty.this,
                            getString(R.string.dialog_please_wait), "");
                    flag = "cor3";

                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Criteria criteria = new Criteria();
                    mprovider = locationManager.getBestProvider(criteria, false);
                    //gpslocation=(TextView)findViewById(R.id.gps_location);
                    if (mprovider != null && !mprovider.equals("")) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        location = locationManager.getLastKnownLocation(mprovider);
                        //locationManager.requestLocationUpdates(mprovider, 400, 1, this);
                    }
                    if (location != null) {
                        onLocationChanged(location);

                        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        templocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                        locationchanged(templocation, flag);*/
                    } else
                        Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
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
                    progressDialog = ProgressDialog.show(DemoLocationActiivty.this,
                            getString(R.string.dialog_please_wait), "");
                    flag = "cor4";

                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Criteria criteria = new Criteria();
                    mprovider = locationManager.getBestProvider(criteria, false);
                    //gpslocation=(TextView)findViewById(R.id.gps_location);
                    if (mprovider != null && !mprovider.equals("")) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        location = locationManager.getLastKnownLocation(mprovider);
                        //locationManager.requestLocationUpdates(mprovider, 400, 1, this);
                    }
                    if (location != null) {
                        onLocationChanged(location);
                        flag = "";

                        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        templocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                        locationchanged(templocation, flag);*/
                    } else
                        Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
                } else {
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
                    progressDialog = ProgressDialog.show(DemoLocationActiivty.this,
                            getString(R.string.dialog_please_wait), "");
                    flag = "cor5";

                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Criteria criteria = new Criteria();
                    mprovider = locationManager.getBestProvider(criteria, false);
                    //gpslocation=(TextView)findViewById(R.id.gps_location);
                    if (mprovider != null && !mprovider.equals("")) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        location = locationManager.getLastKnownLocation(mprovider);
                        //locationManager.requestLocationUpdates(mprovider, 400, 1, this);
                    }
                    if (location != null) {

                        onLocationChanged(location);
                        flag = "";


                        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }*/
                        /*templocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                        locationchanged(templocation, flag);*/
                    } else
                        Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
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
                    progressDialog = ProgressDialog.show(DemoLocationActiivty.this,
                            getString(R.string.dialog_please_wait), "");
                    flag = "cor6";

                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Criteria criteria = new Criteria();
                    mprovider = locationManager.getBestProvider(criteria, false);
                    //gpslocation=(TextView)findViewById(R.id.gps_location);
                    if (mprovider != null && !mprovider.equals("")) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        location = locationManager.getLastKnownLocation(mprovider);
                        //locationManager.requestLocationUpdates(mprovider, 400, 1, this);
                    }
                    if (location != null) {
                      /*  if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        templocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);*/
                        onLocationChanged(location);
                        flag = "";

                    } else
                        Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Turn on the Network", Toast.LENGTH_SHORT).show();
                }

                break;
            }


            case R.id.checklocation: {
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                } else {
                    connected = false;
                }


                if (connected) {
                   /* progressDialog = ProgressDialog.show(DemoLocationActiivty.this,
                            getString(R.string.dialog_please_wait), "");
                   */ temp = "check";

                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Criteria criteria = new Criteria();
                    mprovider = locationManager.getBestProvider(criteria, false);
                    //gpslocation=(TextView)findViewById(R.id.gps_location);
                    if (mprovider != null && !mprovider.equals("")) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        location = locationManager.getLastKnownLocation(mprovider);
                        //locationManager.requestLocationUpdates(mprovider, 400, 1, this);
                    }
                    if (location != null) {
                      /*  if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        templocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);*/
                        onLocationChanged(location);
                        //flag = "";

                    } else{}
                        //Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Turn on the Network", Toast.LENGTH_SHORT).show();
                }

                break;
            }
        }
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

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

   /* protected void startLocationUpdates() {
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
*/
}