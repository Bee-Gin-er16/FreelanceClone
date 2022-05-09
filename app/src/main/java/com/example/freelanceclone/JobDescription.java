package com.example.freelanceclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
//TODO update/add fields based on updated JobPosting key values in the xml
public class JobDescription extends AppCompatActivity {
    TextView job_id,job_title,job_desc,job_payout, job_pay_method, job_negotiable;
    Button bt_accept, bt_reject;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    DocumentReference documentReference; //reference job_board collection JobBoardId -> JobId
    DocumentReference userReference; //updates the user of the potential invites his post had using path UserData->UserPost
    DocumentReference statusReference = fStore.document("User Data/"+FirebaseAuth.getInstance().getUid()); //reference current user data
    String id, job_owner_id, status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_description);
        getSupportActionBar().setTitle("Job_Description");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = getIntent().getStringExtra("job_id");
        job_id = findViewById(R.id.edit_job_id);
        job_title = findViewById(R.id.edit_job_title);
        job_desc = findViewById(R.id.edit_desc_info);
        job_payout = findViewById(R.id.edit_job_payout);
        job_pay_method = findViewById(R.id.edit_mode_payment);
        job_negotiable = findViewById(R.id.edit_negotiable);
        bt_accept = findViewById(R.id.bt_check_invites);
        bt_reject = findViewById(R.id.bt_delete);
        documentReference = fStore.document("Job board ID/"+id);
        bt_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendInvite();
            }
        });
        bt_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Going Back",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),Job_Billboard.class));
            }
        });

        //Success on 8:42 PM 24/11
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //String title = documentSnapshot.getString("Job_title");
                job_title.setText(documentSnapshot.getString("Job_title"));
                job_desc.setText(documentSnapshot.getString("Job_description"));
                String pay = "₱ "+documentSnapshot.getString("Job_payout");
                job_payout.setText(pay);
                job_owner_id = documentSnapshot.getString("post_owner");
                job_id.setText(documentSnapshot.getString("post_owner"));
                job_pay_method.setText(documentSnapshot.getString("Pay Method"));
                job_negotiable.setText(documentSnapshot.getString("Negotiable"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(JobDescription.this, "Document does not exists", Toast.LENGTH_SHORT).show();
            }
        });
        statusReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                status = documentSnapshot.getString("Status");
            }
        });

    }

    private void SendInvite() {
        String temp = status;
        if(temp.matches("Employer")){
            Toast.makeText(JobDescription.this, "EMPLOYERS CANNOT REPLY HERE", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            //update the job description data from both User Data and Job board ID
            userReference = fStore.document("User Data/"+job_owner_id+"/User posts/"+id).collection("Request").document(fAuth.getCurrentUser().getUid());
            DocumentReference jobReference = documentReference.collection("Request").document(fAuth.getCurrentUser().getUid());
            HashMap<String, Object> invite = new HashMap<>();

            //Extract the interested employee's name in User Data collection
            DocumentReference userID = fStore.document("User Data/"+fAuth.getCurrentUser().getUid());
            userID.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String name = documentSnapshot.getString("Name");
                    invite.put("guest_name",name);
                    invite.put("guest_id", fAuth.getCurrentUser().getUid());
                    userReference.set(invite);
                    jobReference.set(invite);
                }
            });
            Toast.makeText(JobDescription.this, "You successfully sent an invite, please wait for the employee to respond", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }
    }
    @Override
    public void onBackPressed(){
        Toast.makeText(getApplicationContext(),"Going Back",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),Job_Billboard.class));
    }
}