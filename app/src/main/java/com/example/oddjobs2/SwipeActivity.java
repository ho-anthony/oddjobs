package com.example.oddjobs2;

import java.util.*;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
    private ImageView profilePic;
    private Context mContext;

    // Source: https://stackoverflow.com/questions/6645537/how-to-detect-the-swipe-left-or-right-in-android
    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    // Source end

    //Constant hardcoded stuff for now ----------
    private ArrayList<String> userData;
    private ArrayList<String> jobData;
    private ArrayList<String> userSkills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        mContext = SwipeActivity.this;

        base = findViewById(R.id.linear_layout_swipe);
        skillSet = findViewById(R.id.skill_set_swipe);

        userData = new ArrayList<String>();
        jobData = new ArrayList<String>();
        userSkills = new ArrayList<String>();


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
        else{
            //By default pull user data for now. For testing
            pullUserData();
        }
        // https://developer.android.com/training/transitions





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



        //HARDCODED DATA FOR NOW------------------------
        userData.add("John Smith");
        userData.add("Age: 37");
        userData.add("Location: 30 miles from you");
        userData.add("Bio: I love cats!");

        userSkills.add("fishing");
        userSkills.add("drawing");
        userSkills.add("Some kind of long, complex skill");
        userSkills.add("CARPENTRY");
        userSkills.add("Benchpressing");

        generateLayout(userData);
        displaySkills(userSkills);

    }

    public void pullJobData(){
        //Pull all data from database and convert displayed text to string format.
        //HARDCODED DATA FOR NOW------------------------
        userSkills.add("fishing");
        userSkills.add("drawing");
        userSkills.add("Some kind of long, complex skill");
        userSkills.add("CARPENTRY");
        userSkills.add("Benchpressing");

        jobData.add("Pancake Maker");
        jobData.add("Location: 0.5 miles from you");
        jobData.add("Description: Looking for a good chef.");
        generateLayout(jobData);
        displaySkills(userSkills);
    }

    public void generateLayout(ArrayList<String> data){
        for(int i=0; i<data.size(); i++){
            TextView text = new TextView(getApplicationContext());
            text.setText(data.get(i));

            text.setTextColor(Color.GRAY);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 20, 0, 20);
            params.gravity = Gravity.CENTER_HORIZONTAL;

            //https://stackoverflow.com/questions/14343903/what-is-the-equivalent-of-androidfontfamily-sans-serif-light-in-java-code
            text.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
            //change this to sp later on.
            text.setTextSize(20);

            text.setLayoutParams(params);

            base.addView(text);
        }

    }

    public void displaySkills(List<String> skills){
        for(int i=0; i<skills.size(); i++){
            TextView skill_text = new TextView(mContext);
            skill_text.setText(skills.get(i));
            skill_text.setTextColor(Color.WHITE);
            if(i%2==0){
                //Alternate colors.
                skill_text.setBackgroundResource(R.drawable.teal_button_bg);
            }
            else{
                skill_text.setBackgroundResource(R.drawable.yellow_button_bg);
            }
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 5, 10, 5);
            skill_text.setLayoutParams(params);
            skill_text.setPadding(30,30,30,30);
            skillSet.addView(skill_text);
        }

    }

    // Source used: https://stackoverflow.com/questions/6645537/how-to-detect-the-swipe-left-or-right-in-android
    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
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
                        // https://stackoverflow.com/questions/3053761/reload-activity-in-android
                        finish();
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        startActivity(getIntent());
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        Toast.makeText(SwipeActivity.this, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show ();
                    }

                    // Right to left swipe action
                    else
                    {
                        // https://stackoverflow.com/questions/3053761/reload-activity-in-android
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        startActivity(getIntent());
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        Toast.makeText(SwipeActivity.this, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show ();
                    }

                }
                else
                {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }
    // Source end


}
