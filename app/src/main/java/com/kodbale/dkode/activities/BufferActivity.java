package com.kodbale.dkode.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kodbale.dkode.database.StatusManager;
import com.kodbale.dkode.login.LoginActivity;
import com.kodbale.dkode.MainActivity;
import com.kodbale.dkode.R;


public class BufferActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv;
    Button btn;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;

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
            Log.i("i", "staying here only");
            Log.i("i", mFirebaseUser.getEmail());
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
