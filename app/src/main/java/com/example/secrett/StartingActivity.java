package com.example.secrett;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) { }

            @Override
            public void onFinish() {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                Intent intent = null;
                if (firebaseUser == null) {
                    intent = new Intent(StartingActivity.this,
                            LoginActivity.class);
                } else {
                    intent = new Intent(StartingActivity.this,
                            MainActivity.class);
                }

                startActivity(intent);
                finish();
            }
        }.start();
    }
}