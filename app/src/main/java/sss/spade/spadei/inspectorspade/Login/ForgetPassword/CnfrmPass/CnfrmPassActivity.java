package sss.spade.spadei.inspectorspade.Login.ForgetPassword.CnfrmPass;

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
import android.widget.TextView;


import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import sss.spade.spadei.inspectorspade.DataHandler.DataHandler;
import sss.spade.spadei.inspectorspade.Login.MainActivity;
import sss.spade.spadei.inspectorspade.R;

public class CnfrmPassActivity extends AppCompatActivity {

    TextView wel;
    Button cnfsubmit;
    Context context;
    ShowHidePasswordEditText confrmnewpass ,cnfcnfpass;
    String newpass,cnfrmpass;
    Toolbar mActionBarToolbar;




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent=new Intent(context,MainActivity.class);
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
        setContentView(R.layout.activity_cnfrm_pass);
        context=this;
        cnfsubmit=(Button)findViewById(R.id.cnfsubmit);
        confrmnewpass=(ShowHidePasswordEditText)findViewById(R.id.cnfnewpass);
        cnfcnfpass=(ShowHidePasswordEditText)findViewById(R.id.cnfcnfpass);


        TextView title=(TextView)findViewById(R.id.tittle);
        title.setText("Password Reset");
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        cnfsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int length = confrmnewpass.getText().toString().trim().length();
                int length2 = cnfcnfpass.getText().toString().trim().length();
                newpass=confrmnewpass.getText().toString();
                cnfrmpass=cnfcnfpass.getText().toString();
                if (!(length > 7 && length < 33)) {
                    confrmnewpass.setError(getString(R.string.password_er));
                    //        Toast.makeText(MainActivity.this, length, Toast.LENGTH_SHORT).show();
                } else if (!(length2 > 7 && length2 < 33)) {
                    cnfcnfpass.setError(getString(R.string.password_er));
                    //        Toast.makeText(MainActivity.this, length, Toast.LENGTH_SHORT).show();
                } else if (!newpass.equals(cnfrmpass)) {

                    cnfcnfpass.setError(getString(R.string.samepassword));
                    confrmnewpass.setError(getString(R.string.samepassword));
                }
                else{
                    DataHandler.newInstance().setPasswordonfrgt(newpass);
                    Intent intent = new Intent(context, OtpforpassActivity.class);
                    startActivity(intent);
                    finish();}
            }
        });

    }


}
