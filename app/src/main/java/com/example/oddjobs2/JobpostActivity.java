package com.example.oddjobs2;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.os.Bundle;

public class JobpostActivity extends AppCompatActivity {

    EditText jobTitle, jobCost, jobDescription;
    Button postJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobpost);
        jobTitle = findViewById(R.id.job_title);
        jobCost = findViewById(R.id.job_cost);
        jobDescription = findViewById(R.id.job_description);
        postJob = findViewById(R.id.post_job);

        postJob.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                postJobEvent(v);
            }
        });
    }

    public void postJobEvent(View view){
        Toast.makeText(this, "job title: " + jobTitle.getText(), Toast.LENGTH_SHORT).show ();
        //Job cost should check if valid number.
        Toast.makeText(this, "job cost: " + jobCost.getText(), Toast.LENGTH_SHORT).show ();
        Toast.makeText(this, "job descripton: " + jobDescription.getText(), Toast.LENGTH_SHORT).show ();

    }

}
