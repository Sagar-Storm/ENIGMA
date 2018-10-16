package com.kodbale.dkode;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.kodbale.dkode.activities.EndingActivity;
import com.kodbale.dkode.activities.InfoActivity;
import com.kodbale.dkode.activities.EndingActivity;
import com.kodbale.dkode.activities.ScoreActivity;
import com.kodbale.dkode.database.CurrentQuestion;
import com.kodbale.dkode.database.Question;
import com.kodbale.dkode.database.QuestionManager;
import com.kodbale.dkode.database.StatusManager;
import com.kodbale.dkode.fragments.PicFragment;
import com.kodbale.dkode.login.LoginActivity;


import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_CODE_QR_SCAN = 101;
    private static final String LOGTAG = "QRSCAN";

    private Button submit;
    private Button skip;
    private FrameLayout frame;
    private TextView mTimerTextView;
    private PicFragment imageQuestion;
    private QuestionManager mQuestionManager;
    private StatusManager mStatusManager;
    private android.support.v7.widget.Toolbar toolbar;
    private int camRequestCode = 107;
    private Handler handler;
    public Thread mThread;
    private CurrentQuestion mCurrentQuestion;
    public long timeRemaining = 100;



    // Action bar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.score:
                startActivity(new Intent(getApplicationContext(), ScoreActivity.class));
                break;
            case R.id.info:
                startActivity(new Intent(getApplicationContext(), InfoActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == camRequestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Permission Error");
                alertDialog.setMessage("But I really need the permission to work?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                alertDialog.show();
                ActivityCompat.requestPermissions(getParent(),new String[] {android.Manifest.permission.CAMERA}, camRequestCode);
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiations
        submit = (Button) findViewById(R.id.submit);
        skip = (Button) findViewById(R.id.skip);
        frame = (FrameLayout) findViewById(R.id.frame);
        mTimerTextView = (TextView) findViewById(R.id.timer);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.tools);
        setSupportActionBar(toolbar);
        if(getIntent().getExtras() != null) {

            Long timeExtra = getIntent().getExtras().getLong("TIME_REMAINING");

            if (timeExtra != null) {
                String timeExtraString = Long.toString(timeExtra);
                timeRemaining = timeExtra;
            }
        }
        mTimerTextView.setText(timeRemaining+"");

    /*    *//*
        Asking for permissions
         *//*
        Context context = getApplicationContext();
    *//*    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getParent(),new String[] {android.Manifest.permission.CAMERA}, camRequestCode);
        }*//*

        //getSupportFragmentManager().beginTransaction().replace(R.id.frame, textQuestion).commit();
*/
        submit.setOnClickListener(this);
        skip.setOnClickListener(this);

        mQuestionManager = QuestionManager.get(getApplicationContext());
        mStatusManager = StatusManager.get(getApplicationContext());

        mCurrentQuestion = mStatusManager.getCurrentQuestion();

       /* QuestionManager.get(getApplicationContext()).insertAllQuestions();
        Log.i("i", "inserted questions");

        StatusManager.get(getApplicationContext()).initializeAnsweredList();
        QuestionManager.get(getApplicationContext()).initializeFromFirebaseList();*/
        //QuestionManager.get(getApplicationContext()).initializeNotAnsweredList();
        //QuestionManager.get(getApplicationContext()).initializeAnsweredList();


        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                timeRemaining--;
                    if (timeRemaining > 0) {
                        if (handler != null) {
                            handler.postDelayed(this, 1000);
                            mTimerTextView.setText(timeRemaining + "");
                        }
                    } else {
                        startend();
                    }
                }
        };

        handler = new Handler();
        handler.postDelayed(runnable, 1000);

        if(QuestionManager.get(getApplicationContext()).getNotAnsweredList().size() == 0) {
            Log.i("answered", "you have answered all before");
            Intent intent = new Intent(this, EndingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            handler = null ;
            startActivity(intent);
            finish();

        } else {

            PicFragment picFragment = new PicFragment();

            getSupportFragmentManager().beginTransaction().replace(R.id.frame, picFragment).commit();

        }

    }

    public void startend() {
        startActivity(new Intent(this, EndingActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                submit.setEnabled(false);
                Intent i = new Intent(MainActivity.this,QrCodeActivity.class);
                startActivityForResult( i,REQUEST_CODE_QR_SCAN);
                break;

            case R.id.skip:  //TODO get the next question and create a update the ui
                mStatusManager.incrementQuestionSkipped();
                mStatusManager.updateAnsweredStatusForCurrentQuestion();
                mQuestionManager.updateAnsweredStatusInDb();

                setUpQuestion();
                Toast.makeText(getApplicationContext(), "you are in the next question, no animation", Toast.LENGTH_LONG).show();
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        submit.setEnabled(true);

        if(resultCode != Activity.RESULT_OK)
        {
            Log.d(LOGTAG,"COULD NOT GET A GOOD RESULT.");
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if( result!=null)
            {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }
        if(requestCode == REQUEST_CODE_QR_SCAN)
        {
            if(data==null)
                return;

            String solution = "we set this to some default value";
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");

            // We make the solution equal to the result, so that when the comparison is done, it'll always be right!
            solution = result;

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Scan Result");

            if(result.equals(solution)) {
                alertDialog.setMessage("you successfully cracked the question");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Next Question",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialgo, int which) {
                                mStatusManager.updateScoreForCurrentQuestion();
                                mStatusManager.updateAnsweredStatusForCurrentQuestion();
                                long questionUUID = getQuestionUUID();
                                int currentQuestionScore = getCurrentQuestionScore();
                                mQuestionManager.updateQuestionScoreInDb(questionUUID, currentQuestionScore);
                                mQuestionManager.updateAnsweredStatusInDb();
                                setUpQuestion();

                            }
                        });
            } else {
                alertDialog.setMessage("you failed to answer the question" + " Consider scanning the qrcode again" );
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mStatusManager.incrementNoOfTries();
                                mQuestionManager.updateNumberOfTries();
                                if(mStatusManager.getCurrentQuestion().getQuestion().getNumberOfTries() == 3) {
                                    mStatusManager.updateAnsweredStatusForCurrentQuestion();
                                    mQuestionManager.updateAnsweredStatusInDb();
                                    setUpQuestion();
                                }
                                dialog.dismiss();
                            }
                        });
            }
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }


    public int getQuestionUUID() {
        return mStatusManager.getCurrentQuestion().getQuestion().getQuestionId();
    }

    public int getCurrentQuestionScore() {

        int score =  mStatusManager.getCurrentQuestion().getQuestion().getScore();
        Log.i("i", "score " + score);
        return score;
    }




    @Override
    public void onBackPressed() {

    }

    void setUpQuestion(){
        /*
        The method that gets the next available question and updates the activity and starts timer
         */
        Question question = mQuestionManager.getNextQuestion();

        if(question == null) {

            Intent intent = new Intent(this, EndingActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            handler = null;
            finish();
            return;

        } else {

            mStatusManager.setCurrentQuestion(question);

        }
    }



}
