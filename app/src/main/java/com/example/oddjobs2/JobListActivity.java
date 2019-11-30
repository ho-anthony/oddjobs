package com.example.oddjobs2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class JobListActivity extends AppCompatActivity {

    FloatingActionButton fab;
    PopupWindow puw;
    Button post;
    EditText title, des, pay, location;
    ListView listView;
    static TextView text;
    //ArrayList<String> catalogue = new ArrayList<>();
    ArrayList<DataModel> catalogue = new ArrayList<>();
    static MyCustomAdapter adapter;

    public static void checkEmpty() {
        if(adapter.getCount() == 0){
            text.setText(R.string.no_jobs);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);

        /*fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_layout, null);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                post = popupView.findViewById(R.id.popup_post);
                title = popupView.findViewById(R.id.popup_title);
                des = popupView.findViewById(R.id.popup_des);
                pay = popupView.findViewById(R.id.popup_pay);
                location = popupView.findViewById(R.id.popup_location);
                listView = findViewById(R.id.jobListView);
                text = findViewById(R.id.jobListStatus);

                // Store information and close popup window
                post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String t, d, p, l;
                        if(title.length()==0||des.length()==0||pay.length()==0||location.length()==0){
                            Toast.makeText(JobListActivity.this, "Please fill in all information.", Toast.LENGTH_SHORT).show();
                        } else {
                            t = title.getText().toString();
                            d = des.getText().toString();
                            p = pay.getText().toString();
                            l = location.getText().toString();
                            // Do something with the information
                            String newP = "$" + p;
                            showListView(t, d, newP, l);
                            popupWindow.dismiss();
                        }
                    }
                });
            }
        });*/
    }

    // Function to show the images on the listView by finding them in the database
    public void showListView(String t, String d, String p, String l){
        //ArrayList<DataModel> catalogue = new ArrayList<>();
        listView = findViewById(R.id.jobListView);
        text = findViewById(R.id.jobListStatus);
        // While there is data to read, add it to the listView
        //while(data.moveToNext()){
            catalogue.add(new DataModel(t,d,p,l));
        //}
        // Instantiate custom adapter
        text.setText("");
        adapter = new MyCustomAdapter(catalogue, this);
        listView.setAdapter(adapter);
    }
}
