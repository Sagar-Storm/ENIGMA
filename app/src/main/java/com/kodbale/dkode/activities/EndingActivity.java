package com.kodbale.dkode.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kodbale.dkode.R;

public class EndingActivity extends AppCompatActivity implements View.OnClickListener{

    private Button scoreBtn, exitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ending);
        scoreBtn = (Button) findViewById(R.id.scoreBtn);
        exitBtn = (Button) findViewById(R.id.btn);
        scoreBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scoreBtn:
                startActivity(new Intent(getApplicationContext(), ScoreActivity.class));
                break;
            case R.id.btn:
                finish();
                break;

        }
    }
}
