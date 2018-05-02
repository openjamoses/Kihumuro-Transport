package com.example.john.c_transportation.activities.single_chatts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.example.john.c_transportation.adapters.Other_Adapters;
import com.example.john.c_transportation.utils.DateTime;
import com.firebase.client.Firebase;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.john.c_transportation.utils.Constants.config.BODY;
import static com.example.john.c_transportation.utils.Constants.config.DATETIME;
import static com.example.john.c_transportation.utils.Constants.config.FIREBASE_URL;
import static com.example.john.c_transportation.utils.Constants.config.MESSAGES;
import static com.example.john.c_transportation.utils.Constants.config.REPLY;
import static com.example.john.c_transportation.utils.Constants.config.SHOW_MODE_HIDE;
import static com.example.john.c_transportation.utils.Constants.config.STAGES;
import static com.example.john.c_transportation.utils.Constants.config.STAGES_DECRIPTION;
import static com.example.john.c_transportation.utils.Constants.config.STAGES_NAME;
import static com.example.john.c_transportation.utils.Constants.config.STAGES_NUMBER;
import static com.example.john.c_transportation.utils.Constants.config.STAGES_STATUS;
import static com.example.john.c_transportation.utils.Constants.config.USERNAME;
import static com.example.john.c_transportation.utils.Constants.config.USERS;

/**
 * Created by john on 4/24/18.
 */

public class AddNotification extends AppCompatActivity {
    private ListView listView;
    private static final String TAG = "AddNotification";
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
    }

    private void setListView() {
        databaseReference = FirebaseDatabase.getInstance().
                getReference().child(MESSAGES).child(UserDetails.username);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                List<String> sList = new ArrayList<>();
                Set<String> sSet = new LinkedHashSet<>();
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

                String key_t = "";
                for (com.google.firebase.database.DataSnapshot childData : dataSnapshot.getChildren()) {
                    key_t = childData.getKey();
                }
                for (com.google.firebase.database.DataSnapshot childData : dataSnapshot.getChildren()) {
                    String keyy = childData.getKey();
                    if (key_t.equals(keyy)) {
                        String name = (String) childData.child(USERNAME).getValue();
                        String descriptions = (String) childData.child(BODY).getValue();
                        String date_time = (String) childData.child(DATETIME).getValue();
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
                        if (childData.hasChild(REPLY)) {
                            for (com.google.firebase.database.DataSnapshot childData2 : childData.child(REPLY).getChildren()) {
                                String key3 = childData2.getKey();
                                String stages_name = (String) childData.child(REPLY).child(key3).child(STAGES_NAME).getValue();
                                Log.e(TAG, "Stages: " + stages_name);
                                sList.add(stages_name);
                                sSet.add(stages_name);

                                String body2 = (String) childData.child(REPLY).child(key3).child(BODY).getValue();
                                String date_time2 = (String) childData.child(REPLY).child(key3).child(DATETIME).getValue();
                                String username2 = (String) childData.child(REPLY).child(key3).child(USERNAME).getValue();
                                Log.e(TAG, "body2: " + body2 + "\t date2: " + date_time2 + "\tUsername2: " + username2);
                                //addLayoutSentREPLY(body2, date_time2, username2);
                            }
                        }
                        //Log.e(TAG, "Name: " + name + "\t Desc: " + descriptions + " \t Number: " + number + "\t State: " + status);

                        //}
                    }
                }
                String list1 = "", list2 = "";
                Iterator iterator = sSet.iterator();
                final List<String> ssList = new ArrayList<>();
                while (iterator.hasNext()){
                    ssList.add((String) iterator.next());
                    String t = (String) iterator.next();
                    list1 = list1.concat(t+"/");
                }

                for (int i=0; i<sList.size(); i++){
                    list2 = list2.concat(sList.get(i)+"/");
                }
                //final List<String> list = new ArrayList<>();
                final List<Integer> valuesList = new ArrayList<>();
                //String[] stg = names.split("/");
                for (int i=0; i<ssList.size(); i++){
                   // list.add(stg[i]);
                    int counts = 0;
                    for (int j=0; j<sList.size(); j++){
                        if (ssList.get(i).equals(sList.get(j))){
                            counts ++;
                        }
                    }
                    valuesList.add(counts);
                }
                Other_Adapters adapters = new Other_Adapters(context, name_list, desc_list, number_list, ids_list, SHOW_MODE_HIDE);
                listView.setAdapter(adapters);
                final String finalList = list1;
                final String finalList1 = list2;
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //TODO::::::
                        Intent intent = new Intent(context, ReplyActivity.class);
                        intent.putExtra("list1", finalList);
                        intent.putExtra("list2", finalList1);
                        startActivity(intent);
                       //popup(listView,ssList,valuesList);
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

    private void popup(ListView listView, final List<String> stages, List<Integer> values){
        Log.e(TAG,"List: "+stages);
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
          //  textOut.setText(stringOut);
        }

        Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);

        //Spinner popupSpinner = (Spinner)popupView.findViewById(R.id.popupspinner);
        LinearLayout pop_layout = (LinearLayout) popupView.findViewById(R.id.pop_layout);
       // TextView textout = (TextView) pop_layout.findViewById(R.id.textout);
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        if (stages.size() == 0){
            textOut.setText("NO REPLY FOUND ..!");
        }else {
            textOut.setText(stages.size()+" - REPLIES FOUND..!");
        }
        TextView[] textViews = new TextView[stages.size()];
        for (int i=0; i<stages.size(); i++){
            textViews[i] = new TextView(context);
            textViews[i].setLayoutParams(params);
            textViews[i].setText(stages.get(i)+"   ( "+values.get(i)+" )");
            final int finalI = i;
            textViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //selected = stages[finalI];
                    //message = "I will be available at "+selected;
                    //layout.removeView(view);
                    //message_area.setText(message);
                    //layout.addView(view);
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

        popupWindow.showAsDropDown(listView, 50, -30);

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
