package com.kodbale.dkode.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kodbale.dkode.R;
import com.kodbale.dkode.database.QuestionManager;
import com.kodbale.dkode.database.StatusManager;

public class EndingActivity extends AppCompatActivity implements View.OnClickListener{


    private Button scoreBtn, exitBtn;

    FirebaseAuth mFirebaseAuth = null;
    FirebaseUser mFirebaseUser = null;
    FirebaseAuth.AuthStateListener mAuthListener;

    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ending);

        scoreBtn = (Button) findViewById(R.id.scoreBtn);
        exitBtn = (Button) findViewById(R.id.btn);
        mFirebaseAuth  = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null){
                    System.out.println("User logged in");
                }
                else{
                    System.out.println("User not logged in");
                }
            }
        };
        text = findViewById(R.id.finalScoreHolder);
        text.setText("C-ME");
    }

    public void onScoreButtonClicked(View view) {
        startActivity(new Intent(EndingActivity.this,ScoreActivity.class));
    }

    @Override
    public void onClick(View view) {

    }

    public void onExitButtonClicked(View view) {
        FirebaseAuth.getInstance().signOut();
        StatusManager.get(getApplicationContext()).setAllToNull();
        QuestionManager.get(getApplicationContext()).setAllToNull();
        finish();
    }

    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        StatusManager.get(getApplicationContext()).setAllToNull();
        QuestionManager.get(getApplicationContext()).setAllToNull();
        finish();
    }
}
