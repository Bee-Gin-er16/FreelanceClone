package com.example.freelanceclone;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    Context context;
    ArrayList<RequestModel> requestArrayList;
    String UID, job_title, job_id, owner_id;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    DocumentReference userReference; //for the guest
    DocumentReference jobReference;

    public RequestAdapter(Context context, ArrayList<RequestModel> requestArrayList, String UID, String job_id, String job_title) {
        this.context = context;
        this.requestArrayList = requestArrayList;
        this.UID = UID;
        this.job_id = job_id;
        this.job_title = job_title;
        this.jobReference = db.document("Job board ID/"+job_id);
        jobReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) { owner_id = documentSnapshot.getString("post_owner"); }
        });
    }

    @NotNull
    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.guest_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.ViewHolder holder, int position) {
        RequestModel guest = requestArrayList.get(position);
        holder.tv_guest_name.setText(guest.getguest_name());
        holder.tv_guest_id.setText(guest.getguest_id());
        holder.tv_guest_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Actions(v, guest); }
        });
        holder.tv_guest_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Actions(v, guest); }
        });
    }

    private void Actions(View v, RequestModel guest) {
        androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage("What do you want to do with this guest?");
        alert.setTitle("Employer Guest Action");
        alert.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String, Object> access = new HashMap<>();
                Date current_time = Calendar.getInstance().getTime();
                userReference = db.document("User Data/"+guest.getguest_id())
                        .collection("Approvals").document(job_id);//Will generate collection for the employee who requested to be hired.
                //REMINDER: Make sure there are no spaces after key values
                // because they will also be detected too
                // ok if "post owner" but not "post owner "(one character space after key is detrimental)
                access.put("Date_Granted", current_time);//Date of granted access
                access.put("Job_id", job_id);//for reference purpose
                access.put("Job_title", job_title);//for display purpose
                access.put("Access", true);//in case
                userReference.set(access).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Put hub access on user", Toast.LENGTH_SHORT).show();
                        jobReference = db.document("Job board ID/"+job_id+"/Request/"+guest.getguest_id());
                        jobReference.delete();
                        db.document("User Data/"+owner_id+"/User posts/"+job_id+"/Request/"+guest.getguest_id()).delete();
                        Intent i = new Intent(v.getContext(), LoginActivity.class);
                        context.startActivity(i);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(context, "Document does not exists", Toast.LENGTH_SHORT).show();
                    }
                }); //Works
            }
        }).setNegativeButton("Reject", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                jobReference = db.document("Job board ID/"+job_id+"/Request/"+guest.getguest_id());
                jobReference.delete();
                db.document("User Data/"+owner_id+"/User posts/"+job_id+"/Request/"+guest.getguest_id())
                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Guest deleted from the list", Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(v.getContext(), EmployerPostList.class));
                    }
                });
            }
        }).setNeutralButton("Check Profile Description", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO intent passing on to profile review with intent of the guest id(can be the same layout without the functioning buttons)
                Toast.makeText(context, "Check profile not yet implemented", Toast.LENGTH_SHORT).show();
                /*
                Intent i = new Intent(v.getContext(), EditDescription.class);
                i.putExtra("guest_id",guest.getguest_id());
                context.startActivity(i);*/
            }
        });
        alert.show();
    }

    @Override
    public int getItemCount() { return requestArrayList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_guest_name, tv_guest_id;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_guest_name = itemView.findViewById(R.id.tv_guest_name);
            tv_guest_id = itemView.findViewById(R.id.tv_guest_id);
        }
    }
}
