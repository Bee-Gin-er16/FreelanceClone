package com.example.freelanceclone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    Button bt_openings, bt_post, bt_edit_profile,bt_message,bt_schedule, bt_logout, bt_service, bt_workers, bt_check_upload;
    ProgressDialog progressDialog;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getUid();
    DocumentReference documentReference = db.document("User Data/"+userId);
    String status, user_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bt_openings = findViewById(R.id.bt_openings);
        bt_post = findViewById(R.id.bt_post);
        bt_edit_profile = findViewById(R.id.bt_edit_profile);
        bt_logout = findViewById(R.id.bt_logout);
        bt_service = findViewById(R.id.bt_service);
        bt_message = findViewById(R.id.bt_message);
        bt_schedule = findViewById(R.id.bt_schedule);
        bt_workers = findViewById(R.id.bt_workers);
        bt_check_upload = findViewById(R.id.bt_check_upload);
        progressDialog = new ProgressDialog(this);
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            status = documentSnapshot.getString("Status");
                            user_name = documentSnapshot.getString("Name");
                        } else {
                            Toast.makeText(LoginActivity.this, "Document does not exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
        alert.setTitle("Status Exception");
        alert.setPositiveButton("I understand", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        bt_openings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Job_Billboard.class));
            }
        });
        //EMPLOYERS ONLY : Advertise for offered position
        bt_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.matches("Employer")){
                    startActivity(new Intent(getApplicationContext(),JobPosting.class));
                }else{
                    Toast.makeText(LoginActivity.this, "You are not an employer", Toast.LENGTH_SHORT).show();
                    //TODO Alert Dialog here
                    alert.setMessage("You cannot access button post because you are not an Employer");
                    alert.show();
                }

            }
        });
        //EMPLOYEES ONLY : Advertise for offered services
        bt_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.matches("Employee")){
                    Intent i = new Intent(getApplicationContext(),ServicePost.class);
                    i.putExtra("name",user_name);
                    startActivity(i);
                }else{
                    Toast.makeText(LoginActivity.this, "You are not an employee", Toast.LENGTH_SHORT).show();
                    alert.setMessage("You cannot access button post because you are not an Employee");
                    alert.show();
                }

            }
        });
        bt_workers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(new Intent(getApplicationContext(),ServiceList.class)); }
        });

        //TODO messaging can only receive tokens for now, pls make sure to update
        bt_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),MessagingActivity.class));
                startActivity(new Intent(getApplicationContext(),FindUser.class));
            }
        });
        bt_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.matches("Employee")){
                    Intent i = new Intent(getApplicationContext(), ScheduleEmployee.class); startActivity(i);
                }else{
                    Intent i = new Intent(getApplicationContext(), ScheduleEmployer.class); startActivity(i);
                }
            }
        });
        bt_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(new Intent(getApplicationContext(),Profile.class)); }
        });
        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please wait while logout is processing...");
                progressDialog.setTitle("Logging out? :(");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(),"Logging Out...",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        bt_check_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),UploadPicture.class));
            }
        });

    }
    @Override
    public void onBackPressed(){
        progressDialog.setMessage("Please wait while logout is processing...");
        progressDialog.setTitle("Logging out? :(");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getApplicationContext(),"Logging Out...",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

}