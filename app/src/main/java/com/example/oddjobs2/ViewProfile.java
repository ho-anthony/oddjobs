package com.example.oddjobs2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ViewProfile extends AppCompatActivity implements View.OnClickListener {

    TextView fname, lname, age, location, email, phone;
    Button edit, save;
    EditText eName, eLName, eAge, eLoc, eEmail, ePhone;
    DH dh = new DH();
    Boolean editMode = false;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userid = user.getUid();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();;
    private DatabaseReference mActiveUsers = mDatabase.getReference("Users");
    StorageReference mStorageRef;
    ImageView profilePicture;
    private Uri mImage;
    private static final int UPLOAD_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        mStorageRef = FirebaseStorage.getInstance().getReference("images");
        profilePicture = (ImageView) findViewById((R.id.profilePicture));

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
        profilePicture.setOnClickListener(null);

        getImage(userid);
        getData(userid);
    }

    public void editProfile(View view) {
        editMode = true;
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
        profilePicture.setOnClickListener((View.OnClickListener) this);

        getImage(userid);
        editData(userid);
    }

    public void saveProfile(View view) {
        if(editMode){
            saveData(userid);
        }
        editMode = false;
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
        profilePicture.setOnClickListener(null);

        getImage(userid);
        getData(userid);
    }

    public void getData(String userid){
        mActiveUsers.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("firstName").getValue(String.class);
                fname.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
        mActiveUsers.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("lastName").getValue(String.class);
                lname.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
        mActiveUsers.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("age").getValue(String.class);
                age.setText(String.valueOf(name));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
        mActiveUsers.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("location").getValue(String.class);
                location.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
        mActiveUsers.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                email.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
        mActiveUsers.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("phone").getValue(String.class);
                phone.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void editData(String userid){
        String n = fname.getText().toString();
        String ln = lname.getText().toString();
        String a = age.getText().toString();
        String l = location.getText().toString();
        String e = email.getText().toString();
        String p = phone.getText().toString();
        eName.setText(n);
        eLName.setText(ln);
        eAge.setText(a);
        eLoc.setText(l);
        eEmail.setText(e);
        ePhone.setText(p);

    }

    public void saveData(String userid){
        mActiveUsers.child(userid).child("firstName").setValue(eName.getText().toString());
        mActiveUsers.child(userid).child("lastName").setValue(eLName.getText().toString());
        mActiveUsers.child(userid).child("age").setValue(eAge.getText().toString());
        mActiveUsers.child(userid).child("location").setValue(eLoc.getText().toString());
        mActiveUsers.child(userid).child("phone").setValue(ePhone.getText().toString());
    }

    private void getImage(String userid){
        final long ONE_MEGABYTE = 1024 * 1024;
        mStorageRef.child(userid).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profilePicture.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void uploadImage() {
        if(mImage != null){
            StorageReference reference = mStorageRef.child(userid);
            reference.putFile(mImage);
        }
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
            mImage = data.getData();
            profilePicture.setImageURI(mImage);
            uploadImage();
        }
    }
}
