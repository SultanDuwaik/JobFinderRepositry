package com.example.jobfinder;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jobfinder.job_seeker.MainBottomNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.jobfinder.Login.login;
import static com.example.jobfinder.SelectUserType.selectUserType;

public class SignUp extends AppCompatActivity {

    // Auth for fire base
    private FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    String Job_Seeker_ID;

    String imageUrlDefault;

    Spinner locationSpinner;



    //Buttons for back to login page or Sign up to create account
    Button   Sign_up;
    RadioGroup radioGroup;
    RadioButton radioButton;
    // write information for sign up
    EditText edit_Text_datOfBirth;
    EditText edit_text_first_name, edit_text_last_name, edit_text_email, edit_text_password , edit_text_phone;
    boolean completedProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // connect object with ID
        edit_Text_datOfBirth = findViewById(R.id.dateOfBirthId);
        Sign_up = findViewById(R.id.Sign_up);
        edit_text_first_name = findViewById(R.id.edit_text_first_name);
        edit_text_last_name = findViewById(R.id.edit_text_last_name);
        edit_text_email = findViewById(R.id.edit_text_email);
        edit_text_password = findViewById(R.id.edit_text_password);
        edit_text_phone =findViewById(R.id.edit_text_phone);
        radioGroup = findViewById(R.id.genderText);

        locationSpinner = findViewById(R.id.locationSpinner);
        locationSpinner.setSelection(0);
        imageUrlDefault = "https://firebasestorage.googleapis.com/v0/b/fir-learn-1ba58.appspot.com/o/images%2Fl60Hf.png?alt=media&token=9d83942e-fa11-4f82-8121-161a848e7602";

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                String first_name = edit_text_first_name.getText().toString().trim();
                String last_name = edit_text_last_name.getText().toString().trim();
                String email = edit_text_email.getText().toString().trim();
                String password = edit_text_password.getText().toString().trim();
                String phone = edit_text_phone.getText().toString().trim();
                String gender = radioButton.getText().toString();
                String location = locationSpinner.getSelectedItem().toString().trim();

                RegisterSeekers(first_name , last_name , email , password , phone , gender , location);
            }
        });
    }
    private void RegisterSeekers ( String first_name , String last_name ,String email , String password , String phone , String gender , String location)
    {
        if(TextUtils.isEmpty(first_name))
        {
            Toast.makeText(SignUp.this, "please enter first name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(last_name))
        {
            Toast.makeText(SignUp.this, "please enter last name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email))
        {
            Toast.makeText(SignUp.this, "please enter email", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(SignUp.this, "please enter password", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(SignUp.this, "please enter phone number", Toast.LENGTH_SHORT).show();
        }
        else if(edit_text_password.getText().length()<8)
        {
            Toast.makeText(SignUp.this, "password too short", Toast.LENGTH_SHORT).show();
        }
        else if(edit_text_password.getText().length()>32)
        {
            Toast.makeText(SignUp.this, "password too long", Toast.LENGTH_SHORT).show();
        }
        else if(gender == null){
            Toast.makeText(this, "please choose gender", Toast.LENGTH_SHORT).show();
        }
        else if(location.equals("Select Location"))
        {
            Toast.makeText(this, "Please select your location", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(SignUp.this, "Registered Successfully...", Toast.LENGTH_LONG).show();
                        Job_Seeker_ID = mAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = firebaseFirestore.collection("user").document(Job_Seeker_ID);


                        String userType = getIntent().getStringExtra("userType");
                        completedProfile = false;
                        Map < String , Object > Seeker = new HashMap<>();
                        Seeker.put( "first name" , first_name);
                        Seeker.put( "last name" , last_name);
                        Seeker.put( "email" , email);
                        Seeker.put( "password" , password);
                        Seeker.put( "phone" , phone);
                        Seeker.put( "gender" , gender);
                        Seeker.put( "user type" , userType);
                        Seeker.put( "location" , location);
                        Seeker.put( "image url" , imageUrlDefault);
                        Seeker.put( "profile completed" ,completedProfile);
                        Seeker.put("date of birth", edit_Text_datOfBirth.getText().toString());

                        documentReference.set(Seeker).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                startActivity(new Intent(SignUp.this , MainBottomNavigation.class));
                                finish();
                                selectUserType.finish();
                                login.finish();
                            }
                        });
                    }
                    else {
                        Toast.makeText(SignUp.this, "email address used", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignUp.this , SignUp.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}
