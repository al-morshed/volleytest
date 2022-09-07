package com.example.volleytest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
//    String url="https://run.mocky.io/v3/cb480f94-aaf8-4256-abde-e2689a3f54d3";//by https://designer.mocky.io/design
    String url ="http://210.93.5.253:8000/ar/users/api/";
    Button btnRequest;
    Button btnRequestArray;
    private TextView mTextViewResult;
    private RequestQueue mRequestQueue;//=MySingleTon.getInstance(this.getApplicationContext()).getRequestQueue();
    private JsonObjectRequest mStringRequest;
    public int versionCode = BuildConfig.VERSION_CODE;
    public String versionName = BuildConfig.VERSION_NAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextViewResult = findViewById(R.id.text_view_result);
        btnRequest = (Button) findViewById(R.id.buttonRequest);
        btnRequestArray=(Button) findViewById(R.id.buttonRequestArray);
        // for cache storeg size
        Cache cache= new DiskBasedCache(getCacheDir(),1024*1042);
        Network network=new BasicNetwork(new HurlStack());
        Toast.makeText(getApplicationContext(),
                "int2 version:- "+versionCode+" \n Version :- "+versionName,
                Toast.LENGTH_SHORT).show();
        btnRequest.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v){
//                      sendAndRequestResponse("5732051668AA");
                      sendAndRequestResponse("573205163368AA0");
                  }
              });
        btnRequestArray.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v){
                      sendAndRequestResponseByArray();
                  }
              });
    }

    private void sendAndRequestResponseByArray() {
        String url="http://210.93.5.253:8000/ar/users/api/list";
//        mRequestQueue = Volley.newRequestQueue(this);
        mTextViewResult.setText("");
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String phone = jsonObject.getString("phone");
                                    int log;
                                    if(jsonObject.getString("log")=="null")
                                        log = 0;
                                    else log=jsonObject.getInt("log");
                                    String active = jsonObject.getString("active");
                                    Log.i(" ","Don Get from server");
                                    mTextViewResult.append(phone + ", " + String.valueOf(log) + ", " + active + "\n\n");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("TAG","Error on try catch :" + e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.e("TAG","Error on try catch :" + error.getMessage());
                }
            });
        mRequestQueue = MySingleTon.getInstance(this.getApplicationContext()).getRequestQueue();
        MySingleTon.getInstance(this).addToRequestQueue(request);
//        mRequestQueue.add(request);
        }

    private void sendAndRequestResponse(String id) {
//        String url="http://210.93.5.253:8000/ar/users/api/v2/5732051668AA";
        String url="http://210.93.5.253:8000/ar/users/update/v3/573205163368AA0";
        HashMap<String,String> stringHashMap=new HashMap<String,String>();
        stringHashMap.put("LicenseKey","arafatSoft API");
        stringHashMap.put("OpenReportCount","454");
        stringHashMap.put("address","765.215.52.0");
        //RequestQueue initialized
//        mRequestQueue = Volley.newRequestQueue(this);
        //String Request initialized
        mStringRequest = new JsonObjectRequest(Request.Method.PATCH, url,new JSONObject(stringHashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(getApplicationContext(),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen
                mTextViewResult.setText("Success");
                mRequestQueue.stop();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("TAG","Error :" + error.toString());
                error.printStackTrace();
            }
        });
//        mRequestQueue.add(mStringRequest);
        mRequestQueue = MySingleTon.getInstance(this.getApplicationContext()).getRequestQueue();
        MySingleTon.getInstance(this).addToRequestQueue(mStringRequest);
    }
}