package com.example.cakeshopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;

public class RegistrationPage extends AppCompatActivity {
    EditText userName,userEmail,userPassword,userPin;
    Button signUp;
    FirebaseAuth fAuth;
    FirebaseUser user;
    TextView loginRedirect;
    ConstraintLayout layout;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        userName=findViewById(R.id.Name);
        userEmail=findViewById(R.id.Email);
        userPassword=findViewById(R.id.password);
        userPin=findViewById(R.id.pin4dig);
        signUp=findViewById(R.id.signUp);
        layout=findViewById(R.id.layout);
        progressBar=findViewById(R.id.progress);
        loginRedirect=findViewById(R.id.signInPage);

        progressBar.setVisibility(View.GONE);

        fAuth=FirebaseAuth.getInstance();

        user=FirebaseAuth.getInstance().getCurrentUser();

        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegistrationPage.this,LoginPage.class);
                startActivity(intent);
                finish();
            }
        });

        if(user != null)
        {
            Intent intent=new Intent(RegistrationPage.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser();
            }
        });
    }

    private void registerNewUser() {
        if(isFieldNotEmpty())
        {
            progressBar.setVisibility(View.VISIBLE);
            String name,email,password,pin;
            name=userName.getText().toString();
            email=userEmail.getText().toString();
            password=userPassword.getText().toString();
            pin=userPassword.getText().toString();

            fAuth.createUserWithEmailAndPassword(email,password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            progressBar.setVisibility(View.GONE);
                            Snackbar.make(layout,"Successful",Snackbar.LENGTH_SHORT).show();
                            try {
                                Thread.sleep(1000);
                                Intent intent=new Intent(RegistrationPage.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegistrationPage.this, e+"", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private boolean isFieldNotEmpty() {
        if(userName.getText().toString().equals(""))
        {
            userName.setError("Field is Empty");
            return false;
        }
        if(userEmail.getText().toString().equals(""))
        {
            userEmail.setError("Field is Empty");
            return false;
        }
        if(userPassword.getText().toString().equals(""))
        {
            userPassword.setError("Field is Empty");
            return false;
        }
        if(userPin.getText().toString().equals(""))
        {
            userPin.setError("Field is Empty");
            return false;
        }
        if (userPin.getText().length() != 4)
        {
            AlertDialog.Builder adb=new AlertDialog.Builder(RegistrationPage.this);
            adb.setTitle("Alert!!")
                    .setMessage("Pin Field can only have 4 Digits")
                    .setPositiveButton("Ok",null);
            AlertDialog ad=adb.create();
            ad.show();
        }
        return true;
    }
}