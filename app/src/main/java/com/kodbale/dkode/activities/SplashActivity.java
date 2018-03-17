package com.kodbale.dkode.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kodbale.dkode.MainActivity;
import com.kodbale.dkode.R;

public class SplashActivity extends AppCompatActivity {

    private CountDownTimer countDownTimer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Waits for 3 seconds before moving on to the main activity
        countDownTimer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {}
            @Override
            public void onFinish() {
                startActivity(new Intent(SplashActivity.this, BufferActivity.class));
            }
        }.start();
    }
}
