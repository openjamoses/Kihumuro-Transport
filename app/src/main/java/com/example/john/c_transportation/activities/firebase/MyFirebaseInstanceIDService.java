package com.example.john.c_transportation.activities.firebase;

/**
 * Created by john on 8/31/17.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.john.c_transportation.utils.Constants;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static com.example.john.c_transportation.utils.Constants.config.KEY_TOKEN;
import static com.example.john.c_transportation.utils.Constants.config.SHARED_PREF;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
//Log.e("Token_Refresh","SendTokens Generate wait ");
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        try {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();

            Log.e("Token_Refresh", "SendTokens Generate: " + refreshedToken);
            // Saving reg id to shared preferences
            storeRegIdInPref(refreshedToken);

            // sending reg id to your server
            sendRegistrationToServer(refreshedToken);

            // Notify UI that registration has completed, so the progress indicator can be hidden.
            Intent registrationComplete = new Intent(Constants.config.REGISTRATION_COMPLETE);
            registrationComplete.putExtra("token", refreshedToken);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("Error","Token error: "+e);
        }
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }
}