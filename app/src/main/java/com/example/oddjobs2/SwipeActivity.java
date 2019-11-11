package com.example.oddjobs2;

import java.util.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View;
import android.view.MotionEvent;
import com.google.android.flexbox.FlexboxLayout;
import androidx.appcompat.app.AppCompatActivity;

public class SwipeActivity extends AppCompatActivity {

    private LinearLayout base;
    private FlexboxLayout skillSet;

    // Source: https://stackoverflow.com/questions/6645537/how-to-detect-the-swipe-left-or-right-in-android
    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    // Source end

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        base = findViewById(R.id.linear_layout_swipe);
        skillSet = findViewById(R.id.skill_set_swipe);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(extras.getString("userChoice").equals("workRequest")){
                pullUserData();
            }
            else if(extras.getString("userChoice").equals("workSearch")){
                pullJobData();
            }
            else{
                //throw some exception
            }
        }
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
                startActivity(new Intent(this, ViewProfile.class));
                return true;
            case R.id.menu_job_list:
                startActivity(new Intent(this, JobListActivity.class));
                return true;
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void pullUserData() {
        String name = "Bob Smith";
        String age = "35";
        String bio = "Hello I am Bob Smith, looking for work!";
        List<String> skills = new ArrayList<String>();
        skills.add("fishing");
        skills.add("drawing");
        skills.add("surfing");

        TextView userName = new TextView(getApplicationContext());
        userName.setText("Name: " + name);
        TextView userAge = new TextView(getApplicationContext());
        userAge.setText("Age: " + age);
        TextView userBio = new TextView(getApplicationContext());
        userBio.setText("Bio: " + bio);

        displaySkills(skills);

        base.addView(userName);
        base.addView(userAge);
        base.addView(userBio);

    }

    public void pullJobData(){
        String title = "Toilet plumber";
        String price = "$100";
        String bio = "Looking for toilet plumber!";
        List<String> skills = new ArrayList<String>();
        skills.add("fishing");
        skills.add("drawing");
        skills.add("surfing");

        TextView jobTitle = new TextView(getApplicationContext());
        jobTitle.setText("Title: " + title);
        TextView jobPrice = new TextView(getApplicationContext());
        jobPrice.setText("Price: " + price);
        TextView jobBio = new TextView(getApplicationContext());
        jobBio.setText("Bio: " + bio);

        displaySkills(skills);

        base.addView(jobTitle);
        base.addView(jobPrice);
        base.addView(jobBio);
    }

    public void displaySkills(List<String> skills){

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
