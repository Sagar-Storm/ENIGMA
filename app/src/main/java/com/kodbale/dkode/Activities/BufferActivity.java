package com.kodbale.dkode.Activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kodbale.dkode.MainActivity;
import com.kodbale.dkode.R;

public class BufferActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv;
    Button btn;
    CountDownTimer countDownTimer;
    private long waitTime = 50000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffer);
        tv = (TextView) findViewById(R.id.message);
        btn = (Button) findViewById(R.id.nextPageButton);
        btn.setOnClickListener(this);
        countDownTimer = new CountDownTimer(waitTime,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }.start();

    }

    @Override
    public void onClick(View v) {

    }
}
