package com.example.freelanceclone;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ApprovalAdapter extends RecyclerView.Adapter<ApprovalAdapter.ViewHolder> {
    Context context;
    ArrayList<ApprovalModel> approvalArrayList;

    public ApprovalAdapter(Context context, ArrayList<ApprovalModel> approvalArrayList) {
        this.context = context;
        this.approvalArrayList = approvalArrayList;
    }

    @NotNull
    @Override
    public ApprovalAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.approved_list,parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ApprovalAdapter.ViewHolder holder, int position) {
        ApprovalModel approval = approvalArrayList.get(position);
        holder.tv_app_title.setText(approval.getJob_title());
        holder.tv_app_id.setText(approval.getJob_id());
        holder.tv_app_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoHub(v);
            }
        });
        holder.tv_app_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoHub(v);
            }
        });
    }

    private void GotoHub(View v) {
        Intent i = new Intent(v.getContext(), JobHubActivity.class);
        context.startActivity(i);
    }

    @Override
    public int getItemCount() { return approvalArrayList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_app_title, tv_app_id;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_app_title = itemView.findViewById(R.id.tv_app_title);
            tv_app_id = itemView.findViewById(R.id.tv_app_id);
        }
    }
}
