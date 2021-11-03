package com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.jobs.applied_for;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobfinder.R;
import com.example.jobfinder.CompanyList;
import com.example.jobfinder.job_seeker.ui.Job;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AppliedForAdapter extends RecyclerView.Adapter<AppliedForAdapter.ViewHolder> {


    ArrayList<CompanyList> searchResultList;
    Context context;

    public AppliedForAdapter(Context context , ArrayList<CompanyList> searchResultList)
    {
        this.context = context;
        this.searchResultList = searchResultList;

    }


    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.companies_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Picasso.get().load(searchResultList.get(position).getImage()).into(holder.image);
        holder.companyName.setText(searchResultList.get(position).getCompanyName());
        holder.description.setText(searchResultList.get(position).getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("you clicked" + searchResultList.get(position).getCompanyName());
                Intent intent = new Intent(context , Job.class);
                intent.putExtra("companyId" , searchResultList.get(position).getCompanyId());
                intent.putExtra("postId" , searchResultList.get(position).getPostId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        ImageView image;
        TextView companyName;
        TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.company_image);
            companyName = itemView.findViewById(R.id.company_name);
            description = itemView.findViewById(R.id.company_description);
        }
    }
}
