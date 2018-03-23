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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.kodbale.dkode.database.QuestionManager;
import com.kodbale.dkode.database.StatusManager;
import com.kodbale.dkode.login.LoginActivity;
import com.kodbale.dkode.MainActivity;
import com.kodbale.dkode.R;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


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
    String time ="NULL";
    private StatusManager statusManager;

    public int processing = 0;

    public int fetchedUnixTime = 0;

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



                        //fetches the timestamp of the user. It sets allset = 0 if previously not loggedin, else allset = 1 if loggedin
                        //this sets the timestamp variable to the logged_in_at thing
                        mStatusManager.get(getApplicationContext()).initializeGameLogin();
                    //    new timeLessTimer().execute("");
                        getTime();
                        processing = 1;
                        fetchedUnixTime = 0;
                    }

                    if(StatusManager.get(getApplicationContext()).allSet != -1 && fetchedUnixTime == 1) {
                            allSet();
                    } else {

                        Log.i("time not available", "time not available" + fetchedUnixTime );
                        handler.postDelayed(this, 1000);

                    }

                }
            };

            handler.postDelayed(runnable, 0);


         //   QuestionManager.get(getApplicationContext()).initializeFromFirebaseList();

    }




    public void getTime() {

        String url = "http://www.convert-unix-time.com/api?timestamp=now";
        JSONObject postparams = new JSONObject();
        try {



            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            // display response
                            Log.d("Response", response.toString());
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());
                                 timeStamp = Long.parseLong(jsonObject.get("timestamp").toString());
                                fetchedUnixTime = 1;
                            }catch(Exception e) {
                                Log.i("i", "exception in getTime()");
                            }

                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", error.toString());
                        }
                    }
            );

            getRequest.addMarker("getRequest");
            StatusManager.get(getApplicationContext()).getRequestQueue().add(getRequest);
        } catch (Exception e) {

        }
    }


    public void allSet() {

        btn.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        handler = null;
        processing = 0;
        fetchedUnixTime = 0;
    }

    @Override
    public void onClick(View v) {

        handler = null ;

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if(mStatusManager.getTimeStamp() == null) {
            long unixTime = timeStamp;
            System.out.println("the time is" + unixTime);
            mStatusManager.setTimeStamp(unixTime);
            mStatusManager.writeTimeStampToFirebase(unixTime);

        } else {
            //TODO use a web api to fetch time

            long currentUnixTime = timeStamp;
            long loggedInUnixtime = mStatusManager.getTimeStamp();

            long currentTimeRemaining = 5200 - (currentUnixTime - loggedInUnixtime);

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
