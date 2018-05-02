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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.john.c_transportation.R;
import com.example.john.c_transportation.activities.single_chatts.Chatt_Students;
import com.example.john.c_transportation.activities.single_chatts.UserDetails;
import com.example.john.c_transportation.adapters.Other_Adapters;
import com.example.john.c_transportation.reminder.ScheduleClient;
import com.example.john.c_transportation.utils.DateTime;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.john.c_transportation.utils.Constants.config.BODY;
import static com.example.john.c_transportation.utils.Constants.config.DATETIME;
import static com.example.john.c_transportation.utils.Constants.config.MESSAGES;
import static com.example.john.c_transportation.utils.Constants.config.SHOW_MODE_HIDE;
import static com.example.john.c_transportation.utils.Constants.config.STAGES;
import static com.example.john.c_transportation.utils.Constants.config.STAGES_DECRIPTION;
import static com.example.john.c_transportation.utils.Constants.config.STAGES_NAME;
import static com.example.john.c_transportation.utils.Constants.config.STAGES_NUMBER;
import static com.example.john.c_transportation.utils.Constants.config.STAGES_STATUS;
import static com.example.john.c_transportation.utils.Constants.config.TIME;
import static com.example.john.c_transportation.utils.Constants.config.USERNAME;

/**
 * Created by john on 4/26/18.
 */
public class ListNotifications extends AppCompatActivity {
    private ListView listView;
    private static final String TAG = "MainActivity_C";
    private List<String> name_list = new ArrayList<>();
    private List<String> desc_list = new ArrayList<>();
    private List<Integer> number_list = new ArrayList<>();
    private List<String> ids_list = new ArrayList<>();
    private List<String> date_list = new ArrayList<>();
    private ProgressDialog pd;
    private Context context = this;
    private TextView noText;
    private int totalUsers = 0;
    private DatabaseReference databaseReference;
    String names = "";
    private ScheduleClient scheduleClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        listView = (ListView) findViewById(R.id.listView);
        noText = (TextView) findViewById(R.id.noText);
        try {
            names = getIntent().getStringExtra("names");
        }catch (Exception e){
            e.printStackTrace();
        }
        noText.setText("No new Notification found..!");
        pd = new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.show();
        setListView();
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        try{
            // Create a new service client and bind our activity to this service
            scheduleClient = new ScheduleClient(this);
            scheduleClient.doBindService();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setListView() {
        databaseReference = FirebaseDatabase.getInstance().
                getReference().child(MESSAGES);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                try {
                    name_list = new ArrayList<>();
                    desc_list = new ArrayList<>();
                    number_list = new ArrayList<>();
                    ids_list = new ArrayList<>();
                    date_list = new ArrayList<>();
                    noText.setVisibility(View.GONE);
                    pd.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();

                }

                for (com.google.firebase.database.DataSnapshot childData : dataSnapshot.getChildren()) {
                    String keyy = childData.getKey();
                    String key2 = "";
                    for (com.google.firebase.database.DataSnapshot child2 : childData.getChildren())
                        key2 = child2.getKey();
                    //Log.e(TAG, "Key1: " + keyy + "\t Key2: " + key2);

                    String name = (String) childData.child(key2).child(USERNAME).getValue();
                    String descriptions = (String) childData.child(key2).child(BODY).getValue();
                    String date_time = (String) childData.child(key2).child(DATETIME).getValue();
                    String time = "";
                    if (childData.hasChild(TIME)){
                        time = (String) childData.child(key2).child(TIME).getValue();

                        String[] splits = time.split(":");
                        Calendar c = Calendar.getInstance();
                        //c.set(year, month, day);
                        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splits[0]));
                        c.set(Calendar.MINUTE, Integer.parseInt(splits[1]) - 5);
                        c.set(Calendar.SECOND, 0);
                        // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
                        scheduleClient.setAlarmForNotification(c);

                    }
                    int number = 0;
                    int status = 1;
                    try {
                        if (DateTime.dateDiff(DateTime.getCurrentDate(), date_time) >= 0) {
                            totalUsers++;
                            name_list.add(name);
                            desc_list.add(descriptions);
                            date_list.add(date_time);
                            number_list.add((int) number);
                            ids_list.add(keyy);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Log.e(TAG, "Name: " + name + "\t Desc: " + descriptions + " \t Number: " + number + "\t State: " + status);

                    //}
                }
                Other_Adapters adapters = new Other_Adapters(context, name_list, desc_list, number_list, ids_list, SHOW_MODE_HIDE);
                listView.setAdapter(adapters);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //TODO::::::
                       // UserDetails.stages = ids_list.get(i);
                        UserDetails.chatWith = name_list.get(i);
                        UserDetails.message = desc_list.get(i);
                        UserDetails.date = date_list.get(i);
                        UserDetails.key = ids_list.get(i);

                        Intent intent = new Intent(context, ListStages.class);
                        intent.putExtra("names", names);
                        startActivity(intent);
                        //startActivity(new Intent(context, Chatt_Students.class));
                    }
                });
                if (totalUsers <= 0) {
                    noText.setVisibility(View.VISIBLE);
                    //usersList.setVisibility(View.GONE);
                } else {
                    noText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStop() {
        // When our activity is stopped ensure we also stop the connection to the service
        // this stops us leaking our activity into the system *bad*
        if(scheduleClient != null)
            scheduleClient.doUnbindService();
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}