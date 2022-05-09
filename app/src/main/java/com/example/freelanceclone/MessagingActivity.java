package com.example.freelanceclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

public class MessagingActivity extends AppCompatActivity {
    Button get_token;
    TextView tv_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        getSupportActionBar().setTitle("Messaging");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        get_token = findViewById(R.id.bt_get_token);
        tv_token = findViewById(R.id.tv_token);

        get_token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToken();
            }
        });
    }
    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()){
                    Log.e("FCM","Failed to get token"); return;
                }
                Log.d("FCM", "Token: "+task.getResult());
                tv_token.setText(task.getResult());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.e("FCM","Failed to get token :" +e.getLocalizedMessage());
            }
        });
    }
    @Override
    public void onBackPressed(){
        Toast.makeText(getApplicationContext(),"Going Back",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }
}