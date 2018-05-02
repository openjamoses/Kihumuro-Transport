package com.example.john.c_transportation.activities.single_chatts;

import java.util.UUID;

/**
 * Created by john on 4/22/18.
 */

public class USERID {
    public static String returnID(){
        String uniqueID = UUID.randomUUID().toString();
        return  uniqueID;
    }
}
