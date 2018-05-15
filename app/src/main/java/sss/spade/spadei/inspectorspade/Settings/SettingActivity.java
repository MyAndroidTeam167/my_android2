package sss.spade.spadei.inspectorspade.Settings;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import sss.spade.spadei.inspectorspade.FarmerInformation.FarmerInfoActivity;
import sss.spade.spadei.inspectorspade.LandingActivity.LandingActivity;
import sss.spade.spadei.inspectorspade.Login.ForgetPassword.FrgtPassActivity;
import sss.spade.spadei.inspectorspade.Login.MainActivity;
import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.SharedPref.SharedPreferencesMethod;
import sss.spade.spadei.inspectorspade.Utiltiy.Utility;


public class SettingActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    Context context;
    Toolbar mActionBarToolbar;
    //String[] country = {"English","Hindi"};
    String setlanguage;
    String user_num;
    final String API_NEW_URL = "http://spade.farm/app/index.php/inspectorApp/save_profile_img";
    //private static final String REGISTER_URL_DATA_PROFILE = "https://www.oswalcorns.com/my_farm/myfarmapp/index.php/signUp/fetch_profile";
    String pictureImagePath = "";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    final String USER_NUM = "user_num";
    final String PERSON_NUM = "person_num";
    JSONObject jsonObject;
    private String userChoosenTask;
    ProgressDialog progressDialog;
    @BindView(R.id.inspector_profile_photo)
    CircleImageView inspector_profile_image;
    Bitmap myBitmap;
    String ins_user_num;
    String ins_person_num;



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
        // super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        context = this;
        user_num = SharedPreferencesMethod.getString(context, "UserNum");

        TextView tvlogout = (TextView) findViewById(R.id.logout_setting);
        tvlogout.setOnClickListener(this);
        //TextView tvsetlanguage=(TextView)findViewById(R.id.tvsetlanguage);
        //tvsetlanguage.setOnClickListener(this);
        TextView tvchangepass = (TextView) findViewById(R.id.tvchangepass);
        tvchangepass.setOnClickListener(this);
        TextView tvshare = (TextView) findViewById(R.id.share_setting);
        tvshare.setOnClickListener(this);

        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText("Settings");
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);

        ButterKnife.bind(this);

       /* inspector_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
*/

        jsonObject = new JSONObject();
        ins_person_num = SharedPreferencesMethod.getString(context, "person_num");
        ins_user_num = SharedPreferencesMethod.getString(context, "UserNum");


        //getSupportActionBar().setTitle("My Title");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

      /*  progressDialog = ProgressDialog.show(SettingActivity.this,
                getString(R.string.dialog_please_wait), "");
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();*/


    }


    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
            }
            *//*case 1:{
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }*//*
        }
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(SettingActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //  intent.setType("image*//*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }


    private void cameraIntent() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    private void onCaptureImageResult(Intent data) {
        File imgFile = new File(pictureImagePath);
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            myBitmap = getResizedBitmap(myBitmap, 720, 1080);
            if (myBitmap != null) {
                ImageUploadToServerFunction();
            } else {
                Toast.makeText(context, "Some Error Ocuurred try again later", Toast.LENGTH_SHORT).show();
            }
            inspector_profile_image.setImageBitmap(myBitmap);

            // Exif.setText(ReadExif(imgFile.getAbsolutePath()));

        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        myBitmap = BitmapFactory.decodeFile(picturePath);
        myBitmap = getResizedBitmap(myBitmap, 720, 1080);
        if (myBitmap != null) {
            ImageUploadToServerFunction();
        } else {
            Toast.makeText(context, "Some Error Ocuurred try again later", Toast.LENGTH_SHORT).show();
        }
        inspector_profile_image.setImageBitmap(myBitmap);


    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }


    public void ImageUploadToServerFunction() {
        if (myBitmap != null) {
            Log.e("checkArray", "Reached ImageUploadToServerFunction");
            progressDialog = new ProgressDialog(context);
            progressDialog.show();
            progressDialog.setMessage("Uploading Image");
            progressDialog.setCancelable(false);
            ByteArrayOutputStream byteArrayOutputStreamObject1;
            byteArrayOutputStreamObject1 = new ByteArrayOutputStream();

            myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStreamObject1);

            final String ConvertImage1 = Base64.encodeToString(byteArrayOutputStreamObject1.toByteArray(), Base64.DEFAULT);
*//*
            try {
               *//**//* progressDialog = ProgressDialog.show(context,
                        context.getString(R.string.dialog_please_wait), "");*//**//*
                StringRequest stringRequest = new StringRequest(Request.Method.POST, API_NEW_URL,

                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("Message",response.toString());
                                try {
                                    progressDialog.dismiss();
                                    JSONObject jsonObject=new JSONObject(response);
                                    jsonObject.getString("msg");
                                    Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    progressDialog.dismiss();
                                    e.printStackTrace();
                                }

                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(PERSON_NUM,person_num);
                        params.put(USER_NUM,user_num);
                        params.put("image_data",ConvertImage1);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);
            }catch (Exception e){
                progressDialog.dismiss();
            }*//*

            try {
                // jsonObject.put("name", "myImages");
                jsonObject.put("image_data", ConvertImage1);
                jsonObject.put(PERSON_NUM, ins_person_num);
                jsonObject.put(USER_NUM, ins_user_num);

            } catch (JSONException e) {
                Log.e("JSONObject Here", e.toString());
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API_NEW_URL, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.e("Message from server", jsonObject.toString());
                            //Log.e("Message from server", jsonArray.toString());
                            try {
                                progressDialog.dismiss();
                                String imglist = jsonObject.getString("msg");
                                Log.e("Message_img", imglist);
                                Toast.makeText(context, imglist, Toast.LENGTH_LONG).show();

                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Some error occurred Please Re-upload images", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("Message from server err", volleyError.toString());
                    Toast.makeText(getApplication(), "Error Occurred", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(200 * 30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(this).add(jsonObjectRequest);

        } else {
            Toast.makeText(context, "Capture Image First", Toast.LENGTH_SHORT).show();
        }
    }*/


    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        // Toast.makeText(getApplicationContext(),country[position] , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logout_setting) {

            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("foo-bar");
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("user_" + user_num);
                            SharedPreferencesMethod.clear(context);
                            SharedPreferencesMethod.setBoolean(context, "Login", false);
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();


        }

        if (v.getId() == R.id.tvchangepass) {
            Intent intent = new Intent(context, FrgtPassActivity.class);
            intent.putExtra("chanage_pass", "from_setting");
            startActivity(intent);
            finish();
        }
       /* if (v.getId() == R.id.tvsetlanguage) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.my_dialog_layout, null);
            dialogBuilder.setView(dialogView);
            final Spinner spinner = (Spinner) dialogView.findViewById(R.id.mySpinnersetlang);
            spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) context);
            ArrayAdapter aa = new ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item,country);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(aa);

            *//*AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();*//*

            dialogBuilder.setCancelable(true);
            dialogBuilder.setMessage("Change Language");
            dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    setlanguage=spinner.getSelectedItem().toString();



                    if(setlanguage.equals("Hindi")){
                        changeLang("hi");
                        Log.e("Lang :",setlanguage);
                        String languageselected = setlanguage;
                        SharedPreferencesMethod.setString(context,SharedPreferencesMethod.LANGUAGE,languageselected);
                        Intent intent = new Intent(context, SettingActivity.class);
                        startActivity(intent);
                        finish();
                        //dialog.cancel();
                    }
                    else if(setlanguage.equals("English")){
                        changeLang("en");
                        Log.e("Lang :",setlanguage);
                        String languageselected = setlanguage;
                        SharedPreferencesMethod.setString(context,SharedPreferencesMethod.LANGUAGE,languageselected);
                        Intent intent = new Intent(context, SettingActivity.class);
                        startActivity(intent);
                        finish();
                       // dialog.cancel();
                    }
                    else{
                        Toast.makeText(context, "Something Wrong", Toast.LENGTH_SHORT).show();
                    }

                }

            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
//                        finish();
                    dialog.cancel();
                }
            });
            // Create the AlertDialog object and return it
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

            *//*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setMessage("Want to submit your response?");
            alertDialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }

            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
//                        finish();
                    dialog.cancel();
                }
            });
            // Create the AlertDialog object and return it
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }*//*
        }*/
        if (v.getId() == R.id.share_setting) {
            /*Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            startActivity(Intent.createChooser(sharingIntent, "Share With"));*/
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            //return true;
        }
    }


    private void changeLang(String lang) {

        Locale myLocale;
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

/*
    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {
        public AsyncTaskRunner() {
            super();
        }


        @Override
        protected String doInBackground(final String... params) {

            try {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL_DATA_PROFILE,

                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                               */
/* String firstname = null;
                                String lastname = null;
                                String middlename = null;
                                String adharno = null;
                                String panno = null;
                                String dob = null;
                                String mobno1 = null;
                                String mobno2 = null;
                                String landlineno = null;
                                String email = null;
                                String addL1 = null;
                                String addL2 = null;
                                String addL3 = null;
                                String city = null;
                                String state = null;
                                String country = null;
                                String person_num;*//*

                                String profile_img = null;


                                try {
                                    JSONObject jobject = new JSONObject(response);
                                   */
/* firstname = jobject.getString("firstName");
                                    lastname = jobject.getString("lastName");
                                    middlename = jobject.getString("middleName");
                                    adharno = jobject.getString("adhaarNo");
                                    panno = jobject.getString("PanNo");
                                    dob = jobject.getString("dob");
                                    mobno1 = jobject.getString("mobNo1");
                                    mobno2 = jobject.getString("mobNo2");
                                    landlineno = jobject.getString("landLineNo");
                                    email = jobject.getString("email");
                                    addL1 = jobject.getString("addL1");
                                    addL2 = jobject.getString("addL2");
                                    addL3 = jobject.getString("addL3");
                                    city = jobject.getString("city");
                                    state = jobject.getString("state");
                                    country = jobject.getString("country");
                                    person_num = jobject.getString("person_num");
                                   *//*
 profile_img = jobject.getString("profile_img");


                                   */
/* if (addL2.matches("")) {
                                        addL2 = addL2;
                                    } else {
                                        addL2 = ", " + addL2;
                                    }
                                    if (addL3.matches("")) {
                                        addL3 = addL3;
                                    } else {
                                        addL3 = ", " + addL3;
                                    }
*//*

                                  */
/*  String Name, Address;
                                    Name = firstname + " " + "" + middlename + " " + lastname;
                                    Address = addL1 + addL2 + addL3 + ", " + city + ", " + state + ", " + country;



                                    final String finalMobno = mobno1;
*//*

                                    if (profile_img.equals("null")) {
                                        inspector_profile_image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_person_black_18dp));
                                    } else {
                                        Uri uriprofile = Uri.parse(profile_img);
                                        Picasso.with(inspector_profile_image.getContext()).load(uriprofile).into(inspector_profile_image);
                                        myBitmap = ((BitmapDrawable) inspector_profile_image.getDrawable()).getBitmap();
                                    }

                                    progressDialog.dismiss();

                                    // showImage(uriprofile);
                                    //ImagePopup imagePopup = new ImagePopup(context);

                                } catch (JSONException e) {

                                    progressDialog.dismiss();
                                    e.printStackTrace();
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

                        if (user_num != null) {
                            params.put(USER_NUM, user_num);
                        }

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
*/

}
