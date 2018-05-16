package sss.spade.spadei.inspectorspade.SignUp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import java.util.HashMap;
import java.util.Map;

import sss.spade.spadei.inspectorspade.DataHandler.DataHandler;
import sss.spade.spadei.inspectorspade.Login.MainActivity;
import sss.spade.spadei.inspectorspade.R;

import static sss.spade.spadei.inspectorspade.Validations.Validations.emailValidator;
import static sss.spade.spadei.inspectorspade.Validations.Validations.isValidMobile;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar mActionBarToolbar;
    Context context;
    private static final String REGISTER_URL = "http://spade.farm/app/index.php/signUp/insert_new_user";
    public static final String KEY_MOBILE = "mobNo";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_INSPECTOR = "is_inspector";
    ProgressDialog progressDialog;
    public static final String KEY_FARMER = "is_farmer";
    public static final String KEY_ADMIN = "is_admin";
    String isinspector = "Y", isadmin = "N", isfarmer = "N";
    EditText emailsignup, mobilesignup;
    ShowHidePasswordEditText passsignup,passsignupconfirm;
    Button registor;
    TextView olduser;
    ConnectivityManager connectivityManager;
    String EmailSign,MobileSign,PassSign,Passsignconfirm;
    Boolean connected=false;




    @Override
    public void onBackPressed() {
        Intent intent=new Intent(context,MainActivity.class);
        startActivity(intent);
        finish();
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
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        context = this;

        TextView title=(TextView)findViewById(R.id.tittle);
        title.setText("Register New Account");
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        init();

    }

    private void init() {
        emailsignup = (EditText) findViewById(R.id.emailsignup);
        mobilesignup = (EditText) findViewById(R.id.mobilesignup);
        passsignup = (ShowHidePasswordEditText) findViewById(R.id.signuppass);
        registor = (Button) findViewById(R.id.registerbutt);
        olduser = (TextView) findViewById(R.id.olduser);
        passsignupconfirm = (ShowHidePasswordEditText) findViewById(R.id.signuppassconfirm);

        registor.setOnClickListener(this);
        olduser.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId() /*to get clicked view id**/) {
            case R.id.registerbutt:{

                PassSign = passsignup.getText().toString();
                Passsignconfirm = passsignupconfirm.getText().toString();
                int length = passsignup.getText().toString().trim().length();
                int length2 = passsignupconfirm.getText().toString().trim().length();

                if (!emailValidator(emailsignup.getText().toString().trim())) {
                    emailsignup.setError(getString(R.string.invalid_email));
                } else if (!isValidMobile(mobilesignup.getText().toString().trim())) {
                    mobilesignup.setError(getString(R.string.invalid_mobile_no));
                } else if (!(length > 7 && length < 33)) {
                    passsignup.setError(getString(R.string.password_er));
                    passsignup.setText("");
                    //        Toast.makeText(MainActivity.this, length, Toast.LENGTH_SHORT).show();
                } else if (!(length2 > 7 && length2 < 33)) {
                    passsignupconfirm.setError(getString(R.string.password_er));
                    passsignupconfirm.setText("");
                    //        Toast.makeText(MainActivity.this, length, Toast.LENGTH_SHORT).show();
                } else if (!PassSign.equals(Passsignconfirm)) {
                    passsignup.setText("");
                    passsignupconfirm.setText("");
                    passsignup.setError(getString(R.string.samepassword));
                    passsignupconfirm.setError(getString(R.string.samepassword));
                } else {
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        connected = true;
                    } else {
                        connected = false;
                    }

                    if(connected){
                    try {
                        progressDialog = ProgressDialog.show(SignUpActivity.this,
                                getString(R.string.dialog_please_wait), "");
                        EmailSign = emailsignup.getText().toString().trim();
                        MobileSign   = mobilesignup.getText().toString().trim();
                        PassSign   = passsignup.getText().toString().trim();

                        AsyncTaskRunner runnermainact = new AsyncTaskRunner();
                        runnermainact.execute(EmailSign, MobileSign, PassSign, REGISTER_URL);
                    } catch (Exception ex) {
                    }
                }else {
                        Toast.makeText(context, R.string.internet_not_connected, Toast.LENGTH_SHORT).show();
                    }
                }
                break;}
            case R.id.olduser:{
                Intent intent=new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
                break;}
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
            urlonrec=params[3];

            try {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlonrec,

                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if(response.equals("\"Password And User added Successfully\""))
                                {
                                    DataHandler.newInstance().setSignUpMobile(mobilonrec);
                                    DataHandler.newInstance().setSignUpmail(emailonrec);
                                    DataHandler.newInstance().setSignUpPassword(passonrec);
                                    progressDialog.dismiss();
                                    Intent intent=new Intent(context,MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(context, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                    finish();

                                }
                                else{

                                    progressDialog.dismiss();
                                    Toast.makeText(SignUpActivity.this,response, Toast.LENGTH_LONG).show();

                                }

                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Log.e("Error",error.toString());
                                Toast.makeText(SignUpActivity.this, R.string.error_text, Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        if(MobileSign!=null)
                        params.put(KEY_MOBILE,MobileSign);
                        if(PassSign!=null)
                        params.put(KEY_PASSWORD,PassSign);
                        if(EmailSign!=null)
                        params.put(KEY_EMAIL, EmailSign);
                        if(isinspector!=null)
                        params.put(KEY_INSPECTOR, isinspector);
                        if(isfarmer!=null)
                        params.put(KEY_FARMER, isfarmer);
                        if(isadmin!=null)
                        params.put(KEY_ADMIN, isadmin);

                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);
            }catch (Exception e){}
            return "";
        }


        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(String s) {

        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

    }



}
