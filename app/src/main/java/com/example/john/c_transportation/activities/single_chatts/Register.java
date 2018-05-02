package com.example.john.c_transportation.activities.single_chatts;

/**
 * Created by john on 3/23/18.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.john.c_transportation.R;
import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.john.c_transportation.utils.Constants.config.BODY;
import static com.example.john.c_transportation.utils.Constants.config.CONDUCTOR_TYPE;
import static com.example.john.c_transportation.utils.Constants.config.DATETIME;
import static com.example.john.c_transportation.utils.Constants.config.FIREBASE_URL;
import static com.example.john.c_transportation.utils.Constants.config.FIRST_NAME;
import static com.example.john.c_transportation.utils.Constants.config.LAST_NAME;
import static com.example.john.c_transportation.utils.Constants.config.ONLINE;
import static com.example.john.c_transportation.utils.Constants.config.PASSWORD;
import static com.example.john.c_transportation.utils.Constants.config.STUDENT_TYPE;
import static com.example.john.c_transportation.utils.Constants.config.USERNAME;
import static com.example.john.c_transportation.utils.Constants.config.USERS;
import static com.example.john.c_transportation.utils.Constants.config.USER_TYPE;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText input_username, input_password,input_fname,input_lname;
    private Button btn_submit;
    private String username, password, fname, lname, user_type;
    private Context context = this;
    private Spinner type_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        input_username = (EditText)findViewById(R.id.input_username);
        input_password = (EditText)findViewById(R.id.input_password);
        input_fname = (EditText)findViewById(R.id.input_fname);
        input_lname = (EditText)findViewById(R.id.input_lname);
        type_spinner = (Spinner) findViewById(R.id.type_spinner);
        type_spinner.setOnItemSelectedListener(this);
        btn_submit = (Button)findViewById(R.id.btn_submit);
        Firebase.setAndroidContext(this);

        setSpinner();
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = input_username.getText().toString();
                password = input_password.getText().toString();
                fname = input_fname.getText().toString();
                lname = input_lname.getText().toString();
                user_type = type_spinner.getSelectedItem().toString().trim();

                if(username.equals("")){
                    input_password.setError("can't be blank");
                }
                else if(password.equals("")){
                    input_password.setError("can't be blank");
                }
                else if(!username.matches("[A-Za-z0-9]+")){
                    input_username.setError("only alphabet or number allowed");
                }
                else if(username.length()<5){
                    input_username.setError("at least 5 characters long");
                }
                else if(password.length()<5){
                    input_password.setError("at least 5 characters long");
                }
                else if(fname.equals("")){
                    input_fname.setError("can't be blank");
                }
                else if(lname.equals("")){
                    input_lname.setError("can't be blank");
                }

                else {
                    final ProgressDialog pd = new ProgressDialog(context);
                    pd.setMessage("Loading...");
                    pd.show();

                    String url = FIREBASE_URL+USERS+".json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            Firebase reference = new Firebase(FIREBASE_URL+USERS);

                            if(s.equals("null")) {
                                reference.child(username);

                                Map<String, String> map = new HashMap<String, String>();
                                map.put(FIRST_NAME, fname);
                                map.put(LAST_NAME, lname);
                                map.put(USERNAME, username);
                                map.put(PASSWORD, password);
                                map.put(USER_TYPE, user_type);
                                map.put(ONLINE, "online");
                                //map.put("photoUrl", "");
                                reference.child(username).push().setValue(map);

                                Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                            }
                            else {
                                try {
                                    JSONObject obj = new JSONObject(s);
                                    if (!obj.has(username)) {

                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put(FIRST_NAME, fname);
                                        map.put(LAST_NAME, lname);
                                        map.put(USERNAME, username);
                                        map.put(PASSWORD, password);
                                        map.put(USER_TYPE, user_type);
                                        map.put(ONLINE, "online");

                                        reference.child(username).push().setValue(map);
                                        Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Toast.makeText(Register.this, "username already exists", Toast.LENGTH_LONG).show();
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
                            System.out.println("" + volleyError );
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                    rQueue.add(request);
                }
            }
        });

    }

    private void setSpinner(){
        List<String> list = new ArrayList<>();
        list.add(STUDENT_TYPE);
        list.add(CONDUCTOR_TYPE);
        try{

            ArrayAdapter<String> dataAdapter8 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter8.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            type_spinner.setAdapter(dataAdapter8);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
