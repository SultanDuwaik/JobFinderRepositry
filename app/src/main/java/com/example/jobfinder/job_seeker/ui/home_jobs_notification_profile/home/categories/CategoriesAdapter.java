package com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.home.categories;

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
import com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.home.HomeFragment;
import com.example.jobfinder.job_seeker.ui.search_result.SearchResultActivity;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    ArrayList<Categories> categoriesData;
    Context context;
    public CategoriesAdapter(Context context , ArrayList<Categories> categoriesData)
    {
        this.categoriesData = categoriesData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.text.setText(categoriesData.get(position).getText());
        holder.image.setImageResource(categoriesData.get(position).getImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SearchResultActivity.class);
                intent.putExtra("selected category" , categoriesData.get(position).getText());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView text;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.categoryText);
            image = itemView.findViewById(R.id.categoryImage);
        }
    }
}
