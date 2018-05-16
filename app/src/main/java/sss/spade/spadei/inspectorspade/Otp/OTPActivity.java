package sss.spade.spadei.inspectorspade.Otp;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sss.spade.spadei.inspectorspade.DataHandler.DataHandler;
import sss.spade.spadei.inspectorspade.LandingActivity.LandingActivity;
import sss.spade.spadei.inspectorspade.PersonData.FillProfileActivity;
import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.SharedPref.SharedPreferencesMethod;

public class OTPActivity extends AppCompatActivity {

    EditText actotp;
    Toolbar mActionBarToolbar;
    String charset = "UTF-8";
    String mobileno;
     String OTPVERIFY="http://spade.farm/app/index.php/signUp/do_is_active_1";
    String usernum="";
    String response,finres,status,details,detailssessionid,otppp;
    TextView otpsent,otpreposne;
    String mob,mainurl,otpmessage;
    Context context;
    public static final String KEY_USERNUM = "user_num";
    String TAGIP="IPADRESS";
    StringBuilder responsebuild;
    StringBuilder responsebuildver;
    String responsever,finrespver;
    Boolean smspermission=false;
    ProgressDialog progressDialog;
    Button subotp;
    BroadcastReceiver receiver;
    TextView resendotp;
    String Ipaddress;
    final String authkey = "996859f5-9935-11e7-94da-0200cd936042";
    ConnectivityManager connectivityManager;
    boolean connected = false;



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*Intent intent=new Intent(context,MainActivity.class);
        startActivity(intent);
        finish();*/
        finish();
        return super.onOptionsItemSelected(item);
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        resendotp=(TextView)findViewById(R.id.resendotp);
        actotp=(EditText)findViewById(R.id.newactotp);
        subotp=(Button)findViewById(R.id.submitotppp);
        otpsent=(TextView)findViewById(R.id.sent);
        otpreposne=(TextView)findViewById(R.id.otpresponse);
        otpsent.setVisibility(View.GONE);
        otpreposne.setVisibility(View.GONE);

        context=this;
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


        TextView title=(TextView)findViewById(R.id.tittle);
        title.setText("Enter OTP");
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);

        //getSupportActionBar().setTitle("My Title");

        if (getSupportActionBar() != null){
           /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);*/
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        sendotp();

        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendotp();
            }
        });



        receiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equalsIgnoreCase("otp")) {
                    otpmessage = intent.getStringExtra("message");
                    progressDialog.dismiss();
                    actotp.setText(otpmessage);
                    submitotp();
                }
                    //Do whatever you want with the code here

            }
        };




        subotp.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    connected = true;
                } else {
                    connected = false;
                }


                if (connected) {
                    submitotp();
                }
                    else {
                    Toast.makeText(context, R.string.internet_not_connected, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void submitotp() {
        progressDialog = ProgressDialog.show(OTPActivity.this,"Please Wait...", "");
        otppp=actotp.getText().toString().trim();
        String verifyURL= "https://2factor.in/API/V1/"+authkey+"/SMS/VERIFY/"+detailssessionid+"/"+otppp;
        mobileno="";
        //verifyURL="https://2factor.in/API/V1/996859f5-9935-11e7-94da-0200cd936042/SMS/VERIFY/742627ff-994d-11e7-94da-0200cd936042/66135";
        usernum= DataHandler.newInstance().getLoginUsernum();
        OtpAsynctaskrunner runner = new OtpAsynctaskrunner();
        runner.execute(verifyURL, mobileno,usernum);
        Ipaddress=getIPAddress(true);
        //Toast.makeText(OTPActivity.this, Ipaddress, Toast.LENGTH_SHORT).show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        Log.e(TAGIP,Ipaddress);
        Log.e("TIME",currentDateandTime);
      //  Toast.makeText(OTPActivity.this, currentDateandTime, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void sendotp() {
        mobileno= DataHandler.newInstance().getLoginMobile();
        if(smspermission==smsPermission()) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_SMS}, 1);
        }
        usernum="";
        String mainUrl = "https://2factor.in/API/V1/" + authkey + "/SMS/" + mobileno + "/AUTOGEN";
        OtpAsynctaskrunner runner = new OtpAsynctaskrunner();
        runner.execute(mainUrl,mobileno,usernum);
        progressDialog = ProgressDialog.show(OTPActivity.this,"Please Wait Fetching Otp","To dismiss click anywhere");
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new Dialog.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
                //Toast.makeText(OTPActivity.this, "Hereeeeeeeeeeeeeee", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    private class OtpAsynctaskrunner extends AsyncTask<String, Void, String> {
        public OtpAsynctaskrunner() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            otpsent.setText(details);
            otpreposne.setText(status);

            if(mob.equals(""))
            {
                if(details.equals("OTP Matched")&& status.equals("Success"))
                {
                    progressDialog.dismiss();
                    if(!usernum.equals("")) {

                        try {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, OTPVERIFY,
                                    new com.android.volley.Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if (response.equals("\"Successful\"")) {
                                                Intent intent = new Intent(context, LandingActivity.class);
                                                SharedPreferencesMethod.setBoolean(context, "Login", true);

                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(OTPActivity.this, "User Not found", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    },
                                    new com.android.volley.Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            progressDialog.dismiss();
                                            Toast.makeText(OTPActivity.this, R.string.error_text, Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put(KEY_USERNUM, usernum);
                                    return params;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                            requestQueue.add(stringRequest);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }else{}

                    /*Intent intent=new Intent(context,ProfileActivity.class);
                    startActivity(intent);*/
                }


                else {
                    progressDialog.dismiss();
                    Toast.makeText(OTPActivity.this, "Please Enter Correct OTP", Toast.LENGTH_SHORT).show();
                }

            }
            else {
              //  Toast.makeText(OTPActivity.this, "Not Possible", Toast.LENGTH_SHORT).show();

            }

            //actotp.setText(otpmessage);
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected String doInBackground(String... params) {
            mainurl = params[0];
            mob = params[1];
            usernum=params[2];

            URLConnection myURLConnection = null;
            URL myURL = null;
            BufferedReader reader = null;


//Prepare parameter string
            StringBuilder sbPostData = new StringBuilder(mainurl);
//final string
            mainurl = sbPostData.toString();
            try {
                //prepare connection
                myURL = new URL(mainurl);
                myURLConnection = myURL.openConnection();
               myURLConnection.connect();

                reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
                responsebuild=new StringBuilder();
                response="";
                //reading response
                while ((response = reader.readLine()) != null) {
                    //print response
                    Log.e("RESPONSE", "" + response);
                   responsebuild.append(response);
                }

                try {
                    finres=responsebuild.toString();
                    JSONObject mainObject = new JSONObject(finres);
                      status = mainObject.getString("Status");
                     details = mainObject.getString("Details");

                    if(!mob.equals("")&&usernum.equals("")){
                    if(status.equals("Error")){
                        runOnUiThread(new Runnable(){

                            @Override
                            public void run(){
                                //update ui here
                                // display toast here
                                                  Toast.makeText(OTPActivity.this, "Please Resend Otp Server Error", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }}
                    if (!mob.equals(""))
                    {
                        detailssessionid=details;
                        Log.e("Detailedsession ID", "" + detailssessionid);

                    }
                    else {}
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //finally close connection
                reader.close();

            } catch (IOException e) {

                HttpURLConnection httpConn = null;

                try {
                    myURLConnection = myURL.openConnection();
                    httpConn = (HttpURLConnection) myURLConnection;

                    InputStream str=httpConn.getErrorStream();
                    httpConn.connect();

                    BufferedReader br = new BufferedReader(new InputStreamReader(str));

                    responsebuild=new StringBuilder();
                    response="";

                    //reading response
                    while ((response = br.readLine()) != null) {
                        //print response
                        Log.e("RESPONSE", "" + response);
                        responsebuild.append(response);
                    }

                    try {
                        finres = responsebuild.toString();
                        JSONObject mainObject = new JSONObject(finres);
                        status = mainObject.getString("Status");
                        details = mainObject.getString("Details");
                    }catch (Exception t){
                        t.printStackTrace();
                    }

                    br.close();


                } catch (IOException e1) {
                    e1.printStackTrace();
                }


            }





            return details;
        }



    }


    public boolean smsPermission()
    {
        String permission = "android.permission.READ_SMS";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }





    }
