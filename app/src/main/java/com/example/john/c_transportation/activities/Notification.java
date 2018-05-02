package com.example.john.c_transportation.activities;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.john.c_transportation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.john.c_transportation.utils.Constants.config.UID;

/**
 * Created by john on 4/25/18.
 */

public class Notification extends AppCompatActivity {
    private Context context = this;
    OkHttpClient mClient ;
    Button sendButton;

    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send);
        mClient = new OkHttpClient();
        sendButton =  (Button) findViewById(R.id.sendButton);
        String refreshedToken = "djtC3FeuaxQ:APA91bHPR3g_XhgI0azWdAguM1Sixj-03ZiPHaDe7-wI50IZdJQPxnQcqdsvIt5kOQG1esGWWE4gxkWlWZDof9l3Io7dLdy9Qybx_TZSeQxHeUGWtFBQy6JqvR2WH6hL3vw2pFLlbj0u";//add your user refresh tokens who are logged in with firebase.

        final JSONArray jsonArray = new JSONArray();
        jsonArray.put(refreshedToken);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(jsonArray,context.getResources().getString(R.string.app_name),"How r u","Http:\\google.com","My Name is Vishal");
            }
        });
    }

    public void sendMessage(final JSONArray recipients, final String title, final String body, final String icon, final String message) {



        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    notification.put("body", body);
                    notification.put("title", title);
                    notification.put("icon", icon);

                    JSONObject data = new JSONObject();
                    data.put("message", message);
                    root.put("notification", notification);
                    root.put("data", data);
                    root.put("registration_ids", recipients);

                    String result = postToFCM(root.toString());
                    Log.d("Main Activity", "Result: " + result);
                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    JSONObject resultJson = new JSONObject(result);
                    int success, failure;
                    success = resultJson.getInt("success");
                    failure = resultJson.getInt("failure");
                    Toast.makeText(context, "Message Success: " + success + "Message Failed: " + failure, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Message Failed, Unknown error occurred.", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    String postToFCM(String bodyString) throws IOException {
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url(FCM_MESSAGE_URL)
                .post(body)
                .addHeader("Authorization", "key=" + UID)
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }
}
