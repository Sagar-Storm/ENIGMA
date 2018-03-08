package com.kodbale.dkode.Database;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by sagar on 3/7/18.
 */

public class StatusManager {
    public static final String TAG = "Status Manager";

    private FirebaseAuth mAuth = null;
    private FirebaseUser mUser = null;
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
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mCurrentQuestion  = new CurrentQuestion(null, 300);
    }

    public static StatusManager get(Context c) {
        if(mStatusManager == null) {
            mStatusManager = new StatusManager(c);
        }
        return mStatusManager;
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

    public void setmTotalScore(int mTotalScore) {
        this.mTotalScore = mTotalScore;
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
