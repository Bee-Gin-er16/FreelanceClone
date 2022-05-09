package com.example.freelanceclone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
   Context context;
   ArrayList<User> userArrayList;

   StorageReference storageReference;

    public UserAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NotNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserAdapter.ViewHolder holder, int position) {
        User user = userArrayList.get(position);
        holder.name.setText(user.getName());
        holder.status.setText(user.getStatus());
        holder.sex.setText(user.getSex());
        //TODO pass an extra to the next activity which is the main chat UI but make the UI first
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Adapter works name clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() { return userArrayList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, status, sex;
        ImageView profile;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.username);
            status = itemView.findViewById(R.id.status);
            sex = itemView.findViewById(R.id.sex);
            profile = itemView.findViewById(R.id.profile);
        }
    }
}
