package com.example.john.c_transportation.activities.single_chatts;

/**
 * Created by john on 3/29/18.
 */

public class Chatt_Users {


    private String name;
    private String imageUrl;

    public Chatt_Users() {
    }

    public Chatt_Users(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
