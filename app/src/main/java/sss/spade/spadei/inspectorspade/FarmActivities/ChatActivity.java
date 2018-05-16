/*
package sss.spade.spadei.inspectorspade.FarmActivities;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sss.spade.spadei.inspectorspade.FarmActivities.Adapter.CustomAdapter;
import sss.spade.spadei.inspectorspade.FarmActivities.GetterSetter.GetSet;
import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.SharedPref.SharedPreferencesMethod;

public class ChatActivity extends AppCompatActivity {
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
    ImageView imageView;
    final String KEY_TOKEN="token2";
    String ct1="";
    final String URL = "http://spade.farm/app/index.php/farmApp/send_chat_messages";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        */
/*super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] adobe_products = getResources().getStringArray(R.array.adobe_products);

        // Binding resources Array to ListAdapter
        this.setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.label, adobe_products));*//*

        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        context = this;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        listview = (ListView)findViewById(R.id.lvComments);
        ct1= SharedPreferencesMethod.getString(context,"cctt");

       */
/* commentButton=(Button)findViewById(R.id.commentButton);
        newComment = (EditText)findViewById(R.id.newComment);*//*

        */
/*final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }*//*

        Bundle extras = getIntent().getExtras();
        fetch_id = extras.getString("fetchId");

      */
/*  etcomment=(EditText)findViewById(R.id.chat_content);
        etcomment.setVisibility(View.GONE);
        imageView=(ImageView)findViewById(R.id.post_button);
        imageView.setVisibility(View.GONE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;
        listview.setLayoutParams(params);
      *//*

      callVolley();


//        commentButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String comment = newComment.getText().toString();
//
//            }
//        });
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
                            Log.e("TAG",response);
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

                                //Toast.makeText(context, msg_type+"mdbhafj", Toast.LENGTH_SHORT).show();

                                Log.e("MSG TYPE And AUDIO REPLY : ",msg_type+"  "+audio_reply);

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
                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, final View view,
                                                        int position, long id) {
                                    final String item = (String) parent.getItemAtPosition(position);
//                                    Toast.makeText(context,item,LENGTH_SHORT).show();
                                    */
/*view.animate().setDuration(2000).alpha(0)
                                            .withEndAction(new Runnable() {
                                                @Override
                                                public void run() {
                                                    list.remove(item);
                                                    adapter.notifyDataSetChanged();
                                                    view.setAlpha(1);
                                                }
                                            });*//*

                                }
                            });
                            Log.e("checkArray :","this is resp: "+response);
                            Log.e("checkArray:", String.valueOf(listData.size()));
//                            Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                        }catch (Exception e) {
                            e.printStackTrace();
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
                        Log.e("checkArray", String.valueOf(networkResponse.statusCode));
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
                if(ct1!=null){
                    params.put(KEY_TOKEN,ct1);
                }
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}*/
