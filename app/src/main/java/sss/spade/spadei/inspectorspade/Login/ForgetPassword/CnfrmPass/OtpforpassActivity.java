package sss.spade.spadei.inspectorspade.Login.ForgetPassword.CnfrmPass;

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
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import sss.spade.spadei.inspectorspade.DataHandler.DataHandler;
import sss.spade.spadei.inspectorspade.Login.MainActivity;
import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.SharedPref.SharedPreferencesMethod;

public class OtpforpassActivity extends AppCompatActivity {
    Toolbar mActionBarToolbar;
    EditText otpforpass;
    TextView resendotpforpass,otpsentforpass,otpreposneforpass;
    Button subotpforpass;
    String otpforpasss;
    Context context;
    String OTPVERIFY="http://spade.farm/app/index.php/signUp/forget_password";
    String response,status,details,detailedsessionid;
    String finres;
    final String KEY_MOBILE="mobNo";
    final String KEY_EMAIL="email";
    final String KEY_FORGETPASS="password";

    ProgressDialog progressDialog;
    Boolean smspermission=false;
    BroadcastReceiver receiver;
    StringBuilder responsebuild;

    String otpmessage;
    String mobileno,mainurl;
    ConnectivityManager connectivityManager;
    boolean connected = false;



    String email="0";
    String passonfrgt="";
    String usernum;
    final String authkey = "996859f5-9935-11e7-94da-0200cd936042";

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent=new Intent(context,MainActivity.class);
        startActivity(intent);
        finish();
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpforpass);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        resendotpforpass=(TextView)findViewById(R.id.resendotpforpass);
        otpforpass=(EditText)findViewById(R.id.otpforpass);
        subotpforpass=(Button)findViewById(R.id.submitforpass);
        otpsentforpass=(TextView)findViewById(R.id.sentforpass);
        otpreposneforpass=(TextView)findViewById(R.id.otpresponseforpass);
        otpsentforpass.setVisibility(View.GONE);
        otpreposneforpass.setVisibility(View.GONE);
        context=this;
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


        TextView title=(TextView)findViewById(R.id.tittle);
        title.setText("Enter OTP");
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);

        //getSupportActionBar().setTitle("My Title");

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        sendotp();

        resendotpforpass.setOnClickListener(new View.OnClickListener() {
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
                    otpforpass.setText(otpmessage);
                    //submitotp();
                }//Do whatever you want with the code here
            }
        };

        subotpforpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    connected = true;
                } else {
                    connected = false;
                }
                if (connected) {
                    submitotp();}else{
                    Toast.makeText(context, R.string.internet_not_connected, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void submitotp() {
        mobileno= DataHandler.newInstance().getMoborEmailonforget();
        passonfrgt= DataHandler.newInstance().getPasswordonfrgt();
        progressDialog = ProgressDialog.show(OtpforpassActivity.this,"Please Wait...", "");
        otpforpasss=otpforpass.getText().toString().trim();
        String verifyURL= "https://2factor.in/API/V1/"+authkey+"/SMS/VERIFY/"+detailedsessionid+"/"+otpforpasss;
        //verifyURL="https://2factor.in/API/V1/996859f5-9935-11e7-94da-0200cd936042/SMS/VERIFY/742627ff-994d-11e7-94da-0200cd936042/66135";
        OtpAsynctaskrunner runner = new OtpAsynctaskrunner();
        runner.execute(verifyURL, mobileno,passonfrgt,email);
    }

    private void sendotp() {

        mobileno= DataHandler.newInstance().getMoborEmailonforget();
        passonfrgt="";
        email="0";
        if(smspermission==smsPermission()) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_SMS}, 1);
        }
        String mainUrl = "https://2factor.in/API/V1/" + authkey + "/SMS/" + mobileno + "/AUTOGEN";
        mobileno="";
        OtpAsynctaskrunner runner = new OtpAsynctaskrunner();
        runner.execute(mainUrl,mobileno,passonfrgt,email);
        progressDialog = ProgressDialog.show(OtpforpassActivity.this,"Please Wait Fetching Otp","To dismiss click anywhere");
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new Dialog.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
                progressDialog.dismiss();
            }
        });
    }


    private class OtpAsynctaskrunner extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            otpsentforpass.setText(details);
            otpreposneforpass.setText(status);

            if(!mobileno.equals("")&&!passonfrgt.equals("")&&email.equals("0"))
            {

                if(details.equals("OTP Matched")&& status.equals("Success"))
                {
                    //progressDialog.dismiss();
                    //if(!usernum.equals("")) {

                    try {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, OTPVERIFY,
                                new com.android.volley.Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("\"Password Changed Successfully\"")) {
                                            progressDialog.dismiss();
                                            Toast.makeText(OtpforpassActivity.this, "Password changed Successfully", Toast.LENGTH_SHORT).show();
                                            SharedPreferencesMethod.clear(context);
                                            SharedPreferencesMethod.setBoolean(context, "Login", false);
                                            Intent intent = new Intent(context, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(OtpforpassActivity.this, "Password not changed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                },
                                new com.android.volley.Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.dismiss();
                                        Toast.makeText(OtpforpassActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                if(email!=null)
                                params.put(KEY_EMAIL, email);
                                if(passonfrgt!=null)
                                params.put(KEY_FORGETPASS, passonfrgt);
                                if(mobileno!=null)
                                params.put(KEY_MOBILE, mobileno);

                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(stringRequest);
                    } catch (Exception e) {
                    }

                    //}else{}

                    /*Intent intent=new Intent(context,ProfileActivity.class);
                    startActivity(intent);*/
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(OtpforpassActivity.this, "Please Enter Correct OTP", Toast.LENGTH_SHORT).show();
                }

            } else {

            }

            //actotp.setText(otpmessage);
            super.onPostExecute(s);        }


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
        protected String doInBackground(String... strings) {

            mainurl = strings[0];
            mobileno = strings[1];
            passonfrgt=strings[2];
            email=strings[3];

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

                    if(mobileno.equals("")&&passonfrgt.equals("")){

                        detailedsessionid=details;
                        Log.e("Detailedsession ID", "" + detailedsessionid);

                        if(status.equals("Error")){
                            runOnUiThread(new Runnable(){
                                @Override
                                public void run(){
                                    //update ui here
                                    // display toast here
                                    Toast.makeText(OtpforpassActivity.this, "Please Resend Otp Server Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }}

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

        public OtpAsynctaskrunner() {
            super();
        }
    }


    public boolean smsPermission()
    {
        String permission = "android.permission.READ_SMS";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
}
