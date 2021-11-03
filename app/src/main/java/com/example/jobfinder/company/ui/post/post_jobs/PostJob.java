package com.example.jobfinder.company.ui.post.post_jobs;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jobfinder.R;
import com.example.jobfinder.company.MainBottomNavigationCompany;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostJob extends Fragment {

    // Auth for fire base
    private FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    String company_ID;

    HashMap <String , Object> post = new HashMap<>();
    Button save_post_details;
    EditText title , jobDescription, skillsRequired, jobDetails , jobRole , addressLine , salary;
    Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_job, container, false);

        // Edit Text
        title = view.findViewById(R.id.title);
        jobDescription = view.findViewById(R.id.job_description);
        skillsRequired = view.findViewById(R.id.skills_required);
        jobDetails = view.findViewById(R.id.job_details);
        jobRole = view.findViewById(R.id.jobRolePost);
        addressLine = view.findViewById(R.id.jobAddressLine);
        salary = view.findViewById(R.id.salaryPost);

        //Button
        save_post_details = view.findViewById(R.id.save_post_details);

        //Spinner
        spinner = view.findViewById(R.id.spinner);

        //connected firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        company_ID = mAuth.getCurrentUser().getUid();


        save_post_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Titles = title.getText().toString().trim();
                String Job_descriptions = jobDescription.getText().toString().trim();
                String Skills_Required = skillsRequired.getText().toString().trim();
                String Job_Details = jobDetails.getText().toString().trim();
                String Spinner = spinner.getSelectedItem().toString().trim();
                String Job_Role = jobRole.getText().toString().trim();
                String Address_Line = addressLine.getText().toString().trim();
                String Salary = salary.getText().toString().trim();


                PostMethod(Titles, Job_descriptions , Skills_Required , Job_Details , Spinner , Job_Role , Address_Line , Salary);

            }
        });
        return view;
    }

    private void PostMethod(String titles, String job_descriptions, String skills_required, String job_details, String spinner ,String Job_Role , String Address_Line, String Salary) {
        if(TextUtils.isEmpty(titles))
        {
            Toast.makeText(getContext(), "please enter title", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(job_descriptions))
        {
            Toast.makeText(getContext(), "please enter job descriptions", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(skills_required))
        {
            Toast.makeText(getContext(), "please enter skills completePost", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(job_details))
        {
            Toast.makeText(getContext(), "please enter skills completePost", Toast.LENGTH_SHORT).show();
        }
        if(spinner.equals("Select Category"))
        {
            Toast.makeText(getContext(), "please select your category", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(Job_Role))
        {
            Toast.makeText(getContext(), "please enter skills completePost", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(Address_Line))
        {
            Toast.makeText(getContext(), "please enter skills completePost", Toast.LENGTH_SHORT).show();
        }

        else
        {
            CollectionReference collectionReference = firebaseFirestore.collection("user");

            collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        post.put( "title" , titles );
                        post.put( "job description" , job_descriptions );
                        post.put( "skills required" , skills_required );
                        post.put( "job details" , job_details );
                        post.put( "job category" , spinner );
                        post.put( "job role" , Job_Role);
                        post.put( "address line" , Address_Line);
                        if(salary!=null)
                        {
                            post.put("salary" , Salary);
                        }
                        else
                        {
                            post.put("salary", "not specified");
                        }

                    collectionReference.document(company_ID).collection("post").add(post);

                    title.getText().clear();
                    jobDescription.getText().clear();
                    skillsRequired.getText().clear();
                    jobDetails.getText().clear();
                    jobRole.getText().clear();
                    addressLine.getText().clear();
                    salary.getText().clear();

                    startActivity(new Intent(getContext() , MainBottomNavigationCompany.class));
                }
            });
        }
    }
}