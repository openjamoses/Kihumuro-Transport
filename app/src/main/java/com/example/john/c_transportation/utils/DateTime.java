package com.example.john.c_transportation.utils;

import android.content.Context;
import android.util.Log;

import com.example.john.c_transportation.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 7/9/17.
 */

public class DateTime {
    public static String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = dateFormat();
        String strDate = sdf.format(c.getTime());
        return strDate;
    }
    public static String getYear(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = dateFormat();
        String strDate = sdf.format(c.getTime());
        return strDate;
    }
    public static String getCurrentTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String strTime = sdf.format(c.getTime());
        return strTime;
    }
    public static String addDays(String date, int days){
        String dt_return = date;  // Start date
        try {

            SimpleDateFormat sdf = dateFormat();
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(date));
            c.add(Calendar.DATE, days);  // number of days to add
            dt_return = sdf.format(c.getTime());  // dt is now the new date
        }catch (Exception e){
            e.printStackTrace();
        }

        return dt_return;
    }

    public static SimpleDateFormat dateFormat(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf;
    }
    public static int day(String dateString){
        // BEGIN USER CODE
        int day = 0;
        try {
            SimpleDateFormat regularDateFormat = dateFormat();
            Date date = regularDateFormat.parse(dateString);
            System.out.println("date: " + date.toString());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            day = calendar.get(Calendar.DAY_OF_WEEK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return day;
    }

    public static List<String> daysList(Context context){
        List<String> list = new ArrayList<>();
        list.add("Unknown");
        list.add(context.getResources().getString(R.string.sunday));
        list.add(context.getResources().getString(R.string.monday));
        list.add(context.getResources().getString(R.string.tuesday));
        list.add(context.getResources().getString(R.string.wednesday));
        list.add(context.getResources().getString(R.string.thursday));
        list.add(context.getResources().getString(R.string.friday));
        list.add(context.getResources().getString(R.string.saturday));
        return list;
    }
    public static String timeDiff(String time1, String time2){
        String time_diff = "";
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            Date startDate = simpleDateFormat.parse(time1);
            Date endDate = simpleDateFormat.parse(time2);

            long difference = endDate.getTime() - startDate.getTime();
            if(difference<0)
            {
                Date dateMax = simpleDateFormat.parse("24:00");
                Date dateMin = simpleDateFormat.parse("00:00");
                difference=(dateMax.getTime() -startDate.getTime() )+(endDate.getTime()-dateMin.getTime());
            }
            int days = (int) (difference / (1000*60*60*24));
            int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
            int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
            Log.i("log_tag","Hours: "+hours+", Mins: "+min);

            if (hours == 0){
                time_diff = min+" minutes";
            }else {
                time_diff = hours+" hours and "+min+" minutes";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return time_diff;
    }

    public static boolean istimeDiff(String time1, String time2, int max){
        boolean isCorrect = true;
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            Date startDate = simpleDateFormat.parse(time1);
            Date endDate = simpleDateFormat.parse(time2);

            long difference = endDate.getTime() - startDate.getTime();
            if(difference<0)
            {
                Date dateMax = simpleDateFormat.parse("24:00");
                Date dateMin = simpleDateFormat.parse("00:00");
                difference=(dateMax.getTime() -startDate.getTime() )+(endDate.getTime()-dateMin.getTime());
            }
            int days = (int) (difference / (1000*60*60*24));
            int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
            int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
            Log.i("log_tag","Hours: "+hours+", Mins: "+min);

            if (days == 0 && hours == 0){
                if (min > max){
                    isCorrect = true;
                }else {
                    isCorrect = false;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return isCorrect;
    }

    public static long dateDiff(String current_date, String dateTime){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        long diff = 0;

        try {
            //Convert to Date
            Date startDate = df.parse(current_date);
            Calendar c1 = Calendar.getInstance();
            //Change to Calendar Date
            c1.setTime(startDate);

            //Convert to Date
            Date endDate = df.parse(dateTime);
            Calendar c2 = Calendar.getInstance();
            //Change to Calendar Date
            c2.setTime(endDate);

            //get Time in milli seconds
            long ms1 = c1.getTimeInMillis();
            long ms2 = c2.getTimeInMillis();
            //get difference in milli seconds
            diff = ms2 - ms1;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Find number of days by dividing the mili seconds
        int diffInDays = (int) (diff / (24 * 60 * 60 * 1000));
        Log.e("DateTime", "Date1: "+current_date+"\tNextDate: "+dateTime+"\t Diff: "+diff);
        return diff;
    }
}
