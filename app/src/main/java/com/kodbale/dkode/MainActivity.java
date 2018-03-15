package com.kodbale.dkode;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.kodbale.dkode.activities.EndingActivity;
import com.kodbale.dkode.activities.InfoActivity;
import com.kodbale.dkode.activities.ScoreActivity;
import com.kodbale.dkode.database.CurrentQuestion;
import com.kodbale.dkode.database.Question;
import com.kodbale.dkode.database.QuestionManager;
import com.kodbale.dkode.database.StatusManager;
import com.kodbale.dkode.fragments.PicFragment;
import com.kodbale.dkode.login.LoginActivity;


import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_CODE_QR_SCAN = 101;
    private static final String LOGTAG = "QRSCAN";
    private static final long MAX_TIME = 60000;

    private Button submit;
    private Button skip;
    private FrameLayout frame;
    private TextView timer;
    private CountDownTimer countDownTimer;
    private long countDownTime = MAX_TIME;
    private PicFragment imageQuestion;
    private QuestionManager mQuestionManager;
    private StatusManager mStatusManager;
    private android.support.v7.widget.Toolbar toolbar;
    private int camRequestCode = 107;

    private CurrentQuestion mCurrentQuestion;



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
        timer = (TextView) findViewById(R.id.timer);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.tools);
        setSupportActionBar(toolbar);


        /*
        Asking for permissions
         */
        Context context = getApplicationContext();
    /*    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getParent(),new String[] {android.Manifest.permission.CAMERA}, camRequestCode);
        }*/

        //getSupportFragmentManager().beginTransaction().replace(R.id.frame, textQuestion).commit();
        startCountDown();
        submit.setOnClickListener(this);
        skip.setOnClickListener(this);

        mQuestionManager = QuestionManager.get(getApplicationContext());
        mStatusManager = StatusManager.get(getApplicationContext());

        mCurrentQuestion = mStatusManager.getCurrentQuestion();

        if (mStatusManager.getUser() == null){
            mQuestionManager = null;
            mStatusManager = null;
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(getApplicationContext(), "You should login to continue", Toast.LENGTH_SHORT).show();
        }

        QuestionManager.get(getApplicationContext()).insertAllQuestions();
        Log.i("i", "inserted questions");

        QuestionManager.get(getApplicationContext()).initializeNotAnsweredList();
        QuestionManager.get(getApplicationContext()).initializeAnsweredList();

        if(QuestionManager.get(getApplicationContext()).getNotAnsweredList().size() == 0) {
            Log.i("answered", "you have answered all before");
            Intent intent = new Intent(this, EndingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            PicFragment picFragment = new PicFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, picFragment).commit();

        }

    }

    @Override
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.submit:
             submit.setEnabled(false);
             Intent i = new Intent(MainActivity.this,QrCodeActivity.class);
             startActivityForResult( i,REQUEST_CODE_QR_SCAN);
             break;

         case R.id.skip:

             SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
             sweetAlertDialog.setTitleText("You really want to skip?")
                     .setContentText("Won't be able to come back to this question!")
                     .setConfirmText("Yes, just skip!")
                     .setCancelText("No, I want to solve it!")
                     .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                         @Override
                         public void onClick(SweetAlertDialog sweetAlertDialog) {
                             mStatusManager.incrementQuestionSkipped();
                             mStatusManager.updateAnsweredStatusForCurrentQuestion();
                             mQuestionManager.updateAnsweredStatusInDb();
                             setUpQuestion();
                             sweetAlertDialog.dismiss();
                         }
                     })
                     .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                         @Override
                         public void onClick(SweetAlertDialog sweetAlertDialog) {
                             sweetAlertDialog.dismiss();
                         }
                     })
                     .show();

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
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Couldn't get a good angle, take photo again!")
                        .show();

            }
            return;

        }
        if(requestCode == REQUEST_CODE_QR_SCAN)
        {
            if(data==null)
                return;

            String solution = "something";
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");

            solution = result;

            SweetAlertDialog sweetAlertDialog  = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);

            if(result.equals(solution)) {
                sweetAlertDialog.setTitleText("Good job!")
                        .setContentText("You Solved the problem!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.setTitleText("Next Question!")
                                        .setContentText("Proceed to next question!")

                                        .setCustomImage(R.drawable.ic_navigate_next_black_24dp)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                countDownTimer.cancel();
                                                mStatusManager.updateScoreForCurrentQuestion();
                                                mStatusManager.updateAnsweredStatusForCurrentQuestion();
                                                long questionUUID = getQuestionUUID();
                                                int currentQuestionScore = getCurrentQuestionScore();
                                                mQuestionManager.updateQuestionScoreInDb(questionUUID, currentQuestionScore);
                                                mQuestionManager.updateAnsweredStatusInDb();
                                                setUpQuestion();
                                            }
                                        })
                                        .changeAlertType(SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                            }
                        });

            } else {
                sweetAlertDialog.setTitleText("Failed mate!")
                        .setContentText("You scanned the wrong qr!")
                        .setConfirmText("Retry")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                mStatusManager.incrementNoOfTries();
                                mQuestionManager.updateNumberOfTries();
                                if(mStatusManager.getCurrentQuestion().getQuestion().getNumberOfTries() == 3) {
                                    mStatusManager.updateAnsweredStatusForCurrentQuestion();
                                    mQuestionManager.updateAnsweredStatusInDb();
                                    setUpQuestion();
                                    sweetAlertDialog.dismiss();
                                }
                            }
                        })
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
            }
            sweetAlertDialog.show();
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


    void startCountDown(){
        countDownTimer = new CountDownTimer(countDownTime,1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                countDownTime = millisUntilFinished;
                int mins = (int) (countDownTime/1000) / 60;
                int secs = (int) (countDownTime/1000) % 60;
                String timeToShow = String.format(Locale.getDefault(),"%02d:%02d",mins,secs);
                timer.setText(timeToShow);
                mStatusManager.setCurrentQuestionTimeRemaining((int)countDownTime/1000);

            }

            @Override
            public void onFinish() {
                //Get next question
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Time up!");
                alertDialog.setMessage("Oops times up");

                mStatusManager.updateAnsweredStatusForCurrentQuestion();
                mQuestionManager.updateAnsweredStatusInDb();

                setUpQuestion();
            }
        }.start();
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

            finish();

        } else {

            mStatusManager.setCurrentQuestion(question);

            countDownTime = MAX_TIME;

            countDownTimer.start();

        }

        countDownTime = MAX_TIME;
        countDownTimer.start();

    }



}
