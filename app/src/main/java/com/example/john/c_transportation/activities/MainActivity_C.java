package com.example.john.c_transportation.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.example.john.c_transportation.activities.single_chatts.AddNotification;
import com.example.john.c_transportation.activities.single_chatts.Chatt_Conductor;
import com.example.john.c_transportation.activities.single_chatts.Chatt_Students;
import com.example.john.c_transportation.activities.single_chatts.Register;
import com.example.john.c_transportation.activities.single_chatts.UserDetails;
import com.example.john.c_transportation.adapters.Other_Adapters;
import com.example.john.c_transportation.core.Phone;
import com.example.john.c_transportation.utils.Directory;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.example.john.c_transportation.utils.Constants.config.BODY;
import static com.example.john.c_transportation.utils.Constants.config.CONDUCTOR_TYPE;
import static com.example.john.c_transportation.utils.Constants.config.DATETIME;
import static com.example.john.c_transportation.utils.Constants.config.FIREBASE_URL;
import static com.example.john.c_transportation.utils.Constants.config.FIRST_NAME;
import static com.example.john.c_transportation.utils.Constants.config.LAST_NAME;
import static com.example.john.c_transportation.utils.Constants.config.MESSAGES;
import static com.example.john.c_transportation.utils.Constants.config.ONLINE;
import static com.example.john.c_transportation.utils.Constants.config.PASSWORD;
import static com.example.john.c_transportation.utils.Constants.config.SHOW_MODE_HIDE;
import static com.example.john.c_transportation.utils.Constants.config.STAGES;
import static com.example.john.c_transportation.utils.Constants.config.STAGES_DECRIPTION;
import static com.example.john.c_transportation.utils.Constants.config.STAGES_NAME;
import static com.example.john.c_transportation.utils.Constants.config.STAGES_NUMBER;
import static com.example.john.c_transportation.utils.Constants.config.STAGES_STATUS;
import static com.example.john.c_transportation.utils.Constants.config.USERNAME;
import static com.example.john.c_transportation.utils.Constants.config.USERS;
import static com.example.john.c_transportation.utils.Constants.config.USER_TYPE;

/**
 * Created by john on 1/3/18.
 */

public class MainActivity_C extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    private Context context = this;
    DrawerLayout drawer;
    private ListView listView;
    private static final String TAG = "MainActivity_C";
    private List<String> name_list = new ArrayList<>();
    private List<String> desc_list = new ArrayList<>();
    private List<Integer> number_list = new ArrayList<>();
    private List<String> ids_list = new ArrayList<>();
    private ProgressDialog pd;
    private TextView noText;
    private int totalUsers = 0;

    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        try{

            listView = (ListView) findViewById(R.id.listView);
            noText = (TextView) findViewById(R.id.noText);
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            //staticPaymets();
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            //fab.setVisibility(View.GONE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserDetails.stages = "";
                   startActivity(new Intent(context,Chatt_Conductor.class));
                }
            });
            //fab.setVisibility(View.GONE);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (drawer.isDrawerOpen(Gravity.START)) {
                        drawer.closeDrawer(Gravity.START);
                    } else {
                        drawer.openDrawer(Gravity.START);
                    }
                }
            });
            //toggleDrawer(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
        pd = new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.show();
        setListView();
        //sendToken();
    }
    @Override
    public void onBackPressed() {
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setListView(){
        databaseReference = FirebaseDatabase.getInstance().
                getReference().child(STAGES);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                try {
                    name_list = new ArrayList<>();
                    desc_list = new ArrayList<>();
                    number_list = new ArrayList<>();
                    ids_list = new ArrayList<>();
                    noText.setVisibility(View.GONE);
                    pd.dismiss();
                }catch (Exception e){
                    e.printStackTrace();

                }

                for (com.google.firebase.database.DataSnapshot childData: dataSnapshot.getChildren()) {
                    String keyy = childData.getKey();
                    String key2 = "";
                    for (com.google.firebase.database.DataSnapshot child2: childData.getChildren())
                         key2 = child2.getKey();
                    Log.e(TAG, "Key1: "+keyy+"\t Key2: "+key2);

                    String name = (String) childData.child(key2).child(STAGES_NAME).getValue();
                        String descriptions = (String) childData.child(key2).child(STAGES_DECRIPTION).getValue();
                        int number = 0;
                        int status = 1;
                        try {
                            number = (int) childData.child(key2).child(STAGES_NUMBER).getValue();
                            status = (int) childData.child(key2).child(STAGES_STATUS).getValue();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Log.e(TAG, "Name: "+name+"\t Desc: "+descriptions+" \t Number: "+number+"\t State: "+status);
                        if (status == 1){
                            totalUsers ++;
                            name_list.add(name);
                            desc_list.add(descriptions);
                            number_list.add((int) number);
                            ids_list.add(keyy);
                        }
                    //}
                }
                Other_Adapters adapters = new Other_Adapters(context,name_list,desc_list,number_list,ids_list,SHOW_MODE_HIDE);
                listView.setAdapter(adapters);
                if(totalUsers <= 0){
                    noText.setVisibility(View.VISIBLE);
                    //usersList.setVisibility(View.GONE);
                }
                else{
                    noText.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item != null && item.getItemId() == android.R.id.home) {
            if (drawer.isDrawerOpen(Gravity.START)) {
                drawer.closeDrawer(Gravity.START);
            }
            else {
                drawer.openDrawer(Gravity.START);
            }
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_details) {
            // proceed(date,ACTION_ASSESSEMENT);
            popStageDialog();

        } else if (id == R.id.nav_add) {
            String names = "";
            for (int i=0; i<name_list.size(); i++){
                names = names.concat(names+"/");
            }
            Log.e(TAG, "Stages: "+names);
            Intent intent = new Intent(context, AddNotification.class);
            intent.putExtra("names", names);
            startActivity(intent);
        }else if (id == R.id.nav_exit) {
            finish();
        }
        return true;
    }
    private void popStageDialog(){
        final AlertDialog dialog;
        try{
            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_add_stages, null);
            // this is set the view from XML inside AlertDialog
            alert.setView(view);
            Button btn_add = (Button) view.findViewById(R.id.btn_add);
            final EditText input_name = (EditText) view.findViewById(R.id.input_name);
            final EditText input_desc = (EditText) view.findViewById(R.id.input_desc);
            // disallow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(true);
            dialog = alert.create();
            dialog.show();
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String name  = input_name.getText().toString().trim();
                    if (name.equals("")){
                        input_name.setError("Enter the name..!");
                    }
                    final String desc  = input_desc.getText().toString().trim();
                    if (desc.equals("")){
                        input_desc.setError("Enter the descriptions");
                    }
                    if (!name.equals("") && !desc.equals("")){
                        final ProgressDialog pd = new ProgressDialog(context);
                        pd.setMessage("Loading...");
                        pd.show();

                        String url = FIREBASE_URL+STAGES+".json";
                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                            @Override
                            public void onResponse(String s) {
                                Firebase reference = new Firebase(FIREBASE_URL+STAGES);
                                int status = 1;
                                int number = 0;
                                if(s.equals("null")) {
                                    reference.child(name);

                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put(STAGES_NAME, name);
                                    map.put(STAGES_DECRIPTION, desc);
                                    map.put(STAGES_NUMBER, String.valueOf(number));
                                    map.put(STAGES_STATUS, String.valueOf(status));
                                    //map.put("photoUrl", "");
                                    reference.child(name).push().setValue(map);
                                    Toast.makeText(context, "New Stage added successful", Toast.LENGTH_LONG).show();
                                    //setListView();
                                    dialog.dismiss();
                                }
                                else {
                                    try {
                                        JSONObject obj = new JSONObject(s);
                                        if (!obj.has(name)) {

                                            Map<String, String> map = new HashMap<String, String>();
                                            map.put(STAGES_NAME, name);
                                            map.put(STAGES_DECRIPTION, desc);
                                            map.put(STAGES_NUMBER, String.valueOf(number));
                                            map.put(STAGES_STATUS, String.valueOf(status));

                                            reference.child(name).push().setValue(map);
                                            Toast.makeText(context, "New Stage added successful", Toast.LENGTH_LONG).show();
                                            //TODO::
                                            //finish();
                                            //setListView();
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(context, "stage name already exists", Toast.LENGTH_LONG).show();
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

                        RequestQueue rQueue = Volley.newRequestQueue(context);
                        rQueue.add(request);
                    }
                    //dialog.dismiss();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
