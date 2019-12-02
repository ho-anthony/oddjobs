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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View;
import android.view.MotionEvent;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class SwipeActivity extends AppCompatActivity {

    private LinearLayout base;
    private FlexboxLayout skillSet;
    private ImageView profilePic;
    private Context mContext;
    Button post;
    EditText title, des, pay;
    DH dh = new DH();
    ArrayList<String> mySkills;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUsers;
    private DatabaseReference mJobs;
    AddSkillsFragment addSkills = new AddSkillsFragment();

    // Source: https://stackoverflow.com/questions/6645537/how-to-detect-the-swipe-left-or-right-in-android
    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    String location = "";
    double latitude, longitude;
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
        mySkills = new ArrayList<>();

        base = findViewById(R.id.linear_layout_swipe);
        skillSet = findViewById(R.id.skill_set_swipe);

        userData = new ArrayList<String>();
        jobData = new ArrayList<String>();
        userSkills = new ArrayList<String>();

        // https://developers.google.com/places/android-sdk/start
        // Initialize the SDK
        Places.initialize(getApplicationContext(), "AIzaSyCj0-7H-hAXU1n8fuvTr9_tH6zK2SVZ7uM");
        final PlacesClient placesClient = Places.createClient(this);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(extras.getString("userChoice").equals("workRequest")){
                pullUserData();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                checkActiveJob(uid);
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
            case R.id.menu_sign_out:
                startActivity(new Intent(this, LoginActivity.class));
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

    public void showPopup(){
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        findViewById(R.id.background).post(new Runnable() {
            public void run() {
                // show the popup window
                popupWindow.showAtLocation(findViewById(R.id.background), Gravity.CENTER, 0, 0);
            }
        });

        post = popupView.findViewById(R.id.popup_post);
        title = popupView.findViewById(R.id.popup_title);
        des = popupView.findViewById(R.id.popup_des);
        pay = popupView.findViewById(R.id.popup_pay);




        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_popup);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                location = place.getName();
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                Log.i("fatDebug", "Place: " + place.getName() + ", " + place.getLatLng());

            }

            @Override
            public void onError(Status status) {
                Log.i("fatDebug", "An error occurred: " + status);
            }
        });

        // Store information and close popup window
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t, d, p, l;
                if(title.length()==0||des.length()==0||pay.length()==0||location.length()==0){
                    Toast.makeText(SwipeActivity.this, "Please fill in all information.", Toast.LENGTH_SHORT).show();
                } else {
                    t = title.getText().toString();
                    d = des.getText().toString();
                    p = pay.getText().toString();
                    float newP = Float.parseFloat(p);
                    // Do something with the information
                    //String newP = "$" + p;
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    //ArrayList<String> skills;
                    //skills = addSkills.getSkills();
                    Log.i("myTag", "newJob: "+mySkills);
                    dh.newJob(uid,t,d,newP,location,latitude,longitude,mySkills);
                    popupWindow.dismiss();
                }
            }
        });
    }

    public void checkActiveJob (String uid){
        mDatabase = FirebaseDatabase.getInstance();
        mUsers = mDatabase.getReference("Users");
        mJobs = mDatabase.getReference("Jobs");
        DatabaseReference job = mUsers.child(uid).child("userCurrentJob");
        job.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jobID = dataSnapshot.getValue(String.class);
                if(jobID.equals("NONE")){
                    showPopup();
                    return;
                }
                Log.i("myTag", "onDataChange:" + jobID);
                DatabaseReference active = mJobs.child(jobID).child("active");
                active.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot1) {
                        Boolean end = dataSnapshot1.getValue(Boolean.class);
                        if(!end){
                            showPopup();
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

}
