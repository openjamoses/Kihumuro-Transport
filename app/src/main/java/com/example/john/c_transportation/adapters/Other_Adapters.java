package com.example.john.c_transportation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.john.c_transportation.R;
import java.util.List;
import static com.example.john.c_transportation.utils.Constants.config.SHOW_MODE_SHOW;
/**
 * Created by john on 11/6/17.
 */

public class Other_Adapters extends BaseAdapter {
    Context context;
    List<String> name,descriptions,ids;

    List<Integer> numbers;
    String show_mode;

    LayoutInflater inflter;
    public Other_Adapters(Context applicationContext, List<String> name, List<String> descriptions,List<Integer>  numbers, List<String>  ids, String show_mode) {
        this.context = applicationContext;
        this.name = name;
        this.descriptions = descriptions;
        this.ids = ids;
        this.numbers = numbers;
        this.show_mode = show_mode;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.stages_adapter, null);
        try {
            TextView text_name = (TextView) view.findViewById(R.id.text_name);
            TextView text_description = (TextView) view.findViewById(R.id.text_description);
            TextView text_numbers = (TextView) view.findViewById(R.id.text_numbers);

            text_name.setText(name.get(i));
            text_description.setText(descriptions.get(i));
            text_numbers.setText("( "+numbers.get(i)+" )");
            if (show_mode.equals(SHOW_MODE_SHOW)){
                text_numbers.setVisibility(View.VISIBLE);
            }else {
                text_numbers.setVisibility(View.GONE);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }
}
