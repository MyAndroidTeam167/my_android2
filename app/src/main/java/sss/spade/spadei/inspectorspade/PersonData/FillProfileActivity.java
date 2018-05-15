package sss.spade.spadei.inspectorspade.PersonData;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import sss.spade.spadei.inspectorspade.DataHandler.DataHandler;
import sss.spade.spadei.inspectorspade.LandingActivity.LandingActivity;
import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.SharedPref.SharedPreferencesMethod;

public class FillProfileActivity extends AppCompatActivity {

    private static final String REGISTER_URL = "http://spade.farm/app/index.php/farmApp/insert_new_data_in_person";
    ProgressDialog progressDialog;
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_MIDDLE_NAME = "middleName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_ADHAR_NO = "adhaarNo";
    public static final String KEY_PAN_NO = "PanNo";
    public static Boolean FILL_SUCESSFUL = false;
    public static final String KEY_DOB = "dob";
    public static final String KEY_ADDRESSID = "address_id";
    public static final String KEY_MOBNO1 = "mobNo1";
    public static final String KEY_MOBNO2 = "mobNo2";
    public static final String KEY_LANDLINE = "landLineNo";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USERNUM = "user_num";
    public static final String KEY_ADDL1 = "addL1";
    public static final String KEY_ADDL2 = "addL2";
    public static final String KEY_ADDL3 = "addL3";
    public static final String KEY_CITY = "city";
    public static final String KEY_STATE = "state";
    public static final String KEY_COUNTRY = "country";
    private static final String DEFAULT_LOCAL = "India";
    private static final String DEFAULT_LOCAL_STATE = "Madhya Pradesh";
    private static final String DEFAULT_LOCAL_CITY = "Indore";


    Calendar myCalendar = Calendar.getInstance();
    ConnectivityManager connectivityManager;
    boolean connected = false;
    String ct1;
    final String KEY_TOKEN="token1";



    Boolean islogin;
    Boolean is_offline_enables=false;

    String Email,mobileno,usernum;
    String sfrstnamefill,smiddlenamefill,slastnamefill,sadharnofill,spannofill,sdobfill,saddressidfill,saddl1profileadd,saddl2profileadd, saddl3profileadd;
    Spinner cityprofileadd,stateprofileadd,citizenship;
    String scityprofileadd, sstateprofileadd, scitizenship;

    String smobno2fill,slandlineNofill;
    EditText emailfill,mobilefill,frstnamefill,middnamefill,lastnamefill,adharnofill;
    EditText pannofill,dobfill,mobtwofill,landlinefill,addl1profileadd,addl2profileadd,addl3profileadd;
    Button saveprofile,testtt;
    Context context;
    TextView accountSignup;
    String Countryidonclick="";
    String stateidonclick="";
    Toolbar mActionBarToolbar;
    Boolean is_binded=false;
    boolean connected1 = false;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*Intent intent=new Intent(context,MainActivity.class);
        startActivity(intent);
        finish();*/
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
            connected1 = true;
        } else {
            connected1 = false;
        }
        is_offline_enables=SharedPreferencesMethod.getBoolean(context,SharedPreferencesMethod.IS_OFFLINE_ENABLED);

        if(is_offline_enables) {

            if (connected1) {
                is_binded = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.BINDED);
                if (is_binded) {
                    setContentView(R.layout.activity_fill_profile);


                    Email = DataHandler.newInstance().getLoginEmail();
                    mobileno = DataHandler.newInstance().getLoginMobile();
                    emailfill = (EditText) findViewById(R.id.emailfillprofile);
                    mobilefill = (EditText) findViewById(R.id.mobilenoprofile);
                    frstnamefill = (EditText) findViewById(R.id.firstnameprofile);

                    addl1profileadd = (EditText) findViewById(R.id.addl1profileadd);
                    addl2profileadd = (EditText) findViewById(R.id.addl2profileadd);
                    addl3profileadd = (EditText) findViewById(R.id.addl3profileadd);

                    cityprofileadd = (Spinner) findViewById(R.id.cityprofileadd);
                    stateprofileadd = (Spinner) findViewById(R.id.stateprofileadd);
                    citizenship = (Spinner) findViewById(R.id.countryprofileadd);
                    //frstnamefill.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES );
                    middnamefill = (EditText) findViewById(R.id.Middlenameprofile);
                    lastnamefill = (EditText) findViewById(R.id.Lastnameprofile);
                    adharnofill = (EditText) findViewById(R.id.Aadharnoprofile);
                    pannofill = (EditText) findViewById(R.id.Pannoprofile);
                    dobfill = (EditText) findViewById(R.id.dobprofile);
                    // addressidfill=(EditText)findViewById(R.id.addressidprofile);
                    mobtwofill = (EditText) findViewById(R.id.alternatemobprofile);
                    landlinefill = (EditText) findViewById(R.id.landlinenoprofile);
                    saveprofile = (Button) findViewById(R.id.Saveprofile);
                    //accountSignup=(TextView)findViewById(R.id.login_title_registor);
                    // testtt=(Button)findViewById(R.id.testprofile);
                    // context=this;
                    mobileno = SharedPreferencesMethod.getString(context, "Mobile");
                    Email = SharedPreferencesMethod.getString(context, "Email");
                    Log.e("TAG:", mobileno);

                    ct1 = SharedPreferencesMethod.getString(context, "cctt");
                    connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                    usernum = SharedPreferencesMethod.getString(context, "UserNum");

                    TextView title = (TextView) findViewById(R.id.tittle);
                    title.setText("Fill Profile");
                    mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
                    setSupportActionBar(mActionBarToolbar);

                    //getSupportActionBar().setTitle("My Title");

                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                        getSupportActionBar().setDisplayShowTitleEnabled(false);
                    }


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

                                if (clean.length() < 8) {
                                    clean = clean + ddmmyyyy.substring(clean.length());
                                } else {
                                    //This part makes sure that when we finish entering numbers
                                    //the date is correct, fixing it otherwise
                                    int day = Integer.parseInt(clean.substring(0, 2));
                                    int mon = Integer.parseInt(clean.substring(2, 4));
                                    int year = Integer.parseInt(clean.substring(4, 8));

                                    mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                                    cal.set(Calendar.MONTH, mon - 1);
                                    year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                                    cal.set(Calendar.YEAR, year);
                                    // ^ first set year for the line below to work correctly
                                    //with leap years - otherwise, date e.g. 29/02/2012
                                    //would be automatically corrected to 28/02/2012

                                    day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                                    clean = String.format("%02d%02d%02d", day, mon, year);
                                }

                                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                                        clean.substring(2, 4),
                                        clean.substring(4, 8));

                                sel = sel < 0 ? 0 : sel;
                                current = clean;
                                dobfill.setText(current);
                                dobfill.setSelection(sel < current.length() ? sel : current.length());
                            }

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }

                        private String current = "";
                        private String ddmmyyyy = "DDMMYYYY";
                        private Calendar cal = Calendar.getInstance();

                    };

                    dobfill.addTextChangedListener(tw);

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


                    dobfill.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            final int DRAWABLE_LEFT = 0;
                            final int DRAWABLE_TOP = 1;
                            final int DRAWABLE_RIGHT = 2;
                            final int DRAWABLE_BOTTOM = 3;

                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                if (event.getRawX() >= (dobfill.getRight() - dobfill.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                    // your action here
                                    new DatePickerDialog(FillProfileActivity.this, date, myCalendar
                                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                                    return true;
                                }
                            }
                            return false;
                        }
                    });

    /*    dobfill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(FillProfileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
*/


/*
        accountSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sdobfill = dobfill.getText().toString();

                if(!isValiddob(sdobfill))
                {
                    dobfill.setError("Please Enter Date of birth");
                }else {
                    SimpleDateFormat fromUser = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

                    try {

                        String reformattedStr = myFormat.format(fromUser.parse(sdobfill));
                        sdobfill=reformattedStr;
                        Toast.makeText(FillProfileActivity.this, "*"+sdobfill+"*", Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
*/


                    saveprofile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sdobfill = dobfill.getText().toString();
                            sfrstnamefill = frstnamefill.getText().toString();
                            smiddlenamefill = middnamefill.getText().toString();
                            slastnamefill = lastnamefill.getText().toString();
                            sadharnofill = adharnofill.getText().toString();
                            spannofill = pannofill.getText().toString();

                            saddressidfill = addl1profileadd.getText().toString();
                            smobno2fill = mobtwofill.getText().toString();
                            slandlineNofill = landlinefill.getText().toString();

                            if (sfrstnamefill.matches("")) {
                                frstnamefill.setError("First Name Should not be null");
                            } else if (slastnamefill.matches("")) {
                                lastnamefill.setError("Last Name Should not be null");
                            } else if (!isValidaadhar(adharnofill.getText().toString().trim())) {
                                adharnofill.setError("Invalid Aadhar Number");
                            } else if (saddressidfill.matches("")) {
                                addl1profileadd.setError("Address line1 should not be null");
                            }
               /* else if (!isInvalidpanno(pannofill.getText().toString().trim())) {
                    pannofill.setError("Invalid Panno.");
                }*/
                            else if (!isValiddob(sdobfill)) {
                                dobfill.setError("Please Enter Date of birth");
                            } else if (!isValidMobile(mobtwofill.getText().toString().trim())) {
                                mobtwofill.setError("Invalid Mobile Number");
                            } else {
                                SimpleDateFormat fromUser = new SimpleDateFormat("dd/MM/yyyy");
                                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");


                                try {
                                    String reformattedStr = myFormat.format(fromUser.parse(sdobfill));
                                    dobfill.setText(myFormat.format(myCalendar.getTime()));
                                    sdobfill = reformattedStr;
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                                    connected = true;
                                } else {
                                    connected = false;
                                }

                                if (connected) {
                                    progressDialog = ProgressDialog.show(FillProfileActivity.this,
                                            "Please Wait...", "");
                                    try {
                                        GetText();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(context, R.string.internet_not_connected, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                    emailfill.setText(Email);
                    emailfill.setEnabled(false);
                    mobilefill.setText(mobileno);
                    mobilefill.setEnabled(false);


                    JSONObject objcount = null;
                    try {
                        objcount = new JSONObject(loadJSONFromAssetcountry(context));
                        JSONArray m_jArrycount = objcount.getJSONArray("countries");
                        final String[] newcountry = new String[m_jArrycount.length()];
                        final String[] newcountryid = new String[m_jArrycount.length()];
                        ArrayList<String> countries = new ArrayList<String>();

                        for (int i = 0; i < m_jArrycount.length(); i++) {
                            JSONObject jo_insidecount = m_jArrycount.getJSONObject(i);
                            newcountry[i] = jo_insidecount.getString("name");
                            newcountryid[i] = jo_insidecount.getString("id");
                            countries.add(newcountry[i]);

                        }

                        //Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);
                        ArrayAdapter<String> countryadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);
                        citizenship.setAdapter(countryadapter);
                        citizenship.setSelection(countryadapter.getPosition(DEFAULT_LOCAL));

                        citizenship.setEnabled(false);


                        citizenship.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                ArrayList<String> state = new ArrayList<String>();
                                Countryidonclick = newcountryid[position];
                                JSONObject objstate = null;
                                int cntryidcout = 0;

                                try {

                                    objstate = new JSONObject(loadJSONFromAssetstate(context));
                                    JSONArray m_jArrystate = objstate.getJSONArray("states");
                                    String[] newstate = new String[m_jArrystate.length()];
                                    String[] newstateid = new String[m_jArrystate.length()];
                                    String[] newstatecountid = new String[m_jArrystate.length()];
                                    for (int j = 0; j < m_jArrystate.length(); j++) {
                                        JSONObject jo_insidestate = m_jArrystate.getJSONObject(j);
                                        newstatecountid[j] = jo_insidestate.getString("country_id");
                                        if (newstatecountid[j].equals(Countryidonclick)) {
                                            newstate[j] = jo_insidestate.getString("name");
                                            newstateid[j] = jo_insidestate.getString("id");
                                            cntryidcout++;
                                        } else {
                                        }

                                    }

                                    final String[] finalstate = new String[cntryidcout];
                                    final String[] finalstateid = new String[cntryidcout];

                                    for (int k = 0, l = 0; k < m_jArrystate.length(); k++, l++) {
                                        if (newstatecountid[k].equals(Countryidonclick)) {
                                            finalstate[l] = newstate[k];
                                            finalstateid[l] = newstateid[k];
                                            state.add(finalstate[l]);
                                        } else {
                                            l--;
                                        }

                                    }

                                    ArrayAdapter<String> stateadapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, state);
                                    stateprofileadd.setAdapter(stateadapter);
                                    stateprofileadd.setSelection(stateadapter.getPosition(DEFAULT_LOCAL_STATE));


                                    //Toast.makeText(FarmAddActivity.this, Integer.toString(cntryidcout), Toast.LENGTH_SHORT).show();


                                    stateprofileadd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                            ArrayList<String> city = new ArrayList<String>();

                                            // Toast.makeText(FarmAddActivity.this, finalstate[position] + finalstateid[position], Toast.LENGTH_SHORT).show();


                                            JSONObject objcity = null;
                                            int stateidcount = 0;
                                            stateidonclick = finalstateid[position];

                                            try {
                                                objcity = new JSONObject(loadJSONFromAssetcity(context));
                                                JSONArray m_jArrycity = objcity.getJSONArray("cities");
                                                String[] newcitye = new String[m_jArrycity.length()];
                                                String[] newcityid = new String[m_jArrycity.length()];
                                                String[] newcitycountid = new String[m_jArrycity.length()];

                                                for (int m = 0; m < m_jArrycity.length(); m++) {
                                                    JSONObject jo_insidecity = m_jArrycity.getJSONObject(m);
                                                    newcitycountid[m] = jo_insidecity.getString("state_id");
                                                    if (newcitycountid[m].equals(stateidonclick)) {
                                                        newcitye[m] = jo_insidecity.getString("name");
                                                        newcityid[m] = jo_insidecity.getString("id");
                                                        stateidcount++;
                                                    } else {
                                                    }

                                                }

//                                    Toast.makeText(FarmAddActivity.this, Integer.toString(m_jArrycity.length()), Toast.LENGTH_SHORT).show();
                                                final String[] finalcity = new String[stateidcount];
                                                final String[] finalcityid = new String[stateidcount];

                                                for (int n = 0, o = 0; n < m_jArrycity.length(); n++, o++) {
                                                    if (newcitycountid[n].equals(stateidonclick)) {
                                                        finalcity[o] = newcitye[n];
                                                        finalcityid[o] = newcityid[n];
                                                        city.add(finalcity[o]);
                                                    } else {
                                                        o--;
                                                    }

                                                }
                                                //Toast.makeText(FarmAddActivity.this, Integer.toString(stateidcount), Toast.LENGTH_SHORT).show();
                                                ArrayAdapter<String> cityadapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, city);
                                                cityprofileadd.setAdapter(cityadapter);
                                                cityprofileadd.setSelection(cityadapter.getPosition(DEFAULT_LOCAL_CITY));


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {


                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


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


    public String loadJSONFromAssetcountry(Context mcontext) {
        String json = null;
        try {
            InputStream is = mcontext.getAssets().open("countries.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public String loadJSONFromAssetstate(Context mcontext) {
        String json = null;
        try {
            InputStream is = mcontext.getAssets().open("states.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public String loadJSONFromAssetcity(Context mcontext) {
        String json = null;
        try {
            InputStream is = mcontext.getAssets().open("cities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public  void  GetText()  throws JSONException
    {
        // Get user defined values

        sfrstnamefill=frstnamefill.getText().toString();
        smiddlenamefill=middnamefill.getText().toString();
        slastnamefill=lastnamefill.getText().toString();
        sadharnofill=adharnofill.getText().toString();
        spannofill=pannofill.getText().toString();
        //sdobfill=dobfill.getText().toString();
        // saddressidfill=addressidfill.getText().toString();
        saddressidfill="";
        smobno2fill=mobtwofill.getText().toString();
        slandlineNofill=landlinefill.getText().toString();
        saddl1profileadd= addl1profileadd.getText().toString();
        saddl2profileadd = addl2profileadd.getText().toString();
        saddl3profileadd = addl3profileadd.getText().toString();
        scitizenship = citizenship.getSelectedItem().toString();
        scityprofileadd = cityprofileadd.getSelectedItem().toString();
        sstateprofileadd = stateprofileadd.getSelectedItem().toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(!response.equals("")) {
                            DataHandler.newInstance().setProfilepersonnum(response);
                            progressDialog.dismiss();
                            FILL_SUCESSFUL=true;
                            SharedPreferencesMethod.setBoolean(context,"fillProfileSuccesfull",FILL_SUCESSFUL);
                            SharedPreferencesMethod.setBoolean(context, "Login", true);
                            SharedPreferencesMethod.setString(context,"person_num",response);
                            //DataHandler.newInstance().setFromActivty("fillprofile");
                            Toast.makeText(FillProfileActivity.this, response, Toast.LENGTH_LONG).show();
                            Intent intent =new Intent(context,LandingActivity.class);
                            startActivity(intent);
                            finish();


                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.e(TAG,error.toString());
                        progressDialog.dismiss();
                        Toast.makeText(FillProfileActivity.this,error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("ERROR:",error.toString());


                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_FIRST_NAME,sfrstnamefill);
                params.put(KEY_MIDDLE_NAME, smiddlenamefill);
                params.put(KEY_LAST_NAME, slastnamefill);
                params.put(KEY_ADHAR_NO, sadharnofill);
                params.put(KEY_PAN_NO, spannofill);
                params.put(KEY_DOB, sdobfill);
                params.put(KEY_ADDRESSID, saddressidfill);
                params.put(KEY_MOBNO1, mobileno);
                params.put(KEY_MOBNO2, smobno2fill);
                params.put(KEY_LANDLINE, slandlineNofill);
                params.put(KEY_ADDL1,saddl1profileadd);
                params.put(KEY_ADDL2,saddl2profileadd);
                params.put(KEY_ADDL3,saddl3profileadd);
                params.put(KEY_COUNTRY,scitizenship);
                params.put(KEY_STATE,sstateprofileadd);
                params.put(KEY_CITY,scityprofileadd);
                params.put(KEY_EMAIL, Email);
                params.put("is_inspector","Y");
                params.put("is_admin","N");
                params.put("is_farmer","N");
                if(usernum!=null){
                    params.put(KEY_USERNUM, usernum);

                }
                if(ct1!=null){
                    params.put(KEY_TOKEN,ct1);
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

        dobfill.setText(sdf.format(myCalendar.getTime()));
    }

    private boolean isValidaadhar(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            // if(phone.length() < 6 || phone.length() > 13) {
            if (phone.length() != 12) {
                check = false;
                //txtPhone.setError("Not Valid Number");
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }


    private boolean isValiddob(String dob) {
        boolean check = false;

        if(!dob.matches("")){

            if(Pattern.matches("[0-9]{2}[/]{1}[0-9]{2}[/]{1}[0-9]{4}",dob)) {

                check = true;
            }
            else{ check=false;}
        }else{check=false;}
        return check;

    }





    private boolean isValidMobile(String phone) {
        boolean check = false;
        if(!phone.matches("")) {
            if (!Pattern.matches("[a-zA-Z]+", phone)) {
                // if(phone.length() < 6 || phone.length() > 13) {
                if (phone.length() != 10) {
                    check = false;
                    //txtPhone.setError("Not Valid Number");
                } else {
                    check = true;
                }
            } else {
                check = false;
            }
        }else{ check=true;}
        return check;
    }




    private boolean isInvalidpanno(String trim) {

        Boolean pan=false;
        //Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

        //  spannofill = spannofill.trim();

        //  Matcher matcher = pattern.matcher(spannofill);

        if(Pattern.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}",trim)) {

            pan = true;
        }
        else{ pan=false;}

        return pan;
    }

    void basic_title(){
        TextView title=(TextView)findViewById(R.id.tittle);
        title.setText("Fill Profile");
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

}
