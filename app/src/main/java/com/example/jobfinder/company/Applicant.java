package com.example.jobfinder.company;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jobfinder.R;
import com.example.jobfinder.job_seeker.MainBottomNavigation;
import com.example.jobfinder.job_seeker.ui.Job;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Applicant extends AppCompatActivity {

    public static Activity applicant;
    public static boolean isApplicantOpen = false;

    String postId, userId , companyImage, companyName, jobSeekerName, jobSeekerEmail, companyEmail;
    Button accept , reject;
    SimpleDateFormat dateFormat;
    Boolean alreadyNotified = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_applicant);

        applicant = this;
        isApplicantOpen = true;

        TextView name = findViewById(R.id.jobSeekerName);
        TextView preferredJob = findViewById(R.id.JobSeekerPreferredJob);
        TextView userSkills = findViewById(R.id.JobSeekerSkills);
        TextView description = findViewById(R.id.JobSeekerDescription);
        TextView education = findViewById(R.id.JobSeekerEducation);
        TextView experience = findViewById(R.id.JobSeekerExperience);
        ImageView image = findViewById(R.id.JobSeekerImage);
        TextView email = findViewById(R.id.JobSeekerEmail);
        TextView phone = findViewById(R.id.JobSeekerPhone);
        TextView gender = findViewById(R.id.JobSeekerGender);
        TextView location = findViewById(R.id.JobSeekerLocation);
        accept = findViewById(R.id.acceptJobSeeker);
        reject = findViewById(R.id.rejectJobSeeker);

        dateFormat = new SimpleDateFormat("HH:mm");


        Bundle bundle =getIntent().getExtras();
        if( bundle != null){
            userId = bundle.getString("user id");
            postId = bundle.getString("post id");
        }

        FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error)
            {
                companyImage = value.get("image url").toString();
                companyName  = value.get("name").toString();
                companyEmail = value.get("email").toString();
            }
        });


        FirebaseFirestore.getInstance().collection("user").document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error)
            {
                jobSeekerName = value.get("first name").toString()+" "+value.get("last name").toString();
                jobSeekerEmail = value.get("email").toString();


                name.setText(jobSeekerName);
                preferredJob.setText(value.get("preferred job").toString());
                userSkills.setText(value.get("skills").toString());
                description.setText(value.get("description").toString());
                education.setText(value.get("education").toString());
                experience.setText(value.get("experience").toString());
                Picasso.get().load(value.get("image url").toString()).into(image);
                email.setText(jobSeekerEmail);
                phone.setText(value.get("phone").toString());
                gender.setText(value.get("gender").toString());
                location.setText(value.get("location").toString());
            }
        });


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                deleteFrom();
                String time = dateFormat.format(new Date());
                Map<String,Object> jobSeekerNotification = new HashMap<>();
                jobSeekerNotification.put("image url",companyImage);
                jobSeekerNotification.put("name",companyName);
                jobSeekerNotification.put("time stamp", time);
                jobSeekerNotification.put("notification",companyName+" accepted your appliance and will be looking to contact you through your email");

                FirebaseFirestore.getInstance().collection("user")
                        .document(userId)
                        .collection("notification")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (DocumentChange doc : task.getResult().getDocumentChanges()) {
                                    if (doc.getDocument().getId().equals(userId)) {
                                        alreadyNotified = true;
                                    }
                                }
                                if (!alreadyNotified) {
                                    FirebaseFirestore.getInstance().collection("user")
                                            .document(userId).collection("notification").document(userId).set(jobSeekerNotification);
                                }
                            }
                        });

                Intent intent = new Intent(Applicant.this , MainBottomNavigationCompany.class);
                startActivity(intent);
                finish();
                MailService companyMail = new MailService(Applicant.this, companyEmail,"You Accepted an applicant through JobFinder"
                        ,"Dear "+companyName+";\nYou Accepted "+jobSeekerName+" for a role in your company, so we would like to provide his email so you can contact him/her. \n\nApplicant Email: "+jobSeekerEmail+".\n\n\nBest Wishes;\nJobFinder");

                MailService jobSeekerMail = new MailService(Applicant.this,jobSeekerEmail,"A Company Has Accepted You for a Job"
                        ,"Dear " + jobSeekerName + ";\nYou have been accepted by " + companyName + " for a job, they will be contacting you through your email.\n\n\nBest Wishes;\nJobFinder");


                companyMail.execute();
                jobSeekerMail.execute();
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                deleteFrom();

                String time = dateFormat.format(new Date());

                Map<String,Object> jobSeekerNotification = new HashMap<>();
                jobSeekerNotification.put("image url",companyImage);
                jobSeekerNotification.put("name",companyName);
                jobSeekerNotification.put("time stamp", time);
                jobSeekerNotification.put("notification","thanks for the appliance, but "+companyName+" is looking for other type of applicants");

                FirebaseFirestore.getInstance().collection("user")
                        .document(userId)
                        .collection("notification")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (DocumentChange doc : task.getResult().getDocumentChanges()) {
                                    if (doc.getDocument().getId().equals(userId)) {
                                        alreadyNotified = true;
                                    }
                                }
                                if (!alreadyNotified) {
                                    FirebaseFirestore.getInstance().collection("user")
                                            .document(userId).collection("notification").document(userId).set(jobSeekerNotification);
                                }
                            }
                        });

                finish();
                Intent intent = new Intent(Applicant.this , MainBottomNavigationCompany.class);
                intent.putExtra("open post" ,true);
                startActivity(intent);
            }
        });
    }

    private void deleteFrom()
    {
        FirebaseFirestore.getInstance()
                .collection("user")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("post")
                .document(postId)
                .collection("applicants")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
                    {
                        for(DocumentChange doc:value.getDocumentChanges())
                        {
                            if(doc.getDocument().get("user id").equals(userId))
                            {
                                doc.getDocument().getReference().delete();
                            }
                        }
                    }
                });

        FirebaseFirestore.getInstance().collection("user").document(userId).collection("applied for").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentChange doc:value.getDocumentChanges())
                {
                    if(doc.getDocument().get("post Id").equals(postId))
                    {
                        doc.getDocument().getReference().delete();
                    }
                }
            }
        });


        FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("notification").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                for(DocumentChange doc:task.getResult().getDocumentChanges())
                {
                    if(doc.getDocument().get("name").equals(jobSeekerName))
                    {
                        doc.getDocument().getReference().delete();
                    }
                }
            }
        });


    }
}