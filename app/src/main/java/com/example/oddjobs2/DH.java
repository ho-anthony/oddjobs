package com.example.oddjobs2;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

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
    private DatabaseReference mActiveUsers;
    private DatabaseReference mSkillMapUsers;
    private DatabaseReference mSkillMapJobs;
    private DatabaseReference mActiveJobs;
    private DatabaseReference mUsers;
    private DatabaseReference mJobs;


    public DH(){
        mDatabase = FirebaseDatabase.getInstance();
        mActiveUsers = mDatabase.getReference("ActiveUsers");
        mUsers = mDatabase.getReference("Users");
        mJobs = mDatabase.getReference("Jobs");
        mSkillMapUsers = mDatabase.getReference("SkillMapUsers");
        mSkillMapJobs = mDatabase.getReference("SkillMapJobs");
        mActiveJobs = mDatabase.getReference("ActiveJobs");

    }

    /*
     * Creates an brand new user.
     * Returns the unique database key for the user created.
     */
    public void newUser(
            String UID,
            String firstName,
            String lastName,
            String age,
            String location,
            String phone,
            ArrayList<String> skills
    ){
        String userKey = UID;
        mUsers.child(userKey).child("firstName").setValue(firstName);
        mUsers.child(userKey).child("lastName").setValue(lastName);
        mUsers.child(userKey).child("age").setValue(age);
        mUsers.child(userKey).child("location").setValue(location);
        // Default item in skills/ jobs set is "N/A" mapped to false

        mUsers.child(userKey).child("phone").setValue(phone);

        mUsers.child(userKey).child("userSkills").child("NONE").setValue(false);
        for(String skill : skills){
            mUsers.child(userKey).child("userSkills").child(skill).setValue(true);
            mSkillMapUsers.child(skill).child(userKey).setValue(true);
        }

        mUsers.child(userKey).child("userOldJobs").child("NONE").setValue(false);
        mUsers.child(userKey).child("userCurrentJob").setValue("NONE");

    }

    /*
     * Creates an brand new Job. First argument is job poster's user key
     * Depending on how login is implemented, this may need to be refactored to store login credentials
     */
    public void newJob(String UID,
                       String title,
                       String description,
                       float price,
                       String location,
                       ArrayList<String> skills
    ){
        String posterKey = UID;
        String jobKey = mJobs.push().getKey();
        mJobs.child(jobKey).child("jobPosterKey").setValue(posterKey);
        mJobs.child(jobKey).child("jobTitle").setValue(title);
        mJobs.child(jobKey).child("jobPrice").setValue(price);
        mJobs.child(jobKey).child("jobDescription").setValue(description);
        mJobs.child(jobKey).child("jobLocation").setValue(location);

        mJobs.child(jobKey).child("jobSkills").child("NONE").setValue(false);
        for(String skill : skills){
            mJobs.child(jobKey).child("jobSkills").child(skill).setValue(true);
            mSkillMapJobs.child(skill).child(jobKey).setValue(true);
        }

        // Taker = person who accepted the job. If it is true, the job is removed from "active Jobs"
        mJobs.child(jobKey).child("jobTakerKey").setValue(false);
        mJobs.child(jobKey).child("active").setValue(true);

        mUsers.child(posterKey).child("userCurrentJob").setValue(jobKey);

        mActiveJobs.child(jobKey).setValue(false);
    }

    private boolean end;
        public boolean checkActiveJob (String uid){
            end = false;
            DatabaseReference job = mUsers.child(uid).child("userCurrentJob");
            job.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String jobID = dataSnapshot.getValue(String.class);
                    Log.i("myTag", "onDataChange:" + jobID);
                    DatabaseReference active = mJobs.child(jobID).child("active");
                    active.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {
                            end = dataSnapshot1.getValue(Boolean.class);
                            Log.i("myTag", "onDataChange:" + end);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            throw databaseError.toException();
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw databaseError.toException();
                }
            });
            Log.i("myTag", "onDataChange:" + end);
            return end;
        }


    public void acceptJob(){
        // Job is taken off ActiveJobs
    }

    public void updateUser(){
        // make sure to remove the skill mappings in the SkillMapUsers if user skills change
    }

    public void updateJob(){
        // make sure to remove the skill mappings in the SkillMapJobss if job skills change
    }

}



