package com.example.john.c_transportation.activities.single_chatts;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.john.c_transportation.R;
import com.firebase.client.Firebase;
import com.github.thunder413.datetimeutils.DateTimeStyle;
import com.github.thunder413.datetimeutils.DateTimeUnits;
import com.github.thunder413.datetimeutils.DateTimeUtils;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import static com.example.john.c_transportation.utils.Constants.config.CONDUCTOR_TYPE;
import static com.example.john.c_transportation.utils.Constants.config.FIREBASE_URL;
import static com.example.john.c_transportation.utils.Constants.config.USERS;
import static com.example.john.c_transportation.utils.Constants.config.USER_TYPE;

/**
 * Created by john on 4/22/18.
 */

public class ListConductors extends AppCompatActivity {
    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    Firebase reference;
    private Context context = this;
    private static final String TAG = "ListConductors";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        usersList = (ListView)findViewById(R.id.usersList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);



        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        pd = new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.show();

        String url = FIREBASE_URL+USERS+".json";

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

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = al.get(position);
                startActivity(new Intent(context, Chatt_Students.class));
            }
        });

        //showDate();
    }
    private void showDate(){
        try {

            Date date = new Date();
            Log.d(TAG, "Date To String >> " + DateTimeUtils.formatDate(new Date()));
            Log.d(TAG, "String To Date >> " + DateTimeUtils.formatDate("2017-06-13 04:14:49"));
            Log.d(TAG, "IsToDay >> " + DateTimeUtils.isToday(new Date()));
            Log.d(TAG, "IsToDay String >> " + DateTimeUtils.isToday("2017-06-15 04:14:49"));
            Log.d(TAG, "IsYesterdaY Date >> " + DateTimeUtils.isYesterday(new Date()));
            Log.d(TAG, "IsYesterdaY String >> " + DateTimeUtils.isYesterday("2017-06-12 04:14:49"));
            Log.d(TAG, "TimeAgo String >> " + DateTimeUtils.getTimeAgo(this, "2017-06-13 04:14:49"));
            Log.d(TAG, "TimeAgo Date >> " + DateTimeUtils.getTimeAgo(this, date));
            Log.d(TAG, "Diff in milliseconds >> " + DateTimeUtils.getDateDiff(new Date(), DateTimeUtils.formatDate("2017-06-13 04:14:49"), DateTimeUnits.MILLISECONDS));
            Log.d(TAG, "Diff in seconds >> " + DateTimeUtils.getDateDiff(new Date(), DateTimeUtils.formatDate("2017-06-13 04:14:49"), DateTimeUnits.SECONDS));
            Log.d(TAG, "Diff in minutes >> " + DateTimeUtils.getDateDiff(new Date(), DateTimeUtils.formatDate("2017-06-13 04:14:49"), DateTimeUnits.MINUTES));
            Log.d(TAG, "Diff in hours >> " + DateTimeUtils.getDateDiff(new Date(), DateTimeUtils.formatDate("2017-06-13 04:14:49"), DateTimeUnits.HOURS));
            Log.d(TAG, "Diff in days >> " + DateTimeUtils.getDateDiff(new Date(), DateTimeUtils.formatDate("2017-06-13 04:14:49"), DateTimeUnits.DAYS));
            Log.d(TAG, "Extract time from date >> " + DateTimeUtils.formatTime(new Date()));
            Log.d(TAG, "Extract time from dateString >> " + DateTimeUtils.formatTime("2017-06-13 04:14:49"));
            Log.d(TAG, "Millis to time  >> " + DateTimeUtils.millisToTime(25416660));
            Log.d(TAG, "Time to millis  >> " + DateTimeUtils.timeToMillis("14:20"));
            Log.d(TAG, "Revert Millis to time  >> " + DateTimeUtils.millisToTime(860000));
            Log.d(TAG, "FormatWithStyle  FULL >> " + DateTimeUtils.formatWithStyle(new Date(), DateTimeStyle.FULL));
            Log.d(TAG, "FormatWithStyle  LONG >> " + DateTimeUtils.formatWithStyle(new Date(), DateTimeStyle.LONG));
            Log.d(TAG, "FormatWithStyle  MEDIUM >> " + DateTimeUtils.formatWithStyle(new Date(), DateTimeStyle.MEDIUM));
            Log.d(TAG, "FormatWithStyle  SHORT >> " + DateTimeUtils.formatWithStyle(new Date(), DateTimeStyle.SHORT));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Log.e(TAG, obj+"");
            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();
                //Log.e(TAG, "Next-key:: "+key+"\t"+UserDetails.username);
                if(!key.equals(UserDetails.username)) {
                    Iterator iterator2 = obj.getJSONObject(key).keys();
                    while (iterator2.hasNext()){
                        String key2 = iterator2.next().toString();
                        String user_type = obj.getJSONObject(key).getJSONObject(key2).getString(USER_TYPE);
                        //Log.e(TAG, "Next-key2:: "+key+"\t"+user_type);
                        if (user_type.equals(CONDUCTOR_TYPE)){
                            al.add(key);
                            totalUsers++;
                            ///startActivity(new Intent(context, MainActivity_C.class));
                        }
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(totalUsers <= 0){
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
        }

        pd.dismiss();

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.stages = al.get(position);
                startActivity(new Intent(context, Chatt_Students.class));
            }
        });
    }

    public void doOnSuccess2(String oldKey,String s) {
        try {
            JSONObject obj = new JSONObject(s);
            Iterator i = obj.keys();
            String key = "";

            while (i.hasNext()) {
                key = i.next().toString();
                String user_type = obj.getJSONObject(key).getString(USER_TYPE);
                if (user_type.equals(CONDUCTOR_TYPE)){
                    al.add(oldKey);
                    totalUsers++;
                    ///startActivity(new Intent(context, MainActivity_C.class));
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