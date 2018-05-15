package sss.spade.spadei.inspectorspade.BindFarmer;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import sss.spade.spadei.inspectorspade.Login.MainActivity;
import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.SharedPref.SharedPreferencesMethod;
import sss.spade.spadei.inspectorspade.Validations.Validations;

public class BindingActivity extends AppCompatActivity {

    private static final String SEARCH_MOB_NO = "http://spade.farm/app/index.php/inspectorApp/search_by_mobNo";
    private static final String BIND_USER_TO_COMP = "http://spade.farm/app/index.php/inspectorApp/bind_farmer";
    Context context;
    ProgressDialog progressDialog;
    @BindView(R.id.mobile_typed)
    EditText mobile_typed;
    @BindView(R.id.search_butt_binding)
    ImageView Search_butt_binding;
    @BindView(R.id.bind_user_linear_lay)
    LinearLayout bind_user_linear_lay;
    @BindView(R.id.farmer_mobile_number)
    TextView farmer_mob_no;
    @BindView(R.id.Bind_farmer_butt)
    Button bind_farmer_butt;
    @BindView(R.id.contact_select_imge)
    ImageView select_contact;
    String user_num = "";
    String comp_num = "";
    private final int REQUEST_CODE = 99;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    Boolean read_contacts = false;
    ConnectivityManager connectivityManager;
    boolean connected = false;
    Boolean is_binded = false;
    Toolbar mActionBarToolbar;
    Boolean is_offline_enables=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }

        is_offline_enables=SharedPreferencesMethod.getBoolean(context,SharedPreferencesMethod.IS_OFFLINE_ENABLED);

        if(is_offline_enables) {
            if (connected) {
                is_binded = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.BINDED);
                if (is_binded) {
                    setContentView(R.layout.activity_binding);
                    basic_title();
                    ButterKnife.bind(this);
                    comp_num = SharedPreferencesMethod.getString(context, "cctt");
                    if (read_contacts == checkContactPermission()) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_CONTACTS}, 99);
                    }
                    Search_butt_binding.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(View view) {
                            String mob_no;
                            mob_no = mobile_typed.getText().toString().trim();

                            if (!Validations.isValidMobile(mob_no)) {
                                mobile_typed.setError("Invalid Mobile Number");
                                //mobile_typed.setHint("Hello");
//                                mobile_typed.setTooltipText("tooltip");
                            } else {
                                progressDialog = ProgressDialog.show(BindingActivity.this, getString(R.string.dialog_please_wait), "");
                                AsyncTaskRunner runner = new AsyncTaskRunner();
                                runner.execute(mob_no);
                            }


                        }
                    });

                    bind_farmer_butt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new AlertDialog.Builder(context)
                                    .setMessage("Are you sure you want to bind this Farmer to your Company?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //Toast.makeText(BindingActivity.this, SharedPreferencesMethod.getString(context,"cctt"), Toast.LENGTH_SHORT).show();
                                            progressDialog = ProgressDialog.show(BindingActivity.this, getString(R.string.dialog_please_wait), "");
                                            BindFarmer();
                                        }
                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                        }
                    });


                    select_contact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                            startActivityForResult(intent, REQUEST_CODE);
                            //Toast.makeText(BindingActivity.this, "hello", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    setContentView(R.layout.not_binded_layout);
                    basic_title();
                }
            } else {
                setContentView(R.layout.internet_not_connencted);
                basic_title();
            }
        }else{

        }
    }

        private class AsyncTaskRunner extends AsyncTask<String, Void, String> {
            public AsyncTaskRunner() {
                super();
            }


            @Override
            protected String doInBackground(final String... params) {
                try {
                    final String mob_typed_asyn = params[0];
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, SEARCH_MOB_NO,
                            new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JSONObject jsonObject = null;
                                    JSONObject resultObject = null;
                                    try {
                                        jsonObject = new JSONObject(response);
                                        String message = jsonObject.getString("msg");
                                        String status = jsonObject.getString("status");

                                        if (status.equals("0")) {
                                            Toast.makeText(context, "User with this Mobile doest not exist", Toast.LENGTH_SHORT).show();
                                            bind_user_linear_lay.setVisibility(View.GONE);
                                        } else {
                                            String result = jsonObject.getString("result");
                                            resultObject = new JSONObject(result);
                                            String mobile_no = resultObject.getString("mobNo");
                                            String email = resultObject.getString("email");
                                            user_num = resultObject.getString("user_num");
                                            Log.e("Mobile", mobile_no);
                                            bind_user_linear_lay.setVisibility(View.VISIBLE);
                                            farmer_mob_no.setText(mobile_no);
                                        }
                                        progressDialog.dismiss();


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        progressDialog.dismiss();

                                    }

                                }


                            },
                            new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();

                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("mob", mob_typed_asyn);


                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest);
                } catch (Exception e) {
                    progressDialog.dismiss();
                }
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


    void BindFarmer() {

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, BIND_USER_TO_COMP,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonObject = null;
                            JSONObject resultObject = null;
                            try {
                                jsonObject = new JSONObject(response);
                                String message = jsonObject.getString("msg");
                                String status = jsonObject.getString("status");

                                if (status.equals("0")) {
                                    Toast.makeText(context, "Unable to bind User at the moment Please try again later", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Farmer succesfully Binded to your Company", Toast.LENGTH_SHORT).show();
                                    bind_user_linear_lay.setVisibility(View.GONE);
                                    bind_user_linear_lay.setVisibility(View.GONE);
                                    mobile_typed.setText("");
                                }
                                progressDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }

                        }


                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_num", user_num);
                    params.put("token1", comp_num);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            progressDialog.dismiss();
        }
    }


    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (REQUEST_CODE):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String num = "";
                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            numbers.moveToNext();
                            num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).trim();
                            if (num.startsWith("+")) {
                                if (num.length() == 13) {
                                    String str_getMOBILE = num.substring(3);
                                    mobile_typed.setText(str_getMOBILE);
                                } else if (num.length() == 14) {
                                    String str_getMOBILE = num.substring(4);
                                    mobile_typed.setText(str_getMOBILE);
                                }


                            } else {
                                if (num.length() == 12) {
                                    String str_getMOBILE = num.substring(2);
                                    mobile_typed.setText(str_getMOBILE);
                                } else if (num.length() == 11) {
                                    String str_getMOBILE = num.substring(1);
                                    mobile_typed.setText(str_getMOBILE);
                                } else if (num.length() == 10) {
                                    mobile_typed.setText(num);
                                } else {
                                    Toast.makeText(context, "No Contact Number for this Contact", Toast.LENGTH_SHORT).show();
                                }
                            }
                            //for multiple contacts use bellow code
                            /*while (numbers.moveToNext()) {
                                num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                Toast.makeText(BindingActivity.this, "Number="+num, Toast.LENGTH_LONG).show();
                            }*/
                        }
                    }
                    break;
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            /*// Android version is lesser than 6.0 or the permission is already granted.
            List<String> contacts = getContactNames();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts);
            lstNames.setAdapter(adapter);*/
        }
    }

    public boolean checkContactPermission() {
        String permission = "android.permission.READ_CONTACTS";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*Intent intent=new Intent(context,MainActivity.class);
        startActivity(intent);
        finish();*/
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    void basic_title(){
        TextView title=(TextView)findViewById(R.id.tittle);
        title.setText("Bind Farmer");
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }
}

