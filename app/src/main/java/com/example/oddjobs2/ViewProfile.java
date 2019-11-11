package com.example.oddjobs2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ViewProfile extends AppCompatActivity {

    TextView fname, lname, age, location, email, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        age = findViewById(R.id.age);
        location = findViewById(R.id.location);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phoneNum);
    }
}
