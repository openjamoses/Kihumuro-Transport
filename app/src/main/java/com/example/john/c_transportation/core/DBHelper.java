package com.example.john.c_transportation.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.util.Log;

import com.example.john.c_transportation.utils.Constants;


public class DBHelper extends SQLiteOpenHelper {
    private final Handler handler;
    private static DBHelper instance;
    public static synchronized DBHelper getHelper(Context context)
    {
        if (instance == null)
            instance = new DBHelper(context);
        return instance;
    }
    public DBHelper(Context context) {
        super(context, Constants.config.DB_NAME, null, Constants.config.DB_VERSION);
        handler = new Handler(context.getMainLooper());
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO : Creating tables
        db.execSQL(Create_Table.create.CREATE_REGISTRATION);
        db.execSQL(Create_Table.create.CREATE_PICTURE);
        Log.e("DATABASE OPERATION",Constants.config.TOTAL_TABLES+" Tables  created / open successfully");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: Updating table here
        db.execSQL("DROP TABLE IF EXISTS "+ Constants.config.TABLE_REGISTRATION);
        db.execSQL("DROP TABLE IF EXISTS "+ Constants.config.TABLE_PICTURE);
       
        onCreate(db);
        Log.e("DATABASE OPERATION", Constants.config.TOTAL_TABLES+" Table created / open successfully");

    }
    /************** Insertion ends here **********************/
}