package com.example.cakeshopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginPage extends AppCompatActivity {
    TextView signUpPageRedirect;
    FirebaseAuth firebaseAuth;
    EditText email,password;
    Button signIn;
    ProgressBar progress;
    ConstraintLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        signUpPageRedirect=findViewById(R.id.signUpPage);
        signUpPageRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginPage.this,RegistrationPage.class);
                startActivity(intent);
                finish();
            }
        });

        firebaseAuth=FirebaseAuth.getInstance();
        email=findViewById(R.id.Email);
        password=findViewById(R.id.password);
        signIn=findViewById(R.id.signIn);
        progress=findViewById(R.id.progress);
        layout=findViewById(R.id.layout);

        progress.setVisibility(View.GONE);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(fieldIsNotEmpty())
                    {
                        progress.setVisibility(View.VISIBLE);
                        signInUser();
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });

    }

    private void signInUser() {

        String enteredEmail=email.getText().toString();
        String enteredPassword=password.getText().toString();
        firebaseAuth.signInWithEmailAndPassword(enteredEmail,enteredPassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Snackbar.make(layout,"Login Successful",Snackbar.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);
                        Intent intent=new Intent(LoginPage.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(e instanceof FirebaseAuthInvalidUserException) {
                            progress.setVisibility(View.GONE);
                            Toast.makeText(LoginPage.this,"Email Doesn't Exists", Toast.LENGTH_SHORT).show();
                        }
                        if(e instanceof FirebaseAuthInvalidCredentialsException) {
                            progress.setVisibility(View.GONE);
                            Toast.makeText(LoginPage.this,"Password is Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean fieldIsNotEmpty() {
        if(email.getText().toString().length() == 0)
        {
            email.setError("Field is Empty");
            return false;
        }
        if(password.getText().toString().length()==0)
        {
            password.setError("Field is Empty");
            return false;
        }
        return true;
    }
}