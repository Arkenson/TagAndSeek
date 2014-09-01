package com.example.myapplication.drawyourcity.app;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;

/**
 * Created by Caroline Arkenson on 2014-05-02.
 *
 * Handles the connection to Parse Database
 */
public class app extends Application {

    @Override public void onCreate(){
        super.onCreate();

        Parse.initialize(this, "3kYO5H8HgwFUEZAOnCmzcUt3fOAoGdwUptrUn98I", "22uLwq50zRl28RaJC6VgLmLA5MPhIN2FA8vFDxwK"); //Parse Application ID and Application Key
    }
}
