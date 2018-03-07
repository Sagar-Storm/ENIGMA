package com.kodbale.dkode.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.kodbale.dkode.R;

public class EndingActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ending);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scoreBtn:
                startActivity(new Intent(getApplicationContext(),ScoreActivity.class));
                break;
            case R.id.btn:
                finish();
                break;
            case R.id.leader:
                Toast.makeText(getApplicationContext(),"Where's the leader board",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
