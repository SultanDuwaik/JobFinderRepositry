package com.example.jobfinder.company.ui.post.applicants;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jobfinder.R;
import com.example.jobfinder.company.Applicant;
import com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.jobs.applied_for.AppliedForAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class Applicants extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore db;
    MyAdapterUser myAdapter;
    ArrayList<Users> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_applicants, container, false);

        recyclerView =view.findViewById(R.id.recycleViewuser);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        list = new ArrayList<Users>();

        EventChangeListenerusers();

        return view;
    }

    private void EventChangeListenerusers() {
        db.collection("user").document(FirebaseAuth.getInstance().getUid()).collection("post").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
            {
                for(DocumentChange doc:value.getDocumentChanges())
                {
                    doc.getDocument().getReference().collection("applicants").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            for(DocumentChange doc1:value.getDocumentChanges())
                            {
                                list.add(new Users(doc1.getDocument().get("name").toString()
                                        ,doc1.getDocument().get("image url").toString()
                                        ,doc.getDocument().get("title").toString()
                                        ,doc1.getDocument().get("post id").toString()
                                        ,doc1.getDocument().get("user id").toString()));

                                myAdapter = new MyAdapterUser(getActivity(), list);
                                recyclerView.setAdapter(myAdapter);
                            }
                        }
                    });
                }
            }
        });
    }
}
