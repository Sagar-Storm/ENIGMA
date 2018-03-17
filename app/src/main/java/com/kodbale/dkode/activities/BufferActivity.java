package com.kodbale.dkode.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.firebase.auth.FirebaseUser;
import com.kodbale.dkode.database.StatusManager;
import com.kodbale.dkode.login.LoginActivity;
import com.kodbale.dkode.MainActivity;
import com.kodbale.dkode.R;


public class BufferActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv;
    Button btn;
    CountDownTimer countDownTimer;
    StatusManager mStatusManager;
 //   private long waitTime = 5000;

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
            //startActivity(new Intent(getApplicationContext(), MainActivity.class));
            Log.i("i", "returning to login becz i suck");
            finish();
        } else {
            Log.i("i", "staying here only");
            Log.i("i", user.getEmail());
        }

    }

    @Override
    public void onClick(View v) {


            //startService(i);
        startActivity(new Intent(this, MainActivity.class));
            finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("a", "started");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("a", "stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("a", "destoryed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("a", "paused");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("a", "resumed");
    }
}
