package com.kodbale.dkode.Activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.kodbale.dkode.Database.StatusManager;
import com.kodbale.dkode.Login.LoginActivity;
import com.kodbale.dkode.MainActivity;
import com.kodbale.dkode.R;

public class BufferActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv;
    Button btn;
    CountDownTimer countDownTimer;
    StatusManager mStatusManager;
    private long waitTime = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffer);
        tv = (TextView) findViewById(R.id.message);
        btn = (Button) findViewById(R.id.nextPageButton);
        btn.setOnClickListener(this);

        mStatusManager = StatusManager.get(getApplicationContext());
        FirebaseUser user = mStatusManager.getUser();

        if(user == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            Log.i("i", "returning to login becz i suck");
            finish();
        }

//        countDownTimer = new CountDownTimer(waitTime,1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//
//            }
//
//            @Override
//            public void onFinish() {
//
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                finish();
//            }
//        }.start();

    }

    @Override
    public void onClick(View v) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
    }
}
