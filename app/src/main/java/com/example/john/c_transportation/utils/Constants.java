package com.example.john.c_transportation.utils;

/**
 * Created by john on 12/30/17.
 */

public class Constants {
    public abstract class config {
        /****** URL DECLARATION ******************************/
        public static final String DB_NAME = "registration_db";
        public static final int DB_VERSION = 1;
        public static final int TOTAL_TABLES = 2;

        public static final String APP_FOLDER = "student_registration";
        public static final String IMAGE_SUB_FOLDER = "photos";

        public static final String TABLE_REGISTRATION = "registration_tb";
        public static final String REGISTRATION_ID = "registration_id";
        public static final String REGISTRATION_DATE = "registration_date";
        public static final String REGISTRATION_TIME = "registration_time";
        public static final String REGISTRATION_NUMBER = "registration_number";
        public static final String SEMESTER = "semester";
        public static final String COURSE = "course";
        public static final String REGISTRATION_STATUS = "registration_status";

        public static final String TABLE_PICTURE = "picture_tb";
        public static final String PICTURE_ID = "picture_id";
        public static final String PICTURE_NAME = "picture_name";
        public static final String PICTURE_STATUS = "picture_status";
        //TODO::: FIREBASE FULL SETUP
        public static final String VIDEO_SUB_FOLDER = "videos";
        public static final String AUDIO_SUB_FOLDER = "audio";
        public static final String PROFILE_PATIENT = "patient_img";
        public static final String PROFILE_DOCTOR = "doctor_img";

        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String PASSWORD = "password";
        public static final String USER_PHOTO = "user_photo";
        public static final String USER_TYPE = "user_type";
        public static final String ONLINE = "online";
        public static final String STAGES = "stages";
        public static final String STAGES_NAME = "stages_name";
        public static final String STAGES_DECRIPTION = "stages_description";
        public static final String STAGES_NUMBER = "stages_number";
        public static final String STAGES_STATUS = "stages_status";



        public static final String MESSAGES = "messages";
        public static final String USERS = "users";
        public static final String CONDUCTORS = "Conductor";
        public static final String REPLY = "reply";

        public static final String STUDENT_TYPE = "Student";
        public static final String CONDUCTOR_TYPE = "Conductor";

        public static final String BODY = "body";
        public static final String SHOW_MODE_SHOW = "show";
        public static final String SHOW_MODE_HIDE = "hide";
        public static final String DATETIME = "date_time";
        public static final String DATE = "date";
        public static final String TIME = "time";
        public static final String USERNAME = "username";

        public static final String STORAGE_PATH_UPLOADS = "photos/";
        public static final String DATABASE_PATH_UPLOADS = "uploads";

        public static final String FIREBASE_URL = "https://foodscanner-466e0.firebaseio.com/";

        public static final String STORAGE_REFERENCE_CHATT = "gs://mobicarev3.appspot.com/chatts/";
        public static final String STORAGE_REFERENCE_PROFILE = "gs://mobicarev3.appspot.com/profiles/";

        // global topic to receive app wide push notifications
        public static final String TOPIC_GLOBAL = "global";

        // broadcast receiver intent filters
        public static final String REGISTRATION_COMPLETE = "registrationComplete";
        public static final String PUSH_NOTIFICATION = "pushNotification";

        // id to handle the notification in the notification tray
        public static final int NOTIFICATION_ID = 100;
        public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

        public static final String SHARED_PREF = "ah_firebase";
        public static final String KEY_TOKEN = "regId";
        public static final String IMEI = "imei";
        public static final String DEVICES = "devices";
        public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
        public static final String UID = "AAAALBVkRSA:APA91bFmhm26qDteRrxDHr7x1wWRxXRFkaQn3KjPAHwvoBA-Ypus8N52r-9f4F2ps5epndJQeCMWPPQrM85fkFniYWLjzSOJAiAPyLGkdlSNSiitG12lbgNp7Gb_p9WQTrX9h64jjuJV";

    }
}
