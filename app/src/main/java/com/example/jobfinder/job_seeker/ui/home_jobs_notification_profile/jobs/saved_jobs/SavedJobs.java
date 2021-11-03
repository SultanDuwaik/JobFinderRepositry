package com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.jobs.saved_jobs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobfinder.R;
import com.example.jobfinder.CompanyList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class SavedJobs extends Fragment {
    ArrayList<CompanyList> companyListArrayList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.saved_jobs,container,false);

        recyclerView = view.findViewById(R.id.saved_jobs_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);





        FirebaseFirestore.getInstance().collection("user")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("saved jobs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                companyListArrayList = new ArrayList<>();
                for (DocumentChange doc : task.getResult().getDocumentChanges())
                {
                    companyListArrayList.add(new CompanyList(
                            doc.getDocument().get("job image").toString()
                            ,doc.getDocument().get("job title").toString()
                            ,doc.getDocument().get("job role").toString()
                            ,doc.getDocument().get("company id").toString()
                            ,doc.getDocument().get("post id").toString()));

                    adapter = new SavedJobsAdapter(getActivity(), companyListArrayList);
                    recyclerView.setAdapter(adapter);
                }


            }
        });





        return view;
    }
}
