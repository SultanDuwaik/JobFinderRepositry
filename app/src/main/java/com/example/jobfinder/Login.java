package com.example.jobfinder;

import static com.example.jobfinder.SplashScreen.splashScreen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jobfinder.company.MainBottomNavigationCompany;
import com.example.jobfinder.job_seeker.MainBottomNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    Button login_button , navigation_to_sign_up;
    EditText edit_text_email_login, edit_text_password_login;

    public static Activity login;

    DocumentReference documentReference;
    FirebaseAuth mAuth;
    ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadingBar = new ProgressDialog(this);

        login = this;

        //if not logged out , stay in account on open
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
        {
            checkUser();
            finish();
        }
        login_button = findViewById(R.id.loginbutton);
        navigation_to_sign_up = findViewById(R.id.navigation_to_signup);
        edit_text_email_login = findViewById(R.id.edit_text_email_login);
        edit_text_password_login = findViewById(R.id.edit_text_password_login);

        mAuth = FirebaseAuth.getInstance();

        // Loading bar and database(firebase)
//go to signup
        navigation_to_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this , SelectUserType.class);
                startActivity(intent);
            }
        });
//login button
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Seeker_email_Login = edit_text_email_login.getText().toString().trim();
                String Seeker_password_Login = edit_text_password_login.getText().toString().trim();
                // Job Seekers Login Method
                LoginSeekers(Seeker_email_Login,Seeker_password_Login);
            }
        });

        splashScreen.finish();
    }
// to log in , check if user in authentication
    private void LoginSeekers( String seeker_email_Login , String  seeker_password_Login)
    {
       if(TextUtils.isEmpty(seeker_email_Login) || TextUtils.isEmpty(seeker_password_Login))
       {
           Toast.makeText(Login.this, "Please Enter your account information", Toast.LENGTH_LONG).show();
       }
       else {
           loadingBar.show();
           loadingBar.setMessage("please wait..");
           loadingBar.setCancelable(true);

           mAuth.signInWithEmailAndPassword(seeker_email_Login , seeker_password_Login).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful())
                   {
                       Toast.makeText(Login.this, "logged in successfully", Toast.LENGTH_SHORT).show();
                       loadingBar.dismiss();
                       checkUser();
                   }
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e)
               {
                   Toast.makeText(Login.this, "invalid email or password", Toast.LENGTH_SHORT).show();
               }
           });
       }
    }

    //to check if the user who logged in is company or job seeker
    public void checkUser()
    {
               documentReference = FirebaseFirestore.getInstance().collection("user").
               document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        FirebaseFirestore ffs = FirebaseFirestore.getInstance();


        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    if(task.getResult().exists())
                    {
                        if (task.getResult().getString("user type").equals("job seeker"))
                        {
                            Intent intentJobSeeker = new Intent(Login.this, MainBottomNavigation.class);
                            startActivity(intentJobSeeker);
                            finish();
                        }
                        else if (task.getResult().getString("user type").equals("company"))
                        {
                            Intent intentJobSeeker = new Intent(Login.this, MainBottomNavigationCompany.class);
                            startActivity(intentJobSeeker);
                            finish();
                        }
                    }
                    else
                    {
                        System.out.println("error");
                    }
                }
                else
                {
                    System.out.println("Firebase Access Denied");
                }
            }
        });
    }
}