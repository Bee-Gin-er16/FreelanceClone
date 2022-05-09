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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

//TODO make a recyclerview adapter and model class for invite
public class RequestList extends AppCompatActivity {
    RecyclerView rv_requests;
    ArrayList<RequestModel> requestArrayList;
    RequestAdapter requestAdapter;
    ProgressDialog progressDialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String job_id; String UID = FirebaseAuth.getInstance().getUid(); String job_title;
    /*
    ("/User Data/"+UID+"/User posts/"+job_id+"/Request") = User Data to Request reference
    in this case, UID is the employee in charge of accepting or declining guests, ie. the user
    (/Job board ID/+job_id+/Request) = Job Board ID to Request reference
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        getSupportActionBar().setTitle("Requests to take your job");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        job_id = getIntent().getStringExtra("job_id");
        job_title = getIntent().getStringExtra("job_title");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        requestArrayList = new ArrayList<RequestModel>();
        requestAdapter = new RequestAdapter(RequestList.this, requestArrayList, UID, job_id, job_title);
        rv_requests = findViewById(R.id.rv_requests);
        rv_requests.setHasFixedSize(true);
        rv_requests.setLayoutManager(new LinearLayoutManager(this));
        rv_requests.setAdapter(requestAdapter);

        EventChangeListener();
    }

    private void EventChangeListener() {
        requestArrayList.clear();
        db.collection("Job board ID/"+job_id+"/Request")
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
                                RequestModel request = dc.getDocument().toObject(RequestModel.class);
                                requestArrayList.add(request);
                            }
                        }
                        requestAdapter.notifyDataSetChanged();
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();

                    }
                });
    }

    //Due to the EditDescription having no data in start, this will be the exit point
    @Override
    public void onBackPressed(){
        Toast.makeText(getApplicationContext(),"Going Back",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),EmployerPostList.class));
    }
}