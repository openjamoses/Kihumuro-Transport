package com.example.john.c_transportation.activities.firebase;

import android.content.Context;
import android.content.SharedPreferences;

import static com.example.john.c_transportation.utils.Constants.config.KEY_TOKEN;
import static com.example.john.c_transportation.utils.Constants.config.SHARED_PREF;

/**
 * Created by john on 8/31/17.
 */

public class DeviceToken {
    Context context;
    public DeviceToken(Context context){
        this.context = context;
    }
    public String token(){
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF, 0);
        String regId = pref.getString(KEY_TOKEN, null);
        return regId;
    }

}
