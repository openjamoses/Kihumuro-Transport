package com.example.john.c_transportation.core;


import com.example.john.c_transportation.utils.Constants;

/**
 * Created by john on 8/12/17.
 */

public class Create_Table {
    public abstract class create{
        //todo: queries to create the table
        public static final String CREATE_REGISTRATION=
                "CREATE TABLE "+ Constants.config.TABLE_REGISTRATION +" ("+ Constants.config.REGISTRATION_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " "+Constants.config.REGISTRATION_DATE+" TEXT,"+Constants.config.REGISTRATION_TIME+" TEXT, "+Constants.config.REGISTRATION_NUMBER+" TEXT, " +
                        " "+Constants.config.SEMESTER+" TEXT,"+Constants.config.COURSE+" TEXT,"+Constants.config.REGISTRATION_STATUS+" TEXT );";
        public static final String CREATE_PICTURE =
                "CREATE TABLE "+ Constants.config.TABLE_PICTURE +" ("+ Constants.config.PICTURE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " "+Constants.config.PICTURE_NAME+" TEXT,"+Constants.config.REGISTRATION_ID+" INTEGER,"+Constants.config.PICTURE_STATUS+" INTEGER);";
    }
}
