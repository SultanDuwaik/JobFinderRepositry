package com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jobfinder.Login;
import com.example.jobfinder.R;
import com.example.jobfinder.SplashScreen;
import com.example.jobfinder.job_seeker.MainBottomNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class CompleteProfile extends AppCompatActivity {

    EditText preferredJob , education , skills , experience , description;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        preferredJob = findViewById(R.id.preferredJobText);
        education = findViewById(R.id.educationText);
        skills = findViewById(R.id.skills_completeProfile);
        experience = findViewById(R.id.experience_completeProfile);
        description = findViewById(R.id.description_completeProfile);

        save = findViewById(R.id.save_complete_profile);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (TextUtils.isEmpty(preferredJob.getText()) || TextUtils.isEmpty(education.getText()) || TextUtils.isEmpty(skills.getText())
                        || TextUtils.isEmpty(experience.getText()) || TextUtils.isEmpty(description.getText()))
               {

                    Toast.makeText(CompleteProfile.this, "one of the required fields is not filled", Toast.LENGTH_SHORT).show();
               }
               else
               {

                   Map<String, Object> map = new HashMap<>();
                   map.put("preferred job", preferredJob.getText().toString().trim());
                   map.put("education", education.getText().toString().trim());
                   map.put("skills", skills.getText().toString().trim());
                   map.put("experience", experience.getText().toString().trim());
                   map.put("description", description.getText().toString().trim());

                   DocumentReference reference = FirebaseFirestore.getInstance().collection("user")
                           .document(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    reference.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            FirebaseFirestore.getInstance().collection("user").
                                    document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update("profile completed",true);

                            Intent intent = new Intent(CompleteProfile.this , MainBottomNavigation.class);
                            intent.putExtra("open profile" , true);
                            startActivity(intent);
                            finish();
                        }
                    });
               }
           }
        });
    }

}