package com.example.oddjobs2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JobListActivity extends AppCompatActivity {

    FloatingActionButton fab;
    PopupWindow puw;
    Button post;
    EditText title, des, pay, location;
    ListView listView;
    static TextView text, text2, text3, text6;
    //ArrayList<String> catalogue = new ArrayList<>();
    ArrayList<DataModel> catalogue = new ArrayList<>();
    static MyCustomAdapter adapter;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUsers;
    private DatabaseReference mJobs;
    String jobID;
    String status;

    public static void checkEmpty() {
        if(adapter.getCount() == 0){
            text.setText(R.string.no_jobs);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);
        checkActiveJob();
    }

    // Function to show the images on the listView by finding them in the database
    public void showListView(String t, String d, String p, String l){
        //ArrayList<DataModel> catalogue = new ArrayList<>();
        listView = findViewById(R.id.jobListView);
        text = findViewById(R.id.jobListStatus);
        // While there is data to read, add it to the listView
        catalogue.add(new DataModel(t,d,p,l));
        //}
        // Instantiate custom adapter
        text.setVisibility(View.GONE);
        adapter = new MyCustomAdapter(catalogue, this);
        listView.setAdapter(adapter);
    }


    public void checkActiveJob (){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mUsers = mDatabase.getReference("Users");
        mJobs = mDatabase.getReference("Jobs");
        DatabaseReference job = mUsers.child(uid).child("userCurrentJob");
        job.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                jobID = dataSnapshot.getValue(String.class);
                if(jobID.equals("NONE")){
                    return;
                }
                DatabaseReference active = mJobs.child(jobID).child("active");
                active.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot1) {
                        Boolean active = dataSnapshot1.getValue(Boolean.class);
                        /*if(active){
                            getJobInfo();
                        }*/
                        if(active){
                            DatabaseReference taker = mJobs.child(jobID).child("jobTakerKey");
                            taker.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    try {
                                        Boolean key = dataSnapshot.getValue(Boolean.class);
                                        if(!key){
                                            status = "No Match";
                                            getJobInfo();
                                        }
                                    } catch (Exception e) {
                                    }
                                    try {
                                        String key = dataSnapshot.getValue(String.class);
                                        if(key != null){
                                            status = "In Progress";
                                            getJobInfo();
                                        }
                                    } catch (Exception e) {
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    throw databaseError.toException();
                                }
                            });
                        }
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
    }

    public void getJobInfo(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mUsers = mDatabase.getReference("Users");
        mJobs = mDatabase.getReference("Jobs");
        DatabaseReference job = mUsers.child(uid).child("userCurrentJob");
        job.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jobID = dataSnapshot.getValue(String.class);
                DatabaseReference active = mJobs.child(jobID).child("jobTitle");
                active.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot1) {
                        String end = dataSnapshot1.getValue(String.class);
                        showListView(end, status, end, end);
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
    }
}
