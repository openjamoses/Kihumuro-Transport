package com.example.john.c_transportation.activities.single_chatts;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.john.c_transportation.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 5/1/18.
 */

public class ReplyActivity extends AppCompatActivity {
    private Context context = this;
    private String name1, name2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);


        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        name1 = getIntent().getStringExtra("list1");
        name2 = getIntent().getStringExtra("list2");

        String[]  splits1 = name1.split("/");
        String[]  splits2 = name2.split("/");
        List<String> list = new ArrayList<>();
        List<Integer> value = new ArrayList<>();

        for (int i=0; i<splits1.length; i++){
            int count = 0;
            list.add(splits1[i]);
            for (int j=0; j<splits2.length; j++){
                if (splits1[i].equals(splits2[j])){
                    count ++;
                }
            }
            value.add(count);
        }


        TextView textOut = (TextView)findViewById(R.id.textout);
        String stringOut = textOut.getText().toString();
        if(!stringOut.equals("")){
            //  textOut.setText(stringOut);
        }

        Button btnDismiss = (Button)findViewById(R.id.dismiss);

        //Spinner popupSpinner = (Spinner)popupView.findViewById(R.id.popupspinner);
        LinearLayout pop_layout = (LinearLayout)findViewById(R.id.pop_layout);
        // TextView textout = (TextView) pop_layout.findViewById(R.id.textout);
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        if (list.size() == 0){
            textOut.setText("NO REPLY FOUND ..!");
        }else {
            textOut.setText(list.size()+" - REPLIES FOUND..!");
        }
        TextView[] textViews = new TextView[list.size()];
        for (int i=0; i<list.size(); i++){
            textViews[i] = new TextView(context);
            textViews[i].setLayoutParams(params);
            textViews[i].setText(list.get(i)+"   ( "+value.get(i)+" )");
            pop_layout.addView(textViews[i]);
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
