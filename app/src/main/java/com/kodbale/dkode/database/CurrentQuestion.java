package com.kodbale.dkode.database;

/**
 * Created by sagar on 3/9/18.
 */

public class CurrentQuestion  {

    public int mTimeAnsweredAt;
    private Question mQuestion ;

    CurrentQuestion(Question question, int timeRemaining) {
        mQuestion = question;
        mTimeAnsweredAt = timeRemaining;
    }

    public void setCurrentQuestion(Question question) {
        mQuestion = question;
    }

    public void setTimeAnsweredAt(int timeRemaining) {
        mTimeAnsweredAt = timeRemaining;
    }

    public Question getQuestion() {
        return mQuestion;
    }

    public int getTimeRemaining() {
        return mTimeAnsweredAt;
    }



}
