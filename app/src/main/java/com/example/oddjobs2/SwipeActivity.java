package com.example.oddjobs2;

import java.util.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View;
import android.view.MotionEvent;
import com.google.android.flexbox.FlexboxLayout;
import androidx.appcompat.app.AppCompatActivity;

public class SwipeActivity extends AppCompatActivity {

    private ImageView userPhoto;
    private TextView userName, userAge, userGender, userLocation, userBio;
    private FlexboxLayout skillSet;

    // Source: https://stackoverflow.com/questions/6645537/how-to-detect-the-swipe-left-or-right-in-android
    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    // Source end

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        userPhoto = findViewById(R.id.user_photo);
        userName = findViewById(R.id.user_name);
        userAge = findViewById(R.id.user_age);
        userGender = findViewById(R.id.user_gender);
        userLocation = findViewById(R.id.user_location);
        userBio = findViewById(R.id.user_bio);
        skillSet = findViewById(R.id.skill_set);

        pullUserData();
    }

    // https://developer.android.com/guide/topics/ui/menus
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.base_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            case R.id.menu_job_list:
                startActivity(new Intent(this, JobListActivity.class));
                return true;
            case R.id.menu_settings:
                Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void pullUserData() {
        //Pull next user data from database
        //This function is called when user swipes screen or when app first loads.
        //Hardcoded for now.
        userName.setText("Next User");
        userAge.setText("40");
        userGender.setText("F");
        userLocation.setText("Santa Cruz");
        String long_test_text = "Testing a looooooooooooooooooooooooooooooo" +
                "00000000000000000000000000000000000000000000000000" +
                "00000000000000000000000000000000000000000000000000" +
                "00000000000000000000000000000000000000000000000000" +
                "00000000000000000000000000000000000000000000000000" +
                "00000000000000000000000000000000000000000000000000" +
                "ong bio";
        userBio.setText(long_test_text);

        //This list is added to https://github.com/google/flexbox-layout
        List<String> skills = new ArrayList<String>();
        skills.add("fishing");
        skills.add("biking");
        skills.add("car repair");
        skills.add("flying");
        skills.add("a very long skill, just to test");
        skills.add("burping");
        skills.add("fishing");
        skills.add("a very long skill, just to test");
        skills.add("burping");
        skills.add("car repair");
        skills.add("flying");
        skills.add("a very long skill, just to test");
        skills.add("burping");
        skills.add("fishing");
        skills.add("a very long skill, just to test");
        skills.add("burping");
        skills.add("fishing");
        skills.add("biking");
        skills.add("fishing");
        skills.add("biking");
        skills.add("fishing");
        skills.add("biking");

        int counter = 0;
        for(String skill : skills){
            TextView skill_text = new TextView(getApplicationContext());
            skill_text.setText(skill);
            if(counter%2==0){
                //Alternate colors.
                skill_text.setBackgroundResource(R.color.tealButton);
            }
            else{
                skill_text.setBackgroundResource(R.color.yellowButton);
            }
            skill_text.setPadding(10,5,5,10);
            skillSet.addView(skill_text);
            counter++;
        }
    }

    // Source used: https://stackoverflow.com/questions/6645537/how-to-detect-the-swipe-left-or-right-in-android
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    // Left to Right swipe action
                    if (x2 > x1)
                    {
                        Toast.makeText(this, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show ();
                    }

                    // Right to left swipe action
                    else
                    {
                        Toast.makeText(this, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show ();
                    }
                }
                else
                {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }
    // Source end


}
