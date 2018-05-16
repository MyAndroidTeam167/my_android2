package sss.spade.spadei.inspectorspade.SoilHealthCard;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import sss.spade.spadei.inspectorspade.DataHandler.DataHandler;
import sss.spade.spadei.inspectorspade.LandingActivity.LandingActivity;
import sss.spade.spadei.inspectorspade.LandingActivity.ShowFarmData.ShowFarmDataActivity;
import sss.spade.spadei.inspectorspade.PersonData.FillProfileActivity;
import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.SharedPref.SharedPreferencesMethod;

public class InspectorSoilCardInputActivity extends AppCompatActivity {
    Context context;
    Toolbar mActionBarToolbar;
    Button submitCard;
    EditText healthCardNum, collectionDate, laboratoryName;
    String strHealthCardNum, strCollectionDate, strLaboratoryName;
    String[] Parameter, Unit, Rating;
    String soil_test_farm_num="";
    ProgressDialog progressDialog;
    Calendar myCalendar = Calendar.getInstance();



    String strtestValue1, strtestValue2, strtestValue3, strtestValue4, strtestValue5, strtestValue6, strtestValue7, strtestValue8, strtestValue9, strtestValue10, strtestValue11, strtestValue12;
    //    Spinner cuRating, cuUnit, mnRating, mnUnit, feRating, feUnit, bRating, bUnit, znRating, znUnit, sRating, sUnit;
//    Spinner kRating, kUnit, pRating, pUnit, nRating, nUnit, ocRating, ocUnit, ecRating, ecUnit, phRating, phUnit;
    EditText phTestValue, ecTestValue, ocTestValue, nTestValue, pTestValue, kTestValue, sTestValue, znTestValue, bTestValue, feTestValue, MnTestValue, cuTestValue;

    JSONArray jsonParameter;
    final String PARAMS_TEST_VALUE_1 = "test_value_1";
    final String PARAMS_TEST_VALUE_2 = "test_value_2";
    final String PARAMS_TEST_VALUE_3 = "test_value_3";
    final String PARAMS_TEST_VALUE_4 = "test_value_4";
    final String PARAMS_TEST_VALUE_5 = "test_value_5";
    final String PARAMS_TEST_VALUE_6 = "test_value_6";
    final String PARAMS_TEST_VALUE_7 = "test_value_7";
    final String PARAMS_TEST_VALUE_8 = "test_value_8";
    final String PARAMS_TEST_VALUE_9 = "test_value_9";
    final String PARAMS_TEST_VALUE_10 = "test_value_10";
    final String PARAMS_TEST_VALUE_11 = "test_value_11";
    final String PARAMS_TEST_VALUE_12 = "test_value_12";
    String PARAMS;
    String testVal;

    String[] TestValue;

    final String PARAMS_FARM_NUM = "farm_num";
    final String KEY_TOKEN = "token4";
    final String PARAMS_SAMPLE_NUM = "sample_num";
    final String PARAMS_COLLECTION_DATE = "collection_date";
    final String PARAMS_LABORATORY="laboratory";
    //    final String PARAMS_PARAMETER = "parameter";
    final String PARAMS_TEST_VALUE = "test_value";
    final String PARAMS_UNIT = "unit";
    final String PARAMS_RATING = "rating";
    final String REGISTER_URL="http://spade.farm/app/index.php/farmApp/inspector_soil_card_input";
    String farm_num;
    String from_activity="";
    String ct1;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(from_activity.equals("soilnotdone")){
            Intent intent = new Intent(context, SoilTestNotDoneListActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(context, ShowFarmDataActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(from_activity.equals("soilnotdone")){
            Intent intent = new Intent(context, SoilTestNotDoneListActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(context, ShowFarmDataActivity.class);
            startActivity(intent);
            finish();
        }
           }

    protected void onCreate(Bundle savedInstanceState) {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspector_soil_card_input);
        context = this;
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            from_activity = extras.getString("from");
            // and get whatever type user account id is
        }
        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText("Enter Soil Card");
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        farm_num= DataHandler.newInstance().getFarmnum();

        ct1= SharedPreferencesMethod.getString(context,"cctt");
        submitCard = (Button) findViewById(R.id.submitCard);
        healthCardNum = (EditText) findViewById(R.id.healthCardNum);
        collectionDate = (EditText) findViewById(R.id.collectionDate);
        laboratoryName = (EditText) findViewById(R.id.laboratoryName);

        phTestValue = (EditText) findViewById(R.id.phTestValue);
        ecTestValue = (EditText) findViewById(R.id.ECTestValue);
        ocTestValue = (EditText) findViewById(R.id.OCTestValue);
        nTestValue = (EditText) findViewById(R.id.NTestValue);
        pTestValue = (EditText) findViewById(R.id.PTestValue);
        kTestValue = (EditText) findViewById(R.id.KTestValue);
        sTestValue = (EditText) findViewById(R.id.STestValue);
        znTestValue = (EditText) findViewById(R.id.ZnTestValue);
        bTestValue = (EditText) findViewById(R.id.BTestValue);
        feTestValue = (EditText) findViewById(R.id.FeTestValue);
        MnTestValue = (EditText) findViewById(R.id.MnTestValue);
        cuTestValue = (EditText) findViewById(R.id.CuTestValue);

        init();

        submitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Button Clicked", Toast.LENGTH_SHORT).show();
                TestValue= new String[12];

                TestValue[0] = phTestValue.getText().toString().trim();
                TestValue[1] = ecTestValue.getText().toString().trim();
                TestValue[2] = ocTestValue.getText().toString().trim();
                TestValue[3] = nTestValue.getText().toString().trim();
                TestValue[4] = pTestValue.getText().toString().trim();
                TestValue[5] = kTestValue.getText().toString().trim();
                TestValue[6] = sTestValue.getText().toString().trim();
                TestValue[7] = znTestValue.getText().toString().trim();
                TestValue[8] = bTestValue.getText().toString().trim();
                TestValue[9] = feTestValue.getText().toString().trim();
                TestValue[10] = MnTestValue.getText().toString().trim();
                TestValue[11] = cuTestValue.getText().toString().trim();
                strHealthCardNum = healthCardNum.getText().toString().trim();
                strCollectionDate = collectionDate.getText().toString().trim();
                strLaboratoryName = laboratoryName.getText().toString().trim();

                SimpleDateFormat fromUser = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");


                try {
                    String reformattedStr = myFormat.format(fromUser.parse(strCollectionDate));
                    //dobfill.setText(myFormat.format(myCalendar.getTime()));
                    strCollectionDate=reformattedStr;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setMessage("Want to submit your Soil Health Card?");
                    alertDialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try{
                                progressDialog = ProgressDialog.show(InspectorSoilCardInputActivity.this,
                                        getString(R.string.dialog_please_wait), "");
                                GetText(TestValue);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
//                        finish();
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //getSupportActionBar().setTitle("My Title");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void init() {

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    collectionDate.setText(current);
                    collectionDate.setSelection(sel < current.length() ? sel : current.length());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

        };

        collectionDate.addTextChangedListener(tw);

       /* testtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FarmAddActivity.class);
                startActivity(intent);
            }
        });
*/


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };



        collectionDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (collectionDate.getRight() - collectionDate.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        new DatePickerDialog(InspectorSoilCardInputActivity.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        return true;
                    }
                }
                return false;
            }
        });

    }




    public  void  GetText(String[] TestValue)  throws JSONException
    {
        final String[] TestValueArr = TestValue;
//        Log.e("checkArray","print testValueArr"+TestValueArr[0]+TestValueArr[1]+TestValueArr[2]+TestValueArr[3]);
        // Get user defined values
//        this.TestValue=TestValue;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        JSONObject jsonObject =null;
//                        JSONArray jsonArray = null;
//                        int size;
                        try{
//                            response = response.replace("\"","").trim();
                            if(from_activity.equals("soilnotdone")){
                            Log.e("checkArray","This is response "+response);
                            progressDialog.dismiss();
                            Toast.makeText(context, "Soil health card Data Submitted Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(context,SoilTestNotDoneListActivity.class);
                            startActivity(intent);
                            finish();
                            }else{
                                Log.e("checkArray","This is response "+response);
                                progressDialog.dismiss();
                                Toast.makeText(context, "Soil health card Data Submitted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent =new Intent(context,LandingActivity.class);
                                startActivity(intent);
                                finish();
                            }
//                           Log.e("checkArray","Reached End");
                        }catch (Exception e){
                            e.printStackTrace();
                            progressDialog.dismiss();

                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Log.e(TAG,error.toString());
                        Toast.makeText(InspectorSoilCardInputActivity.this,R.string.error_text, Toast.LENGTH_LONG).show();
                        Log.e("ERROR:",error.toString());
                        progressDialog.dismiss();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(PARAMS_LABORATORY,strLaboratoryName);
                params.put(PARAMS_COLLECTION_DATE,strCollectionDate);
                params.put(PARAMS_SAMPLE_NUM,strHealthCardNum);
                params.put(PARAMS_FARM_NUM,farm_num);
                params.put(KEY_TOKEN,ct1);
                for(int i=1;i<=12;i++){
                    PARAMS = "PARAMS_TEST_VALUE_"+i;
                    testVal = "test_value_"+i;
                    PARAMS = testVal;
//                    Log.e("checkArray",PARAMS+"Test Value = "+TestValueArr[i-1]);
                    params.put(PARAMS,TestValueArr[i-1]);
                }
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        collectionDate.setText(sdf.format(myCalendar.getTime()));
    }

}