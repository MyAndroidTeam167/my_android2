package sss.spade.spadei.inspectorspade.PersonData;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.SharedPref.SharedPreferencesMethod;

public class EditProfileActivity extends AppCompatActivity {

    public static final String REGISTER_URL = "http://spade.farm/app/index.php/farmApp/edit_user_profile_data";
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

    String ct1="";
    final String KEY_TOKEN="token1";

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context, ShowProfileActivity.class);
        startActivity(intent);
        finish();    }

    public static final String KEY_EMAIL = "email";
    public static final String KEY_USERNUM = "user_num";
    public static final String KEY_ADDL1 = "addL1";
    public static final String KEY_ADDL2 = "addL2";
    public static final String KEY_ADDL3 = "addL3";
    public static final String KEY_CITY = "city";
    public static final String KEY_STATE = "state";
    public static final String KEY_COUNTRY = "country";
    private static String DEFAULT_LOCAL = "India";
    private static String DEFAULT_LOCAL_STATE = "Madhya Pradesh";
    private static String DEFAULT_LOCAL_CITY = "Indore";
    public static final String KEY_PERSON_NUM = "person_num";
    public static final String KEY_PROFILE_IMG = "profile_img";

    Calendar myCalendar = Calendar.getInstance();
    Toolbar mActionBarToolbar;

    String Email, mobileno, usernum;
    String sfrstnamefill, smiddlenamefill, slastnamefill, sadharnofill, spannofill, sdobfill, saddressidfill, saddl1profileadd, saddl2profileadd, saddl3profileadd;
    Spinner cityprofileadd, stateprofileadd, citizenship;
    String scityprofileadd, sstateprofileadd, scitizenship;
    String First_Name, Middle_Name, Last_Name, Aadhar_No, Pan_No, Dob, Mobile_No, Mobile_Two, Landline_No, Email_edit, AddL1, AddL2, AddL3, City, State, Country, Name, Address;
    String smobno2fill, slandlineNofill;
    String Personnum;
    EditText emailfill, mobilefill, frstnamefill, middnamefill, lastnamefill, adharnofill;
    EditText pannofill, dobfill, mobtwofill, landlinefill, addl1profileadd, addl2profileadd, addl3profileadd;
    Button saveprofile, testtt;
    Context context;
    TextView accountSignup;
    String Countryidonclick = "";
    String stateidonclick = "";
    String Profileimage;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(context, ShowProfileActivity.class);
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
        setContentView(R.layout.activity_edit_profile);
        context = this;

        ct1=SharedPreferencesMethod.getString(context,"cctt");
        emailfill = (EditText) findViewById(R.id.emailfillprofileedit);
        mobilefill = (EditText) findViewById(R.id.mobilenoprofileedit);
        frstnamefill = (EditText) findViewById(R.id.firstnameprofileedit);

        addl1profileadd = (EditText) findViewById(R.id.addl1profileaddedit);
        addl2profileadd = (EditText) findViewById(R.id.addl2profileaddedit);
        addl3profileadd = (EditText) findViewById(R.id.addl3profileaddedit);
        cityprofileadd = (Spinner) findViewById(R.id.cityprofileaddedit);
        stateprofileadd = (Spinner) findViewById(R.id.stateprofileaddedit);
        citizenship = (Spinner) findViewById(R.id.countryprofileaddedit);
        //frstnamefill.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES );
        middnamefill = (EditText) findViewById(R.id.Middlenameprofileedit);
        lastnamefill = (EditText) findViewById(R.id.Lastnameprofileedit);
        adharnofill = (EditText) findViewById(R.id.Aadharnoprofileedit);
        pannofill = (EditText) findViewById(R.id.Pannoprofileedit);
        dobfill = (EditText) findViewById(R.id.dobprofileedit);
        // addressidfill=(EditText)findViewById(R.id.addressidprofile);
        mobtwofill = (EditText) findViewById(R.id.alternatemobprofileedit);
        landlinefill = (EditText) findViewById(R.id.landlinenoprofileedit);
        saveprofile = (Button) findViewById(R.id.Saveprofileedit);
        //accountSignup=(TextView)findViewById(R.id.Edit_title_registor);
        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText("Edit Profile");
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        First_Name = SharedPreferencesMethod.getString(context, "first_name");
        Middle_Name = SharedPreferencesMethod.getString(context, "middle_name");
        Last_Name = SharedPreferencesMethod.getString(context, "last_name");
        Aadhar_No = SharedPreferencesMethod.getString(context, "aadhar_no");
        Pan_No = SharedPreferencesMethod.getString(context, "pan_no");
        Dob = SharedPreferencesMethod.getString(context, "dob");
        Mobile_No = SharedPreferencesMethod.getString(context, "mobNo");
        Mobile_Two = SharedPreferencesMethod.getString(context, "altermobNo");
        Landline_No = SharedPreferencesMethod.getString(context, "landline_no");
        Email_edit = SharedPreferencesMethod.getString(context, "email");
        AddL1 = SharedPreferencesMethod.getString(context, "profileaddl1");
        AddL2 = SharedPreferencesMethod.getString(context, "profileaddl2");
        AddL3 = SharedPreferencesMethod.getString(context, "profileaddl3");
        City = SharedPreferencesMethod.getString(context, "profilecity");
        State = SharedPreferencesMethod.getString(context, "profilestate");
        Country = SharedPreferencesMethod.getString(context, "profilecountry");
        usernum = SharedPreferencesMethod.getString(context, "UserNum");
        Personnum = SharedPreferencesMethod.getString(context, "person_num");
        Profileimage=SharedPreferencesMethod.getString(context,"profile_img_farmer");


        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {

            String reformattedStr = myFormat.format(fromUser.parse(Dob));
            Dob=reformattedStr;
            // Toast.makeText(EditProfileActivity.this, "*"+sdobfill+"*", Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Toast.makeText(context, usernum + " $$ " + Personnum, Toast.LENGTH_SHORT).show();


        emailfill.setText(Email_edit);
        mobilefill.setText(Mobile_No);
        frstnamefill.setText(First_Name);
        middnamefill.setText(Middle_Name);
        lastnamefill.setText(Last_Name);
        adharnofill.setText(Aadhar_No);
        pannofill.setText(Pan_No);
        dobfill.setText(Dob);
        mobtwofill.setText(Mobile_Two);
        landlinefill.setText(Landline_No);
        addl1profileadd.setText(AddL1);
        addl2profileadd.setText(AddL2);
        addl3profileadd.setText(AddL3);


        DEFAULT_LOCAL = Country;
        DEFAULT_LOCAL_STATE = State;
        DEFAULT_LOCAL_CITY = City;

        //getSupportActionBar().setTitle("My Title");


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
                        new DatePickerDialog(EditProfileActivity.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        return true;
                    }
                }
                return false;
            }
        });


        saveprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sdobfill = dobfill.getText().toString();
                sfrstnamefill = frstnamefill.getText().toString();
                smiddlenamefill = middnamefill.getText().toString();
                slastnamefill = lastnamefill.getText().toString();
                sadharnofill = adharnofill.getText().toString();
                spannofill = pannofill.getText().toString();

                saddressidfill = /*addressidfill.getText().toString();*/"";
                smobno2fill = mobtwofill.getText().toString();
                slandlineNofill = landlinefill.getText().toString();

                if (sfrstnamefill.matches("")) {
                    frstnamefill.setError("First Name Should not be null");
                } else if (slastnamefill.matches("")) {
                    lastnamefill.setError("Last Name Should not be null");
                } else if (!isValidaadhar(adharnofill.getText().toString().trim())) {
                    adharnofill.setError("Invalid Aadhar Number");
                }/* else if (!isInvalidpanno(pannofill.getText().toString().trim())) {
                    pannofill.setError("Invalid Panno.");
                }*/ else if (!isValiddob(sdobfill)) {
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

                    progressDialog = ProgressDialog.show(EditProfileActivity.this,
                            "Please Wait...", "");


                    AsyncTaskRunner runner = new AsyncTaskRunner();
                    runner.execute();


                }
            }
        });

        if (mobileno != null) {
            emailfill.setText(Email_edit);
            emailfill.setEnabled(false);
            mobilefill.setText(Mobile_No);
            mobilefill.setEnabled(false);

        } else {
            mobilefill.setEnabled(false);


        }

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

    public void GetText() throws JSONException {
        // Get user defined values

        sfrstnamefill = frstnamefill.getText().toString();
        smiddlenamefill = middnamefill.getText().toString();
        slastnamefill = lastnamefill.getText().toString();
        sadharnofill = adharnofill.getText().toString();
        spannofill = pannofill.getText().toString();
        //  saddressidfill="";
        smobno2fill = mobtwofill.getText().toString();
        slandlineNofill = landlinefill.getText().toString();
        saddl1profileadd = addl1profileadd.getText().toString();
        saddl2profileadd = addl2profileadd.getText().toString();
        saddl3profileadd = addl3profileadd.getText().toString();
        scitizenship = citizenship.getSelectedItem().toString();
        scityprofileadd = cityprofileadd.getSelectedItem().toString();
        sstateprofileadd = stateprofileadd.getSelectedItem().toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (!response.equals("")) {
                            // DataHandler.newInstance().setPersonnum(response);
                            progressDialog.dismiss();
                            FILL_SUCESSFUL = true;
                            SharedPreferencesMethod.setBoolean(context, "fillProfileSuccesfull", FILL_SUCESSFUL);
                            Toast.makeText(EditProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, ShowProfileActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                },


                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", error.toString());
                        progressDialog.dismiss();
                        Toast.makeText(EditProfileActivity.this, error.toString(), Toast.LENGTH_LONG).show();


                    }


                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_FIRST_NAME, sfrstnamefill);
                params.put(KEY_MIDDLE_NAME, smiddlenamefill);
                params.put(KEY_LAST_NAME, slastnamefill);
                params.put(KEY_ADHAR_NO, sadharnofill);
                params.put(KEY_PAN_NO, spannofill);
                params.put(KEY_DOB, sdobfill);
                // params.put(KEY_ADDRESSID, saddressidfill);
                params.put(KEY_MOBNO1, mobileno);
                params.put(KEY_MOBNO2, smobno2fill);
                params.put(KEY_LANDLINE, slandlineNofill);
                params.put(KEY_ADDL1, saddl1profileadd);
                params.put(KEY_ADDL2, saddl2profileadd);
                params.put(KEY_ADDL3, saddl3profileadd);
                params.put(KEY_COUNTRY, scitizenship);
                params.put(KEY_STATE, sstateprofileadd);
                params.put(KEY_CITY, scityprofileadd);
                params.put(KEY_EMAIL, Email);
                params.put("is_inspector","Y");
                params.put("is_admin","N");
                params.put("is_farmer","N");
                params.put(KEY_TOKEN,ct1);
                if (usernum != null) {
                    params.put(KEY_USERNUM, usernum);

                }
                if (Personnum != null) {
                    params.put(KEY_PERSON_NUM, Personnum);
                }

                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response.headers == null) {
                    // cant just set a new empty map because the member is final.
                    Log.d("Response Header = ", String.valueOf(response.headers));
                    response = new NetworkResponse(
                            response.statusCode,
                            response.data,
                            Collections.<String, String>emptyMap(), // this is the important line, set an empty but non-null map.
                            response.notModified);


                }
                return super.parseNetworkResponse(response);
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {
        public AsyncTaskRunner() {
            super();
        }

        @Override
        protected String doInBackground(String... params) {


            sfrstnamefill = frstnamefill.getText().toString();
            smiddlenamefill = middnamefill.getText().toString();
            slastnamefill = lastnamefill.getText().toString();
            sadharnofill = adharnofill.getText().toString();
            spannofill = pannofill.getText().toString();
            smobno2fill = mobtwofill.getText().toString();
            slandlineNofill = landlinefill.getText().toString();
            saddl1profileadd = addl1profileadd.getText().toString();
            saddl2profileadd = addl2profileadd.getText().toString();
            saddl3profileadd = addl3profileadd.getText().toString();
            scitizenship = citizenship.getSelectedItem().toString();
            scityprofileadd = cityprofileadd.getSelectedItem().toString();
            sstateprofileadd = stateprofileadd.getSelectedItem().toString();
            Email=emailfill.getText().toString();


            try {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,


                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                if (response.equals("\"0\"")) {
                                    progressDialog.dismiss();

                                } else {
                                    Toast.makeText(context, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(context, ShowProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(EditProfileActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }
                        }) {
                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        Log.d("Response Header = ", String.valueOf(response.headers));
                        return super.parseNetworkResponse(response);
                        //progressDialog.dismiss();

                    }

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        if (sfrstnamefill != null) {
                            params.put(KEY_FIRST_NAME, sfrstnamefill);
                        }
                        if (smiddlenamefill != null) {
                            params.put(KEY_MIDDLE_NAME, smiddlenamefill);
                        }
                        if (slastnamefill != null) {
                            params.put(KEY_LAST_NAME, slastnamefill);
                        }
                        if (sadharnofill != null) {
                            params.put(KEY_ADHAR_NO, sadharnofill);
                        }
                        if (spannofill != null) {
                            params.put(KEY_PAN_NO, spannofill);
                        }
                        if (sdobfill != null) {
                            params.put(KEY_DOB, sdobfill);
                        }
                        if (Mobile_No != null) {
                            params.put(KEY_MOBNO1, Mobile_No);
                        }
                        if (smobno2fill != null) {
                            params.put(KEY_MOBNO2, smobno2fill);
                        }
                        if (slandlineNofill != null) {
                            params.put(KEY_LANDLINE, slandlineNofill);
                        }
                        if (saddl1profileadd != null) {
                            params.put(KEY_ADDL1, saddl1profileadd);
                        }
                        if (saddl2profileadd != null) {
                            params.put(KEY_ADDL2, saddl2profileadd);
                        }
                        if (saddl3profileadd != null) {
                            params.put(KEY_ADDL3, saddl3profileadd);
                        }
                        params.put(KEY_COUNTRY, DEFAULT_LOCAL);

                        if (sstateprofileadd != null) {
                            params.put(KEY_STATE, sstateprofileadd);
                        }
                        if (scityprofileadd != null) {
                            params.put(KEY_CITY, scityprofileadd);
                        }
                        if (Email != null) {
                            params.put(KEY_EMAIL, Email);
                        }
                        // params.put(KEY_ADDRESSID, saddressidfill);
                        if (usernum != null) {
                            params.put(KEY_USERNUM, usernum);

                        }
                        if (Personnum != null) {
                            params.put(KEY_PERSON_NUM, Personnum);
                        }
                        if(ct1!=null){
                            params.put(KEY_TOKEN,ct1);
                        }
                        if(Profileimage!=null){
                            params.put(KEY_PROFILE_IMG,Profileimage);
                        }
                        params.put("is_farmer","N");
                        params.put("is_inspector","Y");
                        params.put("is_admin","N");
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
           /* progressDialog = ProgressDialog.show(EditProfileActivity.this,
                    getString(R.string.dialog_please_wait), "");*/

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

        if (!dob.matches("")) {

            if (Pattern.matches("[0-9]{2}[/]{1}[0-9]{2}[/]{1}[0-9]{4}", dob)) {

                check = true;
            } else {
                check = false;
            }
        } else {
            check = false;
        }
        return check;

    }


    private boolean isValidMobile(String phone) {
        boolean check = false;
        if (!phone.matches("")) {
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
        } else {
            check = true;
        }
        return check;
    }


    private boolean isInvalidpanno(String trim) {

        Boolean pan = false;
        //Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

        //  spannofill = spannofill.trim();

        //  Matcher matcher = pattern.matcher(spannofill);

        if (Pattern.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}", trim)) {

            pan = true;
        } else {
            pan = false;
        }

        return pan;
    }
}
