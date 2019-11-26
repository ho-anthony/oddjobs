package com.example.oddjobs2;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

//Database Helper Class
public class DH {
    /*
    // DEPRECATED

    @IgnoreExtraProperties
    public static class User {
        public String name;
        public String bio;

        public User(String myName){
            name = myName;
            bio = "";

        }
    }
    */

    private FirebaseDatabase mDatabase;
    //private DatabaseReference mActiveUsers;
    //private DatabaseReference mSkillMappings;
    //private DatabaseReference mActiveJobs;
    private DatabaseReference mUsers;
    private DatabaseReference mJobs;


    public DH(){
        mDatabase = FirebaseDatabase.getInstance();
        mUsers = mDatabase.getReference("Users");
        mJobs = mDatabase.getReference("Jobs");
    }

    public String newUser(String name, String bio){
        String userKey = mUsers.push().getKey();
        mUsers.child(userKey).child("userName").setValue(name);
        mUsers.child(userKey).child("userBio").setValue(bio);
        // Default item in skills/ jobs set is "N/A" mapped to false
        mUsers.child(userKey).child("userSkills").child("NONE").setValue(false);
        mUsers.child(userKey).child("userJobs").child("NONE").setValue(false);

        return userKey;
    }

    public String newJob(String title, String posterKey){
        String jobKey = mJobs.push().getKey();
        mJobs.child(jobKey).child("jobTitle").setValue(title);
        mJobs.child(jobKey).child("jobPosterKey").setValue(posterKey);
        mJobs.child(jobKey).child("jobSkills").child("NONE").setValue(false);
        // Taker = person who accepted the job. If it is true, the job is taken off "active Jobs"
        mJobs.child(jobKey).child("jobTakerKey").setValue(false);

        return jobKey;
    }
}


