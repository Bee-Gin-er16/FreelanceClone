package com.example.freelanceclone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class FindUser extends AppCompatActivity {
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db;
    RecyclerView recyclerView;

    ArrayList<User> userList;
    UserAdapter userAdapter;
    ProgressDialog progressDialog;
    EditText et_user_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);
        getSupportActionBar().setTitle("Messaging");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        db = FirebaseFirestore.getInstance();
        userList = new ArrayList<User>();
        userAdapter = new UserAdapter(FindUser.this, userList);
        recyclerView = findViewById(R.id.rv_userlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userAdapter);

        EventChangeListener();
    }

    private void EventChangeListener() {
        userList.clear();
        db.collection("User Data")
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
                                User user = dc.getDocument().toObject(User.class);
                                if(user.getUID().matches(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                    continue;
                                }
                                userList.add(user);
                            }
                        }
                        userAdapter.notifyDataSetChanged();
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