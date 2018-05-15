package sss.spade.spadei.inspectorspade.Login.NeedHelp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import sss.spade.spadei.inspectorspade.Login.MainActivity;
import sss.spade.spadei.inspectorspade.R;

public class HelpActivity extends AppCompatActivity {

    WebView wv1;
    ProgressDialog pd;
    Toolbar mActionBarToolbar;

    SwipeRefreshLayout mSwipeRefreshLayout;
    Context context;


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
        setContentView(R.layout.activity_help);

        context=this;


        TextView title=(TextView)findViewById(R.id.tittle);
        title.setText("Help");
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);

        //getSupportActionBar().setTitle("My Title");

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        // b1=(Button)findViewById(R.id.button);
        //ed1=(EditText)findViewById(R.id.editText);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        wv1=(WebView)findViewById(R.id.needhelp);
        pd = new ProgressDialog(HelpActivity.this);
        pd.setMessage("Please wait Loading...");
        pd.show();
        wv1.setWebViewClient(new MyBrowser());

        String url = "http://spade.farm/app/index.php/farmApp/need_help_page";
        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setLoadWithOverviewMode(true);
        wv1.getSettings().setUseWideViewPort(true);
        wv1.getSettings().setBuiltInZoomControls(true);

        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv1.loadUrl(url);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent=new Intent(context,HelpActivity.class);
                startActivity(intent);

            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(context,MainActivity.class);
        startActivity(intent);
        finish();
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            if (!pd.isShowing()) {
                pd.show();
            }
            return true;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            System.out.println("on finish");
            if (pd.isShowing()) {
                pd.dismiss();
            }

        }
    }
}
