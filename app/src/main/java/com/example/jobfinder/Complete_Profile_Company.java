package com.example.jobfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jobfinder.company.MainBottomNavigationCompany;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Complete_Profile_Company extends AppCompatActivity {

    //Auth for firebase
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    String Company_ID , companyImageUrlDefault;

    //Button for sign up to create account
    Button create_account;

    //write information for sign up company
    EditText edit_text_overview_company , edit_text_address_company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete__profile__company);

        create_account = findViewById(R.id.create_account);
        edit_text_overview_company  = findViewById(R.id.edit_text_overview_company);
        edit_text_address_company  = findViewById(R.id.edit_text_address_company);
        companyImageUrlDefault = "https://firebasestorage.googleapis.com/v0/b/fir-learn-1ba58.appspot.com/o/images%2Fproject_icon_concept_only_logo.png?alt=media&token=8a9f2b70-75b8-4829-a49e-313f51749bb8";

        mAuth= FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String overview = edit_text_overview_company.getText().toString().trim();
                String address = edit_text_address_company.getText().toString().trim();
                String name = getIntent().getStringExtra("name");
                String email = getIntent().getStringExtra("email");
                String password = getIntent().getStringExtra("password");
                String phone = getIntent().getStringExtra("phone");
                String location = getIntent().getStringExtra("location");

                RegisterCompany( name , email , password , phone , location , overview , address );
            }
        });
    }
    private void RegisterCompany(String name, String email, String password, String phone, String location, String overview, String address) {

        if(TextUtils.isEmpty(address))
        {
            Toast.makeText(this, "please enter the company Address", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(overview))
        {
            Toast.makeText(this, "Write an overview on the Company", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Company_ID = mAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = firebaseFirestore.collection("user").document(Company_ID);

                        String userType = getIntent().getStringExtra("userType");

                        Map<String , Object> Company = new HashMap<>();
                        Company.put("name" , name);
                        Company.put("email" , email);
                        Company.put("password" , password);
                        Company.put("phone" , phone);
                        Company.put("user type" , userType);
                        Company.put("location" , location);
                        Company.put("overview" , overview);
                        Company.put("address" , address);
                        Company.put("has notifications" , false);
                        Company.put("image url", companyImageUrlDefault);

                        documentReference.set(Company).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(Complete_Profile_Company.this , MainBottomNavigationCompany.class));
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(Complete_Profile_Company.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Complete_Profile_Company.this , Signup_Company.class));
                    }
                }
            });
        }
    }
}