package com.example.john.c_transportation.dbOperations;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.john.c_transportation.core.DBHelper;
import com.example.john.c_transportation.utils.Constants;

import static com.example.john.c_transportation.utils.Constants.config.PICTURE_ID;
import static com.example.john.c_transportation.utils.Constants.config.PICTURE_NAME;
import static com.example.john.c_transportation.utils.Constants.config.PICTURE_STATUS;
import static com.example.john.c_transportation.utils.Constants.config.REGISTRATION_ID;


/**
 * Created by john on 4/20/18.
 */

public class Pictures {
    private Context context;
    private static final String TAG = "Pictures";
    public Pictures(Context context){
        this.context = context;
    }

    public String save(int registration_id,String picture_name, int picture_status) {
        SQLiteDatabase database = DBHelper.getHelper(context).getWritableDatabase();
        String message = null;
        try{
            //database.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(REGISTRATION_ID,registration_id);
            contentValues.put(PICTURE_NAME,picture_name);
            contentValues.put(PICTURE_STATUS,picture_status);
            database.insert(Constants.config.TABLE_PICTURE, null, contentValues);
            //database.setTransactionSuccessful();
            message = "pictures Details saved!";

        }catch (Exception e){
            e.printStackTrace();
            message = "Sorry, error: "+e;
        }finally {
            //database.close();
            // database.endTransaction();
        }
        return message;
    }

    public String edit(int picture_id,int registration_id,String picture_name, String picture_status, String registration_number, String semester, String course, int registration_status) {
        SQLiteDatabase database = DBHelper.getHelper(context).getWritableDatabase();
        String message = null;
        try{
            //database.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(REGISTRATION_ID,registration_id);
            contentValues.put(PICTURE_NAME,picture_name);
            contentValues.put(PICTURE_STATUS,picture_status);
            database.update(Constants.config.TABLE_PICTURE
                    ,contentValues,PICTURE_ID+"="+picture_id, null);
            message = "picture details updated!";

        }catch (Exception e){
            e.printStackTrace();
            message = "Sorry, error: "+e;
        }finally {
            //database.close();
            // database.endTransaction();
        }
        return message;
    }
}
