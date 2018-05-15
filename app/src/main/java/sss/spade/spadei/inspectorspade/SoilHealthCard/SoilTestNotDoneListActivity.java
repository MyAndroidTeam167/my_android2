package sss.spade.spadei.inspectorspade.SoilHealthCard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import sss.spade.spadei.inspectorspade.DataHandler.DataHandler;
import sss.spade.spadei.inspectorspade.LandingActivity.Adapter.LandingAdapter;
import sss.spade.spadei.inspectorspade.LandingActivity.Adapter.RecyclerTouchListener;
import sss.spade.spadei.inspectorspade.LandingActivity.LandingActivity;
import sss.spade.spadei.inspectorspade.LandingActivity.ShowFarmData.ShowFarmDataActivity;
import sss.spade.spadei.inspectorspade.LandingActivity.models.Farmers;
import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.SharedPref.SharedPreferencesMethod;
import sss.spade.spadei.inspectorspade.SoilHealthCard.GetterSetterForSoil.GetterSetterForSoil;
import sss.spade.spadei.inspectorspade.SoilHealthCard.SoilListAdapter.SoilListAdapter;

public class SoilTestNotDoneListActivity extends AppCompatActivity {

    @BindView(R.id.recy_soil_farm_list)
    RecyclerView recyclerViewSoilTest;
   /* @BindView(R.id.no_ntwrk_text)
    TextView no_ntwrk_tv;*/
    @BindView(R.id.no_farm_avaialable_for_soil_health_card)
    TextView no_farm_data;
    private LinearLayoutManager mLayoutManager;
    private List<GetterSetterForSoil> SoilTestList;
    private static final String REGISTER_URL = "http://spade.farm/app/index.php/inspectorApp/fetch_farm_for_soil_card_addition";
   public static final String KEY_INSPECTOR_NUM = "inspector_num";
    String inspectornum="152";
    ProgressDialog progressDialog;
    ConnectivityManager connectivityManager;
    boolean connected = false;
    Boolean is_binded=false;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SoilListAdapter soilListAdapter;
    Context context;
    Toolbar mActionBarToolbar;
    String farm_num="";
    String ct1="";
    Boolean is_offline_enables=false;
    final String KEY_TOKEN="token3";


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(context,LandingActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent=new Intent(context,LandingActivity.class);
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
        context=this;
       /* no_ntwrk_tv.setVisibility(View.GONE);
        no_farm_data.setVisibility(View.GONE);*/


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
                    setContentView(R.layout.activity_soil_test_not_done_list);
                    ButterKnife.bind(this);
                    mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefreshlanding);
                    mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

                    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            Intent intent = new Intent(context, SoilTestNotDoneListActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                    basic_title();
                    ct1 = SharedPreferencesMethod.getString(context, "cctt");
                    inspectornum = SharedPreferencesMethod.getString(context, "UserNum");
                    init();
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

    private void init() {


            //no_ntwrk_tv.setVisibility(View.GONE);
            progressDialog = ProgressDialog.show(SoilTestNotDoneListActivity.this,
                    getString(R.string.dialog_please_wait), "");
            AsyncTaskRunner runnermainact = new AsyncTaskRunner();
            runnermainact.execute();
        /*}else {
            no_ntwrk_tv.setVisibility(View.VISIBLE);
            Toast.makeText(context, R.string.internet_not_connected, Toast.LENGTH_SHORT).show();

        }*/
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
                                SoilTestList=new ArrayList<>();

                                try {
                                    jsonarray = new JSONArray(response);
                                    GetterSetterForSoil soillists ;

                                    String farmpetname=null;
                                    String soiltype=null,irrigationtype=null;
                                    String crop_assigned_status = null;
                                    String addL1 = null;
                                    String addL2 = null;
                                    String addL3 = null;
                                    String city = null;
                                    String state = null;
                                    String country = null;
                                    String Address=null;
                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                                        farmpetname=jsonobject.getString("farm_pet_name");
                                        soiltype=jsonobject.getString("soilType");
                                        irrigationtype=jsonobject.getString("irrigationType");
                                        farm_num=jsonobject.getString("farm_num");
                                        crop_assigned_status=jsonobject.getString("is_crop_assigned");
                                        addL1 = jsonobject.getString("addL1");
                                        addL2 = jsonobject.getString("addL2");
                                        addL3 = jsonobject.getString("addL3");
                                        city = jsonobject.getString("city");
                                        state = jsonobject.getString("state");
                                        country = jsonobject.getString("country");

                                        if(addL2.matches("")){
                                            addL2=addL2;
                                        }
                                        else{
                                            addL2=", "+addL2;
                                        }
                                        if(addL3.matches("")){
                                            addL3=addL3;
                                        }
                                        else{
                                            addL3=", "+addL3;
                                        }
                                        Address=addL1 + addL2 + addL3+", "+city+", "+state+", "+country;

                                        //Log.e("Tag",Name);
                                        soillists=new GetterSetterForSoil();
                                        soillists.setSoil_test_farm_pet_name(farmpetname);
                                        soillists.setSoil_test_count(i+1);
                                        soillists.setSoil_test_soil_type(soiltype);
                                        //soillists.setSoil_test_crop_assigned_status(crop_assigned_status);
                                        //soillists.setSoil_test_irri_type(irrigationtype);
                                        soillists.setSoil_test_address(Address);
                                        soillists.setSoil_test_farm_num(farm_num);
                                        SoilTestList.add(soillists);


                                    }

                                    progressDialog.dismiss();
                                    soilListAdapter=new SoilListAdapter(SoilTestList,context,"from_landing");
                                    recyclerViewSoilTest.setHasFixedSize(true);

                                    recyclerViewSoilTest.setAdapter(soilListAdapter);
                                    soilListAdapter.notifyDataSetChanged();

                                    mLayoutManager = new LinearLayoutManager(context);
                                    mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                    recyclerViewSoilTest.setLayoutManager(mLayoutManager);

                                    /*recyclerViewSoilTest.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerViewSoilTest, new RecyclerTouchListener.ClickListener() {
                                        @Override
                                        public void onClick(View view, int position) {
                                            final GetterSetterForSoil soillists = SoilTestList.get(position);
                                            Intent intent =new Intent(context,InspectorSoilCardInputActivity.class);
                                            intent.putExtra("from","soilnotdone");
                                            DataHandler.newInstance().setFarmnum(soillists.getSoil_test_farm_num());
                                            //intent.putExtra("soil_farm_num",soillists.getSoil_test_farm_num());
                                            startActivity(intent);
                                            finish();
                                            }
                                        @Override
                                        public void onLongClick(View view, int position) {
                                        }
                                    }));*/
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                    soilListAdapter=new SoilListAdapter(SoilTestList,context,"from_landing");
                                    recyclerViewSoilTest.setAdapter(soilListAdapter);
                                    if(soilListAdapter.getItemCount()==0){
                                        no_farm_data.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(SoilTestNotDoneListActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(KEY_INSPECTOR_NUM,inspectornum);
                        params.put(KEY_TOKEN,ct1);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);
            }catch (Exception e){
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

    void basic_title(){
        TextView title=(TextView)findViewById(R.id.tittle);
        title.setText("Soil Health Card List");
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

}
