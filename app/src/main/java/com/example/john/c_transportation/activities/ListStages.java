package com.example.john.c_transportation.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.john.c_transportation.R;
import com.example.john.c_transportation.activities.single_chatts.Chatt_Students;
import com.example.john.c_transportation.activities.single_chatts.UserDetails;
import com.example.john.c_transportation.adapters.Other_Adapters;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.john.c_transportation.utils.Constants.config.MESSAGES;
import static com.example.john.c_transportation.utils.Constants.config.SHOW_MODE_HIDE;
import static com.example.john.c_transportation.utils.Constants.config.STAGES;
import static com.example.john.c_transportation.utils.Constants.config.STAGES_DECRIPTION;
import static com.example.john.c_transportation.utils.Constants.config.STAGES_NAME;
import static com.example.john.c_transportation.utils.Constants.config.STAGES_NUMBER;
import static com.example.john.c_transportation.utils.Constants.config.STAGES_STATUS;

/**
 * Created by john on 4/26/18.
 */

public class ListStages extends AppCompatActivity{
    private ListView listView;
    private static final String TAG = "MainActivity_C";
    private List<String> name_list = new ArrayList<>();
    private List<String> desc_list = new ArrayList<>();
    private List<Integer> number_list = new ArrayList<>();
    private List<String> ids_list = new ArrayList<>();
    private ProgressDialog pd;
    private Context context = this;
    private TextView noText;
    private int totalUsers = 0;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        listView = (ListView) findViewById(R.id.listView);
        noText = (TextView) findViewById(R.id.noText);

        pd = new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.show();
        setListView();
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
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //TODO::::::
                        UserDetails.stages = ids_list.get(i);
                        //UserDetails.stages = name_list.get(i);
                        String names = name_list.get(i)+"/";
                        for (int a=0; a<name_list.size(); a++){
                            if (!name_list.get(a).equals(name_list.get(i))){
                                names = names.concat(name_list.get(a)+"/");
                            }
                        }
                        Intent intent = new Intent(context, Chatt_Students.class);
                        intent.putExtra("names", names);
                        startActivity(intent);
                        //startActivity(new Intent(context, ListNotifications.class));
                    }
                });
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
}
