package com.example.freelanceclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
//TODO update/add fields based on updated JobPosting key values in the xml
public class EditDescription extends AppCompatActivity {
    TextView job_id,job_title,job_desc,job_payout;
    Button bt_requests, bt_delete;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    DocumentReference jobUserReference; //reference user post collection
    DocumentReference jobColReference; //reference job_board collection
    CollectionReference requestReference; //updates the user of the potential invites his post had using path UserData->UserPost

    String id, job_owner_id, status; //id is job_id from extra,

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_description);
        getSupportActionBar().setTitle("Preview Description");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        id = getIntent().getStringExtra("job_id");
        job_id = findViewById(R.id.edit_job_id);
        job_title = findViewById(R.id.edit_job_title);
        job_desc = findViewById(R.id.edit_desc_info);
        job_payout = findViewById(R.id.edit_job_payout);
        job_id.setText(id);

        bt_delete = findViewById(R.id.bt_delete);
        bt_requests = findViewById(R.id.bt_check_invites);

        jobColReference = fStore.document("Job board ID/"+id);//job in collection
        jobUserReference = fStore.document("User Data/"+fAuth.getCurrentUser().getUid()+"/User posts/"+id); //job in user
        requestReference = fStore.collection("User Data/"+job_owner_id+"/User posts/"+id+"/Invites/"); //job requests

        jobUserReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                job_title.setText(documentSnapshot.getString("Job_title"));
                job_desc.setText(documentSnapshot.getString("Job_description"));
                job_payout.setText(documentSnapshot.getString("Job_payout"));
                job_owner_id = documentSnapshot.getString("post owner");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(EditDescription.this, "Document not found", Toast.LENGTH_SHORT).show();
            }
        });

        job_payout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "Enter new payout for update";
                ChangeData("Job_payout",msg);
            }
        });
        job_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "Enter new title for update";
                ChangeData("Job_title",msg);
            }
        });
        job_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "Enter new description for update";
                ChangeData("Job_description",msg);
            }
        });

        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDoc();
            }
        });
        bt_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditDescription.this);
                alert.setMessage("When going back you will be redirected to your list to prevent errors\nDo you wish to see the requests?");
                alert.setTitle("Notice!");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(),RequestList.class));
                        dialog.dismiss();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(EditDescription.this, "Cancelled", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                alert.show();

            }
        });

    }

    private void DeleteDoc() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Are you sure you want to delete this post?\nYou will be redirected to your profile");
        alert.setTitle("Delete this post?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(EditDescription.this, "Document deleted(NOT YET implemented)", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(EditDescription.this, "Document not deleted(NOT YET implemented)", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private void ChangeData(String key, String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(msg);
        alert.setTitle("Update Your "+key);
        final EditText edittext = new EditText(EditDescription.this);
        alert.setView(edittext);
        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                String YouEditTextValue = edittext.getText().toString();
                jobUserReference.update(key,YouEditTextValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),"Successfully updated data for "+msg+" Go Back to Profile",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),EmployerPostList.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                    }
                });
                jobColReference.update(key, YouEditTextValue);
            }
        });
        alert.setNegativeButton("Don't Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
                dialog.dismiss();
            }
        });
        alert.show();
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(getApplicationContext(),"Going Back",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),EmployerPostList.class));
    }
}