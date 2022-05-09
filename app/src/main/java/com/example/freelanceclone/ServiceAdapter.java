package com.example.freelanceclone;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder>{

    Context context;
    ArrayList<ServiceModel> serviceList;
    private String service_id;

    public ServiceAdapter(Context context, ArrayList<ServiceModel> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.service_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceAdapter.ViewHolder holder, int position) {
        ServiceModel service = serviceList.get(position);
        /*BIG REMINDER: Model Class variables have to reflect with the same name as those stored key values
        e.g: Service_name as key should be the same for "String Service_name" variable in model class so that data can be passed
        from cloud Firestore to RecyclerviewOnBindHolder
        For now, name cannot be resolved yet. refer to ServicePost class to see the key value for data.
         */
        /*TODO replace line under here to " holder.tv_provided_user.setText(service.getUID()); "
           When you reset the database all over(ie: fresh start)
        */
        holder.tv_provided_user.setText(service.getPoster()); //doesn't work yet for now it's the owners uid.
        holder.tv_provided_service.setText(service.getService());
        holder.tv_provided_min.setText(service.getMin());
        holder.tv_provided_max.setText(service.getMax());
        service_id = service.getUID();

    }

    @Override
    public int getItemCount() { return serviceList.size(); }

    public void filter_list(ArrayList<ServiceModel> filteredList) {
        serviceList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_provided_user,tv_provided_service,tv_provided_min,tv_provided_max;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Experimental Delete if it does not work.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Clicked on ?Item", Toast.LENGTH_SHORT).show();
                }
            });
            tv_provided_user = itemView.findViewById(R.id.tv_provided_user);
            tv_provided_service = itemView.findViewById(R.id.tv_provided_service);
            tv_provided_min = itemView.findViewById(R.id.tv_provided_min);
            tv_provided_max = itemView.findViewById(R.id.tv_provided_max);
        }
    }
}