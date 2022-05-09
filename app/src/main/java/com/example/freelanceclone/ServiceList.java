package com.example.freelanceclone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ServiceList extends AppCompatActivity {
    FirebaseFirestore db;
    RecyclerView recyclerView;
    //Data holder for job model
    ArrayList<ServiceModel> serviceModelArrayList;
    ServiceAdapter serviceAdapter;
    EditText et_service_search;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);
        getSupportActionBar().setTitle("Available services");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        db = FirebaseFirestore.getInstance();
        serviceModelArrayList = new ArrayList<ServiceModel>();
        serviceAdapter = new ServiceAdapter(ServiceList.this, serviceModelArrayList);
        recyclerView = findViewById(R.id.rv_service);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        et_service_search = findViewById(R.id.et_service_search);
        et_service_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) { filter(s.toString()); }
        });

        recyclerView.setAdapter(serviceAdapter);
        EventChangeListener();
    }

    private void filter(String toString) {
        ArrayList<ServiceModel> filteredList = new ArrayList<>();
        for(ServiceModel service: serviceModelArrayList){
            if(service.getService().toLowerCase().contains(toString.toLowerCase())){
                filteredList.add(service);
            }
        }
        serviceAdapter.filter_list(filteredList);
    }

    private void EventChangeListener() {
        //Goto collection reference and iterate through documents inside
        //Right now, reference is pointing at collection, so anything in User Services
        db.collection("User Services")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error",error.getMessage()); return;
                        }

                        //Alternative 2 Not working UPDATE: Now it does
                        for(DocumentChange dc : value.getDocumentChanges()){
                            //ServiceModel serviceModel = d.toObject(ServiceModel.class);
                            serviceModelArrayList.add(dc.getDocument().toObject(ServiceModel.class));
                        }

                        serviceAdapter.notifyDataSetChanged();
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                        //End 2
                    }
                });
    }
}