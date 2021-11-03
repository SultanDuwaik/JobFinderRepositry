package com.example.jobfinder.company.ui.notifications_company;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobfinder.R;
import com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.notifications.ModelClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationsCompanyAdapter extends RecyclerView.Adapter<NotificationsCompanyAdapter.ViewHolder>
{
    ArrayList<ModelClass> notificationsData;
    Context context;

    public NotificationsCompanyAdapter(ArrayList<ModelClass> notificationsData , Context context)
    {
        this.context = context;
        this.notificationsData = notificationsData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

        Picasso.get().load(notificationsData.get(position).getNotificationImage()).into(holder.notificationImage);
        holder.notificationFrom.setText(notificationsData.get(position).getFromText());
        holder.notificationText.setText(notificationsData.get(position).getNotificationContent());
        holder.notificationTimestamp.setText(notificationsData.get(position).getNotificationTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("you clicked on "+ notificationsData.get(position).getFromText());
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView notificationImage;
        TextView notificationFrom;
        TextView notificationText;
        TextView notificationTimestamp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationImage = itemView.findViewById(R.id.notification_image);
            notificationFrom = itemView.findViewById(R.id.notification_from_name);
            notificationText = itemView.findViewById(R.id.notification_description);
            notificationTimestamp = itemView.findViewById(R.id.notification_timestamp);
        }
    }
}
