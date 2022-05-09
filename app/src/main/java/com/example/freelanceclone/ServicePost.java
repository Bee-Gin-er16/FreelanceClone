package com.example.freelanceclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class ServicePost extends AppCompatActivity {
    EditText et_service_title, et_min_desired, et_max_desired, et_add_desc;
    Button bt_post, bt_back;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String userId = fAuth.getUid();
    String username;
    HashMap<String, Object> service_map = new HashMap<>();
    //DocumentReference documentReference = db.collection("User Data").document(userId);
    DocumentReference documentReference;
    DocumentReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_post);
        getSupportActionBar().setTitle("Offer Your Services");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        username = getIntent().getStringExtra("name");
        et_service_title = findViewById(R.id.et_service_title);
        et_min_desired = findViewById(R.id.et_min_desired);
        et_max_desired = findViewById(R.id.et_max_desired);
        et_add_desc = findViewById(R.id.et_add_desc);
        bt_post = findViewById(R.id.bt_postService);
        bt_back = findViewById(R.id.button3);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
        bt_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Always state your data in local scope not global if passing data to cloud
                String service = et_service_title.getText().toString();
                String min = et_min_desired.getText().toString();
                String max = et_max_desired.getText().toString();
                String desc = et_add_desc.getText().toString();
                String UID = fAuth.getUid();
                String fileID = UUID.randomUUID().toString();
                String s_name = username; //idk why but it works

                if (TextUtils.isEmpty(service)) {
                    et_service_title.setError("Required Field...");
                    return;
                }
                if (TextUtils.isEmpty(min)) {
                    et_min_desired.setError("Required Field...");
                    return;
                }
                if (TextUtils.isEmpty(max)) {
                    et_max_desired.setError("Required Field...");
                    return;
                }
                if (TextUtils.isEmpty(desc)) {
                    et_add_desc.setError("Required Field...");
                    return;
                }
                Date current_time = Calendar.getInstance().getTime();

                service_map.put("Poster",s_name);
                service_map.put("Service",service);
                service_map.put("Min",min);
                service_map.put("Max",max);
                service_map.put("Desc",desc);
                //UID will help link back to the user
                service_map.put("UID",UID);
                service_map.put("Date_posted",current_time.toString());
                collectionReference = db.collection("User Services").document(fileID);
                documentReference = db.document("User Data/"+userId).collection("Services").document(fileID);
                documentReference.set(service_map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Successfully added job post ", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
                collectionReference.set(service_map);
            }
        });

    }
}