package com.example.instagram;

import android.app.Application;
import android.util.Log;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ParseApplication extends Application {
    // initializes parse sdk as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        // register your parse models
        //ParseObject.registerSubclass(Post.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("nMUN4v5CR6HAody8ZlTjNxaQtYWo2oNEGy1kPcXb")
                .clientKey("vCvVxXWDGOazayDrHUnqZUvTCTHaveuPGL3487hM")
                .server("https://parseapi.back4app.com")
                .build());
    }
}
