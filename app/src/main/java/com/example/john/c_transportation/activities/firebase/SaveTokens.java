package com.example.john.c_transportation.activities.firebase;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.john.c_transportation.activities.single_chatts.Register;
import com.example.john.c_transportation.core.Phone;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.john.c_transportation.utils.Constants.config.DEVICES;
import static com.example.john.c_transportation.utils.Constants.config.FIREBASE_URL;
import static com.example.john.c_transportation.utils.Constants.config.IMEI;
import static com.example.john.c_transportation.utils.Constants.config.KEY_TOKEN;
import static com.example.john.c_transportation.utils.Constants.config.USERNAME;

/**
 * Created by john on 4/25/18.
 */

public class SaveTokens {
    public static void saveTokens(Context context, final String username, final String tokens){
        Firebase.setAndroidContext(context);
        String url = FIREBASE_URL+DEVICES+".json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                Firebase reference = new Firebase(FIREBASE_URL+DEVICES);

                if(s.equals("null")) {
                    reference.child(KEY_TOKEN);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(USERNAME, username);
                    map.put(KEY_TOKEN, tokens);
                    reference.child(tokens).push().setValue(map);
                }
                else {
                    try {
                        JSONObject obj = new JSONObject(s);
                        if (!obj.has(tokens)) {

                            Map<String, String> map = new HashMap<String, String>();
                            map.put(USERNAME, username);
                            map.put(KEY_TOKEN, tokens);
                            reference.child(tokens).push().setValue(map);
                        } else {
                            //Toast.makeText(Register.this, "username already exists", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError );
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }
}
