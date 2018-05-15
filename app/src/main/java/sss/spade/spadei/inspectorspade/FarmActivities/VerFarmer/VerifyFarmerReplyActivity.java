package sss.spade.spadei.inspectorspade.FarmActivities.VerFarmer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sss.spade.spadei.inspectorspade.FarmActivities.Adapter.CustomAdapter;
import sss.spade.spadei.inspectorspade.FarmActivities.GetterSetter.GetSet;
import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.SharedPref.SharedPreferencesMethod;

public class VerifyFarmerReplyActivity extends AppCompatActivity {

    Context context;
    List<GetSet> listData = new ArrayList<>();
    GetSet dataObj ;
    Button commentButton;
    CustomAdapter adapter;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    EditText newComment;
    private ListView listview;
    String fetch_id;
    EditText etcomment;
    ImageView btcommen;
    final String URL = "http://spade.farm/app/index.php/farmApp/send_chat_messages";
    String SAVE_INSPECTOR_RESPONSE_URL = "http://spade.farm/app/index.php/inspectorApp/save_inspector_reply";
    final String INSPECTOR_REPLY = "txtComment";
    final String FARM_DWORK_NUM = "farm_dwork_num";
    String Inspector_comment;
    final String KEY_TOKEN="token2";
    String ct1="";


    private void releaseMediaPlayer() {
        for(int i=0;i<adapter.listData.size();i++) {

           /* if (adapter.player[i] != null) {
                if (adapter.player[i].isPlaying()) {
                    adapter.player[i].stop();
                }*/
            GetSet getSet=adapter.listData.get(i);
          if(!getSet.getAudioReply().equals("") && !getSet.getAudioReply().equals("null")){
              adapter.player[i].release();

          }
            //adapter.player[i] = null;
/*
            }
*/
        }
    }

    @Override
    public void onBackPressed() {
        releaseMediaPlayer();
        super.onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_farmer_reply);

        context = this;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        listview = (ListView)findViewById(R.id.lvCommentsVerfarmer);
        Bundle extras = getIntent().getExtras();
        fetch_id = extras.getString("fetchId");
        ct1= SharedPreferencesMethod.getString(context,"cctt");
        callVolley();

         etcomment=(EditText)findViewById(R.id.chat_content);
         btcommen=(ImageView)findViewById(R.id.post_button);

        btcommen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute();

                //submit();
                /*Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                final String formattedDate = df.format(c);
                dataObj = new GetSet();
                dataObj.setMessage(Inspector_comment);
                dataObj.setDate(formattedDate);
                dataObj.setByFarmer("N");
                dataObj.setByAdmin("N");
                dataObj.setByInspector("Y");
                listData.add(dataObj);
               *//* adapter = new CustomAdapter(context, listData);
                listview.setAdapter(adapter);*//*
               scrollMyListViewToBottom();
                adapter.notifyDataSetChanged();
                etcomment.setText("");*/
               // progressDialog.dismiss();
            }
        });

    }

    private void submit() {



    }

    public void callVolley(){
        progressDialog.show();
        Log.d("checkArray","Reached callVolley");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        JSONArray jsonArray;
                        try {
                            progressDialog.dismiss();
                            jsonArray = new JSONArray(response);
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                String text_reply=jsonObject.getString("text_reply");
                                String date=jsonObject.getString("date");
                                String by_farmer=jsonObject.getString("by_farmer");
                                String by_admin = jsonObject.getString("by_admin");
                                String by_inspector=jsonObject.getString("by_inspector");
                                String msg_type=jsonObject.getString("msgType");
                                String audio_reply=jsonObject.getString("audio_reply");

                                dataObj = new GetSet();
                                dataObj.setMessage(text_reply);
                                dataObj.setDate(date);
                                dataObj.setByFarmer(by_farmer);
                                dataObj.setByAdmin(by_admin);
                                dataObj.setByInspector(by_inspector);
                                dataObj.setMsgType(msg_type);
                                dataObj.setAudioReply(audio_reply);

                                listData.add(dataObj);
                            }
                            adapter = new CustomAdapter(context, listData);
                            listview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                          /*  listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, final View view,
                                                        int position, long id) {
                                    final String item = (String) parent.getItemAtPosition(position);
//                                    Toast.makeText(context,item,LENGTH_SHORT).show();
                                    *//*view.animate().setDuration(2000).alpha(0)
                                            .withEndAction(new Runnable() {
                                                @Override
                                                public void run() {
                                                    list.remove(item);
                                                    adapter.notifyDataSetChanged();
                                                    view.setAlpha(1);
                                                }
                                            });*//*
                                }
                            });*/
                            Log.e("checkArray :","this is resp: "+response);
                            Log.e("checkArray:", String.valueOf(listData.size()));
//                            Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                        }catch (Exception e) {
                            e.printStackTrace();
                            adapter = new CustomAdapter(context, listData);
                            listview.setAdapter(adapter);
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.e("checkArray",error.toString());
//                        Toast.makeText(context,"Failed!! Image too large",Toast.LENGTH_LONG).show();
                        NetworkResponse networkResponse = error.networkResponse;
                        if(networkResponse!=null) {
                            Log.e("checkArray", String.valueOf(networkResponse.statusCode));
                        }
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Log.e("checkArray","timeout error");

                        } else if (error instanceof AuthFailureError) {
                            Log.e("checkArray","authfailure error");

                        } else if (error instanceof ServerError) {
                            Log.e("checkArray","Server error");

                        } else if (error instanceof NetworkError) {
                            //TODO
                            Log.e("checkArray","network error");

                        } else if (error instanceof ParseError) {
                            Log.e("checkArray","parse error");
                            //TODO
                        }
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                String ID = "id";
                params.put(ID,fetch_id);
                params.put(KEY_TOKEN,ct1);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private void scrollMyListViewToBottom() {
        listview.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listview.setSelection(adapter.getCount() - 1);
            }
        });
    }

    public boolean isResponseFilled(String text){
        boolean flag = true;
        if(text.isEmpty()){
            flag=false;
        }
        if(text.equals("null")){
            flag=false;
        }
        if(text.equals(" ")){
            flag=false;
        }
        return flag;
    }


    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {
        public AsyncTaskRunner() {
            super();
        }

        @Override
        protected String doInBackground(String... params) {
            Inspector_comment=etcomment.getText().toString().trim();
            if(!isResponseFilled(Inspector_comment)){

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        progressDialog.dismiss();
                        etcomment.setError("Enter Reponse");
                        // Stuff that updates the UI

                    }
                });

            }
            else {
                StringRequest stringRequestNew = new StringRequest(Request.Method.POST, SAVE_INSPECTOR_RESPONSE_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("check","This is resp"+response);
                                if(response.equals("true")) {
                                    Toast.makeText(context, "Action Submitted", Toast.LENGTH_SHORT).show();
                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                    final String formattedDate = df.format(c);
                                    dataObj = new GetSet();
                                    dataObj.setMessage(Inspector_comment);
                                    dataObj.setDate(formattedDate);
                                    dataObj.setByFarmer("N");
                                    dataObj.setByAdmin("N");
                                    dataObj.setByInspector("Y");
                                    dataObj.setMsgType("1");
                                    dataObj.setAudioReply("");
                                    listData.add(dataObj);
               /* adapter = new CustomAdapter(context, listData);
                listview.setAdapter(adapter);*/

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            scrollMyListViewToBottom();
                                            adapter.notifyDataSetChanged();
                                            etcomment.setText("");
                                            progressDialog.dismiss();
                                            // Stuff that updates the UI

                                        }
                                    });

                                    //Intent intent = new Intent(context, TaskListActivity.class);
                                    //startActivity(intent);
                                    //finish();
                                }else {
                                    Toast.makeText(VerifyFarmerReplyActivity.this, "Unable to Submit Response at the moment Please try again later", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("checkArray",error.toString());
                                            /*NetworkResponse networkResponse = error.networkResponse;
                                            Log.e("checkArray",String.valueOf(networkResponse.statusCode));*/
                                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                    final int checkArray = Log.e("checkArray", "timeout error");

                                } else if (error instanceof AuthFailureError) {
                                    Log.e("checkArray","authfailure error");

                                } else if (error instanceof ServerError) {
                                    Log.e("checkArray","Server error");

                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    Log.e("checkArray","network error");

                                } else if (error instanceof ParseError) {
                                    Log.e("checkArray","parse error");
                                    //TODO
                                }
                            }
                        }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();

                        Log.e("check","Reached Params");
                        if (Inspector_comment != null) {
                            params.put(INSPECTOR_REPLY, Inspector_comment);
                        }
                        if (fetch_id != null) {
                            params.put(FARM_DWORK_NUM, fetch_id);
                        }
                        if(ct1!=null){
                            params.put(KEY_TOKEN,ct1);
                        }
                        return params;
                    }
                };

                RequestQueue requestQueueNew = Volley.newRequestQueue(context);
                requestQueueNew.add(stringRequestNew);

                            /*Toast.makeText(context, "Action Submitted", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(context,LandingActivity.class);
                            startActivity(intent);*/

            }



            return null;
        }


        @Override
        protected void onPreExecute() {
            progressDialog =new ProgressDialog(context);
            progressDialog.setMessage("Loading....");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {


        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

}
