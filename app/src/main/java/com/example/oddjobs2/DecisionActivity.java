package com.example.oddjobs2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DecisionActivity extends AppCompatActivity {
    Button workRequestButton;
    Button workSearchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision);
        workRequestButton = findViewById(R.id.workRequestButton);
        workSearchButton = findViewById(R.id.workSearchButton);
    }

    public void navWorkRequest(View v) {
        //Toast.makeText(this, "Navigates to employer screen", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this, SwipeActivity.class);
        i.putExtra("userChoice", "workRequest");
        startActivity(i);


    }

    public void navWorkSearch(View v) {
        //Toast.makeText(this,"Navigates to employee screen", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this, SwipeActivity.class);
        i.putExtra("userChoice", "workSearch");
        startActivity(i);


    }
}
