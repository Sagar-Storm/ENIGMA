package com.kodbale.dkode.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;


public class BufferActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BufferActivity";
    TextView tv;
    Button btn;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    StatusManager mStatusManager;
    private ProgressBar progressBar;
    public Handler handler ;
    public long timeStamp;
    String time;
    private StatusManager statusManager;

    public int processing = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffer);
        tv = (TextView) findViewById(R.id.message);
        btn = (Button) findViewById(R.id.nextPageButton);
        btn.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.loadingDataProgressBar);
        mFirebaseAuth = mFirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        statusManager = StatusManager.get(getApplicationContext());
        btn.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        // if user is null then take him to login screen
        if(mFirebaseUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            Log.i(BufferActivity.TAG, "returning to login becz i suck");
            finish();
            return;

        }


        //setting the user and firebase auth in statusmanager so that it is helpful
        statusManager.setAuth(mFirebaseAuth);
        statusManager.get(getApplicationContext()).setUser(mFirebaseUser);



            Log.i("i", "staying here only");

            Log.i("i", mFirebaseUser.getEmail());

            QuestionManager.get(getApplicationContext()).insertAllQuestions();

            Log.i("i", "inserted questions");

            mStatusManager = StatusManager.get(getApplicationContext());


            handler = new Handler();

            Runnable runnable = new Runnable() {
                @Override
                public void run() {





                    if(processing == 0) {

                        mStatusManager.setFirebaseDatabase(FirebaseDatabase.getInstance());

                        //initialize the answered list and set it. the answered list contains the list of id's that have been attended.

                        //once the answered_list has been set, we call initializefromfirebaselist to initialize answered and not answeredlist of questionManager
                        // it also sets the mAllquestions of questionManager
                        mStatusManager.get(getApplicationContext()).initializeAnsweredList();

                        //TODO fetch the numberOfTries for each question
                        mStatusManager.get(getApplicationContext()).initializeNumberOfTriesListInFirebase();


                        //TODO fetch the scores for each question

                        // mStatuManager.get(getApplicationContext()).initializenumberoftries();

                        // mStatuManager.get(getApplicationContext()).initializescores();


                        //fetches the timestamp of the user. It sets allset = 0 if previously not loggedin, else allset = 1 if loggedin
                        //this sets the timestamp variable to the logged_in_at thing
                        mStatusManager.get(getApplicationContext()).initializeGameLogin();

                        processing = 1;
                        new timeLessTimer().execute("");

                    }

                    if(StatusManager.get(getApplicationContext()).allSet != -1 && time!="") {
                            allSet();
                        Date date;
                        Log.i(TAG, "run: "+time);
                        try{
                            DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy kk:mm:ss z");
                            date = (Date)formatter.parse(time);
                            timeStamp = date.getTime()/1000;
                            Toast.makeText(getApplicationContext(),timeStamp+"",Toast.LENGTH_LONG).show();
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"errr",Toast.LENGTH_LONG).show();
                        }

                    } else {
                        handler.postDelayed(this, 100);
                    }

                }
            };

            handler.postDelayed(runnable, 0);


         //   QuestionManager.get(getApplicationContext()).initializeFromFirebaseList();

    }

    private class timeLessTimer extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            time = StatusManager.get(getApplicationContext()).getCurrentTime();

            return null;
        }
    }


    public void allSet() {

        btn.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        handler = null;
        processing = 0;

    }

    @Override
    public void onClick(View v) {

        handler = null ;

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if(mStatusManager.getTimeStamp() == null) {
            long unixTime = System.currentTimeMillis() / 1000L;
            System.out.println("the time is" + unixTime);
            mStatusManager.setTimeStamp(unixTime);
            mStatusManager.writeTimeStampToFirebase(unixTime);

        } else {
            //TODO use a web api to fetch time

            long currentUnixTime = System.currentTimeMillis()/1000L;
            long loggedInUnixtime = mStatusManager.getTimeStamp();

            long currentTimeRemaining = 1000 - (currentUnixTime - loggedInUnixtime);

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
