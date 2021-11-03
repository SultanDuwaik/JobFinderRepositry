package com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobfinder.R;
import com.example.jobfinder.company.ui.notifications_company.NotificationsCompanyAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ModelClass> notificationsData;

    String currentJobSeekerId;
    FloatingActionButton clearNotifications;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        notificationsData = new ArrayList<>();

        clearNotifications = view.findViewById(R.id.clearNotificationsJobSeeker);
        currentJobSeekerId = FirebaseAuth.getInstance().getUid();
        recyclerView = view.findViewById(R.id.RecyclerView);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        FirebaseFirestore.getInstance().collection("user").document(currentJobSeekerId)
                .collection("notification").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().isEmpty())
                {
                    System.out.println("collection does not exist");
                }
                else
                {
                    for(DocumentChange doc : task.getResult().getDocumentChanges())
                    {
                        notificationsData.add(new ModelClass(
                                 doc.getDocument().get("image url").toString()
                                ,doc.getDocument().get("name").toString()
                                ,doc.getDocument().get("notification").toString()
                                ,doc.getDocument().get("time stamp").toString()));
                        adapter = new NotificationsCompanyAdapter(notificationsData , getContext());
                        recyclerView.setAdapter(adapter);
                    }
                }
            }
        });

        clearNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("user")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("notification").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
                    {
                        for (DocumentChange doc :value.getDocumentChanges())
                        {
                            doc.getDocument().getReference().delete();
                        }
                    }
                });

                notificationsData.clear();
                adapter = new NotificationsCompanyAdapter(notificationsData , getContext());
                recyclerView.setAdapter(adapter);

            }
        });
        return view;
    }
}