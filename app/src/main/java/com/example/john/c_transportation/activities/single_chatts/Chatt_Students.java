package com.example.john.c_transportation.activities.single_chatts;

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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;

import static com.example.john.c_transportation.utils.Constants.config.BODY;
import static com.example.john.c_transportation.utils.Constants.config.DATETIME;
import static com.example.john.c_transportation.utils.Constants.config.MESSAGES;
import static com.example.john.c_transportation.utils.Constants.config.REPLY;
import static com.example.john.c_transportation.utils.Constants.config.STAGES;
import static com.example.john.c_transportation.utils.Constants.config.STAGES_NAME;
import static com.example.john.c_transportation.utils.Constants.config.USERNAME;

/**
 * Created by john on 4/22/18.
 */

public class Chatt_Students  extends AppCompatActivity {
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
    private String names = "";
    String message = "";
    String selected = "";
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
            header_title.setText(UserDetails.chatWith);
            subtitle_view.setText(UserDetails.stages);

            mClient = new OkHttpClient();

        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            names = getIntent().getStringExtra("names");
            message = "I will be available at "+names.split("/")[0];
            selected = names.split("/")[0];
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

        Log.e(TAG, "Message: "+UserDetails.message+"\t"+UserDetails.date);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        message_layout.setVisibility(View.GONE);

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
                    addLayoutRECIEVED(UserDetails.chatWith, UserDetails.message, UserDetails.date);
                    int status = 0;
                    String key = "";
                    for (com.google.firebase.database.DataSnapshot child: dataSnapshot.getChildren()){
                        key = child.getKey();
                    }

                    for (com.google.firebase.database.DataSnapshot child: dataSnapshot.getChildren()){
                         String key2 = child.getKey();
                        if (key.equals(key2)){
                            if (child.hasChild(REPLY)){
                                for (com.google.firebase.database.DataSnapshot childData : child.child(REPLY).getChildren()){
                                    String user_key = childData.getKey();
                                    String body2 = (String) child.child(REPLY).child(user_key).child(BODY).getValue();
                                    String date_time2 = (String) child.child(REPLY).child(user_key).child(DATETIME).getValue();
                                    String username2 = (String) child.child(REPLY).child(user_key).child(USERNAME).getValue();
                                    Log.e(TAG, "body2: " + body2 + "\t date2: " + date_time2 + "\tUsername2: " + username2);
                                    if (UserDetails.username.equals(username2)) {
                                        addLayoutSentREPLYYOU(body2, date_time2, "You");
                                        status ++;
                                    } else {
                                        addLayoutSentREPLY(body2, date_time2, username2);
                                    }
                                }
                            }
                        }
                    }
                    if (status == 0) {
                        addReplyBox(key);
                    }

                }catch (Exception e){
                    e.printStackTrace();

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
                        databaseReference.child(REPLY).push().setValue(map);
                        message_area.setText("");

                        new FetchDevices(context).fetchDevicesSingle(mClient,UserDetails.chatWith,message);
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
            final ImageView sendButton = (ImageView) view.findViewById(R.id.sendButton);
            final ImageView pick_imageView = (ImageView) view.findViewById(R.id.pick_imageView);
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String messageText = messageArea.getText().toString().trim();
                    if (!messageText.equals("")){
                        if (!messageText.equals("")) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put(BODY, messageText);
                            map.put(STAGES_NAME, selected);
                            map.put(DATETIME,  DateTime.getCurrentDate()+" "+DateTime.getCurrentTime());
                            map.put(USERNAME, UserDetails.username);
                            //map.put("photoUrl", "");
                            databaseReference.child(key).child(REPLY).push().setValue(map);
                            messageArea.setText("");
                            layout.removeView(view);
                        }
                    }
                }
            });

            messageArea.setText(message);
            messageArea.setEnabled(false);
            pick_imageView.setVisibility(View.GONE);
            pick_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //showMenu(pick_imageView,messageArea);
                    popup(pick_imageView,names.split("/"),view);
                }
            });
            layout.addView(view);
            scrollView.fullScroll(View.FOCUS_DOWN);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void popup(ImageView imageView, final String[] stages, View view){
        LayoutInflater layoutInflater =
                (LayoutInflater)getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        //Update TextView in PopupWindow dynamically
        TextView textOut = (TextView)popupView.findViewById(R.id.textout);
        String stringOut = textOut.getText().toString();
        if(!stringOut.equals("")){
            textOut.setText(stringOut);
        }

        Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);

        //Spinner popupSpinner = (Spinner)popupView.findViewById(R.id.popupspinner);
        LinearLayout pop_layout = (LinearLayout) popupView.findViewById(R.id.pop_layout);
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView[] textViews = new TextView[stages.length];
        for (int i=1; i<stages.length; i++){
            textViews[i] = new TextView(context);
            textViews[i].setLayoutParams(params);
            textViews[i].setText(stages[i]);
            final int finalI = i;
            textViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selected = stages[finalI];
                    message = "I will be available at "+selected;
                    layout.removeView(view);
                    message_area.setText(message);
                    layout.addView(view);
                    popupWindow.dismiss();
                }
            });
            pop_layout.addView(textViews[i]);
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(context,
                        android.R.layout.simple_spinner_item, stages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // popupSpinner.setAdapter(adapter);

        btnDismiss.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }});

        popupWindow.showAsDropDown(imageView, 50, -30);

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