package com.example.jobfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jobfinder.job_seeker.MainBottomNavigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SelectUserType extends AppCompatActivity {

     Button Navigation_to_login_seeker , Navigation_to_login_company;

     public static Activity selectUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);

        Navigation_to_login_seeker = findViewById(R.id.Navigation_to_login_seeker);
        Navigation_to_login_company = findViewById(R.id.Navigation_to_login_company);

        selectUserType = this;

        Navigation_to_login_seeker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectUserType.this , SignUp.class);
                intent.putExtra("userType", "job seeker");
                startActivity(intent);
            }
        });
        Navigation_to_login_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectUserType.this , Signup_Company.class);
                intent.putExtra("userType" , "company");
                startActivity(intent);
            }
        });
    }
}