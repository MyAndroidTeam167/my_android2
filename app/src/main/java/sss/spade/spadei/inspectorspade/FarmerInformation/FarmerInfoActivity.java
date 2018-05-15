package sss.spade.spadei.inspectorspade.FarmerInformation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.ConditionVariable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.rey.material.app.Dialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.RelativeLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import sss.spade.spadei.inspectorspade.DataHandler.DataHandler;
import sss.spade.spadei.inspectorspade.FarmLocation.GetFarmLocationActivity;
import sss.spade.spadei.inspectorspade.LandingActivity.LandingActivity;
import sss.spade.spadei.inspectorspade.Login.MainActivity;
import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.SharedPref.SharedPreferencesMethod;
import sss.spade.spadei.inspectorspade.Utiltiy.Utility;
import sss.spade.spadei.inspectorspade.VerifyFarm.VerifyaFarm.VerifySingleFarmActivity;

public class FarmerInfoActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar mActionBarToolbar;
    Context context;
    final String API_NEW_URL = "http://spade.farm/app/index.php/inspectorApp/save_profile_img";
    private static final String REGISTER_URL_DATA_PROFILE = "http://spade.farm/app/index.php/signUp/fetch_profile";

    final String USER_NUM = "user_num";
    final String PERSON_NUM = "person_num";
    String pictureImagePath = "";
    ConnectivityManager connectivityManager;
    boolean connected = false;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    ArrayList<String> encodedImageList;
    JSONObject jsonObject;
    ProgressDialog progressDialog;
    String ct1;
    final String KEY_TOKEN="token1";

    @BindView(R.id.capture_image_profile)
    CircleImageView capture_image_profile;
    @BindView(R.id.farmer_profile_photo)
    CircleImageView farmer_profile_image;
   /* @BindView(R.id.upload_image_farmer_profile)
    Button butt_upload_img;*/
    Bitmap myBitmap;
    String user_num, person_num;
    @BindView(R.id.preview_pro_pop_up)
    TextView preview_pro;
    @BindView(R.id.email_pro_farmer)
    TextView email_pro_farmer;
    @BindView(R.id.mob_pro_farmer)
    TextView mob_pro_farmer;
    @BindView(R.id.pan_pro_farmer)
    TextView pan_pro_farmer;
    @BindView(R.id.aadhar_pro_farmer)
    TextView aadhar_pro_farmer;
    /*@BindView(R.id.add_pro_farmer)
    TextView add_pro_farmer;*/
    @BindView(R.id.alt_mob_pro_farmer)
    TextView alt_mob_pro_farmer;
    @BindView(R.id.farmer_info_name)
    TextView farmer_info_name;
    @BindView(R.id.farmer_info_address)
    TextView farmer_info_address;
    @BindView(R.id.gobackbuttfarmer_profile)
    ImageView gobackbutt;


    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent=new Intent(context,LandingActivity.class);
        startActivity(intent);
        finish();
        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context, LandingActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
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
        setContentView(R.layout.activity_farmer_info);
        context = this;
        ButterKnife.bind(this);
        user_num = DataHandler.newInstance().getUser_num();
        person_num = DataHandler.newInstance().getPerson_num();
        jsonObject = new JSONObject();
        encodedImageList = new ArrayList<>();
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


        ct1= SharedPreferencesMethod.getString(context,"cctt");
        gobackbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LandingActivity.class);
                startActivity(intent);
                finish();
            }
        });
        progressDialog = ProgressDialog.show(FarmerInfoActivity.this,
                getString(R.string.dialog_please_wait), "");
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();


        final ImagePopup imagePopup = new ImagePopup(context);
                                    /*imagePopup.setWindowHeight(500); // Optional
                                    imagePopup.setWindowWidth(500); // Optional
                                    imagePopup.setBackgroundColor(Color.BLACK);  // Optional
                                    imagePopup.setFullScreen(false); // Optional
                                    imagePopup.setHideCloseIcon(true);  // Optional
                                    imagePopup.setImageOnClickClose(true);  // Optional*/
        imagePopup.setBackgroundColor(Color.TRANSPARENT);
        imagePopup.setImageOnClickClose(true);// Optional
        imagePopup.setHideCloseIcon(true);

        capture_image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    connected = true;
                } else {
                    connected = false;
                }
                    if(connected)
                     selectImage();
                    else
                        Toast.makeText(context, R.string.internet_not_connected, Toast.LENGTH_SHORT).show();


            }
        });
        farmer_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(myBitmap!=null) {
                    //* Initiate Popup view *
                    Drawable d = new BitmapDrawable(getResources(), myBitmap);
                    imagePopup.initiatePopup(d);
                    imagePopup.viewPopup();
                }

            }
        });
       /* butt_upload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myBitmap!=null){
                ImageUploadToServerFunction();}
                else {
                    Toast.makeText(context, "Select an Image First", Toast.LENGTH_SHORT).show();
                }

            }
        });*/




        //CircleImageView imageView = (CircleImageView) findViewById(R.id.farmer_profile_photo);

        //imagePopup.in
        //imagePopup.initiatePopupWithPicasso(profile_img);



        preview_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    connected = true;
                } else {
                    connected = false;
                }
                if(connected)
                    selectImage();
                else
                    Toast.makeText(context, R.string.internet_not_connected, Toast.LENGTH_SHORT).show();
               /* if(myBitmap!=null) {
                    *//** Initiate Popup view **//*
                    Drawable d = new BitmapDrawable(getResources(), myBitmap);
                    imagePopup.initiatePopup(d);
                    imagePopup.viewPopup();
                }*/
            }
        });




       /* TextView title=(TextView)findViewById(R.id.tittle);
        title.setText("Farmer Info");
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }*/
    }


    @Override
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
            /*case 1:{
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }*/
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(FarmerInfoActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(FarmerInfoActivity.this);

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
        //  intent.setType("image/*");
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
            if(myBitmap!=null){
                ImageUploadToServerFunction();}
            else {
                Toast.makeText(context, "Some Error Ocuurred try again later", Toast.LENGTH_SHORT).show();
            }
            farmer_profile_image.setImageBitmap(myBitmap);

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
        if(myBitmap!=null){
            ImageUploadToServerFunction();}
        else {
            Toast.makeText(context, "Some Error Ocuurred try again later", Toast.LENGTH_SHORT).show();
        }
        farmer_profile_image.setImageBitmap(myBitmap);


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
/*
            try {
               *//* progressDialog = ProgressDialog.show(context,
                        context.getString(R.string.dialog_please_wait), "");*//*
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
            }*/

            try {
                // jsonObject.put("name", "myImages");
                jsonObject.put("image_data", ConvertImage1);
                jsonObject.put(PERSON_NUM, person_num);
                jsonObject.put(USER_NUM, user_num);
                jsonObject.put(KEY_TOKEN,ct1);

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
    }

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

                                String message = null;
                                String status = null;
                                String result=null;

                                String firstname = null;
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
                                String person_num;
                                String profile_img = null;


                                try {
                                    JSONObject new_jobject = new JSONObject(response);
                                    message = new_jobject.getString("msg");
                                    status = new_jobject.getString("status");
                                    result = new_jobject.getString("result");

                                    if (!status.equals("0")) {

                                        JSONObject jobject = new JSONObject(result);
                                        firstname = jobject.getString("firstName");
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
                                        profile_img = jobject.getString("profile_img");

                                        progressDialog.dismiss();

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

                                        String Name, Address;
                                        Name = firstname + " " + "" + middlename + " " + lastname;
                                        Address = addL1 + addL2 + addL3 + ", " + city + ", " + state + ", " + country;


                                        farmer_info_name.setText(Name);
                                        farmer_info_address.setText(Address);
                                        email_pro_farmer.setText("Email : " + email);
                                        mob_pro_farmer.setText("Mob No : " + mobno1);
                                        aadhar_pro_farmer.setText("Aadhar No : " + adharno);
                                        pan_pro_farmer.setText("Pan No : " + panno);
                                        alt_mob_pro_farmer.setText("Mob No(Alt) : " + mobno2);

                                        final String finalMobno = mobno1;
                                        mob_pro_farmer.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                String strnumber = finalMobno;
                                                Uri number = Uri.parse("tel:" + strnumber);
                                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                                intent.setData(number);
                                                startActivity(intent);
                                            }
                                        });
                                    /*nametv.setText(Name);
                                    emailtv.setText(email);
                                    addresstv.setText(Address);
                                    aadharnotv.setText(adharno);
                                    pannotv.setText(panno);
                                    mobnotv.setText(mobno1);
                                    mobnoalttv.setText(mobno2);
                                    landlionenotv.setText(landlineno);
                                    dobtv.setText(dob);
*/
                                        if (profile_img.equals("null")) {
                                            farmer_profile_image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_person_light_grey_24dp));
                                        } else {
                                            Uri uriprofile = Uri.parse(profile_img);
                                            Picasso.with(farmer_profile_image.getContext()).load(uriprofile).into(farmer_profile_image);
                                            myBitmap = ((BitmapDrawable) farmer_profile_image.getDrawable()).getBitmap();
                                        }
                                        // showImage(uriprofile);
                                        //ImagePopup imagePopup = new ImagePopup(context);


                                    }

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
                        if(ct1!=null){
                            params.put(KEY_TOKEN,ct1);
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


    public void showImage(Uri imageUri) {
        Dialog builder = new Dialog(context, android.R.style.Theme_Light);
//        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(this);
        imageView.setImageURI(imageUri);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }


}
