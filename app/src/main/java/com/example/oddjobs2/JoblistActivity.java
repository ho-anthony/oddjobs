package com.example.oddjobs2;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.os.Bundle;

public class JoblistActivity extends AppCompatActivity {

    private ImageButton addJob, loadJobs;
    private ScrollView jobListBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joblist);
        addJob = findViewById(R.id.add_job);
        loadJobs = findViewById(R.id.load_jobs);
        jobListBox = findViewById(R.id.job_list_box);

        // Source Referenced: https://stackoverflow.com/questions/10673628/implementing-onclicklistener-for-dynamically-created-buttons-in-android
        addJob.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                addJobEvent(v);
            }
        });
        loadJobs.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                loadJobsEvent(v);
            }
        });
        //End Source
    }

    public void loadJobsEvent(View view){
        Toast.makeText(this, "Load Job Button Clicked", Toast.LENGTH_SHORT).show ();
    }

    public void addJobEvent(View view){
        Toast.makeText(this, "Add Job Button Clicked", Toast.LENGTH_SHORT).show ();
    }

    public void generateJobList(){
        //Generate jobs objects in jobListBox
    }
}
