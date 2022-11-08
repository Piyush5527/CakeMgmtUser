package com.example.cakeshopper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirstLoadingPage extends AppCompatActivity {
    ImageView imageView;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_loading_page);

        imageView = findViewById(R.id.cakeIcon);
        progressBar = findViewById(R.id.progress);
        fAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        try {
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);
            imageView.startAnimation(animation);
            CountDownTimer countDownTimer = new CountDownTimer(1000, 500) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    if (isNetworkConnected() && isUserLoggedIn()) {
//                        Toast.makeText(FirstLoadingPage.this, "Net connected and logged in", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        Intent intent=new Intent(FirstLoadingPage.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (isNetworkConnected()==true && isUserLoggedIn()==false) {
                        Intent intent=new Intent(FirstLoadingPage.this,RegistrationPage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        AlertDialog.Builder adb=new AlertDialog.Builder(FirstLoadingPage.this);
                        adb.setTitle("Alert")
                                .setMessage("Internet is Not connected!!")
                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        onFinish();
                                    }
                                }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                });
                        AlertDialog ad=adb.create();
                        ad.show();
                    }
                }
            };
            countDownTimer.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private boolean isUserLoggedIn() {
        if (fUser != null) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isNetworkConnected() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (connectivityManager != null) {
                networkInfo = connectivityManager.getActiveNetworkInfo();
            }
            return networkInfo != null && networkInfo.isConnected();
        } catch (Exception ex) {
            return false;
        }
    }
}