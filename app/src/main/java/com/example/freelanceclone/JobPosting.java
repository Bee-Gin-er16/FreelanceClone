package com.example.freelanceclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
//TODO timepicker for start-end, Day picker maybe a spinner(MWF or TTH default one-time job)?
public class JobPosting extends AppCompatActivity {
    EditText et_job_title, et_job_description,
            et_job_payout, et_skill_requirements;
    Button bt_to_post, bt_cancel_post;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    Spinner pay_method, negotiable;

    String userId = fAuth.getUid();
    String username;
    HashMap<String, Object> job_post = new HashMap<>();
    //DocumentReference documentReference = db.collection("User Data").document(userId);
    //NOTE: Declare references globally before getting the data in local scopes for important variables like status,username,userid
    DocumentReference documentReference;
    DocumentReference collectionReference;
    DocumentReference name = db.document("User Data/"+userId);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_posting);
        getSupportActionBar().setTitle("Post Your Job");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_job_title = findViewById(R.id.et_job_title);
        et_job_description = findViewById(R.id.et_job_description);
        et_job_payout = findViewById(R.id.et_job_payout);
        et_skill_requirements = findViewById(R.id.et_skill_requirements);
        bt_to_post = findViewById(R.id.bt_to_post);
        bt_cancel_post = findViewById(R.id.bt_cancelpost);

        pay_method = findViewById(R.id.pay_method);
        ArrayAdapter<CharSequence> pay_adapter = ArrayAdapter.createFromResource(this,
                R.array.Pay_Method, android.R.layout.simple_spinner_item);
        pay_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pay_method.setAdapter(pay_adapter);
        pay_method.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Payment method: "+choice, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        negotiable = findViewById(R.id.negotiable);
        ArrayAdapter<CharSequence> choice_adapter = ArrayAdapter.createFromResource(this,
                R.array.Choices, android.R.layout.simple_spinner_item);
        negotiable.setAdapter(choice_adapter);
        negotiable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Negotiable?: "+choice, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        name.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                username = documentSnapshot.getString("Name");
            }
        });

        bt_cancel_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Cancelled Post now going back", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        bt_to_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_job_title.getText().toString().trim();
                String description = et_job_description.getText().toString().trim();
                String payout = et_job_payout.getText().toString().trim();
                String skills = et_skill_requirements.getText().toString().trim();

                if (TextUtils.isEmpty(title)) {
                    et_job_title.setError("Required Field...");
                    return;
                }
                if (TextUtils.isEmpty(description)) {
                    et_job_description.setError("Required Field...");
                    return;
                }
                if (TextUtils.isEmpty(payout)) {
                    et_job_payout.setError("Required Field...");
                    return;
                }
                if (TextUtils.isEmpty(skills)) {
                    et_skill_requirements.setError("Required Field...");
                    return;
                }
                String temp = username;
                //Database putting through users sub document (problematic) 16/11/2021
                Date current_time = Calendar.getInstance().getTime();
                String fileID = UUID.randomUUID().toString();
                String method = pay_method.getSelectedItem().toString();
                //TODO get values of spinner pay_method and negotiable
                job_post.put("post_owner",fAuth.getCurrentUser().getUid());
                job_post.put("owner",temp);
                job_post.put("Job_ID",fileID);
                job_post.put("Job_finished", false);
                job_post.put("Job_title",title);
                job_post.put("Job_description",description);
                job_post.put("Job_payout",payout);
                job_post.put("Skill_requirements",skills);
                job_post.put("Date_posted",current_time.toString());
                job_post.put("Negotiable",negotiable.getSelectedItem().toString());
                job_post.put("Pay Method",method);
                collectionReference = db.collection("Job board ID").document(fileID);
                documentReference = db.document("User Data/"+userId).collection("User posts").document(fileID);
                documentReference.set(job_post).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                collectionReference.set(job_post);

            }
        });
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(getApplicationContext(),"Going Back",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }
}