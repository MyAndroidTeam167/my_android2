package sss.spade.spadei.inspectorspade.LandingActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import sss.spade.spadei.inspectorspade.BindFarmer.BindingActivity;
import sss.spade.spadei.inspectorspade.DataHandler.DataHandler;
import sss.spade.spadei.inspectorspade.FarmerInformation.FarmerInfoActivity;
import sss.spade.spadei.inspectorspade.LandingActivity.Adapter.LandingAdapter;
import sss.spade.spadei.inspectorspade.LandingActivity.Adapter.RecyclerTouchListener;
import sss.spade.spadei.inspectorspade.LandingActivity.Adapter.SliderPagerAdapter;
import sss.spade.spadei.inspectorspade.LandingActivity.ShowFarmData.ShowFarmDataActivity;
import sss.spade.spadei.inspectorspade.LandingActivity.models.Farmers;
import sss.spade.spadei.inspectorspade.Login.MainActivity;
import sss.spade.spadei.inspectorspade.Notification.NotificationActivity;
import sss.spade.spadei.inspectorspade.PersonData.FillProfileActivity;
import sss.spade.spadei.inspectorspade.PersonData.ShowProfileActivity;
import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.Settings.SettingActivity;
import sss.spade.spadei.inspectorspade.SharedPref.SharedPreferencesMethod;
import sss.spade.spadei.inspectorspade.SoilHealthCard.InspectorSoilCardInputActivity;
import sss.spade.spadei.inspectorspade.SoilHealthCard.SoilTestNotDoneListActivity;
import sss.spade.spadei.inspectorspade.VerifyFarm.VerifyFarmActivity;
import sss.spade.spadei.inspectorspade.app.Config;

public class LandingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context context;
    RecyclerView recyclerView;
    LandingAdapter landingAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<Farmers> farmersList;
    private static final String REGISTER_URL = "http://spade.farm/app/index.php/inspectorApp/get_farms_under_inspector";
    private static final String REGISTER_URL_DATA_PROFILE = "http://spade.farm/app/index.php/signUp/fetch_profile";
    private static final String URL_CHECK_COMPANY_NUM = "http://spade.farm/app/index.php/farmApp/is_binded";

    public static final String KEY_INSPECTOR_NUM = "inspector_num";
    final String USER_NUM = "user_num";
    TextView mywidget;

    String inspectornum="152";
    ProgressDialog progressDialog;
    NavigationView navigationView;

    String date[];
    Boolean exit = false;
    String gps1,gps2,gps3,gps4,gps5,gps6;
    ConnectivityManager connectivityManager;
    boolean connected = false;
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView nntwrktxt;
    ImageView iv_noti;
    final String KEY_TOKEN="token2";
    final String KEY_TOKEN_FETCH_PROFILE="token1";

    String ct1;
    Boolean login_status=false;
    String user_num;
    Boolean is_binded=false;
    int DELAY_MILLIS=0;

    ImageView farm_latest_image;

    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    SliderPagerAdapter sliderPagerAdapter;
    ArrayList<String> slider_image_list;
    private TextView[] dots;
    int page_position = 0;
    RelativeLayout rel_binding_lay;
    RelativeLayout no_farm_rel;


    private Handler handler;
    private final int delay = 2000;
    private int page = 0;
    Runnable runnable = new Runnable() {
        public void run() {
            if (sliderPagerAdapter.getCount() == page) {
                page = 0;
            } else {
                page++;
            }
            vp_slider.setCurrentItem(page, true);
            handler.postDelayed(this, delay);
        }
    };



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
        setContentView(R.layout.activity_landing);
        SharedPreferencesMethod.setBoolean(context,SharedPreferencesMethod.IS_OFFLINE_ENABLED,true);
        TextView title=(TextView)findViewById(R.id.tittlelanding);
        title.setText("Farms Under Inspector");
        handler = new Handler();
        Toolbar toolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout_landing);
        rel_binding_lay=(RelativeLayout)findViewById(R.id.rel_binding_lay);
        nntwrktxt=(TextView)findViewById(R.id.no_ntwrk_text);
        //toolbar.setTitle("Kumar");
        setSupportActionBar(toolbar);
        iv_noti=(ImageView)findViewById(R.id.notification_icon);
        FirebaseMessaging.getInstance().subscribeToTopic("foo-bar");
        FirebaseMessaging.getInstance().subscribeToTopic("user_140");
        inspectornum=SharedPreferencesMethod.getString(context,"UserNum");
        displayFirebaseRegId();
        mywidget=(TextView)findViewById(R.id.mywidget);
        mywidget.setSelected(true);
        ct1=SharedPreferencesMethod.getString(context,"cctt");
        login_status=SharedPreferencesMethod.getBoolean(context,"Login");

        user_num=SharedPreferencesMethod.getString(context,"UserNum");

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefreshlanding);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        no_farm_rel=(RelativeLayout)findViewById(R.id.no_farm_rel);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent=new Intent(context,LandingActivity.class);
                startActivity(intent);
                finish();
            }
        });

       // ButterKnife.bind(this);
       // farm_latest_image=(ImageView)findViewById(R.id.latest_farm_image);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        /*Picasso.with(context)
                .load("https://www.oswalcorns.com/my_farm/myfarmapp/uploads/user_140/img_53_1_20180321_081043.png")
                .into(farm_latest_image);*/

        if (getSupportActionBar() != null){
            /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);*/
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        iv_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,NotificationActivity.class);
                startActivity(intent);
            }
        });
/*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

         navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();

        MenuItem tools= menu.findItem(R.id.titletools);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s.length(), 0);
        tools.setTitle(s);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        final CircleImageView landing_profile_pic;
        landing_profile_pic=   (CircleImageView)header.findViewById(R.id.profile_landing);

        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }


        if (connected) {


            if(login_status){

            is_binded = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.BINDED);

            if (is_binded) {
                DELAY_MILLIS = 0;
            } else {
                DELAY_MILLIS = 2000;
                rel_binding_lay.setVisibility(View.VISIBLE);
                progressDialog = ProgressDialog.show(LandingActivity.this,
                        getString(R.string.dialog_please_wait), "");
                bindUser();
            }


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {


                    is_binded=SharedPreferencesMethod.getBoolean(context,SharedPreferencesMethod.BINDED);
                    Log.e("BINDING VALUE",is_binded.toString());


                    if (is_binded) {
                        ct1=SharedPreferencesMethod.getString(context,"cctt");
                        try {
                            init();
                            rel_binding_lay.setVisibility(View.GONE);


                            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL_DATA_PROFILE,

                                    new com.android.volley.Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {


                                            String profile_img = null;
                                            String message = null;
                                            String status = null;
                                            String result = null;
                                            String person_num = null;

                                            try {
                                                JSONObject jobject = new JSONObject(response);
                                                message = jobject.getString("msg");
                                                status = jobject.getString("status");
                                                result = jobject.getString("result");

                                                if (!status.equals("0")) {
                                                    JSONObject iprofileObject = new JSONObject(result);

                                                    profile_img = iprofileObject.getString("profile_img");
                                                    person_num = iprofileObject.getString("person_num");
                                                    SharedPreferencesMethod.setString(context, "person_num", person_num);

                                                    if (!person_num.equals("")) {
                                                       // hideItem();
                                                        mywidget.setVisibility(View.GONE);
                                                    } else {
                                                        mywidget.setVisibility(View.VISIBLE);
                                                    }
                                                    if (profile_img.equals("null")) {
                                                        landing_profile_pic.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_person_black_18dp));
                                                    } else {
                                                        Uri uriprofile = Uri.parse(profile_img);
                                                        Picasso.with(landing_profile_pic.getContext()).load(uriprofile).into(landing_profile_pic);
                                                        // myBitmap = ((BitmapDrawable) farmer_profile_image.getDrawable()).getBitmap();
                                                    }

                                                } else {
                                                    mywidget.setVisibility(View.VISIBLE);
                                                    //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                                                }
                                                //profile_img = jobject.getString("profile_img");

                                                //progressDialog.dismiss();

                                                // showImage(uriprofile);
                                                //ImagePopup imagePopup = new ImagePopup(context);

                                            } catch (JSONException e) {

                                                // progressDialog.dismiss();
                                                e.printStackTrace();
                                            }
                                        }


                                    },
                                    new com.android.volley.Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("Error",error.toString());
                                            Toast.makeText(LandingActivity.this, R.string.error_text, Toast.LENGTH_LONG).show();

                                            // progressDialog.dismiss();
                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();

                                    if (inspectornum != null) {
                                        params.put(USER_NUM, inspectornum);
                                    }
                                    if (ct1 != null) {
                                        params.put(KEY_TOKEN_FETCH_PROFILE, ct1);
                                    }

                                    return params;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                            requestQueue.add(stringRequest);

                            landing_profile_pic.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(context, ShowProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } catch (Exception e) {
                        }


                    }

                    else{
                        rel_binding_lay.setVisibility(View.VISIBLE);

                    }


                }
            }, DELAY_MILLIS);


            }else{new AlertDialog.Builder(this)
                    .setMessage("Please Login again.....")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("foo-bar");
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("user_"+user_num);
                            //SharedPreferencesMethod.clear(context);
                            SharedPreferencesMethod.setBoolean(context, "Login", false);
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                            finish();                       }
                    })
                    /*.setNegativeButton("No", null)*/
                    .show();
            }
        }
        else {
            nntwrktxt.setVisibility(View.VISIBLE);
            Toast.makeText(context, R.string.internet_not_connected, Toast.LENGTH_SHORT).show();

        }







    }

    private void init() {
        vp_slider = (ViewPager) findViewById(R.id.vp_slider);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);

        slider_image_list = new ArrayList<>();
        slider_image_list.add("https://fthmb.tqn.com/LdEwLPTAAmyv-Rv3KPCdJ3RFli4=/768x0/filters:no_upscale():max_bytes(150000):strip_icc()/Coding-58b1fe485f9b5860464afed9.jpg");
        slider_image_list.add("https://fthmb.tqn.com/uXo0h1mgo3x6LfsYEOVQ01_zPL0=/768x0/filters:no_upscale():max_bytes(150000):strip_icc()/483034101-56a9f6723df78cf772abc5e1.jpg");
        slider_image_list.add("https://fthmb.tqn.com/JnrcwFwlaXM86muPEBZal0O6Br0=/768x0/filters:no_upscale():max_bytes(150000):strip_icc()/183099261-56a9f6715f9b58b7d00038da.jpg");
        slider_image_list.add("https://fthmb.tqn.com/jML6Cm9IDrPWv00P1LFnIm3OtJU=/768x0/filters:no_upscale():max_bytes(150000):strip_icc()/153883795-copy-56a9f4953df78cf772abbec0.jpg");

        sliderPagerAdapter = new SliderPagerAdapter(LandingActivity.this, slider_image_list);
        vp_slider.setAdapter(sliderPagerAdapter);

        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                page = position;

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //preparefarmerData();

            nntwrktxt.setVisibility(View.GONE);
            progressDialog = ProgressDialog.show(LandingActivity.this,
                    getString(R.string.dialog_please_wait), "");


            LandingActivity.AsyncTaskRunner runnermainact = new LandingActivity.AsyncTaskRunner();
            runnermainact.execute();


    }

    /*private void preparefarmerData() {

        farmersList=new ArrayList<>();

        Farmers farmers = new Farmers();
        farmers.setFarmname("Tile Wala khet");
        farmersList.add(farmers);


        farmers = new Farmers();
        farmers.setFarmname("Tile Wala khet new");
        farmersList.add(farmers);

        farmers = new Farmers();
        farmers.setFarmname("Tile Wala khet usase bhi new");
        farmersList.add(farmers);

        farmers = new Farmers();
        farmers.setFarmname("Tile Wala khet usase bhi new");
        farmersList.add(farmers);

        farmers = new Farmers();
        farmers.setFarmname("Tile Wala khet usase bhi new");
        farmersList.add(farmers);

        farmers = new Farmers();
        farmers.setFarmname("Tile Wala khet usase bhi new");
        farmersList.add(farmers);

        farmers = new Farmers();
        farmers.setFarmname("Tile Wala khet usase bhi new");
        farmersList.add(farmers);

        farmers = new Farmers();
        farmers.setFarmname("Tile Wala khet usase bhi new");
        farmersList.add(farmers);

        farmers = new Farmers();
        farmers.setFarmname("Tile Wala khet usase bhi new");
        farmersList.add(farmers);

        //landingAdapter.notifyDataSetChanged();
    }
*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.landing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/
        finish();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.notification) {
            Intent intent=new Intent(context, NotificationActivity.class);
            startActivity(intent);
            //finish();

            // Handle the camera action
        } else if (id == R.id.nav_verify_farmer) {
            Intent intent=new Intent(context,VerifyFarmActivity.class);
            startActivity(intent);
            //finish();
        } else if (id == R.id.add_soil_health_card) {

            Intent intent=new Intent(context, SoilTestNotDoneListActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.show_profile) {
            Intent intent=new Intent(context, ShowProfileActivity.class);
            startActivity(intent);
            //finish();

        }
       /* else if (id == R.id.fill_profile_landing) {
            Intent intent=new Intent(context, FillProfileActivity.class);
            startActivity(intent);
            //finish();
        }*/

        else if (id == R.id.bind_farmer) {
            Intent intent=new Intent(context, BindingActivity.class);
            startActivity(intent);
            //finish();
        }

        /*else if (id == R.id.gpsc) {
            Intent intent=new Intent(context, GetFarmLocationActivity.class);
            startActivity(intent);

        }*/ else if (id == R.id.settings) {
            /*new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferencesMethod.clear(context);
                            SharedPreferencesMethod.setBoolean(context, "Login", false);
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                            finish();                        }
                    })
                    .setNegativeButton("No", null)
                    .show();*/

            Intent intent=new Intent(context, SettingActivity.class);
            startActivity(intent);
            finish();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

            try {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,

                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONArray jsonarray = null;
                                farmersList=new ArrayList<>();
                                recyclerView=(RecyclerView)findViewById(R.id.recy_view_farmers);

                                try {
                                    jsonarray = new JSONArray(response);
                                    Farmers farmers ;
                                    String firstname = null;
                                    String lastname = null;
                                    String middlename = null;
                                    String farmpetname=null;
                                    String Name=null;
                                    String soiltype=null,irrigationtype=null,mobno=null,date_of_sowing=null,date_of_harvesting=null;
                                    String cropName=null;
                                    String farm_num=null;
                                    String person_num=null;
                                    String user_num=null;
                                    String profile_img=null;
                                    String latest_farm_image=null;
                                     gps1=null;
                                     gps2=null;
                                     gps3=null;
                                     gps4=null;
                                     gps5=null;
                                     gps6=null;

                                   /*
                                    farmers.setFarmname("Tile Wala khet");
                                    farmersList.add(farmers);*/

                                   date=new String[jsonarray.length()];
                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                                        firstname = jsonobject.getString("firstName");
                                        lastname = jsonobject.getString("lastName");
                                        middlename = jsonobject.getString("middleName");
                                        farmpetname=jsonobject.getString("farm_pet_name");
                                        soiltype=jsonobject.getString("soilType");
                                        irrigationtype=jsonobject.getString("irrigationType");
                                        mobno=jsonobject.getString("mobNo1");
                                        date_of_sowing=jsonobject.getString("date_of_sowing");
                                        date_of_harvesting=jsonobject.getString("expct_dateof_harvest");
                                        cropName=jsonobject.getString("crop_name");
                                        farm_num=jsonobject.getString("farm_num");
                                        person_num=jsonobject.getString("person_num");
                                        user_num=jsonobject.getString("user_num");
                                        profile_img=jsonobject.getString("profile_img");
                                        latest_farm_image=jsonobject.getString("latest_img");
                                        gps1=jsonobject.getString("GPSc1");
                                        gps2=jsonobject.getString("GPSc2");
                                        gps3=jsonobject.getString("GPSc3");
                                        gps4=jsonobject.getString("GPSc4");
                                        gps5=jsonobject.getString("GPSc5");
                                        gps6=jsonobject.getString("GPSc6");



                                        //String imgLink=jsonobject.getString("img_link");
                                        Name=firstname+" "+""+middlename+" "+lastname;

                                        Log.e("Tag",Name);
                                        farmers=new Farmers();
                                        farmers.setFarmname(farmpetname);
                                        farmers.setFarmername(Name);
                                        farmers.setFarmnum(farm_num);
                                        farmers.setUser_num(user_num);
                                        farmers.setPerson_num(person_num);
                                        farmers.setProfile_img(profile_img);
                                        farmers.setLatest_farm_image(latest_farm_image);

                                      /*  farmers.setSoiltype(soiltype);
                                        farmers.setIrrigationtype(irrigationtype);
                                        farmers.setDate_of_harvesting(date_of_harvesting);
                                        farmers.setDate_of_sowing(date_of_sowing);
                                      */  farmers.setMobno(mobno);


                                        farmers.setCropName(cropName);
                                        farmers.setGpsc1(gps1);
                                        farmers.setGpsc2(gps2);
                                        farmers.setGpsc3(gps3);
                                        farmers.setGpsc4(gps4);
                                        farmers.setGpsc5(gps5);
                                        farmers.setGpsc6(gps6);

                                        //farmers.setFarmimgbacklink(imgLink);
                                        farmersList.add(farmers);


                                    }

                                    progressDialog.dismiss();
                                    landingAdapter=new LandingAdapter(farmersList,context,"from_landing");
                                    recyclerView.setHasFixedSize(true);
                                   // recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
                                    //recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(landingAdapter);
                                    landingAdapter.notifyDataSetChanged();

                                    mLayoutManager = new LinearLayoutManager(context);
                                    mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                    recyclerView.setLayoutManager(mLayoutManager);

                                  /*  recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                                        @Override
                                        public void onClick(View view, int position) {
                                            final Farmers farmers = farmersList.get(position);
                                            //TextView mobNo = (TextView) view.findViewById(R.id.farmer_mob_no);
                                            RelativeLayout relativeLayout=(RelativeLayout)view.findViewById(R.id.rel_lay_recy);
                                            CircleImageView imageButton=(CircleImageView) view.findViewById(R.id.user_profile_photo);

                                           *//* mobNo.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    String strnumber=farmers.getMobno();
                                                    Uri number = Uri.parse("tel:" + strnumber);
                                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                                    intent.setData(number);
                                                    startActivity(intent);
                                                }
                                            });*//*
                                            relativeLayout.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent = new Intent(context, ShowFarmDataActivity.class);
                                                    intent.putExtra("farm_num",farmers.getFarmnum());
                                                    SharedPreferencesMethod.setString(context,"GPSC1",farmers.getGpsc1());
                                                    SharedPreferencesMethod.setString(context,"GPSC2",farmers.getGpsc2());
                                                    SharedPreferencesMethod.setString(context,"GPSC3",farmers.getGpsc3());
                                                    SharedPreferencesMethod.setString(context,"GPSC4",farmers.getGpsc4());
                                                    SharedPreferencesMethod.setString(context,"GPSC5",farmers.getGpsc5());
                                                    SharedPreferencesMethod.setString(context,"GPSC6",farmers.getGpsc6());
                                                    //intent.putExtra("farm_pet_name",farmers.getFarmname());
                                                    DataHandler.newInstance().setLanding_farm_pet_name(farmers.getFarmname());
                                                    DataHandler.newInstance().setFarmnum(farmers.getFarmnum());
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                           *//* imageButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                 *//**//*   Intent intent = new Intent(context, FarmerInfoActivity.class);
                                                    *//**//**//**//*intent.putExtra("farm_num",farmers.getFarmnum());
                                                    SharedPreferencesMethod.setString(context,"GPSC1",farmers.getGpsc1());
                                                    SharedPreferencesMethod.setString(context,"GPSC2",farmers.getGpsc2());
                                                    SharedPreferencesMethod.setString(context,"GPSC3",farmers.getGpsc3());
                                                    SharedPreferencesMethod.setString(context,"GPSC4",farmers.getGpsc4());
                                                    SharedPreferencesMethod.setString(context,"GPSC5",farmers.getGpsc5());
                                                    SharedPreferencesMethod.setString(context,"GPSC6",farmers.getGpsc6());
                                                    //intent.putExtra("farm_pet_name",farmers.getFarmname());
                                                    DataHandler.newInstance().setLanding_farm_pet_name(farmers.getFarmname());
                                                    DataHandler.newInstance().setFarmnum(farmers.getFarmnum());

                                                    //intent.putExtra("task_date",taskdata.getTaskDate());*//**//**//**//*
                                                    startActivity(intent);
                                                    finish();*//**//*
                                                }
                                            });*//*
//                                        Toast.makeText(getApplicationContext(),"Description ->"+taskdata.getTaskDescription()+", Id ->"+taskdata.getTaskId(),Toast.LENGTH_SHORT).show();

                                            //Toast.makeText(getApplicationContext(), farmers.getFarmname() + " is selected!", Toast.LENGTH_SHORT).show();
                                        }
                                        @Override
                                        public void onLongClick(View view, int position) {
                                        }
                                    }));*/
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                    landingAdapter=new LandingAdapter(farmersList,context,"from_verify_activity");
                                    recyclerView.setAdapter(landingAdapter);
                                    landingAdapter.notifyDataSetChanged();
                                    if(landingAdapter.getItemCount()==0){
                                        no_farm_rel.setVisibility(View.VISIBLE);
                                    }else{
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
                                Log.e("Error",error.toString());
                                Toast.makeText(LandingActivity.this, R.string.error_text, Toast.LENGTH_LONG).show();
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

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("TAG", "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            Log.e("Firebase Reg Id: ",regId);
        else
            Log.e("Firebase not recieved","");

    }


    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_list.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(18);
            dots[i].setTextColor(Color.parseColor("#000000"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#FFFFFF"));
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(connected)
            if(is_binded)
        handler.postDelayed(runnable, delay);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(connected)
            if(is_binded)
        handler.removeCallbacks(runnable);
    }


    private void bindUser() {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECK_COMPANY_NUM,

                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            JSONObject jobject = null;
                            try {

                                jobject = new JSONObject(response);
                                String result=jobject.getString("result");
                                String status=jobject.getString("status");
                                String message=jobject.getString("msg");
                                JSONObject resultobject=new JSONObject(result);
                                String comp_num=resultobject.getString("comp_num");
                                Log.e("comp_num",comp_num);
                                if(comp_num!=null) {
                                    if(comp_num.equals("0")) {
                                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.BINDED, false);
                                    }else{
                                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.BINDED, true);
                                        SharedPreferencesMethod.setString(context,"cctt",comp_num);
                                    }
                                    progressDialog.dismiss();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();

                            }

                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error",error.toString());
                            Toast.makeText(LandingActivity.this, R.string.error_text, Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(USER_NUM,user_num);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            progressDialog.dismiss();
        }



    }

  /*  private void hideItem()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.fill_profile_landing).setVisible(false);
    }*/

}
