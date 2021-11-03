package com.example.jobfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jobfinder.Complete_Profile_Company;
import com.example.jobfinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Signup_Company extends AppCompatActivity {

    //Auth for fire base
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    //Button for sign up to create account
    Button Next_button;

    //write information for sign up company
    EditText edit_text_name_company, edit_text_email_company,
            edit_text_password_company , edit_text_phone_company ;

    //Spinner
    Spinner  locationSpinner_company ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__company);

        Next_button = findViewById(R.id.Next_button);
        edit_text_name_company = findViewById(R.id.edit_text_name_company);
        edit_text_email_company = findViewById(R.id.edit_text_email_company);
        edit_text_password_company = findViewById(R.id.edit_text_password_company);
        edit_text_phone_company = findViewById(R.id.edit_text_phone_company);

        locationSpinner_company = findViewById(R.id.locationSpinner_company);
        locationSpinner_company.setSelection(0);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edit_text_name_company.getText().toString().trim();
                String email = edit_text_email_company.getText().toString().trim();
                String password = edit_text_password_company.getText().toString().trim();
                String phone = edit_text_phone_company.getText().toString().trim();
                String location = locationSpinner_company.getSelectedItem().toString().trim();
                String userType = getIntent().getStringExtra("userType");


                NavigateToCompleteProfile(name , email , password , phone , location , userType);
            }
        });


    }
    public void NavigateToCompleteProfile(String name, String email, String password, String phone, String location, String userType)
    {

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(Signup_Company.this, "please enter name for company", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email))
        {
            Toast.makeText(Signup_Company.this, "please enter email for company", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(Signup_Company.this, "please enter password for company", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(Signup_Company.this, "please enter phone for company", Toast.LENGTH_SHORT).show();
        }
        else if(location.equals("Select Location"))
        {
            Toast.makeText(Signup_Company.this, "Please select your location", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent = new Intent(Signup_Company.this , Complete_Profile_Company.class);
            intent.putExtra("name" , name);
            intent.putExtra("email" , email);
            intent.putExtra("password" , password);
            intent.putExtra("phone" , phone);
            intent.putExtra("userType" , userType);
            intent.putExtra("location" , location);
            startActivity(intent);
        }
    }



}