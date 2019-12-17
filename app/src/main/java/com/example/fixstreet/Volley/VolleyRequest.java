package com.example.fixstreet.Volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.VolleyLog.TAG;

public class VolleyRequest {
    public static void PostRequest(Context context, String uri, JSONObject data, final VolleyPostCallBack callBack) {
        Log.d("s", "sendData: started ");

        JsonObjectRequest request3 = new JsonObjectRequest(Request.Method.POST, uri,data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: send Successfully" );
                try {
                    callBack.OnSuccess(response.getJSONObject("status"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callBack.OnFailure(error.getMessage());
                Log.d("failed", "onResponsefail: " + error);
                // callback.onError("There is connection Error" + error);
            }

        });

        request3.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        requestQueue.add(request3);

    }
    public static void GetRequest(Context context, String uri, final VolleyPostCallBack callBack) {
        Log.d("s", "sendData: started ");
        Log.d("s", "sendData: started  url is jv jb "+uri);

        JsonObjectRequest request3 = new JsonObjectRequest(Request.Method.GET, uri,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: send Successfully" );
                Log.e(TAG, "onResponse data is : "+response );

                    callBack.OnSuccess(response);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callBack.OnFailure(error.getMessage());
                Log.d("failed", "onResponsefail: " + error);
                // callback.onError("There is connection Error" + error);
            }

        });

        request3.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        requestQueue.add(request3);

    }
}
