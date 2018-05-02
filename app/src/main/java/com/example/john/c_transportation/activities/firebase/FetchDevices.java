package com.example.john.c_transportation.activities.firebase;

import android.content.Context;

import com.example.john.c_transportation.R;
import com.example.john.c_transportation.activities.single_chatts.UserDetails;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

import static com.example.john.c_transportation.utils.Constants.config.DEVICES;
import static com.example.john.c_transportation.utils.Constants.config.KEY_TOKEN;
import static com.example.john.c_transportation.utils.Constants.config.USERNAME;
/**
 * Created by john on 4/26/18.
 */
public class FetchDevices  {
    private DatabaseReference dbReference;
    private Context context;
    public FetchDevices(Context context){
        this.context = context;
        Firebase.setAndroidContext(context);
        dbReference = FirebaseDatabase.getInstance().
                getReference().child(DEVICES);
    }
    public void fetchDevicesAll(final OkHttpClient mClient, final String messages){
        final List<String> list = new ArrayList<>();
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childData: dataSnapshot.getChildren()) {
                    String key = childData.getKey();
                    String key2 = "";
                    for (com.google.firebase.database.DataSnapshot child2 : childData.getChildren()) {
                        key2 = child2.getKey();
                        String username = (String) childData.child(key2).child(USERNAME).getValue();
                        String token = (String) childData.child(key2).child(KEY_TOKEN).getValue();
                        String uid = new DeviceToken(context).token();
                        if (!username.equals(UserDetails.username)){
                            list.add(key);
                        }
                    }
                }
                final JSONArray jsonArray = new JSONArray();
                for (int i=0; i<list.size(); i++){
                    jsonArray.put(list.get(i));
                }
                if (list.size() > 0){
                    new SendNotifications(context,mClient).sendMessage(jsonArray,context.getResources().getString(R.string.app_name),messages,"Http:\\google.com", UserDetails.username);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void fetchDevicesSingle(final OkHttpClient mClient, final String user , final String message){
        final List<String> list = new ArrayList<>();
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childData: dataSnapshot.getChildren()) {
                    String key = childData.getKey();
                    String key2 = "";
                    for (com.google.firebase.database.DataSnapshot child2 : childData.getChildren()) {
                        key2 = child2.getKey();
                        String username = (String) childData.child(key2).child(USERNAME).getValue();
                        String token = (String) childData.child(key2).child(KEY_TOKEN).getValue();
                        String uid = new DeviceToken(context).token();
                        if (username.equals(user)){
                            list.add(key);
                            JSONArray jsonArray = new JSONArray();
                            jsonArray.put(key);
                            new SendNotifications(context,mClient).sendMessage(jsonArray,context.getResources().getString(R.string.app_name),message,"Http:\\google.com", UserDetails.username);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
