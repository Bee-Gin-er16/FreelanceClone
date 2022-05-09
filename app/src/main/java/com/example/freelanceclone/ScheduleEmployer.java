package com.example.freelanceclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ScheduleEmployer extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String userId = fAuth.getCurrentUser().getUid();
    RecyclerView recyclerView;
    ArrayList<JobModel> jobArrayList;
    MyAdapter myAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_post_list);//reusing the employer post list rv viewer
        getSupportActionBar().setTitle("Schedule Employer");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        db = FirebaseFirestore.getInstance();
        jobArrayList = new ArrayList<JobModel>();
        myAdapter = new MyAdapter(ScheduleEmployer.this, jobArrayList);
        recyclerView = findViewById(R.id.rv_employer_post);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myAdapter);
        EventChangeListener();
    }

    private void EventChangeListener() {
        jobArrayList.clear();
        db.collection("User Data/"+userId+"/User posts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error",error.getMessage()); return;
                        }
                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                JobModel job = dc.getDocument().toObject(JobModel.class);
                                if(job.getpost_owner().matches(fAuth.getCurrentUser().getUid())){
                                    jobArrayList.add(job);
                                }
                            }
                            myAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();

                        }
                        myAdapter.notifyDataSetChanged();
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
