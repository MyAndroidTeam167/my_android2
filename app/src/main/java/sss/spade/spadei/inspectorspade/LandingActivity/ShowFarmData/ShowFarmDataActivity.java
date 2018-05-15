package sss.spade.spadei.inspectorspade.LandingActivity.ShowFarmData;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sss.spade.spadei.inspectorspade.DataHandler.DataHandler;
import sss.spade.spadei.inspectorspade.FarmActivities.ShowTaskViewPagerActivity;
import sss.spade.spadei.inspectorspade.FarmActivities.VerFarmer.TaskListActivity;
import sss.spade.spadei.inspectorspade.LandingActivity.LandingActivity;
import sss.spade.spadei.inspectorspade.LandingActivity.models.Farmers;
import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.SharedPref.SharedPreferencesMethod;
import sss.spade.spadei.inspectorspade.SoilHealthCard.GetterSetterForSoil.GetterSetterForSoil;
import sss.spade.spadei.inspectorspade.SoilHealthCard.InspectorSoilCardInputActivity;
import sss.spade.spadei.inspectorspade.SoilHealthCard.ShowSoilHealthCardActivity;
import sss.spade.spadei.inspectorspade.SoilHealthCard.SoilListAdapter.SoilListAdapter;
import sss.spade.spadei.inspectorspade.SoilHealthCard.SoilTestNotDoneListActivity;
import sss.spade.spadei.inspectorspade.TypeFacedTextView.TypefacedTextView;
import sss.spade.spadei.inspectorspade.VerifyFarm.VerifyFarmActivity;

public class ShowFarmDataActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar mActionBarToolbar;
    Context context;
    private static final String REGISTER_URL = "http://spade.farm/app/index.php/inspectorApp/get_farms_under_inspector";
    private static final String REGISTER_URL_FETCH_DATA = "http://spade.farm/app/index.php/farmCalendar/send_farm_calendar_column_data_to_app";
    private static final String FETCH_CROP_DETAILS = "http://spade.farm/app/index.php/farmCalendar/send_farm_crop_details";
    private static final String REGISTER_URL_SOIL_CHECK = "http://spade.farm/app/index.php/inspectorApp/fetch_farm_for_soil_card_addition";
    public static final String KEY_INSPECTOR_NUM = "inspector_num";
    public static final String KEY_FARM_NUM = "farm_num";
    final String KEY_TOKEN = "token3";
    String ct1;
    String url;
    CardView cv_my_activity, cv_verify_activity;

    String inspectornum = "";
    ProgressDialog progressDialog;
    Button activities, verfarmer;
    String farm_num = "", farm_name = "";
    String farm_num_soil_check = "";
    TextView Rating_farmer;

    int count = 0;
    ConnectivityManager connectivityManager;
    boolean connected = false;
    String fetch_crop, fetch_rating;

    @BindView(R.id.tv_sowing_date)
    TypefacedTextView tv_sowing_date;
    @BindView(R.id.tv_crop)
    TypefacedTextView tv_crop_name;
    @BindView(R.id.tv_harvesting_date)
    TypefacedTextView tv_harvest_date;
    @BindView(R.id.tv_completed)
    TypefacedTextView tv_completed_task;
    @BindView(R.id.tv_pending)
    TypefacedTextView tv_pending_task;
    @BindView(R.id.tv_total_count)
    TypefacedTextView tv_total_count;
    /* @BindView(R.id.tv_verify_count)
     TypefacedTextView tv_verify_count;*/
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.show_data_farm_pet_name)
    TypefacedTextView tv_farm_pet_name;
    @BindView(R.id.cv_view_soil_health_card)
    CardView cv_view_soil_health_card;


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
        setContentView(R.layout.activity_show_farm_data);
        context = this;
        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText("");
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        ct1 = SharedPreferencesMethod.getString(context, "cctt");

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefreshshowfarmdata);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        init();

    }

    private void init() {
        /*activities=(Button)findViewById(R.id.showfarmactivities);
        verfarmer=(Button)findViewById(R.id.ver_farmer_reply);*/
        Rating_farmer = (TextView) findViewById(R.id.rating_farmer);
        cv_my_activity = (CardView) findViewById(R.id.cv_my_activities);
        // cv_verify_activity = (CardView) findViewById(R.id.cv_verify_activities);
        ButterKnife.bind(this);

      /*  activities.setOnClickListener(this);
        verfarmer.setOnClickListener(this);*/
        cv_my_activity.setOnClickListener(this);
        // cv_verify_activity.setOnClickListener(this);
       /* Bundle extras = getIntent().getExtras();
        if (extras != null) {
           // farm_num = extras.getString("farm_num");

        }*/
        farm_num = DataHandler.newInstance().getFarmnum();
        farm_name = DataHandler.newInstance().getLanding_farm_pet_name();
        // Toast.makeText(context, farm_num, Toast.LENGTH_SHORT).show();
        tv_farm_pet_name.setText(farm_name);
        inspectornum = SharedPreferencesMethod.getString(context, "UserNum");

       /* progressDialog = ProgressDialog.show(ShowFarmDataActivity.this,
                getString(R.string.dialog_please_wait), "");*/
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }
        if (connected) {
            progressDialog = ProgressDialog.show(ShowFarmDataActivity.this,
                    getString(R.string.dialog_please_wait), "");

            url = REGISTER_URL_FETCH_DATA;
            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.execute(url, "rating");
            url = FETCH_CROP_DETAILS;
            AsyncTaskRunner runnercrop = new AsyncTaskRunner();
            runnercrop.execute(url, "crop");
            //  progressDialog.dismiss();

        } else {
            Toast.makeText(context, R.string.internet_not_connected, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cv_my_activities: {
                Intent intent = new Intent(context, ShowTaskViewPagerActivity.class);
                intent.putExtra("Type", "all_activities");
                intent.putExtra("farm_num", farm_num);
                startActivity(intent);
                finish();
                break;
            }
           /* case R.id.cv_verify_activities:
            {
                Intent intent= new Intent(context, TaskListActivity.class);
                intent.putExtra("farm_num",farm_num);
                startActivity(intent);
                finish();
                break;
            }*/

        }
    }


    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {
        public AsyncTaskRunner() {
            super();
        }


        @Override
        protected String doInBackground(final String... params) {
           /* final String emailonrec,mobilonrec,passonrec,flagonrec,usernumonrec,urlonrec;
            emailonrec = params[0];
            mobilonrec   = params[1];
            passonrec=params[2];
            urlonrec=params[3];*/
            final String type, urlonrec;
            urlonrec = params[0];
            type = params[1];

            try {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlonrec,

                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (type.equals("rating")) {
                                    JSONArray jsonarray = null;
                                    JSONObject jsonObject = null;

                                    try {
                                        jsonObject = new JSONObject(response);
                                        Log.e("Response full", jsonObject.toString());
                                        String status = jsonObject.getString("status");
                                        String status_msg = jsonObject.getString("status_msg");
                                        Log.e("Response status", status.toString());

                                        if (status.equals("1")) {

                                            String result_set = jsonObject.getString("result");

                                            jsonarray = new JSONArray(result_set);


                                        Farmers farmers;
                                        String firstname = null;
                                        String lastname = null;
                                        String middlename = null;
                                        String farmpetname = null;
                                        String Name = null;
                                        String soiltype = null, irrigationtype = null, mobno = null, date_of_sowing = null, date_of_harvesting = null;
                                        String cropName = null;
                                        String farm_num = null;
                                        String delayed_days = null;
                                        String priority = null;
                                        String is_compulsary = null;
                                        int priorityint = 0;
                                        int delayed_dayint = 0;
                                        int sum = 0;
                                        int priority_sum = 0;
                                        int totalcount = 0;
                                        String is_done = null;
                                        int completedcount = 0, pendingcount = 0,upcomingcount=0;
                                        String rating_factor = "0";

                                        for (int i = 0; i < jsonarray.length(); i++) {
                                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                                       /* firstname = jsonobject.getString("firstName");
                                        lastname = jsonobject.getString("lastName");
                                        middlename = jsonobject.getString("middleName");
                                        farmpetname = jsonobject.getString("farm_pet_name");
                                        soiltype = jsonobject.getString("soilType");
                                        irrigationtype = jsonobject.getString("irrigationType");
                                        mobno = jsonobject.getString("mobNo1");
                                        date_of_sowing = jsonobject.getString("date_of_sowing");
                                        date_of_harvesting = jsonobject.getString("expct_dateof_harvest");
                                        cropName = jsonobject.getString("crop_name");
                                        farm_num = jsonobject.getString("farm_num");
                                        //String imgLink=jsonobject.getString("img_link");
                                        Name = firstname + " " + "" + middlename + " " + lastname;*/
                                            delayed_days = jsonobject.getString("delayed_days");
                                            priority = jsonobject.getString("priority_of_activity");
                                            is_compulsary = jsonobject.getString("is_compulsary");
/*
                                        priorityint = Integer.parseInt(priority);
                                        delayed_dayint = Integer.parseInt(delayed_days);
*/
                                            String date = jsonobject.getString("date");
                                            String date_today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                                            is_done = jsonobject.getString("is_done");
                                            rating_factor = jsonobject.getString("rating");



                                       /* if (is_compulsary.equals("Y")) {
                                            sum = priorityint * (30 - delayed_dayint) + sum;
                                            priority_sum = priorityint * 30 + priority_sum;
                                        }*/

                                            if (is_done.equals("Y")) {
                                                completedcount++;
                                            } else {
                                                if (date != null) {
                                                    if (date.compareTo(date_today) < 0) {
                                                        pendingcount++;
                                                    }else{
                                                        upcomingcount++;
                                                    }
                                                }
                                            }
                                            //Log.e("Tag", delayed_days + " " + priority + " " + is_compulsary);
                                        }

                                        totalcount = pendingcount + completedcount+upcomingcount;

                                    /*float rating = (float) sum / (float) priority_sum * 10;
                                    Log.e("Rating", String.valueOf(rating));
                                    DecimalFormat df = new DecimalFormat("#");
                                    df.setRoundingMode(RoundingMode.CEILING)
                                    String.valueOf(Math.round(rating)*/
                                        ;
                                        Rating_farmer.setText(rating_factor+"/10");
                                        tv_pending_task.setText(String.valueOf(pendingcount));
                                        tv_completed_task.setText(String.valueOf(completedcount));
                                        tv_total_count.setText(String.valueOf(totalcount));
                                        fetch_rating = "rating";

                                    }else{}
                                    } catch (JSONException e) {
                                        progressDialog.dismiss();
                                        e.printStackTrace();
                                    }

                                } else {

                                    JSONObject jsonObjectnew;
                                    String crop_name = null;
                                    String date_of_sowing = null;
                                    String Expected_date_of_harvest = null;
                                    try {
                                        Log.e("TAG", response);
                                        jsonObjectnew = new JSONObject(response);
                                        String result_set = jsonObjectnew.getString("result");
                                        String status = jsonObjectnew.getString("status");

                                        if (status.equals("1")) {
                                            JSONObject newjsonObject = new JSONObject(result_set);
                                            crop_name = newjsonObject.getString("crop_name");
                                            date_of_sowing = newjsonObject.getString("date_of_sowing");
                                            Expected_date_of_harvest = newjsonObject.getString("expct_dateof_harvest");

                                            tv_crop_name.setText(crop_name);
                                            tv_harvest_date.setText(Expected_date_of_harvest);
                                            tv_harvest_date.setVisibility(View.GONE);
                                            tv_sowing_date.setText(date_of_sowing);
                                            progressDialog.dismiss();

                                        } else {
                                            tv_crop_name.setText("Not Assigned");
                                            tv_harvest_date.setText("0000-00-00");
                                            tv_harvest_date.setVisibility(View.GONE);
                                            tv_sowing_date.setText("0000-00-00");
                                            progressDialog.dismiss();
                                        }

                                    } catch (JSONException e) {
                                        progressDialog.dismiss();
                                        e.printStackTrace();
                                    }
                                }
                               /* if(fetch_crop.equals("crop") && fetch_rating.equals("rating")){
                                    progressDialog.dismiss();
                                }*/
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //progressDialog.dismiss();
                                progressDialog.dismiss();
                                Log.e("TAG", error.toString());
                                //Toast.makeText(ShowFarmDataActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(KEY_FARM_NUM, farm_num);
                        params.put(KEY_TOKEN, ct1);
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


    @OnClick(R.id.cv_view_soil_health_card)
    public void onButtonClick(View view) {

        try {
            progressDialog = ProgressDialog.show(ShowFarmDataActivity.this,
                    getString(R.string.dialog_please_wait), "");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL_SOIL_CHECK,

                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONArray jsonarray = null;
                            try {
                                Log.e("RESPONSE", response.toString());
                                jsonarray = new JSONArray(response);
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    farm_num_soil_check = jsonobject.getString("farm_num");
                                    if (farm_num.equals(farm_num_soil_check)) {
                                        count++;
                                    }
                                }

                                if (count > 0) {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(context, InspectorSoilCardInputActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(context, ShowSoilHealthCardActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Intent intent = new Intent(context, ShowSoilHealthCardActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(ShowFarmDataActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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


    }

}
