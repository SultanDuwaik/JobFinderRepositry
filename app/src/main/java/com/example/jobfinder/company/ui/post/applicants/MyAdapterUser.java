package com.example.jobfinder.company.ui.post.applicants;

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
import com.example.jobfinder.company.Applicant;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class MyAdapterUser extends RecyclerView.Adapter<MyAdapterUser.MyViewHolder> {

    Context context;
    ArrayList<Users> list;

    public MyAdapterUser(Context context, ArrayList<Users> list) {

        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.companies_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  MyAdapterUser.MyViewHolder holder, int position ) {
        Picasso.get().load(list.get(position).getImage()).into(holder.img1);
        holder.name.setText(list.get(position).getName());
        holder.preferredJob.setText(list.get(position).getJobTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context, Applicant.class);
                intent.putExtra("user id",list.get(position).getUserId());
                intent.putExtra("post id", list.get(position).getPostId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        ImageView img1;
        TextView name,preferredJob;

        public MyViewHolder(@NonNull  View itemView)
        {
            super(itemView);
            name =itemView.findViewById(R.id.company_name);
            img1 =itemView.findViewById(R.id.company_image);
            preferredJob =itemView.findViewById(R.id.company_description);

        }


    }
}
