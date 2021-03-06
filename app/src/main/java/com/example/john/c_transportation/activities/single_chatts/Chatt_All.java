package com.example.john.c_transportation.activities.single_chatts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.john.c_transportation.R;
import com.example.john.c_transportation.utils.DateTime;
import com.firebase.client.Firebase;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import static com.example.john.c_transportation.utils.Constants.config.BODY;
import static com.example.john.c_transportation.utils.Constants.config.DATETIME;
import static com.example.john.c_transportation.utils.Constants.config.MESSAGES;
import static com.example.john.c_transportation.utils.Constants.config.REPLY;
import static com.example.john.c_transportation.utils.Constants.config.USERNAME;

/**
 * Created by john on 4/24/18.
 */

public class Chatt_All extends AppCompatActivity {
    LinearLayout layout;
    //EditText messageArea;
    //private ImageView imageView;
    ScrollView scrollView;
    private static final int REQUEST_INVITE = 1;
    private static final int REQUEST_IMAGE = 2;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 10;
    private static final String TAG = "Chat";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    //uri to store file
    private Uri filePath;
    private Context context = this;

    //firebase objects
    private StorageReference storageReference;
    private DatabaseReference databaseReference, databaseReference2;
    //private TextView textView;
    String conductor_name;

    EditText message_area;
    ImageView sendButton;
    LinearLayout message_layout;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatt1);

        try {
            toolbar = (Toolbar) findViewById(R.id.toolBar);
            setSupportActionBar(toolbar);
            TextView header_title = (TextView) findViewById(R.id.header_title);
            header_title.setText(UserDetails.chatWith);
        }catch (Exception e){
            e.printStackTrace();
        }
        layout = (LinearLayout) findViewById(R.id.layout1);
        message_layout = (LinearLayout) findViewById(R.id.message_layout);
        //messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        message_area = (EditText) findViewById(R.id.messageArea);
        conductor_name = UserDetails.chatWith;
        Log.e(TAG,"Conductor_name:: "+conductor_name);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
       // message_layout.setVisibility(View.GONE);

        Firebase.setAndroidContext(this);
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().
                getReference().child(MESSAGES).child(conductor_name);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                try {
                    layout.removeAllViews();
                }catch (Exception e){
                    e.printStackTrace();

                }

                for (com.google.firebase.database.DataSnapshot childData: dataSnapshot.getChildren()){
                   // String keyy = childData.getKey();
                   // if (lastKey.equals(keyy)) {

                        String body = (String) childData.child(BODY).getValue();
                        String date_time = (String) childData.child(DATETIME).getValue();
                        String username = (String) childData.child(USERNAME).getValue();

                        Log.e(TAG, childData.getKey());
                        String reply = "";

                        if (username.equals(conductor_name)){
                            addLayoutRECIEVED(username, body, date_time);
                        }else if (UserDetails.username.equals(username)) {
                            addLayoutSent(body, date_time, reply);
                        } else {
                            addLayoutSentOTHERS(body, date_time, reply);
                            //addLayoutRECIEVED(username, body, date_time);
                        }

                        //addLayoutRECIEVED(username, body, date_time);
                        if (childData.hasChild(REPLY)) {
                            for (com.google.firebase.database.DataSnapshot childData2 : childData.child(REPLY).getChildren()) {
                                String user_key = childData2.getKey();
                                Log.e(TAG, "ChildKey2: " + user_key);
                                String body2 = (String) childData.child(REPLY).child(user_key).child(BODY).getValue();
                                String date_time2 = (String) childData.child(REPLY).child(user_key).child(DATETIME).getValue();
                                String username2 = (String) childData.child(REPLY).child(user_key).child(USERNAME).getValue();
                                Log.e(TAG, "body2: " + body2 + "\t date2: " + date_time2 + "\tUsername2: " + username2);
                                if (UserDetails.username.equals(username2)) {
                                    addLayoutSentREPLYYOU(body2, date_time2, "You");
                                } else {
                                    addLayoutSentREPLY(body2, date_time2, username2);
                                }
                            }
                        }
                       //TODO addReplyBox(childData.getKey());
                    //}
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //reference1 = new Firebase(FIREBASE_URL + MESSAGES + "/" +conductor_name);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = message_area.getText().toString().trim();
                if (!messageText.equals("")){
                    if (!messageText.equals("")) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put(BODY, messageText);
                        map.put(DATETIME, DateTime.getCurrentDate()+" "+DateTime.getCurrentTime());
                        map.put(USERNAME, UserDetails.username);
                        //map.put("photoUrl", "");
                        databaseReference.push().setValue(map);
                        message_area.setText("");
                    }
                }
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chatt1, menu);
        return true;
    }
    private void addReplyBox(final String key) {
        try {
            View view = LayoutInflater.from(this).inflate(R.layout.type_here, layout, false);
            final EditText messageArea = (EditText) view.findViewById(R.id.message_area);
            ImageView sendButton = (ImageView) view.findViewById(R.id.sendButton);
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String messageText = messageArea.getText().toString().trim();
                    if (!messageText.equals("")){
                        if (!messageText.equals("")) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put(BODY, messageText);
                            map.put(DATETIME,  DateTime.getCurrentDate()+" "+DateTime.getCurrentTime());
                            map.put(USERNAME, UserDetails.username);
                            //map.put("photoUrl", "");
                            databaseReference.child(key).child(REPLY).push().setValue(map);
                            messageArea.setText("");
                        }
                    }
                }
            });

            layout.addView(view);
            scrollView.fullScroll(View.FOCUS_DOWN);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addLayoutSent(String message, String date, String reply) {
        String toDate = date;
        try{
            if (DateTimeUtils.isToday(date)){
                toDate =  DateTimeUtils.getTimeAgo(this, date);
            }else if (DateTimeUtils.isYesterday(date)){
                toDate = "yesterdat at "+DateTimeUtils.formatTime(date);
            }else {
                toDate = DateTimeUtils.getTimeAgo(this, date);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            View view = LayoutInflater.from(this).inflate(R.layout.item_message_sent, layout, false);

            TextView textView = (TextView) view.findViewById(R.id.text_message_body);
            TextView textView1 = (TextView) view.findViewById(R.id.text_message_time);
            textView.setText(message);
            textView1.setText(toDate);

            layout.addView(view);
            scrollView.fullScroll(View.FOCUS_DOWN);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addLayoutSentOTHERS(String message, String date, String reply) {
        String toDate = date;
        try{
            if (DateTimeUtils.isToday(date)){
                toDate =  DateTimeUtils.getTimeAgo(this, date);
            }else if (DateTimeUtils.isYesterday(date)){
                toDate = "yesterdat at "+DateTimeUtils.formatTime(date);
            }else {
                toDate = DateTimeUtils.getTimeAgo(this, date);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            View view = LayoutInflater.from(this).inflate(R.layout.sent_item_1, layout, false);
            TextView textView = (TextView) view.findViewById(R.id.text_message_body);
            TextView textView1 = (TextView) view.findViewById(R.id.text_message_time);
            textView.setText(message);
            textView1.setText(toDate);

            layout.addView(view);
            scrollView.fullScroll(View.FOCUS_DOWN);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addLayoutSentREPLY(String message, String date, String name) {
        String toDate = date;
        try{
            if (DateTimeUtils.isToday(date)){
                toDate =  DateTimeUtils.getTimeAgo(this, date);
            }else if (DateTimeUtils.isYesterday(date)){
                toDate = "yesterdat at "+DateTimeUtils.formatTime(date);
            }else {
                toDate = DateTimeUtils.formatDate(date)+"";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            View view = LayoutInflater.from(this).inflate(R.layout.sent_reply_box, layout, false);

            TextView textView = (TextView) view.findViewById(R.id.text_message_body);
            TextView textView1 = (TextView) view.findViewById(R.id.text_message_time);
            TextView textView2 = (TextView) view.findViewById(R.id.text_name);
            textView.setText(message);
            textView1.setText(toDate);
            textView2.setText(name);
            layout.addView(view);
            scrollView.fullScroll(View.FOCUS_DOWN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addLayoutSentREPLYYOU(String message, String date, String name) {
        String toDate = date;
        try{
            if (DateTimeUtils.isToday(date)){
                toDate =  DateTimeUtils.getTimeAgo(this, date);
            }else if (DateTimeUtils.isYesterday(date)){
                toDate = "yesterdat at "+DateTimeUtils.formatTime(date);
            }else {
                toDate = DateTimeUtils.formatDate(date)+"";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            View view = LayoutInflater.from(this).inflate(R.layout.sent_reply_box_u, layout, false);

            TextView textView = (TextView) view.findViewById(R.id.text_message_body);
            TextView textView1 = (TextView) view.findViewById(R.id.text_message_time);
            TextView textView2 = (TextView) view.findViewById(R.id.text_name);
            textView.setText(message);
            textView1.setText(toDate);
            textView2.setText(name);
            layout.addView(view);
            scrollView.fullScroll(View.FOCUS_DOWN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void addLayoutRECIEVED(String name, String message, String date) {
        try {
            View view = LayoutInflater.from(this).inflate(R.layout.recieve_layout, layout, false);

            TextView textView = (TextView) view.findViewById(R.id.text_message_name);
            TextView textView1 = (TextView) view.findViewById(R.id.text_message_body);
            TextView textView2 = (TextView) view.findViewById(R.id.text_message_time);
            ImageView image_message_profile = (ImageView) view.findViewById(R.id.image_message_profile);
            textView.setText(name);
            textView1.setText(message);
            textView2.setText(date);

            layout.addView(view);
            scrollView.fullScroll(View.FOCUS_DOWN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        if (id == R.id.action_all) {
            startActivity(new Intent(context, Chatt_All.class));
        }
        return super.onOptionsItemSelected(item);
    }
}