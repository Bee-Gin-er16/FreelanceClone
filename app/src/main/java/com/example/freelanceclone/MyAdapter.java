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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    //Make a context and iterable datalist variable
    Context context;
    ArrayList<JobModel> userArrayList;

    //Constructor
    public MyAdapter(Context context, ArrayList<JobModel> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Here is where you attach your item layout ie: job_openings_list
        View v = LayoutInflater.from(context).inflate(R.layout.job_openings_list,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        //Instantiate your model class to hold the data of the model ur simulating
        JobModel job = userArrayList.get(position);

        //Based on the widgets u used on myViewHolder
        holder.tv_provided_title.setText(job.getJob_title());
        holder.tv_provided_payout.setText(job.getJob_payout());
        holder.tv_provided_id.setText(job.getowner());

        //OnClickers redirect to intent
        holder.tv_provided_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ToDescription(v, job); }
        });
        holder.tv_provided_payout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ToDescription(v, job); }
        });
        holder.tv_provided_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ToDescription(v, job); }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public void filter_list(ArrayList<JobModel> filteredList) {
        userArrayList = filteredList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //Here in scope instantiate the widgets used based on your item layout
        TextView tv_provided_title,tv_provided_payout, tv_provided_id;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            //find the widgets
            tv_provided_title = itemView.findViewById(R.id.tv_provided_title);
            tv_provided_payout = itemView.findViewById(R.id.tv_provided_payout);
            tv_provided_id = itemView.findViewById(R.id.tv_provided_id);
        }
    }

    private void ToDescription(View v, JobModel job) {
        if(context.getClass().equals(ScheduleEmployer.class)){
            Intent i = new Intent(v.getContext(), JobHubActivity.class);
            i.putExtra("job_id",job.getJob_ID());
            context.startActivity(i);
        }else if(context.getClass().equals(Job_Billboard.class)){
            Intent i = new Intent(v.getContext(), JobDescription.class);
            i.putExtra("job_id",job.getJob_ID());
            context.startActivity(i);
        }else{
            androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setMessage("What do you want to do with this post?");
            alert.setTitle("Employer Post Action");
            alert.setPositiveButton("Edit Description", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(v.getContext(), EditDescription.class);
                    i.putExtra("job_id",job.getJob_ID());
                    context.startActivity(i);
                }
            }).setNegativeButton("Request List", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(v.getContext(), RequestList.class);
                    i.putExtra("job_id",job.getJob_ID());
                    i.putExtra("job_title",job.getJob_title());
                    context.startActivity(i);
                }
            });
            alert.show();
        }

    }
}
