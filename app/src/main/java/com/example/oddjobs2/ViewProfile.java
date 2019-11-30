package com.example.oddjobs2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewProfile extends AppCompatActivity {

    TextView fname, lname, age, location, email, phone;
    Button edit, save;
    EditText eName, eLName, eAge, eLoc, eEmail, ePhone;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mActiveUsers;
    DH dh = new DH();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        fname = findViewById(R.id.fnameView);
        lname = findViewById(R.id.lnameView);
        age = findViewById(R.id.ageView);
        location = findViewById(R.id.locView);
        email = findViewById(R.id.emailView);
        phone = findViewById(R.id.phoneNumView);
        edit = findViewById(R.id.profileEdit);

        save = findViewById(R.id.profileSave);
        eName = findViewById(R.id.editName);
        eLName = findViewById(R.id.editLName);
        eAge = findViewById(R.id.editAge);
        eLoc = findViewById(R.id.editLoc);
        eEmail = findViewById(R.id.editEmail);
        ePhone = findViewById(R.id.editPhone);

        //String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //fname.setText(uid);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mActiveUsers = mDatabase.getReference("ActiveUsers");
        mActiveUsers.child(userid).setValue(user);
        mActiveUsers.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("email").getValue(String.class);
                fname.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

    }

    public void editProfile(View view) {
        fname.setVisibility(view.GONE);
        lname.setVisibility(view.GONE);
        age.setVisibility(view.GONE);
        location.setVisibility(view.GONE);
        email.setVisibility(view.GONE);
        phone.setVisibility(view.GONE);
        edit.setVisibility(view.GONE);

        save.setVisibility(view.VISIBLE);
        eName.setVisibility(view.VISIBLE);
        eLName.setVisibility(view.VISIBLE);
        eAge.setVisibility(view.VISIBLE);
        eLoc.setVisibility(view.VISIBLE);
        eEmail.setVisibility(view.VISIBLE);
        ePhone.setVisibility(view.VISIBLE);

    }

    public void saveProfile(View view) {
        fname.setVisibility(view.VISIBLE);
        lname.setVisibility(view.VISIBLE);
        age.setVisibility(view.VISIBLE);
        location.setVisibility(view.VISIBLE);
        email.setVisibility(view.VISIBLE);
        phone.setVisibility(view.VISIBLE);
        edit.setVisibility(view.VISIBLE);

        save.setVisibility(view.GONE);
        eName.setVisibility(view.GONE);
        eLName.setVisibility(view.GONE);
        eAge.setVisibility(view.GONE);
        eLoc.setVisibility(view.GONE);
        eEmail.setVisibility(view.GONE);
        ePhone.setVisibility(view.GONE);
    }
}
