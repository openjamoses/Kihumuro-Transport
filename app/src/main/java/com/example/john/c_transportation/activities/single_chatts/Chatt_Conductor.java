package com.example.john.c_transportation.activities.single_chatts;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.john.c_transportation.R;
import com.example.john.c_transportation.activities.firebase.FetchDevices;
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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

import static com.example.john.c_transportation.utils.Constants.config.BODY;
import static com.example.john.c_transportation.utils.Constants.config.DATETIME;
import static com.example.john.c_transportation.utils.Constants.config.MESSAGES;
import static com.example.john.c_transportation.utils.Constants.config.REPLY;
import static com.example.john.c_transportation.utils.Constants.config.TIME;
import static com.example.john.c_transportation.utils.Constants.config.USERNAME;

/**
 * Created by john on 4/22/18.
 */

public class Chatt_Conductor extends AppCompatActivity {
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
    private DatabaseReference databaseReference;
    //private TextView textView;
    String conductor_name;

    EditText message_area;
    ImageView choseImageView, timeImage;
    ImageView sendButton;
    LinearLayout message_layout;
    private Toolbar toolbar;
    String today_is = "";
    String message = "";
    String time = "";
    OkHttpClient mClient ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatt1);

        try {
            toolbar = (Toolbar) findViewById(R.id.toolBar);
            setSupportActionBar(toolbar);
            TextView header_title = (TextView) findViewById(R.id.header_title);
            TextView subtitle_view = (TextView) findViewById(R.id.subtitle_view);
            header_title.setText(UserDetails.username);
            subtitle_view.setText(UserDetails.stages);
            mClient = new OkHttpClient();
        }catch (Exception e){
            e.printStackTrace();
        }
        layout = (LinearLayout) findViewById(R.id.layout1);
        message_layout = (LinearLayout) findViewById(R.id.message_layout);
        //messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        choseImageView = (ImageView) findViewById(R.id.imageView);
        timeImage = (ImageView) findViewById(R.id.timeImage);
        message_area = (EditText) findViewById(R.id.messageArea);
       // message_area.setEnabled(false);
        conductor_name = UserDetails.username;
        Log.e(TAG,"Conductor_name:: "+conductor_name);
       // message_area.setText("Today is - "+DateTime.daysList(context).get(DateTime.day(DateTime.getCurrentDate())));

        message = "Hello everyone, I will be available Today - "+DateTime.daysList(context).get(DateTime.day(DateTime.getCurrentDate()))+" " +
                " at "+UserDetails.stages+" at exactly: , Pliz keep time..! thanks.";
        message_area.setText(message);
        today_is = DateTime.daysList(context).get(DateTime.day(DateTime.getCurrentDate()));
        choseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
            }
        });
        timeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choseTime();
            }
        });
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
                        map.put(TIME, time);
                        //map.put("photoUrl", "");
                        databaseReference.push().setValue(map);
                        new FetchDevices(context).fetchDevicesAll(mClient,message);
                        message_area.setText("");
                    }
                }
            }
        });

    }
    private void showMenu(){
        try{
            PopupMenu popup = new PopupMenu(context, choseImageView);
            //inflating menu from xml resource
            popup.inflate(R.menu.pop_days);
            //adding click listener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    try {
                        switch (item.getItemId()) {
                            case R.id.action_moday:
                                today_is = getResources().getString(R.string.monday);
                                message = "Hello everyone, "+isLess(today_is)+", I will be available at "+UserDetails.stages+" at exactly "+time+". Pliz Keep time. Thanx";
                                message_area.setText(message);
                                break;
                            case R.id.action_tuesday:
                                today_is = getResources().getString(R.string.tuesday);
                                message = "Hello everyone, "+isLess(today_is)+", I will be available at "+UserDetails.stages+" at exactly "+time+". Pliz Keep time. Thanx";
                                message_area.setText(message);
                                break;
                            case R.id.action_wednesdat:
                                today_is = getResources().getString(R.string.wednesday);
                                message = "Hello everyone, "+isLess(today_is)+", I will be available at "+UserDetails.stages+" at exactly "+time+". Pliz Keep time. Thanx";
                                message_area.setText(message);
                                break;
                            case R.id.action_thursday:
                                today_is = getResources().getString(R.string.thursday);
                                message = "Hello everyone, "+isLess(today_is)+", I will be available at "+UserDetails.stages+" at exactly "+time+". Pliz Keep time. Thanx";
                                message_area.setText(message);
                                break;
                            case R.id.action_friday:
                                today_is = getResources().getString(R.string.friday);
                                message = "Hello everyone, "+isLess(today_is)+", I will be available at "+UserDetails.stages+" at exactly "+time+". Pliz Keep time. Thanx";
                                message_area.setText(message);
                                break;
                            case R.id.action_saturday:
                                today_is = getResources().getString(R.string.saturday);
                                message = "Hello everyone, "+isLess(today_is)+", I will be available at "+UserDetails.stages+" at exactly "+time+". Pliz Keep time. Thanx";
                                message_area.setText(message);
                                break;
                            case R.id.action_sunday:
                                today_is = getResources().getString(R.string.sunday);
                                message = "Hello everyone, "+isLess(today_is)+", I will be available at "+UserDetails.stages+" at exactly "+time+". Pliz Keep time. Thanx";
                                message_area.setText(message);
                                break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return false;
                }
            });
            //displaying the popup
            popup.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void choseTime(){
        message_area.setError("");
        sendButton.setEnabled(true);
        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);
                String hrs = String.valueOf(hourOfDay);
                String min =String.valueOf(minute);
                if (hourOfDay < 10){
                    hrs ="0"+hourOfDay;
                }
                if (minute < 10){
                    min ="0"+minute;
                }
                time = hrs+":"+min;
                String nexthr;
                if (DateTime.istimeDiff(DateTime.getCurrentTime(),time,30) == true){
                    nexthr = DateTime.timeDiff(DateTime.getCurrentTime(), time);
                }else {
                    sendButton.setEnabled(false);
                    message_area.setError("Pliz choose a time atleast 30 minutes from now..!");
                   // Toast.makeText(context, "Pliz choose a time atleast 30 minutes from now..!", Toast.LENGTH_LONG).show();
                }

                message = "Hello everyone, "+isLess(today_is)+", I will be available at "+UserDetails.stages+" at exactly "+time+". Pliz Keep time. Thanx";
                message_area.setText(message);
                Log.v(TAG, "The choosen one " + date.getTime());
            }
        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
    }
    private String isLess(String day){
        sendButton.setEnabled(true);
        message_area.setError("");
        String days = "";
        if (DateTime.daysList(context).indexOf(today_is) < DateTime.daysList(context).indexOf( DateTime.daysList(context).get(DateTime.day(DateTime.getCurrentDate())) )){
            days = day+" - Next week";
        }else if (DateTime.daysList(context).indexOf(today_is) == DateTime.daysList(context).indexOf( DateTime.daysList(context).get(DateTime.day(DateTime.getCurrentDate())) )){
            String nexthr = "";
            if (!time.equals("")){
                if (DateTime.istimeDiff(DateTime.getCurrentTime(),time,30) == true){
                    nexthr = DateTime.timeDiff(DateTime.getCurrentTime(), time);
                }else {
                    sendButton.setEnabled(false);
                    message_area.setError("Pliz choose a time atleast 30 minutes from now..!");
                    //Toast.makeText(context, "Pliz choose a time atleast 30 minutes from now..!", Toast.LENGTH_LONG).show();
                }

                days = "Today - "+day;
                if (!nexthr.equals("")){
                    days = "Today - "+day+" ( Next - "+nexthr+" )";
                }
            }
        }else if (DateTime.daysList(context).indexOf(today_is) - DateTime.daysList(context).indexOf( DateTime.daysList(context).get(DateTime.day(DateTime.getCurrentDate())) ) == 1){
            days = "Tomorrow - "+day;
        }else{
            days = day+" - This week";
        }
        return days;
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
                toDate = "yesterday at "+DateTimeUtils.formatTime(date);
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
        return super.onOptionsItemSelected(item);
    }
}


