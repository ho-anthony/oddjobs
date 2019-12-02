package com.example.oddjobs2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.printservice.PrintService;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int UPLOAD_RESULT = 1;
    EditText firstName, lastName, age, phone, keyWords;
    Button submit;
    ImageView profilePicture;
    Boolean picUploaded = false;
    Set<String> mySkills;
    DH databaseHelper;
    StorageReference mStorageRef;
    DatabaseReference mDatabase;
    private Uri mImage;
    String userID;
    String location;
    double latitude, longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        submit = (Button) findViewById(R.id.profileSubmit);
        profilePicture = (ImageView) findViewById((R.id.profilePicture));
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        age = (EditText) findViewById(R.id.age);
        phone = (EditText) findViewById(R.id.phone_number);
        location = "";
        latitude = longitude = 0.0;

        profilePicture.setOnClickListener(this);
        mySkills = new HashSet<String>();
        databaseHelper = new DH();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference("images");
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        // https://developers.google.com/places/android-sdk/start
        // Initialize the SDK
         Places.initialize(getApplicationContext(), "AIzaSyCj0-7H-hAXU1n8fuvTr9_tH6zK2SVZ7uM");
         final PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

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
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.profilePicture:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, UPLOAD_RESULT);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UPLOAD_RESULT && resultCode == RESULT_OK && data != null) {
            Uri image = data.getData();
            mImage = data.getData();
            profilePicture.setImageURI(image);
            picUploaded = true;
        }
    }
    public void submitProfile(View v){
        if(checkValidity()) {

            ArrayList<String> skills = new ArrayList<String>();
            skills.addAll(mySkills);
            databaseHelper.newUser(
                    userID,
                    firstName.getText().toString().trim(),
                    lastName.getText().toString().trim(),
                    age.getText().toString().trim(),
                    location,
                    latitude,
                    longitude,
                    phone.getText().toString().trim(),
                    skills
            );
            uploadImage();
            Intent intent = new Intent(this, DecisionActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this,"Please upload a picture and fill in all of your information",Toast.LENGTH_SHORT).show();
            Toast.makeText(this, mySkills.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkValidity() {
        if(!picUploaded ||
                firstName.toString().trim().equals("") ||
                lastName.toString().trim().equals("") ||
                age.toString().trim().equals("") ||
                location.toString().trim().equals("") ||
                phone.toString().trim().equals("")
//                keyWords.toString().trim().equals("")]
                ){
            return false;
        }
        return true;
    }

    private void uploadImage() {
        if(mImage != null){
            StorageReference reference = mStorageRef.child(userID);
            reference.putFile(mImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ProfileActivity.this, "Upload Success", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
