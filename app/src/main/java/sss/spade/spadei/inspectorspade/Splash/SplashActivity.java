package sss.spade.spadei.inspectorspade.Splash;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import sss.spade.spadei.inspectorspade.LandingActivity.LandingActivity;
import sss.spade.spadei.inspectorspade.Login.MainActivity;
import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.SharedPref.SharedPreferencesMethod;


public class SplashActivity extends AppCompatActivity {

    private Thread mThread;
    private boolean isFinish = false;
    int t = 0;

    Context context;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    String languageset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        context = this;
        ImageView imageView = (ImageView) findViewById(R.id.splash_logo);
        Glide.with(this).load(R.drawable.farmer).into(imageView);
        init();
        scheduleAlarm();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    private void init() {
        context = this;
    }


    private Runnable mRunnable = new Runnable() {

        public void run() {
            mHandler.sendEmptyMessage(0);
        }
    };
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0 && (!isFinish)) {
                stop();
            }

            super.handleMessage(msg);
        }
    };



    @Override
    protected void onDestroy() {
        try {
            mThread.interrupt();
            mThread = null;

        } catch (Exception e) {
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                stop();
            }
        }, 3500);
        super.onResume();

    }

    @Override
    protected void onStop() {
        stop();
        super.onStop();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }




    private void stop() {
        isFinish = true;
        if (t == 0) {
            if (SharedPreferencesMethod.getBoolean(context, "Login")) {
                startActivity(new Intent(context, LandingActivity.class));
                finish();
            }
            else{
                startActivity(new Intent(context, MainActivity.class));
            finish();}
        }
    }

        @Override
        public void finish () {
            t = 1;
            super.finish();
        }

        @Override
        public boolean onTouchEvent (MotionEvent event){
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                stop();
            }
            return super.onTouchEvent(event);
        }

    private void StartAnimations() {

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash_logo);
        iv.clearAnimation();
        iv.startAnimation(anim);
    }

    private void scheduleAlarm() {

    }

}

