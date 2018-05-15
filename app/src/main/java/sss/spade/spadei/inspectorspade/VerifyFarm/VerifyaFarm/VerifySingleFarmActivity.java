package sss.spade.spadei.inspectorspade.VerifyFarm.VerifyaFarm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sss.spade.spadei.inspectorspade.DataHandler.DataHandler;
import sss.spade.spadei.inspectorspade.FarmLocation.GetFarmLocationActivity;
import sss.spade.spadei.inspectorspade.LandingActivity.LandingActivity;
import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.SharedPref.SharedPreferencesMethod;
import sss.spade.spadei.inspectorspade.VerifyFarm.VerifyFarmActivity;

/**
 * Created by user on 15/12/17.
 */

public class VerifySingleFarmActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar mActionBarToolbar;
    Context context;
    TextView mFarmName,mArea,mSoilType,mIrrigationType,mSpecialNote,mFarmAddress;
    Button verify_farm;
    String AREA,SOIL_TYPE,IRRIGATION_TYPE,AddL1,AddL2,AddL3,City,State,Country,FarmNum;
    String Special_Comment,Address;
    private static final String REGISTER_URL_DATA_FARMADD = "http://spade.farm/app/index.php/signUp/fetch_farm_data";
    private static final String REGISTER_URL_VERIFY_FARM = "http://spade.farm/app/index.php/signUp/verify_farm";
    public static final String KEY_FARM_NUM = "farm_num";
    public static final String KEY_TOKEN = "token1";
    ProgressDialog progressDialog;
    String farm_num;
    String Url;
    String farm_name;
    String from;
    String ct1;


    @Override
    public void onBackPressed() {
       /* if(from.equals("verify")){

        }*/
        Intent intent = new Intent(context, VerifyFarmActivity.class);
        intent.putExtra("from","getfarm");
        startActivity(intent);
        finish();
       /* else {
            Intent intent = new Intent(context, GetFarmLocationActivity.class);
            intent.putExtra("from","getfarm");
            startActivity(intent);
            finish();
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* if(from.equals("verify")){
            Intent intent = new Intent(context, VerifyFarmActivity.class);
            intent.putExtra("from","getfarm");
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(context, GetFarmLocationActivity.class);
            intent.putExtra("from","getfarm");
            startActivity(intent);
            finish();
        }*/
       super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    protected void onCreate(Bundle savedInstanceState) {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_single_farm);
        context=this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            from = extras.getString("from");
        }
        farm_num= DataHandler.newInstance().getFarmnum();
        TextView title=(TextView)findViewById(R.id.tittle);
        title.setText("My Farm");
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        ct1= SharedPreferencesMethod.getString(context,"cctt");

        //getSupportActionBar().setTitle("My Title");

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


       /* FarmNum=SharedPreferencesMethod.getString(context,"farm_num");
        UserNum=SharedPreferencesMethod.getString(context,"UserNum");*/
        mFarmName=(TextView)findViewById(R.id.farm_name_tv);
        mArea=(TextView)findViewById(R.id.area_tv);
        mSoilType=(TextView)findViewById(R.id.soil_type_tv);
        mIrrigationType=(TextView)findViewById(R.id.irrigation_type_tv);
        mSpecialNote=(TextView)findViewById(R.id.special_note_tv);
        mFarmAddress=(TextView)findViewById(R.id.farm_address_tv);
        verify_farm=(Button)findViewById(R.id.verify_farm);
        verify_farm.setOnClickListener(this);
        progressDialog = ProgressDialog.show(VerifySingleFarmActivity.this,
                getString(R.string.dialog_please_wait),"");
        Url=REGISTER_URL_DATA_FARMADD;
        AsyncTaskRunner asyncTask=new AsyncTaskRunner();
        asyncTask.execute(Url,"showdata");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.verify_farm:{
                progressDialog = ProgressDialog.show(VerifySingleFarmActivity.this,
                        getString(R.string.dialog_please_wait),"");
                Url=REGISTER_URL_VERIFY_FARM;
                AsyncTaskRunner asyncTask=new AsyncTaskRunner();
                asyncTask.execute(Url,"verify");
                break;
            }
        }
    }


    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {
        public AsyncTaskRunner() {
            super();
        }


        @Override
        protected String doInBackground(final String... params) {
            try {
                final String urlonrecieve,type;
                urlonrecieve=params[0];
                type=params[1];

                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlonrecieve,

                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (type.equals("showdata")) {
                                    //Toast.makeText(context, "Came here  "+farm_num, Toast.LENGTH_SHORT).show();
                                    String soiltype = null;
                                    String area = null;
                                    String irrigationtype = null;
                                    String speccomment = null;
                                    String addl1 = null;
                                    String addl2 = null;
                                    String addl3 = null;
                                    String farm_city = null;
                                    String farm_state = null;
                                    String farm_country = null;
                                    String pet_name = null;
                                    try {
                                        JSONObject jobject = new JSONObject(response);
                                        area = jobject.getString("area");
                                        soiltype = jobject.getString("soilType");
                                        irrigationtype = jobject.getString("irrigationType");
                                        speccomment = jobject.getString("specComment");
                                        addl1 = jobject.getString("addL1");
                                        addl2 = jobject.getString("addL2");
                                        addl3 = jobject.getString("addL3");
                                        farm_city = jobject.getString("city");
                                        farm_state = jobject.getString("state");
                                        farm_country = jobject.getString("country");
                                        pet_name = jobject.getString("farm_pet_name");
                                        farm_name=pet_name;

                                        SOIL_TYPE = soiltype;
                                        AREA = area;
                                        AddL1 = addl1;
                                        AddL2 = addl2;
                                        AddL3 = addl3;
                                        City = farm_city;
                                        State = farm_state;
                                        Country = farm_country;
                                        Special_Comment = speccomment;
                                        IRRIGATION_TYPE = irrigationtype;
                                        if (AddL2.matches("")) {
                                        } else {
                                            AddL2 = ", " + AddL2;
                                        }
                                        if (AddL3.matches("")) {
                                        } else {
                                            AddL3 = ", " + AddL3;
                                        }

                                        Address = AddL1 + AddL2 + AddL3 + ", " + City + ", " + State + ", " + Country;

                                        mFarmName.setText(pet_name);
                                        mArea.setText(AREA);
                                        mSoilType.setText(SOIL_TYPE);
                                        mIrrigationType.setText(IRRIGATION_TYPE);
                                        mSpecialNote.setText(Special_Comment);
                                        mFarmAddress.setText(Address);
                                        progressDialog.dismiss();

                                    } catch (JSONException e) {

                                        progressDialog.dismiss();
                                        e.printStackTrace();
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    if(response.equals("true")){
                                        Toast.makeText(context, farm_name+" Verified", Toast.LENGTH_LONG).show();
                                        Intent intent=new Intent(context, VerifyFarmActivity.class);
                                               startActivity(intent);
                                               finish();
                                                }else{
                                        Toast.makeText(context, "Unable to submit at this moment please try again later", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(VerifySingleFarmActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();

                        if(farm_num!=null){
                            params.put(KEY_FARM_NUM,farm_num);
                        }
                        if(ct1!=null){
                            params.put(KEY_TOKEN,ct1);
                        }
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
