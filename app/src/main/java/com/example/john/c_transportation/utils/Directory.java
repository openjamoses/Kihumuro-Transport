package com.example.john.c_transportation.utils;

import android.os.Environment;

import java.io.File;

import static com.example.john.c_transportation.utils.Constants.config.APP_FOLDER;
import static com.example.john.c_transportation.utils.Constants.config.IMAGE_SUB_FOLDER;


/**
 * Created by john on 1/3/18.
 */
public class Directory {
    public static File create(){
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
       // System.out.println(root +" Root value in saveImage Function");
        File myDir = new File(root + "/"+APP_FOLDER);
        File photo = new File(root + "/"+APP_FOLDER+"/"+IMAGE_SUB_FOLDER);
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        if (!photo.exists()) {
            photo.mkdirs();
        }
        return photo;
    }
}
