package com.example.jobfinder.company.ui;

import static com.example.jobfinder.company.Applicant.applicant;
import static com.example.jobfinder.company.Applicant.isApplicantOpen;
import static com.example.jobfinder.company.MainBottomNavigationCompany.mainBottomNavigationCompany;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragmentCompany extends Fragment {

    RelativeLayout logoutCompany;


    ImageView imageSource , coverImageCompany;

    Uri mImageUri;

    StorageTask storageTask;


    CircleImageView circleImageView;

    DatabaseReference databaseReference;
    ActivityResultLauncher<Intent> mLauncher;

    //TextView for Personal Information

    DocumentReference documentReference;

    TextView  profile_name  , email , phone   ,location , overview , address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_company, container, false);

        //Buttons
        logoutCompany = view.findViewById(R.id.logoutCompany);

        circleImageView = view.findViewById(R.id.imageSourceCompany);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("images").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

         documentReference = FirebaseFirestore.getInstance().collection("user").
                document(FirebaseAuth.getInstance().getCurrentUser().getUid());



        coverImageCompany = view.findViewById(R.id.imageCompany);
        //TextView user name
        profile_name = view.findViewById(R.id.profile_name);

        //TextView more information
        email = view.findViewById(R.id.emailCompany);
        phone = view.findViewById(R.id.phoneCompany);
        location = view.findViewById(R.id.locationCompany);

        //TextView for Personal Information
        overview = view.findViewById(R.id.overview);
        address = view.findViewById(R.id.address);

        //images
        imageSource = view.findViewById(R.id.imageSourceCompany);

        setImageOnStart();

        DocumentReference reference = FirebaseFirestore.getInstance().collection("user")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    profile_name.setText(task.getResult().getString("name"));
                    email.setText(task.getResult().getString("email"));
                    location.setText(task.getResult().getString("location"));
                    profile_name.setText(task.getResult().getString("name"));
                    overview.setText(task.getResult().getString("overview"));
                    address.setText(task.getResult().getString("address"));
                    phone.setText(task.getResult().getString("phone"));
            }
        });
        logoutCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), Login.class));

                mainBottomNavigationCompany.finish();
                if(isApplicantOpen)
                {
                    applicant.finish();
                    isApplicantOpen = false;
                }

            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
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

                            documentReference.update("image url" , uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Picasso.get().load(uri.toString()).into(circleImageView);
                                    Picasso.get().load(uri.toString()).into(coverImageCompany);
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

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
               if(value.get("image url")!=null) {
                    Picasso.get().load(value.get("image url").toString()).into(circleImageView);
                    Picasso.get().load(value.get("image url").toString()).into(coverImageCompany);
                }
            }
        });
    }
}


