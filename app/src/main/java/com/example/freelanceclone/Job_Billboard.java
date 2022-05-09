package com.example.freelanceclone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Job_Billboard extends AppCompatActivity {
    FirebaseFirestore db;
    RecyclerView recyclerView;
    //Data holder for job model
    ArrayList<JobModel> jobArrayList;
    MyAdapter myAdapter;
    ProgressDialog progressDialog;
    EditText et_job_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_billboard);
        getSupportActionBar().setTitle("Job Billboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        db = FirebaseFirestore.getInstance();
        jobArrayList = new ArrayList<JobModel>();
        myAdapter = new MyAdapter(Job_Billboard.this, jobArrayList);
        recyclerView = findViewById(R.id.rv_userlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        et_job_search = findViewById(R.id.et_user_search);
        et_job_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        recyclerView.setAdapter(myAdapter);
        EventChangeListener();
    }

    private void filter(String text) {
        ArrayList<JobModel> filteredList = new ArrayList<JobModel>();

        for(JobModel job : jobArrayList){
            if(job.getJob_title().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(job);
            }
        }

        myAdapter.filter_list(filteredList);
    }

    private void EventChangeListener() {
        //Goto collection reference and iterate through documents inside
        //Right now, reference is pointing at collection, so anything in job board ID
        //will be iterated through unless condition will be applied
        jobArrayList.clear();
        db.collection("Job board ID")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error",error.getMessage()); return;
                        }
                        //Alternative 1
                        //for loop to iterate to every document that is changed
                        for(DocumentChange dc : value.getDocumentChanges()){

                            if(dc.getType() == DocumentChange.Type.ADDED){
                                JobModel job = dc.getDocument().toObject(JobModel.class);
                                Log.i("APP_TITLE", "Information job title: "+job.getJob_title());
                                Log.i("APP_ID", "Information job id: "+job.getJob_ID());
                                if(job.isJob_finished()==false){
                                    jobArrayList.add(job);
                                }
                            }
                            myAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();

                        }
                        //End 1
                        //Alternative 2 Not working UPDATE: Now it does but makes
                        //List<DocumentSnapshot> list = value.getDocuments();
                        /*for(DocumentSnapshot d : list){
                            for(DocumentChange dc : value.getDocumentChanges()){
                                JobModel j = d.toObject(JobModel.class);
                                if(j.isJob_finished()==false){
                                    jobArrayList.add(dc.getDocument().toObject(JobModel.class));
                                }
                            }
                        }*/
                        myAdapter.notifyDataSetChanged();
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                        //End 2
                    }
                });
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(getApplicationContext(),"Going Back",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }

}