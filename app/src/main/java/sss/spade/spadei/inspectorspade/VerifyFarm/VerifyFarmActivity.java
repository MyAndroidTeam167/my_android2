package sss.spade.spadei.inspectorspade.VerifyFarm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sss.spade.spadei.inspectorspade.DataHandler.DataHandler;
import sss.spade.spadei.inspectorspade.FarmLocation.GetFarmLocationActivity;
import sss.spade.spadei.inspectorspade.LandingActivity.Adapter.LandingAdapter;
import sss.spade.spadei.inspectorspade.LandingActivity.Adapter.RecyclerTouchListener;
import sss.spade.spadei.inspectorspade.LandingActivity.LandingActivity;
import sss.spade.spadei.inspectorspade.LandingActivity.models.Farmers;
import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.SharedPref.SharedPreferencesMethod;
import sss.spade.spadei.inspectorspade.VerifyFarm.VerifyaFarm.VerifySingleFarmActivity;

public class VerifyFarmActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private Toolbar toolbar;
    private List<Farmers> farmersList;
    LandingAdapter landingAdapter;
    private static final String REGISTER_URL = "http://spade.farm/app/index.php/inspectorApp/unverified_farms_list";
    private static final String REGISTER_URL_CHECK_GPS = "http://spade.farm/app/index.php/inspectorApp/is_location_tracked";
    String inspectornum = "152";
    public static final String KEY_INSPECTOR_NUM = "inspector_num";
    public static final String KEY_FARM_NUM = "farm_num";
    ProgressDialog progressDialog;
    public String[] parameternamearray, parametervaluearray;
    Boolean status = false;
    Toolbar mActionBarToolbar;
    final String KEY_TOKEN = "token2";
    String ct1 = "";
    Boolean is_binded = false;
    ConnectivityManager connectivityManager;
    boolean connected = false;
    RelativeLayout no_farm_rel;
    Boolean is_offline_enables=false;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context, LandingActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(context, LandingActivity.class);
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
                    setContentView(R.layout.activity_verify_farm);
                    TextView title = (TextView) findViewById(R.id.tittle);
                    title.setText("Verify Farm");
                    mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
                    setSupportActionBar(mActionBarToolbar);
                    no_farm_rel = (RelativeLayout) findViewById(R.id.no_farm_rel);

                    ct1 = SharedPreferencesMethod.getString(context, "cctt");
                    //getSupportActionBar().setTitle("My Title");

                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                        getSupportActionBar().setDisplayShowTitleEnabled(false);
                    }
                    //context = this;
                    inspectornum = SharedPreferencesMethod.getString(context, "UserNum");
                    progressDialog = ProgressDialog.show(VerifyFarmActivity.this,
                            getString(R.string.dialog_please_wait), "");
                    AsyncTaskRunner runnermainact = new AsyncTaskRunner();
                    runnermainact.execute();

                } else {
                    setContentView(R.layout.not_binded_layout);
                    TextView title = (TextView) findViewById(R.id.tittle);
                    title.setText("Verify Farm");
                    mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
                    setSupportActionBar(mActionBarToolbar);

                    ct1 = SharedPreferencesMethod.getString(context, "cctt");
                    //getSupportActionBar().setTitle("My Title");

                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                        getSupportActionBar().setDisplayShowTitleEnabled(false);
                    }
                }
            } else {
                setContentView(R.layout.internet_not_connencted);
                TextView title = (TextView) findViewById(R.id.tittle);
                title.setText("Verify Farm");
                mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
                setSupportActionBar(mActionBarToolbar);

                ct1 = SharedPreferencesMethod.getString(context, "cctt");
                //getSupportActionBar().setTitle("My Title");

                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                }

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
                StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,

                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONArray jsonarray = null;
                                farmersList = new ArrayList<>();
                                recyclerView = (RecyclerView) findViewById(R.id.rectoverify);

                                try {
                                    jsonarray = new JSONArray(response);
                                    Farmers farmers;
                                    String firstname = null;
                                    String lastname = null;
                                    String middlename = null;
                                    String farmpetname = null;
                                    String Name = null;
                                    String soiltype = null, irrigationtype = null, mobno = null, date_of_sowing = null, date_of_harvesting = null;
                                    String cropName = null;
                                    String farm_num = null;
                                    String addL1 = null;
                                    String addL2 = null;
                                    String addL3 = null;
                                    String city = null;
                                    String state = null;
                                    String country = null;
                                    String profile_img = null;


                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                                        firstname = jsonobject.getString("firstName");
                                        lastname = jsonobject.getString("lastName");
                                        middlename = jsonobject.getString("middleName");
                                        farmpetname = jsonobject.getString("farm_pet_name");
                                        soiltype = jsonobject.getString("soilType");
                                        irrigationtype = jsonobject.getString("irrigationType");
                                        mobno = jsonobject.getString("mobNo1");
                                        //     date_of_sowing=jsonobject.getString("date_of_sowing");
                                        //     date_of_harvesting=jsonobject.getString("expct_dateof_harvest");
                                        //     cropName=jsonobject.getString("crop_name");
                                        farm_num = jsonobject.getString("farm_num");
                                        profile_img = jsonobject.getString("profile_img");

                                        addL1 = jsonobject.getString("addL1");
                                        addL2 = jsonobject.getString("addL2");
                                        addL3 = jsonobject.getString("addL3");
                                        city = jsonobject.getString("city");
                                        state = jsonobject.getString("state");
                                        country = jsonobject.getString("country");

                                        if (addL2.matches("")) {
                                            addL2 = addL2;
                                        } else {
                                            addL2 = ", " + addL2;
                                        }
                                        if (addL3.matches("")) {
                                            addL3 = addL3;
                                        } else {
                                            addL3 = ", " + addL3;
                                        }

                                        String Address;
                                        Address = addL1 + addL2 + addL3 + ", " + city + ", " + state + ", " + country;
                                        //String imgLink=jsonobject.getString("img_link");
                                        Name = firstname + " " + "" + middlename + " " + lastname;

                                        //        Log.e("Tag",Name);
                                        farmers = new Farmers();
                                        farmers.setFarmname(farmpetname);
                                        farmers.setFarmername(Name);
                                        farmers.setFarmnum(farm_num);
                                        farmers.setAddress_farm(Address);
                                        farmers.setMobno(mobno);
                                        farmers.setProfile_img(profile_img);
                                        //     farmers.setMobno(mobno);
                                        //      farmers.setCropName(cropName);
                                        farmersList.add(farmers);
                                    }

                                    progressDialog.dismiss();
                                    landingAdapter = new LandingAdapter(farmersList, context, "from_verify_activity");
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setAdapter(landingAdapter);
                                    landingAdapter.notifyDataSetChanged();

                                    mLayoutManager = new LinearLayoutManager(context);
                                    mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                    recyclerView.setLayoutManager(mLayoutManager);

                                    if (landingAdapter.getItemCount() == 0) {
                                        no_farm_rel.setVisibility(View.VISIBLE);
                                    } else {
                                        no_farm_rel.setVisibility(View.GONE);

                                    }


                                  /*  recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                                        @Override
                                        public void onClick(View view, int position) {

                                            final Farmers farmers = farmersList.get(position);
                                            //TextView mobNo = (TextView) view.findViewById(R.id.farmer_mob_no);
                                           *//* RelativeLayout relativeLayout=(RelativeLayout)view.findViewById(R.id.rel_lay_recy);
                                            ImageButton imageButton=(ImageButton)view.findViewById(R.id.user_profile_photo);
*//*
*//*
                                            mobNo.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    //progressDialog.dismiss();
                                                    String strnumber=farmers.getMobno();
                                                    Uri number = Uri.parse("tel:" + strnumber);
                                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                                    intent.setData(number);
                                                    startActivity(intent);
                                                }
                                            });
*//*
                                            try {
                                                progressDialog = ProgressDialog.show(VerifyFarmActivity.this,
                                                        getString(R.string.dialog_please_wait), "");
                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL_CHECK_GPS,

                                                        new com.android.volley.Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                if(response.equals("true")){
                                                                    Intent intent = new Intent(context, VerifySingleFarmActivity.class);
                                                                    DataHandler.newInstance().setFarmnum(farmers.getFarmnum());
                                                                    intent.putExtra("from","verify");
                                                                    //intent.putExtra("farm_num",farmers.getFarmnum());
                                                                    progressDialog.dismiss();
                                                                    startActivity(intent);
                                                                    finish();
                                                                }else{
                                                                    Intent intent = new Intent(context, GetFarmLocationActivity.class);
                                                                    DataHandler.newInstance().setFarmnum(farmers.getFarmnum());
                                                                    //intent.putExtra("farm_num",farmers.getFarmnum());
                                                                    progressDialog.dismiss();
                                                                    startActivity(intent);
                                                                    finish();// Toast.makeText(context, "Coordinat Not Submitted"+response, Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        },
                                                        new com.android.volley.Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(VerifyFarmActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }) {
                                                    @Override
                                                    protected Map<String, String> getParams() {
                                                        Map<String, String> params = new HashMap<String, String>();
                                                        params.put(KEY_FARM_NUM,farmers.getFarmnum());
                                                        return params;
                                                    }
                                                };
                                                RequestQueue requestQueue = Volley.newRequestQueue(context);
                                                requestQueue.add(stringRequest);
                                            }catch (Exception e){
                                                progressDialog.dismiss();
                                            }

                                        *//*
                                            imageButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    try {
                                                        progressDialog = ProgressDialog.show(VerifyFarmActivity.this,
                                                                getString(R.string.dialog_please_wait), "");
                                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL_CHECK_GPS,

                                                                new com.android.volley.Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        if(response.equals("true")){
                                                                            progressDialog.dismiss();
                                                                            Intent intent = new Intent(context, VerifySingleFarmActivity.class);
                                                                            intent.putExtra("farm_num",farmers.getFarmnum());
                                                                            startActivity(intent);
                                                                        }else{
                                                                            progressDialog.dismiss();
                                                                            Intent intent = new Intent(context, GetFarmLocationActivity.class);
                                                                            intent.putExtra("farm_num",farmers.getFarmnum());
                                                                            startActivity(intent);                                                                            // Toast.makeText(context, "Coordinat Not Submitted"+response, Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                },
                                                                new com.android.volley.Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                                        progressDialog.dismiss();
                                                                        Toast.makeText(VerifyFarmActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }) {
                                                            @Override
                                                            protected Map<String, String> getParams() {
                                                                Map<String, String> params = new HashMap<String, String>();
                                                                params.put(KEY_FARM_NUM,farmers.getFarmnum());
                                                                return params;
                                                            }
                                                        };
                                                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                                                        requestQueue.add(stringRequest);
                                                    }catch (Exception e){
                                                        progressDialog.dismiss();

                                                    }
                                                }
                                            });*//*
                                        }
                                        @Override
                                        public void onLongClick(View view, int position) {
                                        }
                                    }));*/
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    landingAdapter = new LandingAdapter(farmersList, context, "from_verify_activity");
                                    recyclerView.setAdapter(landingAdapter);
                                    landingAdapter.notifyDataSetChanged();
                                    if (landingAdapter.getItemCount() == 0) {
                                        no_farm_rel.setVisibility(View.VISIBLE);
                                    } else {
                                        no_farm_rel.setVisibility(View.GONE);

                                    }

                                    progressDialog.dismiss();
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(VerifyFarmActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(KEY_INSPECTOR_NUM, inspectornum);
                        params.put(KEY_TOKEN, ct1);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);
            } catch (Exception e) {
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

}
