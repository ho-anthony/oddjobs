package com.example.oddjobs2;

import android.app.Application;

// https://stackoverflow.com/questions/1944656/android-global-variable
public class MyApp extends Application {
    private int user_choice;
    public final static int EMPLOYER = 0;
    public final static int EMPLOYEE = 1;

    public void set_user_choice(int choice){
        user_choice = choice;
    }
    public int get_user_choice(){
        return user_choice;
    }
}
