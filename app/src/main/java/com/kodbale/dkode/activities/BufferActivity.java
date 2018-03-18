package com.kodbale.dkode.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.kodbale.dkode.database.QuestionManager;
import com.kodbale.dkode.database.StatusManager;
import com.kodbale.dkode.login.LoginActivity;
import com.kodbale.dkode.MainActivity;
import com.kodbale.dkode.R;

import java.time.Instant;


public class BufferActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv;
    Button btn;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    StatusManager mStatusManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffer);
        tv = (TextView) findViewById(R.id.message);
        btn = (Button) findViewById(R.id.nextPageButton);
        btn.setOnClickListener(this);
        mFirebaseAuth = mFirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();


        if(mFirebaseUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            Log.i("i", "returning to login becz i suck");
            finish();
            return;
        } else {
            StatusManager.get(getApplicationContext()).setAuth(mFirebaseAuth);
            StatusManager.get(getApplicationContext()).setUser(mFirebaseUser);
            Log.i("i", "staying here only");
            Log.i("i", mFirebaseUser.getEmail());
            QuestionManager.get(getApplicationContext()).insertAllQuestions();
            Log.i("i", "inserted questions");
            mStatusManager = StatusManager.get(getApplicationContext());
            StatusManager.get(getApplicationContext()).setFirebaseDatabase(FirebaseDatabase.getInstance());
            StatusManager.get(getApplicationContext()).initializeAnsweredList();
            StatusManager.get(getApplicationContext()).initializeGameLogin();
         //   QuestionManager.get(getApplicationContext()).initializeFromFirebaseList();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        if(mStatusManager.getTimeStamp() == null) {
            long unixTime = System.currentTimeMillis() / 1000L;
            System.out.println("the time is" + unixTime);
            mStatusManager.setTimeStamp(unixTime);
        } else {
            long currentUnixTime = System.currentTimeMillis()/1000L;
            long loggedInUnixtime = mStatusManager.getTimeStamp();
            long currentTimeRemaining = 100 - (currentUnixTime - loggedInUnixtime);
            if(currentTimeRemaining <= 0 ) {
                 intent = new Intent(this, EndingActivity.class);
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Toast.makeText(this, "damadama time over", Toast.LENGTH_SHORT).show();
                 startActivity(intent);
                 finish();
                 return;
            }
            intent.putExtra("TIME_REMAINING", currentTimeRemaining);
            System.out.println("current_timeremaining" + currentTimeRemaining+"");
        }
        startActivity(intent);
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
