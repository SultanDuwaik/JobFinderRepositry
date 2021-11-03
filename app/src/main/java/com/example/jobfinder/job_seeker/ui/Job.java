package com.example.jobfinder.job_seeker.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobfinder.R;
import com.example.jobfinder.job_seeker.MainBottomNavigation;
import com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.profile.CompleteProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Job extends AppCompatActivity {

    public static Activity job;
    public static boolean isJobOpen = false;

    ImageView companyImage;
    TextView jobTitle , jobDescription , skillsRequired , jobLocation , jobRole , monthlySalary , companyType, companyName;
    String companyId , postId, companyNameString;
    Button applyButton , unApplyButton ;
    DocumentReference documentReference;

    ImageButton savePost;
    SimpleDateFormat dateFormat;
    String userId , imageUrl;
    Boolean alreadyNotified , alreadyApplied = false , alreadySaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        job = this;
        isJobOpen = true;

        companyName = findViewById(R.id.companyName);
        companyImage = findViewById(R.id.imageSource);
        jobTitle = findViewById(R.id.jobTitle);
        applyButton = findViewById(R.id.applyButton);
        jobDescription = findViewById(R.id.jobDescription);
        skillsRequired = findViewById(R.id.skillsRequired);
        jobLocation = findViewById(R.id.jobAddress);
        jobRole = findViewById(R.id.jobRole);
        monthlySalary = findViewById(R.id.monthlySalary);
        companyType = findViewById(R.id.companyType);
        savePost = findViewById(R.id.saveJobButton);
        unApplyButton = findViewById(R.id.unApplyButton);
        alreadyNotified = false;

        dateFormat = new SimpleDateFormat("HH:mm");


        companyId = getIntent().getStringExtra("companyId");
        postId = getIntent().getStringExtra("postId");
        userId =FirebaseAuth.getInstance().getCurrentUser().getUid();

        documentReference = FirebaseFirestore.getInstance().collection("user").document(companyId);

        ButtonVisibility();


        unApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentReference.collection("post").document(postId).collection("applicants")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        for (DocumentChange doc : value.getDocumentChanges())
                        {
                            if(doc.getDocument().get("post id").equals(postId))
                            {
                                doc.getDocument().getReference().delete();
                            }
                        }
                    }
                });
            FirebaseFirestore.getInstance().collection("user").document(userId).collection("applied for")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for(DocumentChange appliedForDoc: value.getDocumentChanges())
                    {
                        if(appliedForDoc.getDocument().get("post Id").toString().equals(postId))
                        {
                            appliedForDoc.getDocument().getReference().delete();
                        }
                    }
                }
            });
                finish();

            }
        });

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error)
            {
                imageUrl = value.get("image url").toString();
                companyNameString = value.get("name").toString();
                Picasso.get().load(imageUrl).into(companyImage);
            }
        });

        documentReference.collection("post").document(postId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error)
            {

                jobTitle.setText(value.get("title").toString());
                jobDescription.setText(value.get("job description").toString());
                skillsRequired.setText(value.get("skills required").toString());
                companyType.setText(value.get("job category").toString());
                companyName.setText(companyNameString);

            }
        });

        savePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore.getInstance().collection("user").document(userId).collection("saved jobs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        for(DocumentChange doc :task.getResult().getDocumentChanges())
                        {
                             if(doc.getDocument().get("company id").toString().equals(companyId)&&doc.getDocument().get("post id").toString().equals(postId))
                             {
                                 alreadySaved = true;
                             }
                        }

                        if(!alreadySaved)
                        {


                            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                                    documentReference.collection("post").document(postId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot value1, @Nullable FirebaseFirestoreException error) {
                                            HashMap<String, Object> savedJobs = new HashMap<>();
                                            savedJobs.put("company id", companyId);
                                            savedJobs.put("post id", postId);
                                            savedJobs.put("job title" ,value1.get("title").toString() );
                                            savedJobs.put("job image" , value.get("image url").toString());
                                            savedJobs.put("job role" , value1.get("job role").toString());
                                            FirebaseFirestore.getInstance().collection("user").document(userId).collection("saved jobs").add(savedJobs);
                                        }
                                    });


                                }
                            });
                            }
                    }
                });


            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore.getInstance().collection("user").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot hh, @Nullable FirebaseFirestoreException error) {
                        if(hh.get("profile completed").equals(true))
                        {
                            Map<String,Object> appliedForData = new HashMap<>();

                            appliedForData.put( "company Id", companyId );
                            appliedForData.put( "post Id" , postId);
                            appliedForData.put( "title", jobTitle.getText());
                            appliedForData.put( "image url", imageUrl);
                            appliedForData.put( "job role" , jobRole.getText());

                            FirebaseFirestore.getInstance().collection("user")
                                    .document(userId).collection("applied for").document(postId).set(appliedForData);

                            Map<String,Object> applicantsData = new HashMap<>();


                            FirebaseFirestore.getInstance().collection("user").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                    applicantsData.put( "user id" , userId );
                                    applicantsData.put( "post id" , postId);
                                    applicantsData.put( "name" , value.get("first name").toString()+" "+value.get("last name").toString());
                                    applicantsData.put( "preferred job" , value.get("preferred job").toString());
                                    applicantsData.put( "image url" , value.get("image url").toString());

                                    FirebaseFirestore.getInstance().collection("user")
                                            .document(companyId).collection("post").document(postId)
                                            .collection("applicants").add(applicantsData);
                                }
                            });

                            FirebaseFirestore.getInstance().collection("user").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                    String time = dateFormat.format(new Date());
                                    Map<String, Object> notificationCompanyData = new HashMap<>();
                                    notificationCompanyData.put("notification", value.get("first name").toString() + " " + value.get("last name").toString() + " has applied to one of your posts");
                                    notificationCompanyData.put("name", value.get("first name").toString() + " " + value.get("last name").toString());
                                    notificationCompanyData.put("image url", value.get("image url").toString());
                                    notificationCompanyData.put("time stamp", time);


                                    FirebaseFirestore.getInstance().collection("user")
                                            .document(companyId)
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
                                                                .document(companyId).collection("notification").document(userId).set(notificationCompanyData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                FirebaseFirestore.getInstance().collection("user").document(companyId).update("has notifications", true);
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                }
                            });
                            finish();
                        }
                        else
                        {
                            Toast.makeText(Job.this, "you need to complete your profile to apply", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Job.this , MainBottomNavigation.class);
                            intent.putExtra("open profile" , true);
                            startActivity(intent);
                            finish();
                        }
                    }
                });


            }
        });
    }

    private void ButtonVisibility()
    {

        FirebaseFirestore.getInstance().collection("user").document(userId)
                .collection("applied for").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
            {
                for(DocumentChange doc:value.getDocumentChanges())
                {
                    if(doc.getDocument().get("post Id").equals(postId))
                    {
                        alreadyApplied = true;
                    }
                }

                if(alreadyApplied)
                {
                    applyButton.setVisibility(View.GONE);
                    unApplyButton.setVisibility(View.VISIBLE);
                }
                else
                {
                    applyButton.setVisibility(View.VISIBLE);
                    unApplyButton.setVisibility(View.GONE);
                }
            }
        });

    }
}