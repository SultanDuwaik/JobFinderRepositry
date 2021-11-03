package com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.profile;

import static com.example.jobfinder.job_seeker.MainBottomNavigation.mainBottomNavigation;
import static com.example.jobfinder.job_seeker.ui.Job.isJobOpen;
import static com.example.jobfinder.job_seeker.ui.Job.job;
import static com.example.jobfinder.job_seeker.ui.search_result.SearchResultActivity.isSearchResultOpen;
import static com.example.jobfinder.job_seeker.ui.search_result.SearchResultActivity.searchResult;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jobfinder.Login;
import com.example.jobfinder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {


    Button  completeProfile , editProfile;
    RelativeLayout description_form , skills_form , experience_form , preferred_job_form , education_form;
    RelativeLayout logout;
    Uri mImageUri;
    DocumentReference reference;
    Boolean didUserUpload;
    ImageView circleImage , coverImage;
    DatabaseReference databaseReference;
    CircleImageView circleImageView;
    TextView profileName , email ,gender , location ,phone,description , skills, experience , preferredJob , education;
    ActivityResultLauncher<Intent> mLauncher;

    StorageTask storageTask;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("images").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference = FirebaseFirestore.getInstance().collection("user")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());





        circleImage = view.findViewById(R.id.imageSource);

        completeProfile = view.findViewById(R.id.completeProfile);
        logout = view.findViewById(R.id.logout);
        skills_form = view.findViewById(R.id.skillsForm);
        experience_form = view.findViewById(R.id.experienceForm);
        description_form = view.findViewById(R.id.descriptionForm);
        preferred_job_form = view.findViewById(R.id.preferredJobForm);
        education_form = view.findViewById(R.id.educationForm);

        editProfile =view.findViewById(R.id.editProfile);

        coverImage = view.findViewById(R.id.image);

        profileName = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.email);
        gender = view.findViewById(R.id.gender);
        phone = view.findViewById(R.id.phone);
        location = view.findViewById(R.id.location);
        description = view.findViewById(R.id.description);
        skills = view.findViewById(R.id.skills);
        experience = view.findViewById(R.id.experience);
        preferredJob = view.findViewById(R.id.preferredJob);
        education = view.findViewById(R.id.education);

        circleImageView = view.findViewById(R.id.imageSource);

        DocumentReference reference = FirebaseFirestore.getInstance().collection("user")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        setImageOnStart();



        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                profileName.setText(task.getResult().getString("first name")+" "+task.getResult().getString("last name"));
                email.setText(task.getResult().getString("email"));
                gender.setText(task.getResult().getString("gender"));
                location.setText(task.getResult().getString("location"));
                phone.setText(task.getResult().getString("phone"));

                if(task.isSuccessful())
                {
                    if(task.getResult().exists())
                    {
                        if(task.getResult().getString("description") == null)
                        {
                            skills_form.setVisibility(View.INVISIBLE);
                            experience_form.setVisibility(View.INVISIBLE);
                            description_form.setVisibility(View.INVISIBLE);
                            preferred_job_form.setVisibility(View.INVISIBLE);
                            education_form.setVisibility(View.INVISIBLE);
                            editProfile.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            ((ViewGroup)completeProfile.getParent()).removeView(completeProfile);
                            skills_form.setVisibility(View.VISIBLE);
                            experience_form.setVisibility(View.VISIBLE);
                            description_form.setVisibility(View.VISIBLE);
                            preferred_job_form.setVisibility(View.VISIBLE);
                            education_form.setVisibility(View.VISIBLE);
                            editProfile.setVisibility(View.VISIBLE);

                            RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT);
                            params.addRule(RelativeLayout.BELOW , R.id.personal_information_form);
                            params.setMargins(25,10,25,10);
                            description_form.setLayoutParams(params);

                            description.setText(task.getResult().getString("description"));
                            skills.setText(task.getResult().getString("skills"));
                            experience.setText(task.getResult().getString("experience"));
                            preferredJob.setText(task.getResult().getString("preferred job"));
                            education.setText(task.getResult().getString("education"));

                        }
                    }
                    else
                    {
                        System.out.println("task get result null");
                    }
                }
                else
                {
                    System.out.println("not successful");
                }

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext() ,Login.class));

                mainBottomNavigation.finish();

                if(isJobOpen)
                {
                    job.finish();
                    isJobOpen =false;

                }
                if(isSearchResultOpen)
                {
                    searchResult.finish();
                    isSearchResultOpen = false;
                }

            }
        });

        completeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getContext(),CompleteProfile.class));
            }
        });




        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
                didUserUpload = true;

            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),CompleteProfile.class));
            }
        });
        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mLauncher.launch(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == getActivity().RESULT_OK
                                && result.getData() != null && (result.getData()).getData() != null) {
                            mImageUri = (result.getData()).getData();
                            uploadFile();
                        }
                    }
                });
    }

    public void uploadFile()
    {
        if(mImageUri != null)
        {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("images").child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            storageTask = storageReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            reference.update("image url" , uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Picasso.get().load(uri.toString()).into(circleImage);
                                    Picasso.get().load(uri.toString()).into(coverImage);
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void setImageOnStart()
    {
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                if(value.get("image url")!=null)
                {
                    Picasso.get().load(value.get("image url").toString()).into(circleImage);
                    Picasso.get().load(value.get("image url").toString()).into(coverImage);
                }
            }
        });
    }
}

