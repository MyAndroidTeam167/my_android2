package sss.spade.spadei.inspectorspade.Login;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
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

import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sss.spade.spadei.inspectorspade.Alert.alertDialogManager;
import sss.spade.spadei.inspectorspade.DataHandler.DataHandler;
import sss.spade.spadei.inspectorspade.LandingActivity.LandingActivity;
import sss.spade.spadei.inspectorspade.Login.ForgetPassword.FrgtPassActivity;
import sss.spade.spadei.inspectorspade.Login.NeedHelp.HelpActivity;
import sss.spade.spadei.inspectorspade.Otp.OTPActivity;
import sss.spade.spadei.inspectorspade.PersonData.FillProfileActivity;
import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.SharedPref.SharedPreferencesMethod;
import sss.spade.spadei.inspectorspade.SignUp.SignUpActivity;

import static sss.spade.spadei.inspectorspade.Validations.Validations.emailValidator;
import static sss.spade.spadei.inspectorspade.Validations.Validations.isValidMobile;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String REGISTER_URL = "http://spade.farm/app/index.php/signUp/is_login_successful";
    private static final String REGISTER_URL_CHECK_FILLED = "http://spade.farm/app/index.php/signUp/get_app_registry_data";
    //private static final String REGISTER_URL_DATA_PROFILE = "https://www.oswalcorns.com/my_farm/myfarmapp/index.php/signUp/get_person_num";
    public String[] parameternamearray,parametervaluearray;
    String responsee;
    Toolbar mActionBarToolbar;
    TextView tvnewuser;
    Context context;
    TextView forgetpass,helplogin;
    ProgressDialog progressDialog;
    alertDialogManager alert = new alertDialogManager();
    String mobilefromsigup="",passwordfromsignup="";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_MOBILE = "mobNo";
    public static final String KEY_USER_NUM = "user_num";
    ShowHidePasswordEditText password;
    public String usernumfinal;
    EditText emailtext;
    Button loginbutt;
    Boolean locationpermission=false;
    String emaillgoin,mobilelogin,passwordlogin;
    Boolean lock=false;
    private Boolean exit = false;
    ConnectivityManager connectivityManager;
    boolean connected = false;
    final String KEY_TOKEN="token1";
    String ct1;




    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        init();
    }

    private void init() {
        TextView title=(TextView)findViewById(R.id.tittle);
        title.setText(R.string.Accountlogin);
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        context=this;

        ct1=SharedPreferencesMethod.getString(context,"cctt");
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        tvnewuser=(TextView)findViewById(R.id.newUser);
        mobilefromsigup=DataHandler.newInstance().getSignUpMobile();
        passwordfromsignup= DataHandler.newInstance().getSignUpPassword();
        loginbutt=(Button)findViewById(R.id.loginbutt);
        emailtext= (EditText)findViewById(R.id.emaillogin);
        password = (ShowHidePasswordEditText) findViewById(R.id.pass);
        forgetpass=(TextView)findViewById(R.id.forgetpass);
        helplogin=(TextView)findViewById(R.id.help);
        tvnewuser.setOnClickListener(this);
        loginbutt.setOnClickListener(this);
        forgetpass.setOnClickListener(this);
        helplogin.setOnClickListener(this);
        forgetpass.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


        if(mobilefromsigup!=null&&passwordfromsignup!=null){
            if(!mobilefromsigup.equals("") && !passwordfromsignup.equals("")){
                emailtext.setText(mobilefromsigup);
                password.setText(passwordfromsignup);}}
                checkpermission();

    }

    private void checkpermission() {
        if(locationpermission==checkLocationPermission()) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case 1:{
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId() /*to get clicked view id**/) {
            case R.id.newUser: {
                Intent intent = new Intent(context, SignUpActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.forgetpass: {
                Intent intent = new Intent(context, FrgtPassActivity.class);
                startActivity(intent);
                finish();
                break;
            }

            case R.id.help: {
                Intent intent = new Intent(context, HelpActivity.class);
                startActivity(intent);
                finish();
                break;
            }

            case R.id.loginbutt: {
                int length = password.getText().toString().trim().length();

                if ((!emailValidator(emailtext.getText().toString().trim())) && (!isValidMobile(emailtext.getText().toString().trim()))) {
                    emailtext.setError(getString(R.string.invalid_email_mobile));
                } else if (!(length > 7 && length < 33)) {
                    password.setError(getString(R.string.password_er));
                    password.setText("");
                } else {

                    if (emailValidator(emailtext.getText().toString().trim())) {
                        emaillgoin = emailtext.getText().toString();
                    } else {
                        mobilelogin = emailtext.getText().toString();
                    }

                    passwordlogin = password.getText().toString();
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        connected = true;
                    } else {
                        connected = false;
                    }


                    if (connected) {

                        progressDialog = ProgressDialog.show(MainActivity.this,
                                getString(R.string.dialog_please_wait), "");


                        AsyncTaskRunner runnermainact = new AsyncTaskRunner();
                        runnermainact.execute(emaillgoin, mobilelogin, passwordlogin, "mainact", usernumfinal, REGISTER_URL);


                      /*  Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                GetText(usernumfinal);
                                AsyncTaskRunner runnerregistercheck = new AsyncTaskRunner();
                                runnerregistercheck.execute(emaillgoin, mobilelogin, passwordlogin, "regcheck", usernumfinal, REGISTER_URL_CHECK_FILLED);
                                // progressDialog.dismiss();
                            }
                        }, 2700);*/

/*
                        Handler handlerparacheck = new Handler();
                        handlerparacheck.postDelayed(new Runnable() {
                            public void run() {
                                if (lock) {
                                    // Toast.makeText(context, "Going Inside", Toast.LENGTH_SHORT).show();

                                    if (parameternamearray != null) {
                                        if (parameternamearray.length == 0) {
                                            progressDialog.dismiss();
                                            finish();
                                            Intent intent = new Intent(context, OTPActivity.class);
                                            SharedPreferencesMethod.setString(context,"cctt","1");

                                            startActivity(intent);
                                        } else if (parameternamearray.length == 1) {
                                            progressDialog.dismiss();
                                            finish();
                                            Intent intent = new Intent(context, FillProfileActivity.class);
                                            SharedPreferencesMethod.setString(context,"cctt","1");

                                            startActivity(intent);
                                        } else if (parameternamearray.length == 2) {
                                            if (parameternamearray[0].equals("is_otp_verified") && parametervaluearray[0].equals("1")) {
                                                if (parameternamearray[1].equals("is_profile_set") && parametervaluearray[1].equals("1")) {
                                                    progressDialog.dismiss();
                                                    Intent intent = new Intent(context, LandingActivity.class);
                                                    startActivity(intent);
                                                    SharedPreferencesMethod.setBoolean(context, "Login", true);
                                                    SharedPreferencesMethod.setString(context,"cctt","1");

                                                    Toast.makeText(context, R.string.welcome, Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else {
                                                    progressDialog.dismiss();
                                                    Intent intent = new Intent(context, FillProfileActivity.class);
                                                    Toast.makeText(context, R.string.profile_first, Toast.LENGTH_SHORT).show();
                                                    SharedPreferencesMethod.setString(context,"cctt","1");

                                                    startActivity(intent);
                                                    finish();
                                                }
                                            } else {
                                                progressDialog.dismiss();
                                                Intent intent = new Intent(context, OTPActivity.class);
                                                Toast.makeText(context, R.string.otp_first, Toast.LENGTH_SHORT).show();
                                                SharedPreferencesMethod.setString(context,"cctt","1");
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                               *//* else if (parameternamearray.length == 3) {
                                        alert.showAlertDialog(MainActivity.this, "Incorrect User","Please Login with Inspector Login id", false);
                                        progressDialog.dismiss();
                                    }*//*

                                    } else {

                                        alert.showAlertDialog(MainActivity.this, "Error", "Some Error occurred please try again later", false);
                                        progressDialog.dismiss();
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    //Toast.makeText(context, "Unable to login at the moment", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, 2500);*/

                    }else{
                        Toast.makeText(context, R.string.internet_not_connected, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }


    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {
        public AsyncTaskRunner() {
            super();
        }


        @Override
        protected String doInBackground(final String... params) {
            final String emailonrec,mobilonrec,passonrec,flagonrec,usernumonrec,urlonrec;
            emailonrec = params[0];
            mobilonrec   = params[1];
            passonrec=params[2];
            flagonrec=params[3];
            usernumonrec=params[4];
            urlonrec=params[5];

            try {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlonrec,



                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                responsee = response;
                                Log.e("TAG","In response"+emailonrec+" "+mobilonrec+" "+passonrec+" "+flagonrec+" "+usernumonrec+" "+urlonrec);

                                if(flagonrec.equals("mainact")) {

                                    Log.e("TAG",response);
                                    if (response.equals("\"0\"")) {
                                        alert.showAlertDialog(MainActivity.this, getString(R.string.dialog_password_incorrect), getString(R.string.dialog_password_incorrect_helper), false);
                                        progressDialog.dismiss();
                                        usernumfinal=null;
                                        parameternamearray=null;
                                        password.setText("");

                                    } else if(response.equals("\"000\"")){
                                        alert.showAlertDialog(MainActivity.this, getString(R.string.dialog_password_incorrect), getString(R.string.dialog_password_incorrect_helper), false);
                                        progressDialog.dismiss();
                                        usernumfinal=null;
                                        parameternamearray=null;
                                        password.setText("");
                                    }
                                    else{
                                        try {
                                            JSONObject jobject = new JSONObject(response);
                                            String emaillogin = jobject.getString("email");
                                            String mobilelogin = jobject.getString("mobNo");
                                            String usernum = jobject.getString("user_num");
                                            String is_farmer=jobject.getString("is_farmer");
                                            String is_inspector=jobject.getString("is_inspector");
                                            String is_admin=jobject.getString("is_admin");
                                            String comp_num=jobject.getString("comp_num");
                                            String is_active=jobject.getString("is_active");
                                            String is_verified=jobject.getString("is_verified");
                                            usernumfinal = usernum;
                                            DataHandler.newInstance().setLoginEmail(emaillogin);
                                            DataHandler.newInstance().setLoginMobile(mobilelogin);
                                          //  DataHandler.newInstance().setIs_active_otp(is_active);

                                            Log.e("TAG:",mobilelogin);
                                            DataHandler.newInstance().setLoginUsernum(usernum);
                                            SharedPreferencesMethod.setString(context, "Password", passonrec);
                                            SharedPreferencesMethod.setString(context, "UserNum", usernum);
                                            SharedPreferencesMethod.setString(context, "Mobile", mobilelogin);
                                            SharedPreferencesMethod.setString(context, "Email", emaillogin);
                                            Log.e("MainAct","Came here");
                                            Log.e("MainAct","admin"+is_admin+" inspector"+is_inspector+" farmer"+is_farmer);

                                            if(comp_num.equals("0")) {
                                                SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.BINDED, false);
                                            }else{
                                                SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.BINDED, true);
                                            }


                                            if(is_inspector.equals("Y")){
                                                    lock=true;
                                                    if(is_active.equals("Y")){

                                                        Intent intent =new Intent(context,LandingActivity.class);
                                                        SharedPreferencesMethod.setString(context,"cctt",comp_num);
                                                        SharedPreferencesMethod.setBoolean(context, "Login", true);
                                                        startActivity(intent);
                                                        finish();
                                                    }else{
                                                        Intent intent =new Intent(context,OTPActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                            }else{
                                                password.setText("");
                                                alert.showAlertDialog(MainActivity.this, "Incorrect User", "Please Login with Inspector Login id", false);
                                                progressDialog.dismiss();
                                            }

                                        } catch (JSONException e) {

                                            progressDialog.dismiss();
                                            Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        }
                                    }
                                }


                                else if(flagonrec.equals("regcheck")&&usernumonrec!=null){
                                    JSONArray jsonarray = null;
                                    try {
                                        jsonarray = new JSONArray(response);

                                        parameternamearray=new String[jsonarray.length()];
                                        parametervaluearray=new String[jsonarray.length()];
                                        //activitydescriptionfarmal=new String[jsonarray.length()];

                                        for (int i = 0; i < jsonarray.length(); i++) {
                                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                                            String parametername = jsonobject.getString("parameter_name");
                                            String parametervalue=jsonobject.getString("parameter_value");
                                            // String activitydescription=jsonobject.getString("activity_description");

                                            parameternamearray[i]=parametername;
                                            parametervaluearray[i]=parametervalue;
                                            // activitydescriptionfarmal[i]=activitydescription;
                                            Log.e("Date :",parametername+"Parameter value"+parametervalue);
                                        }

                            /*DataHandler.newInstance().setDatefarmcal(datefarmcal);
                            DataHandler.newInstance().setActivityfarmcal(activityfarmcal);
                            DataHandler.newInstance().setActivitydescriptionfarmcal(activitydescriptionfarmal);
*/
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        progressDialog.dismiss();
                                        Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show();

                                    }
                                }


                                else if(flagonrec.equals("profile")&&usernumonrec!=null) {

                                    String person_num=null;


                                    try {
                                        JSONObject jobject = new JSONObject(response);
                                        person_num=jobject.getString("person_num");
                                        SharedPreferencesMethod.setString(context,"person_num",person_num);
                                        Log.e("PERSON_num:",person_num);
                                    } catch (JSONException e) {
                                        progressDialog.dismiss();
                                        e.printStackTrace();
                                        Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show();
                                    }
                                    // Toast.makeText(LoginVerificationActivity.this, firstname, Toast.LENGTH_SHORT).show();
                                }


                                else if(flagonrec.equals("farmadd")&&usernumonrec!=null){
                                    String soiltype = null;
                                    String area = null;
                                    String gpsc1 = null;
                                    String gpsc2 = null;
                                    String gpsc3 = null;
                                    String gpsc4 = null;
                                    String gpsc5 = null;
                                    String gpsc6 = null;
                                    String irrigationtype = null;
                                    String speccomment = null;
                                    String addl1 = null;
                                    String addl2 = null;
                                    String addl3 = null;
                                    String farm_city = null;
                                    String farm_state = null;
                                    String farm_country = null;
                                    String growing_region=null;
                                    String growing_season=null;
                                    String sowing_date=null;
                                    String harvest_date=null;
                                    String crop_name = null;
                                    try {
                                        JSONObject jobject = new JSONObject(response);
                                        area = jobject.getString("area");
                                        gpsc1 = jobject.getString("GPSc1");
                                        gpsc2 = jobject.getString("GPSc2");
                                        gpsc3 = jobject.getString("GPSc3");
                                        gpsc4 = jobject.getString("GPSc4");
                                        gpsc5 = jobject.getString("GPSc5");
                                        gpsc6 = jobject.getString("GPSc6");
                                        soiltype = jobject.getString("soilType");
                                        irrigationtype = jobject.getString("irrigationType");
                                        speccomment = jobject.getString("specComment");
                                        addl1 = jobject.getString("addL1");
                                        addl2 = jobject.getString("addL2");
                                        addl3 = jobject.getString("addL3");
                                        farm_city = jobject.getString("city");
                                        farm_state = jobject.getString("state");
                                        farm_country = jobject.getString("country");
                                        growing_region=jobject.getString("growing_region");
                                        growing_season = jobject.getString("growing_season");
                                        sowing_date=jobject.getString("date_of_sowing");
                                        harvest_date=jobject.getString("expct_dateof_harvest");
                                        crop_name=jobject.getString("crop_name");

                                        SharedPreferencesMethod.setString(context,"farm_area",area);
                                        SharedPreferencesMethod.setString(context,"farm_gpsc1",gpsc1);
                                        SharedPreferencesMethod.setString(context,"farm_gpsc2",gpsc2);
                                        SharedPreferencesMethod.setString(context,"farm_gpsc3",gpsc3);
                                        SharedPreferencesMethod.setString(context,"farm_gpsc4",gpsc4);
                                        SharedPreferencesMethod.setString(context,"farm_gpsc5",gpsc5);
                                        SharedPreferencesMethod.setString(context,"farm_gpsc6",gpsc6);
                                        SharedPreferencesMethod.setString(context,"farm_soil_type",soiltype);
                                        SharedPreferencesMethod.setString(context,"farm_irrigation_type",irrigationtype);
                                        SharedPreferencesMethod.setString(context,"farm_addl1",addl1);
                                        SharedPreferencesMethod.setString(context,"farm_addl2",addl2);
                                        SharedPreferencesMethod.setString(context,"farm_addl3",addl3);
                                        SharedPreferencesMethod.setString(context,"farm_city",farm_city);
                                        SharedPreferencesMethod.setString(context,"farm_state",farm_state);
                                        SharedPreferencesMethod.setString(context,"farm_country",farm_country);
                                        SharedPreferencesMethod.setString(context,"farm_spec_comment",speccomment);
                                        SharedPreferencesMethod.setString(context,"farm_growing_region",growing_region);
                                        SharedPreferencesMethod.setString(context,"farm_growing_season",growing_season);
                                        SharedPreferencesMethod.setString(context,"farm_sowing_date",sowing_date);
                                        SharedPreferencesMethod.setString(context,"farm_harvest_date",harvest_date);
                                        SharedPreferencesMethod.setString(context,"farm_crop_name",crop_name);

                                    } catch (JSONException e) {

                                        progressDialog.dismiss();
                                        e.printStackTrace();
                                        Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show();

                                    }

                                    //  Toast.makeText(LoginVerificationActivity.this, soiltype, Toast.LENGTH_SHORT).show();
                                }


                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Log.e("TAGerror :",error.toString());
                                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        if(passonrec!=null){
                            params.put(KEY_PASSWORD,passonrec);
                        }
                        if(emailonrec!=null){
                            params.put(KEY_EMAIL,emailonrec);
                        }

                        if(mobilonrec!=null){
                            params.put(KEY_MOBILE,mobilonrec);
                        }

                        if(usernumonrec!=null){
                            params.put(KEY_USER_NUM,usernumonrec);
                        }

                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);
            }catch (Exception e){}
            return responsee;
        }


        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(String s) {

            //Toast.makeText(MainActivity.this, s+"Not Getting Response", Toast.LENGTH_SHORT).show();

            /*if(s.equals("\"0\""))

            {
                progressDialog.dismiss();

            }*/
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

    }


/*
    public void GetText(final String usernumfinal){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL_DATA_PROFILE,



                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String person_num=null;


                        try {
                            JSONObject jobject = new JSONObject(response);
                            person_num=jobject.getString("person_num");
                            SharedPreferencesMethod.setString(context,"person_num",person_num);
                            Log.e("PERSON_num:",person_num);
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                if(usernumfinal!=null){
                    params.put(KEY_USER_NUM,usernumfinal);
                }
                if(ct1!=null){
                    params.put(KEY_TOKEN,ct1);
                }

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
*/


}
