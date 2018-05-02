package com.example.john.c_transportation.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.john.c_transportation.R;
import com.example.john.c_transportation.activities.firebase.DeviceToken;
import com.example.john.c_transportation.activities.firebase.SaveTokens;
import com.example.john.c_transportation.activities.firebase.SendTokens;
import com.example.john.c_transportation.activities.single_chatts.ListConductors;
import com.example.john.c_transportation.activities.single_chatts.Register;
import com.example.john.c_transportation.activities.single_chatts.UserDetails;
import com.example.john.c_transportation.core.Phone;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import static com.example.john.c_transportation.utils.Constants.config.CONDUCTOR_TYPE;
import static com.example.john.c_transportation.utils.Constants.config.FIREBASE_URL;
import static com.example.john.c_transportation.utils.Constants.config.PASSWORD;
import static com.example.john.c_transportation.utils.Constants.config.USERS;
import static com.example.john.c_transportation.utils.Constants.config.USER_TYPE;

/**
 * Created by john on 4/21/18.
 */

public class LoginActivity extends AppCompatActivity {
    private EditText input_username, input_password;
    private Button signin_btn;
    private TextView login_text;
    private Context context = this;
    private String username, password;
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        input_username = (EditText) findViewById(R.id.input_username);
        input_password = (EditText) findViewById(R.id.input_password);
        signin_btn = (Button) findViewById(R.id.signin_btn);
        login_text = (TextView) findViewById(R.id.login_text);
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,Register.class));
            }
        });

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Firebase.setAndroidContext(this);
        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = input_username.getText().toString();
                password = input_password.getText().toString();

                if(username.equals("")){
                    input_username.setError("can't be blank");
                }
                else if(password.equals("")){
                    input_password.setError("can't be blank");
                }
                else{
                    String url = FIREBASE_URL+USERS+".json";
                    final ProgressDialog pd = new ProgressDialog(context);
                    pd.setMessage("Loading...");
                    pd.show();

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            if(s.equals("null")){
                                Toast.makeText(context, "user not found", Toast.LENGTH_LONG).show();
                            }
                            else{
                                try {
                                    final JSONObject obj = new JSONObject(s);

                                    Log.e(TAG, obj+"");
                                    if(!obj.has(username)){
                                        Toast.makeText(context, "user not found", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        String url = FIREBASE_URL+USERS+"/"+username+".json";
                                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                                            @Override
                                            public void onResponse(String s) {
                                                doOnSuccess(s);
                                            }
                                        },new Response.ErrorListener(){
                                            @Override
                                            public void onErrorResponse(VolleyError volleyError) {
                                                System.out.println("" + volleyError);
                                            }
                                        });

                                        RequestQueue rQueue = Volley.newRequestQueue(context);
                                        rQueue.add(request);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pd.dismiss();
                        }
                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(context);
                    rQueue.add(request);
                }

            }
        });


    }

    public void sendToken(String username){
        try {
            String token = new DeviceToken(context).token();
            String imei = Phone.getIMEI(context);
            if (token != null) {
                //new SendTokens(context).sendTokenToServer(token, imei);
                SaveTokens.saveTokens(context,username,token);
            }
            Log.e(TAG, "Token::: "+token);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void doOnSuccess(String s) {
        try {
            JSONObject obj = new JSONObject(s);
            Iterator i = obj.keys();
            String key = "";

            while (i.hasNext()) {
                key = i.next().toString();
                if(obj.getJSONObject(key).getString(PASSWORD).equals(password)){
                    UserDetails.username = username;
                    UserDetails.password = password;
                    sendToken(UserDetails.username);
                    String user_type = obj.getJSONObject(key).getString(USER_TYPE);
                    if (user_type.equals(CONDUCTOR_TYPE)){
                        startActivity(new Intent(context, MainActivity_C.class));
                    }else {
                        startActivity(new Intent(context, ListNotifications.class));
                    }
                }else {
                    Toast.makeText(context, "incorrect password", Toast.LENGTH_LONG).show();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
