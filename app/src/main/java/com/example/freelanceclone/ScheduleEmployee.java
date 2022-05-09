package com.example.freelanceclone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ScheduleEmployee extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String userId = fAuth.getCurrentUser().getUid();
    RecyclerView recyclerView;
    //Data holder for job model
    ArrayList<ApprovalModel> approvedArrayList;
    ApprovalAdapter approvalAdapter;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_post_list);
        getSupportActionBar().setTitle("Schedule Employee");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        db = FirebaseFirestore.getInstance();
        approvedArrayList = new ArrayList<ApprovalModel>();
        approvalAdapter = new ApprovalAdapter(ScheduleEmployee.this, approvedArrayList);
        recyclerView = findViewById(R.id.rv_employer_post);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(approvalAdapter);
        EventChangeListener();
    }

    private void EventChangeListener() {
        approvedArrayList.clear();
        db.collection("User Data/"+userId+"/Approvals")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if(error != null){
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.e("Firestore error",error.getMessage()); return;
                }
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        ApprovalModel approve = dc.getDocument().toObject(ApprovalModel.class);
                        Log.i("APP_TITLE", "Information job title: "+approve.getJob_title());
                        Log.i("APP_ID", "Information job id: "+approve.getJob_id());
                        approvedArrayList.add(approve);
                    }
                    approvalAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();

                }
                approvalAdapter.notifyDataSetChanged();
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed(){
        Toast.makeText(getApplicationContext(),"Going Back",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }
}