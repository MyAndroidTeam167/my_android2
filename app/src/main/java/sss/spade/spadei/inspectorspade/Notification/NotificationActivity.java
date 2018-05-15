package sss.spade.spadei.inspectorspade.Notification;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sss.spade.spadei.inspectorspade.DatabaseHandler.Beans.GetProfile;
import sss.spade.spadei.inspectorspade.DatabaseHandler.DatabaseHandler;
import sss.spade.spadei.inspectorspade.LandingActivity.Adapter.RecyclerTouchListener;
import sss.spade.spadei.inspectorspade.LandingActivity.LandingActivity;
import sss.spade.spadei.inspectorspade.Notification.Adapter.ProfileAdapter;
import sss.spade.spadei.inspectorspade.R;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView notirec;
    private ProfileAdapter mAdapter;
    Toolbar mActionBarToolbar;

    String id="0";
    LinearLayoutManager linearLayoutManager;
    Context context;
    // Runnable refresh;
    Handler  mHandler;
    GetProfile getprofile;
    final List<GetProfile> getProfiles = new ArrayList<>();
    int id_count=0;

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
        super.onBackPressed();
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
        setContentView(R.layout.activity_notification);
        context=this;


        TextView title=(TextView)findViewById(R.id.tittle);
        title.setText("Notifications");
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);

        //getSupportActionBar().setTitle("My Title");

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd");
        String formattedDate = df.format(cal.getTime());        //Bitmap icon = null;
        Log.e("Date 7 days earlier",""+formattedDate);
        DatabaseHandler db = new DatabaseHandler(context);
        db.deleteOldNotifications(formattedDate);

        prepareProfileData();

        notirec=(RecyclerView)findViewById(R.id.notirecycl);
        Typeface cFontnormal = Typeface.createFromAsset(getAssets(), "fonts/roboto.regular.ttf");
        Typeface cFontbold = Typeface.createFromAsset(getAssets(), "fonts/Caviar_Dreams_Bold.ttf");
        Typeface cFontitalic = Typeface.createFromAsset(getAssets(), "fonts/CaviarDreams_Italic.ttf");

        mAdapter = new ProfileAdapter(getProfiles,cFontnormal,cFontbold,cFontitalic);
        notirec.setAdapter(mAdapter);
        //mAdapter.notifyDataSetChanged();
        linearLayoutManager=new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //notirec.setHasFixedSize(true);
        notirec.setLayoutManager(linearLayoutManager);
        //notirec.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        //notirec.setItemAnimator(new DefaultItemAnimator());


        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.noti_hide_lay);
       /* if (mAdapter.getItemCount() == 0)
        {
            linearLayout.setVisibility(View.VISIBLE);
            Toast.makeText(context, "New Notifications 0", Toast.LENGTH_SHORT).show();


        }else {

            linearLayout.setVisibility(View.GONE);
            Toast.makeText(context, "New Notifications 1", Toast.LENGTH_SHORT).show();

        }*/

        notirec.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), notirec, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                GetProfile getProfile = getProfiles.get(position);
                Toast.makeText(getApplicationContext(), getProfile.getNoticationdescription() + " is selected!", Toast.LENGTH_SHORT).show();
            }



            @Override
            public void onLongClick(View view, final int position) {

                new AlertDialog.Builder(context)
                        .setMessage("Are you sure you want to remove this Notification?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GetProfile getProfile = getProfiles.get(position);
                                //int id=getProfile.get_id();
                                if(mAdapter.getItemCount()==1){
                                    DatabaseHandler db = new DatabaseHandler(context);
                                    db.deleteContact(new GetProfile(getProfile.get_id()));
                                    getProfiles.remove(position);
                                    mAdapter.notifyDataSetChanged();
                                    Toast.makeText(NotificationActivity.this, "Notification Removed", Toast.LENGTH_SHORT).show();
                                    Intent intent =new Intent(context,NotificationActivity.class);
                                    startActivity(intent);
                                    finish();

                                }else {
                                    DatabaseHandler db = new DatabaseHandler(context);
                                    db.deleteContact(new GetProfile(getProfile.get_id()));
                                    getProfiles.remove(position);
                                    mAdapter.notifyDataSetChanged();
                                    Toast.makeText(NotificationActivity.this, "Notification Removed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }

        }));
        // this.mHandler = new Handler();
        // m_Runnable.run();



        //   prepareProfileData();



        //String[] notification=new String[contacts.size()];
        //String[] id=new String[contacts.size()];

        /*for (GetProfile cn : contacts) {
            String log = "Id: "+cn.get_id()+" ,Notification: " + cn.getNotification();

            id[cn.get_id()]= String.valueOf(cn.get_id());
            notification[cn.get_id()]=cn.getNotification();
            // Writing Contacts to log
            Log.e("Notification in db: ", log);
        }
*/



    }


    private void prepareProfileData() {

        DatabaseHandler db = new DatabaseHandler(this);

        List<GetProfile> contacts = db.getAllNotifications();


        for (GetProfile cn : contacts) {
            String log = "Id: "+cn.get_id()+" ,Notification: " + cn.getNotification();
            // Writing Contacts to log

            getprofile=new GetProfile(cn.get_id(),cn.getNotification(),cn.getNoticationdescription(),cn.getNotidate());
            //getprofile.setYear(123);
            getProfiles.add(getprofile);
            Log.e("Notification in db: ", log);
            id_count=cn.get_id();
        }
        //GetProfile getprofile=new GetProfile("New Activity","new Description","2015");
        //getProfiles.add(getprofile);

        //getprofile=new GetProfile("New Activity 2","new Description 2","2016");
        //getProfiles.add(getprofile);

        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.noti_hide_lay);

        if(id_count==0){
            linearLayout.setVisibility(View.VISIBLE);
        }else{
            linearLayout.setVisibility(View.GONE);

        }




    }


  /*  private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
           // Toast.makeText(NotificationActivity.this,"in runnable",Toast.LENGTH_SHORT).show();
            NotificationActivity.this.mHandler.postDelayed(m_Runnable,500000);

            DatabaseHandler db = new DatabaseHandler(context);

            List<GetProfile> contacts = db.getAllNotifications();


            for (GetProfile cn : contacts) {
                String log = "Id: "+cn.get_id()+" ,Notification: " + cn.getNotification()+" Notification Description"+ cn.getNoticationdescription()+"Date :"+cn.getNotidate();
                id=String.valueOf(cn.get_id());
                // Writing Contacts to log

                getprofile=new GetProfile(Integer.valueOf(id),cn.getNotification(),cn.getNoticationdescription(),cn.getNotidate());
                //getprofile.setYear(123);
                getProfiles.add(getprofile);
                Log.e("Notification in db: ", log);
            }
            LinearLayout linearLayout=(LinearLayout)findViewById(R.id.noti_hide_lay);

            if(id=="0"){
                linearLayout.setVisibility(View.VISIBLE);

            }else{
                linearLayout.setVisibility(View.GONE);

            }

            //GetProfile getprofile=new GetProfile("New Activity","new Description","2015");
            //getProfiles.add(getprofile);

            //getprofile=new GetProfile("New Activity 2","new Description 2","2016");
            //getProfiles.add(getprofile);

        }

    };*/
}
