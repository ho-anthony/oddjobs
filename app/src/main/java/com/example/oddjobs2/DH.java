package com.example.oddjobs2;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

//Database Helper Class
public class DH {
    @IgnoreExtraProperties
    public static class User {
        public String name;
        public String bio;
        public User(String myName, String myBio){
            name = myName;
            bio = myBio;
        }
    }

    private FirebaseDatabase mDatabase;
    private DatabaseReference mActiveUsers;

    public DH(){
        mDatabase = FirebaseDatabase.getInstance();
        mActiveUsers = mDatabase.getReference("ActiveUsers");
    }

    public void test(){

        User test = new User("Lisa","hi im lisa");
        String key = mActiveUsers.push().getKey();
        mActiveUsers.child(key).setValue(test);



    }
}


