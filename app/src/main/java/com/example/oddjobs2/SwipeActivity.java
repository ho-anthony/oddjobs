package com.example.oddjobs2;

import java.util.*;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

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
    Set <String> mySkills;
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

    private Set<String> seenKeys;

    private Map<String, Integer> keys;
    private ArrayList<String> sortedKeys;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        mContext = SwipeActivity.this;
        mySkills = new HashSet<String>();
        sortedKeys = new ArrayList<String>();
        index = 0;


        keys = new Hashtable<>();
        profilePic = findViewById(R.id.swipe_profile_pic);

        base = findViewById(R.id.linear_layout_swipe);
        skillSet = findViewById(R.id.skill_set_swipe);
        seenKeys = new HashSet<String>();

        // https://developers.google.com/places/android-sdk/start
        // Initialize the SDK
        Places.initialize(getApplicationContext(), "AIzaSyCj0-7H-hAXU1n8fuvTr9_tH6zK2SVZ7uM");
        final PlacesClient placesClient = Places.createClient(this);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(extras.getStringArrayList("seenKeys") != null){
                ArrayList<String> seenKeysArray = extras.getStringArrayList("seenKeys");
                seenKeys = new HashSet<String>(seenKeysArray);
            }
            if(extras.getString("userChoice").equals("workRequest")){
                pullUserData();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                checkActiveJob(uid);
            }
            else if(extras.getString("userChoice").equals("workSearch")){
                pullJobData();
                //https://stackoverflow.com/questions/41664409/wait-for-5-seconds/41664445
                //TODO: FIX THIS HARDCODED WAIT
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Log.d("fatDebug", "runnable running");
                        // TODO: ASSUMING KEYS IS POPULATED
                        displayJobKeys();
                    }
                }, 2000);
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
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void pullUserData() {

    }

    public void pullJobData(){
        // TODO: Get skills that user requires
        // TODO: query job to skill mapping for all available jobs
        // TODO: sort jobs based on how many times they're seen.
        Log.d("fatDebug", "Pulling job data");
        final DH dh = new DH();
        String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query qSkills = dh.mUsers.child(userKey).child("userSkills");
        qSkills.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    String skill = data.getKey();
                    Log.d("fatDebug", "user skill: "+skill);


                        Query mappingsToJobs = dh.mSkillMapJobs.child(skill).child("jobKeys");
                        mappingsToJobs.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for(DataSnapshot data: dataSnapshot.getChildren()){
                                    String jobKey = data.getKey();

                                    Query activeJobs = dh.mJobs.child(jobKey).orderByChild("active").equalTo(true);
                                    activeJobs.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String jobKey = dataSnapshot.getKey();
                                            if (keys.containsKey(jobKey)) {
                                                Log.d("fatDebug", "Incremented key");
                                                Integer oldVal = keys.get(jobKey);
                                                keys.put(jobKey, new Integer(oldVal.intValue() + 1));
                                            } else {
                                                Log.d("fatDebug", "added New Key");
                                                keys.put(jobKey, new Integer(0));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Log.d("fatDebug", "skill mapping retrieval canceled");
                                            throw databaseError.toException();
                                        }

                                    });

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("fatDebug", "skill mapping retrieval canceled");
                                throw databaseError.toException();
                            }
                        });
                    }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("fatDebug", "skill retrieval canceled");
                throw databaseError.toException();
            }
        });
    }

    public void displayJobKeys(){
        //TODO: KEEP IT SORTED TO BEGIN WITH INSTEAD OF ITERATING EACH TIME.
        Log.d("fatDebug", "displayKeys called");
         int maxKeyValue = -1;
         String maxKey = "default";
         Object[] keyArray = keys.keySet().toArray();
         for(Object key: keyArray){

             if(keys.get(key).intValue() > maxKeyValue){
                 if(!seenKeys.contains(key)){
                     maxKeyValue = keys.get(key).intValue();
                     maxKey = (String)key;
                 }

             }
         }


         Log.d("fatDebug", maxKey);
         //TODO: GENERATE LAYOUT HERE

        if(maxKey.equals("default")){
            generateLayout(null);
        }

        DH dh = new DH();
        DatabaseReference ref = dh.mJobs.child(maxKey);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> myData = new ArrayList<String>();
                for(int i=0; i<4; i++){
                    myData.add("");
                }
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    String dataKey = data.getKey();
                    if(dataKey.equals("jobTitle")){
                        myData.set(0,"Title: " + data.getValue());
                    }
                    else if(dataKey.equals("jobPrice")){
                        myData.set(1,"Price: " + data.getValue().toString());
                    }
                    else if(dataKey.equals("jobDescription")){
                        myData.set(2, "Description: " + data.getValue());
                    }
                    else if(dataKey.equals("jobLocation")){
                        myData.set(3, "Location: " + data.getValue());
                    }
                    else if(dataKey.equals("jobPosterKey")){
                        getImage(data.getValue().toString());
                    }
                }
                generateLayout(myData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("fatDebug", "skill retrieval canceled");
                throw databaseError.toException();
            }
        });

        DatabaseReference skillsRef = dh.mJobs.child(maxKey).child("jobSkills");
        skillsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> myData = new ArrayList<String>();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if(!data.getKey().equals("NONE")){
                        myData.add(data.getKey());
                    }
                }
                displaySkills(myData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("fatDebug", "skill retrieval canceled");
                throw databaseError.toException();
            }
        });
        //TODO: REMOVE KEY
        seenKeys.add(maxKey);
    }

    public void generateLayout(ArrayList<String> data){
        if(data == null){
            data = new ArrayList<String>();
            data.add("You have no potential matches, sorry!");
        }

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
        if(skills == null){
            return;
        }
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



                        // TODO: DISPLAY NEXT USER/ JOB
                        //TODO: WHEN MATCHED, DISPLAY POPUP TO CHAT OR UNMATCH. JOB POSTER CAN DELETE THEIR JOB HERE
                    }

                    // Right to left swipe action
                    else
                    {
                        // https://stackoverflow.com/questions/3053761/reload-activity-in-android

                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        Intent myIntent = getIntent();
                        myIntent.putStringArrayListExtra("seenKeys", new ArrayList<String>(seenKeys));

                        startActivity(getIntent());
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        Toast.makeText(SwipeActivity.this, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show ();


                        // TODO: DISPLAY NEXT USER/ JOB

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
                    ArrayList<String> skills = new ArrayList<String>();
                    skills.addAll(mySkills);
                    dh.newJob(uid,t,d,newP,location,latitude,longitude,skills);
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
                if((jobID == null) || (jobID.equals("NONE"))){
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

    private void getImage(String userid){
        final long ONE_MEGABYTE = 1024 * 1024;
        FirebaseStorage.getInstance().getReference("images").child(userid).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profilePic.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });
    }

}
