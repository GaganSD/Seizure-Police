package com.hackic.seizure;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpHandler {

  RequestQueue queue;

  public HttpHandler(Context context) {
    this.queue = Volley.newRequestQueue(context);
  }



  public void sendPost(final String token, final String id) {

    Log.d("HTTPPost", "sendPost: Preparing to send");

    JSONObject json = new JSONObject();
    try {
      json.put("token",token);
      json.put("id",id);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    String url = "http://146.169.157.139/update_with_id";

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json,
            new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject response) {
                Log.d("sendpostresponse", "onResponse: successful");
              }
            }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Log.d("sendpostresponse", "onErrorResponse: failure" + error.getMessage());
      }
    });
    Log.d("queue", "added to queue");
    this.queue.add(jsonObjectRequest);
  }

}
