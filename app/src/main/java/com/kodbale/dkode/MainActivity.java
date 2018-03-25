package com.kodbale.dkode;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blikoon.qrcodescanner.QrCodeActivity;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.firebase.auth.FirebaseAuth;
import com.kodbale.dkode.activities.EndingActivity;
import com.kodbale.dkode.activities.InfoActivity;
import com.kodbale.dkode.activities.EndingActivity;
import com.kodbale.dkode.activities.ScoreActivity;
import com.kodbale.dkode.database.CurrentQuestion;
import com.kodbale.dkode.database.Question;
import com.kodbale.dkode.database.QuestionManager;
import com.kodbale.dkode.database.Solutions;
import com.kodbale.dkode.database.StatusManager;
import com.kodbale.dkode.fragments.PicFragment;
import com.kodbale.dkode.login.LoginActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

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
    private ImageButton mQuestionShower;

    public long timeRemaining = 5200;

    public long timeStamp;
    public Date date;


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
            
        }
        return super.onOptionsItemSelected(item);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiations
        mQuestionShower = (ImageButton) findViewById(R.id.questionDisplayer);
        submit = (Button) findViewById(R.id.submit);
        skip = (Button) findViewById(R.id.skip);
        mTimerTextView = (TextView) findViewById(R.id.timer);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.tools);
        setSupportActionBar(toolbar);



        //sees to set the time remaining of the logged_in user
        if(getIntent().getExtras() != null) {
            Long timeExtra = getIntent().getExtras().getLong("TIME_REMAINING");
          //  Toast.makeText(this, "extra was there", Toast.LENGTH_SHORT).show();
            if (timeExtra != null) {
                timeRemaining = timeExtra;
            }
        }

        submit.setOnClickListener(this);
        skip.setOnClickListener(this);

        mQuestionManager = QuestionManager.get(getApplicationContext());
        mStatusManager = StatusManager.get(getApplicationContext());




//decrements the time every second
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                timeRemaining--;
                    if (timeRemaining > 0) {
                        if (handler != null) {
                            handler.postDelayed(this, 1000);
                            StatusManager.get(getApplicationContext()).setTimeRemaining(timeRemaining);
                           // mTimerTextView.setText(timeRemaining + "");
                        }
                    } else {
                        startend();
                    }
                }
        };
        handler = new Handler();
        handler.postDelayed(runnable, 1000);




        if(QuestionManager.get(getApplicationContext()).getNotAnsweredList().size() == 0) {
            Toast.makeText(this,"You have finished all questions", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, EndingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            handler = null ;
            startActivity(intent);
            finish();

        } else {
            ArrayList<Question> notAnsweredList = QuestionManager.get(getApplicationContext()).getNotAnsweredList();
            ArrayList<Question>  answeredList = QuestionManager.get(getApplicationContext()).getAnsweredList();

            //TODO
            //update the database to reflect that the item has been isanswered with score = 0
            Question question = notAnsweredList.get(0);
            notAnsweredList.remove(0);
            answeredList.add(question);
            StatusManager.get(getApplicationContext()).setCurrentQuestion(question);
            int id = getmeimageid(question.getQuestionId());
            Drawable res = getResources().getDrawable(id);
            mQuestionShower.setImageDrawable(res);

        }

    }

    public void startend() {
         Toast.makeText(this, "damadama time over", Toast.LENGTH_SHORT).show();
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

         case R.id.skip:

             new MaterialStyledDialog.Builder(this)
                     .setTitle("Oh no!")
                     .withDivider(true)
                     .setCancelable(false)
                     .setDescription("Are you sure you want to skip, you won't be able to come back to this question!!")
                     .setPositiveText("I'm sure!")
                     .setIcon(R.drawable.broken_heart)
                     .onPositive(new MaterialDialog.SingleButtonCallback() {
                         @Override
                         public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                             mStatusManager.getCurrentQuestion().getQuestion().setNumberOfTries(10);
                             mStatusManager.updateScoreForSkippedQuestion();
                             mStatusManager.updateAnsweredStatusForCurrentQuestion();
                             mStatusManager.updateNumberOfTriesInFirebase();
                            // mQuestionManager.updateAnsweredStatusInDb();
                             setUpQuestion();
                             dialog.dismiss();
                             //Toast.makeText(getApplicationContext(),"Dope",Toast.LENGTH_SHORT).show();
                         }
                     })
                     .setNegativeText("No no!")
                     .onNegative(new MaterialDialog.SingleButtonCallback() {
                         @Override
                         public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                             dialog.dismiss();
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

                new MaterialStyledDialog.Builder(this)
                        .setTitle("Oops...")
                        .setCancelable(true)
                        .setDescription("Couldn't get a good angle, take photo again!")
                        .show();

            }
            return;

        }
        if(requestCode == REQUEST_CODE_QR_SCAN)
        {
            if(data==null)
                return;

            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");

            String answer_text = mStatusManager.getCurrentQuestion().getQuestion().getAnswerText();

            if(Solutions.mSolutions.containsKey(answer_text) && Solutions.mSolutions.get(answer_text).equals(result)) {
                new MaterialStyledDialog.Builder(this)
                        .setTitle("Good job!")
                        .setDescription("You solved the problem!")
                        .setPositiveText("Okay!")
                        .setCancelable(false)
                        .setIcon(R.drawable.celebration)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                mStatusManager.updateScoreAndTimeForCurrentQuestion();
                                mStatusManager.updateAnsweredStatusForCurrentQuestion();
                                mStatusManager.updateNumberOfTriesInFirebase();
                                //long questionUUID = getQuestionUUID();
                                //int currentQuestionScore = getCurrentQuestionScore();

                                //mQuestionManager.updateQuestionScoreInDb(questionUUID, currentQuestionScore);

                               // mQuestionManager.updateAnsweredStatusInDb();
                                setUpQuestion();

                            }
                        })
                        .show();

            } else {


                new MaterialStyledDialog.Builder(this)
                        .setTitle("Failure!")
                        .setDescription("Wrong answer mate!")
                        .setPositiveText("Retry!")
                        .setIcon(R.drawable.thumb)
                        .setCancelable(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                mStatusManager.incrementNoOfTries();
                                //mQuestionManager.updateNumberOfTries();
                                mStatusManager.updateNumberOfTriesInFirebase();

                                if(mStatusManager.getCurrentQuestion().getQuestion().getNumberOfTries() == 3) {
                                    mStatusManager.updateScoreAndTimeForCurrentQuestion();
                                    mStatusManager.updateAnsweredStatusForCurrentQuestion();
                                   // mQuestionManager.updateAnsweredStatusInDb();
                                    setUpQuestion();
                                    dialog.dismiss();
                                }

                                dialog.dismiss();

                            }
                        })
                        .show();
            }


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
            int id = getmeimageid(question.getQuestionId());
            Log.i("i", "the image is " + question.getImagePath());
            Drawable res = getResources().getDrawable(id);
            mQuestionShower.setImageDrawable(res);

        }
    }


    public int getmeimageid(int questionid) {
        switch(questionid) {
            case 1: return R.drawable.one;
            case 2: return R.drawable.two;
            case 3: return R.drawable.three;
            case 4: return R.drawable.four;
            case 5: return R.drawable.five;
            case 6: return R.drawable.six;
            case 7: return R.drawable.seven;
            case 8: return R.drawable.eight;
            case 9: return R.drawable.nine;
            case 10: return R.drawable.ten;
            case 11: return R.drawable.eleven;
            case 12: return R.drawable.twelve;
            case 13: return R.drawable.thirteen;
            case 14: return R.drawable.fourteen;
            case 15: return R.drawable.fifteen;
            default: return R.drawable.enigma_logo;
        }
    }


}
