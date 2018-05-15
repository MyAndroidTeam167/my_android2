package sss.spade.spadei.inspectorspade.Login.ForgetPassword;

import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.TextView;

import sss.spade.spadei.inspectorspade.DataHandler.DataHandler;
import sss.spade.spadei.inspectorspade.LandingActivity.LandingActivity;
import sss.spade.spadei.inspectorspade.Login.ForgetPassword.CnfrmPass.CnfrmPassActivity;
import sss.spade.spadei.inspectorspade.Login.MainActivity;
import sss.spade.spadei.inspectorspade.R;

import static sss.spade.spadei.inspectorspade.Validations.Validations.emailValidator;
import static sss.spade.spadei.inspectorspade.Validations.Validations.isValidMobile;


public class FrgtPassActivity extends AppCompatActivity {

    EditText frgtet;
    Toolbar mActionBarToolbar;

    Button submitfrgt,cancelfrgt;
    Context context;
    String forgetpassmailormobile;
    String from_activity;


    @Override
    public void onBackPressed() {
        if(from_activity!=null){
            if(from_activity.equals("from_setting") )
            {
                Intent intent=new Intent(context,LandingActivity.class);
                startActivity(intent);
                finish();
            }}
        else{
            Intent intent=new Intent(context,MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(from_activity!=null) {

            if (from_activity.equals("from_setting")) {
                Intent intent = new Intent(context, LandingActivity.class);
                startActivity(intent);
                finish();
            }
        }
        else{
            Intent intent=new Intent(context,MainActivity.class);
            startActivity(intent);
            finish();
        }
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
        setContentView(R.layout.activity_frgt_pass);
        frgtet=(EditText)findViewById(R.id.forgetet);
        context=this;

        Intent intent = getIntent();
        from_activity=intent.getStringExtra("chanage_pass");

        submitfrgt=(Button)findViewById(R.id.submitfrgt);
        cancelfrgt=(Button)findViewById(R.id.cancelfrgt);



        TextView title=(TextView)findViewById(R.id.tittle);
        if(from_activity!=null) {
            if (from_activity.equals("from_setting")) {
                title.setText("Change Password");
            }
        }else{
            title.setText("Forgot Password");
        }
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);

        //getSupportActionBar().setTitle("My Title");

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }



        submitfrgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!emailValidator(frgtet.getText().toString().trim())) && (!isValidMobile(frgtet.getText().toString().trim())))
                {
                    frgtet.setError(getString(R.string.invalid_email));
                }
                else{
                    forgetpassmailormobile= frgtet.getText().toString();
                    DataHandler.newInstance().setMoborEmailonforget(forgetpassmailormobile);
                    Intent intent=new Intent(context,CnfrmPassActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        cancelfrgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(from_activity!=null) {

                    if (from_activity.equals("from_setting")) {
                        Intent intent = new Intent(context, LandingActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else{
                    Intent intent=new Intent(context,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

}

