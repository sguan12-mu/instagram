package com.example.instagram;

import android.app.Application;

import com.example.instagram.models.Comment;
import com.example.instagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    // initializes parse sdk as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        // register your parse models
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comment.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("nMUN4v5CR6HAody8ZlTjNxaQtYWo2oNEGy1kPcXb")
                .clientKey("vCvVxXWDGOazayDrHUnqZUvTCTHaveuPGL3487hM")
                .server("https://parseapi.back4app.com")
                .build());
    }
}
