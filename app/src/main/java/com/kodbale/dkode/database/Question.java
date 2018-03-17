package com.kodbale.dkode.database;

import java.util.UUID;

/**
 * Created by sagar on 2/25/18.
 */

public class Question {
    private int mQuestionId;
    private  String mQuestionText ;
    private String mAnswerText;
    private boolean mIsAnswerd;
    private int mScore;
    private int mNumberOfTries = 1;

    public Question() {
        mQuestionText = "Dummy Text";
        mAnswerText = "";
        mQuestionId = -1;
        mIsAnswerd = false;
        mScore = 0;
        mNumberOfTries = 1;
    }


    public int getNumberOfTries() {
        return mNumberOfTries;
    }

    public void setNumberOfTries(int numberOfTries) {
        mNumberOfTries = numberOfTries;
    }

    public void incrementNumberOfTries() {
        mNumberOfTries++;
    }

    public Question(Question q) {
        this.mQuestionText = q.mQuestionText;
        this.mAnswerText = q.mAnswerText;
        this.mQuestionId = q.mQuestionId;
        this.mIsAnswerd = q.mIsAnswerd;
        this.mScore = q.mScore;
    }

    public Question(String mQuestionText, String mAnswerText, int mQuestionId, boolean mIsAnswerd,
        int  mScore
    ) {
        this.mQuestionText = mQuestionText;
        this.mAnswerText  = mAnswerText;
        this.mQuestionId = mQuestionId;
        this.mIsAnswerd = mIsAnswerd;
        this.mScore = mScore;
    }

    public String getQuestionText() {
        return mQuestionText;
    }
    public String getAnswerText() {
        return new String(mAnswerText);
    }

    public boolean isIsAnswerd() {
        return mIsAnswerd;
    }

    public int getScore() {
        return mScore;
    }
    public int getQuestionId() {
        return mQuestionId;
    }



    public void setQuestionId(int mQuestionId) {
        this.mQuestionId = mQuestionId;
    }
    public void setQuestionText(String mQuestionText) {
        this.mQuestionText = mQuestionText;
    }

    public void setAnswerText(String mAnswerText) {
        this.mAnswerText = mAnswerText ;
    }
    public void setIsAnswered(boolean mIsAnswered) {
        this.mIsAnswerd = mIsAnswered;
    }

    public void setScore(int mScore) {
        this.mScore = mScore;
    }

}
