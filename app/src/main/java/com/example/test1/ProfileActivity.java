package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileActivity extends Activity {
    private static final String TAG = "ProfileActivity";
    TextView emailTxt, createdDateTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        emailTxt = findViewById(R.id.emailTxt);
        createdDateTxt = findViewById(R.id.createdDateTxt);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String email = extras.getString("email");
            String date = extras.getString("createdAt");
            emailTxt.setText(email);
            createdDateTxt.setText(date);
            Log.d(TAG, "onCreate: " + email);
        }
    }
}
