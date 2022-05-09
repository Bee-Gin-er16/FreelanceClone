package com.example.freelanceclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class JobHubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_hub);
        getSupportActionBar().setTitle("Job hub (to be worked on for now)!");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}