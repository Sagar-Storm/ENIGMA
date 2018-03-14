package com.kodbale.dkode.database;

/**
 * Created by sagar on 3/9/18.
 */

public class CurrentQuestion  {

    public int mTimeRemaining;
    private Question mQuestion ;

    CurrentQuestion(Question question, int timeRemaining) {
        mQuestion = question;
        mTimeRemaining = timeRemaining;
    }

    public void setCurrentQuestion(Question question) {
        mQuestion = question;
    }

    public void setTimeRemaining(int timeRemaining) {
        mTimeRemaining = timeRemaining;
    }

    public Question getQuestion() {
        return mQuestion;
    }

    public int getTimeRemaining() {
        return mTimeRemaining;
    }



}
