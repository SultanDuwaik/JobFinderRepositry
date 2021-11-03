package com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.jobs.applied_for;

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
import com.example.jobfinder.CompanyList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AppliedFor extends Fragment {



    ArrayList<CompanyList> companyListArrayList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.applied_for,container,false);

        recyclerView = view.findViewById(R.id.applied_for_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        companyListArrayList = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("user")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("applied for").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {

                if(task.getResult().isEmpty())
                {
                    System.out.println("you haven't applied to any company");
                }
                else
                {

                    for(DocumentChange doc :task.getResult().getDocumentChanges())
                    {
                        if(doc.getDocument().exists())
                        {
                            companyListArrayList.add(new CompanyList(
                                             doc.getDocument().get("image url").toString()
                                            ,doc.getDocument().get("title").toString()
                                            ,doc.getDocument().get("job role").toString()
                                            ,doc.getDocument().get("company Id").toString()
                                            ,doc.getDocument().get("post Id").toString()));

                                    adapter = new AppliedForAdapter(getActivity(), companyListArrayList);
                                    recyclerView.setAdapter(adapter);

                        }
                    }
                }
            }
        });
        return view;
    }







}
