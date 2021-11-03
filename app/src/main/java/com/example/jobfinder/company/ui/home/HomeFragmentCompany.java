package com.example.jobfinder.company.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobfinder.R;
import com.example.jobfinder.company.ui.post.applicants.Users;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class HomeFragmentCompany extends Fragment {

    RecyclerView recyclerView;
    CollectionReference db;
    RecyclerView.Adapter  myAdapter;
    ArrayList<Companys> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_company, container, false);




        recyclerView =view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager( getContext() ));

        db = FirebaseFirestore.getInstance().collection("user");
        list = new ArrayList<>();

        EventChangeListener();



        return view;
    }

    private void EventChangeListener() {
        db.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
                    {
                        for(DocumentChange dc : value.getDocumentChanges())
                        {
                            if(dc.getDocument().exists())
                            {
                                if(dc.getDocument().get("user type").equals("company"))
                                {
                                    dc.getDocument().getReference().collection("post").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            for(DocumentChange dc1: value.getDocumentChanges())
                                            {
                                                list.add(new Companys(dc.getDocument().get("image url").toString(), dc1.getDocument().get("title").toString()
                                                                     ,dc1.getDocument().get("job description").toString()));
                                                myAdapter = new MyAdapter(getContext(),list);
                                                recyclerView.setAdapter(myAdapter);
                                            }
                                        }
                                    });
                                }
                            }

                        }
                            //myAdapter.notifyDataSetChanged();
                    }
        });
    }
}