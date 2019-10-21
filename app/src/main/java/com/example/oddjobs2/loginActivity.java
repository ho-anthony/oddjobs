package com.example.oddjobs2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    LinearLayout layout;
    Button loginButton;
    Button signUpButton;
    boolean signUp = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        layout = findViewById(R.id.loginLayout);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);
    }

    public void loginClicked(View v) {
        signUpButton.setVisibility(View.GONE);
        showTextEntries(v);

    }

    public void signUpClicked(View v) {
        loginButton.setVisibility(View.GONE);
        signUp = true;
        showTextEntries(v);
    }

    public void showTextEntries(View v) {
        EditText emailInput = new EditText(this);
        TextView emailHeader = new TextView(this);
        emailHeader.setText("Email:");
        layout.addView(emailHeader);
        emailInput.setBackgroundColor(Color.WHITE);
        layout.addView(emailInput);
        TextView passwordHeader = new TextView(this);
        passwordHeader.setText("Password:");
        layout.addView(passwordHeader);
        EditText passwordInput = new EditText(this);
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordInput.setBackgroundColor(Color.WHITE);
        layout.addView(passwordInput);
        if(signUp) {
            TextView passConfirmHeader = new TextView(this);
            passConfirmHeader.setText("Confirm Password:");
            layout.addView(passConfirmHeader);
            EditText passwordConfirm = new EditText(this);
            passwordConfirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordConfirm.setBackgroundColor(Color.WHITE);
            layout.addView(passwordConfirm);
        }
    }

}
