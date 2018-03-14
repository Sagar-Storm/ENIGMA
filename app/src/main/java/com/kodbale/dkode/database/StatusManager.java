package com.kodbale.dkode.database;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by sagar on 3/7/18.
 */

public class StatusManager {
    public static final String TAG = "Status Manager";

    private FirebaseAuth mAuth = null;
    private FirebaseUser mUser = null;
    private FirebaseDatabase mFirebaseDatabase = null;

    private FirebaseHelper mFirebaseHelper = null;



    private CurrentQuestion mCurrentQuestion;
    int mTotalQuestionsShown = 0;
    int mQuestionAnswered  = 0;
    int mTotalScore = 0;
    int mQuestionSkipped = 0;
    int mQuestionTimedOut = 0;
    int mTotalQuestion = 0;


    private static StatusManager mStatusManager = null;
    private Context mAppContext;



    private StatusManager(Context context) {
        mAppContext = context ;
        mAuth = null;
        mUser = null;
        if(mUser != null) Log.i("i", mUser.getEmail());
        mCurrentQuestion  = new CurrentQuestion(null, 300);
        mFirebaseDatabase = null;
        mFirebaseHelper = new FirebaseHelper();
    }

    public static StatusManager get(Context c) {
        if(mStatusManager == null) {
            mStatusManager = new StatusManager(c);
            Log.i("i", "initializing, initializing");
        }
        return mStatusManager;
    }



    public void writeScoreToFirebase() {
        Question question = getQuestion();
        int score = question.getScore();
        int questionId = question.getQuestionId();
        mFirebaseHelper.writeScore(mTotalScore, score, questionId, mUser);
    }

    public Question getQuestion() {
        return mCurrentQuestion.getQuestion();
    }


    public void setCurrentQuestion(Question question) {
        mCurrentQuestion.setCurrentQuestion(question);
    }

    public void setCurrentQuestionTimeRemaining(int timeRemaining) {
        mCurrentQuestion.setTimeRemaining(timeRemaining);
    }

    public void updateScoreForCurrentQuestion() {
        int score = mCurrentQuestion.getTimeRemaining() * 3;
        mCurrentQuestion.getQuestion().setScore(score);
        updateTotalScore(score);
        writeScoreToFirebase();
    }

    public void updateAnsweredStatusForCurrentQuestion (){
        mCurrentQuestion.getQuestion().setIsAnswered(true);
    }

    public void incrementNoOfTries() {
        mCurrentQuestion.getQuestion().incrementNumberOfTries();
    }

    public CurrentQuestion getCurrentQuestion() {
        return mCurrentQuestion;
    }

    public FirebaseUser getUser() {
        return mUser;
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public void setUser(FirebaseUser user)  {
        mUser = user;
    }

    public void setFirebaseDatabase(FirebaseDatabase firebaseDatabase) {
        if(firebaseDatabase == null) {
            Log.i("databasef", "firebasedatabase is null");
        }
        mFirebaseDatabase = firebaseDatabase;
        mFirebaseHelper.setFirebaseDatabase(mFirebaseDatabase);
    }

    public void setAuth(FirebaseAuth auth)  {
        mAuth = auth;
    }

    public int getTotalQuestionsShown() {
        return mTotalQuestionsShown;
    }

    public int getQuestionAnswered() {
        return mQuestionAnswered;
    }

    public int getTotalScore() {
        return mTotalScore;
    }

    public int getQuestionSkipped() {
        return mQuestionSkipped;
    }

    public int getQuestionTimedOut() {
        return mQuestionTimedOut;
    }


    public void setmTotalQuestionsShown(int mTotalQuestionsShown) {
        this.mTotalQuestionsShown = mTotalQuestionsShown;
    }

    public void setmQuestionAnswered(int mQuestionAnswered) {
        this.mQuestionAnswered = mQuestionAnswered;
    }

    public void setTotalScore(int mTotalScore) {

        this.mTotalScore = mTotalScore;

    }

    public void updateTotalScore(int score) {
        mTotalScore += score;
    }

    public void setmQuestionSkipped(int mQuestionSkipped) {
        this.mQuestionSkipped = mQuestionSkipped;
    }

    public void setmQuestionTimedOut(int mQuestionTimedOut) {
        this.mQuestionTimedOut = mQuestionTimedOut;
    }

    public void incrementQuestionAnswered() {
        mQuestionAnswered++;
        mTotalQuestionsShown++;
    }

    public void incrementQuestionSkipped() {
        mQuestionSkipped++;
        mTotalQuestionsShown++;
    }

    public void incrementQuestionTimedOut() {
        mQuestionTimedOut++;
        mTotalQuestionsShown++;
    }

    public void incrementScore(int score) {
        mTotalScore += score;
    }
}
