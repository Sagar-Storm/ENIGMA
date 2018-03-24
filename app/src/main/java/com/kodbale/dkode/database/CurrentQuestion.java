package com.kodbale.dkode.database;

/**
 * Created by sagar on 3/9/18.
 */

public class CurrentQuestion  {

    public long mTimeAnsweredAt;
    private Question mQuestion ;

    CurrentQuestion(Question question, long timeRemaining) {
        mQuestion = question;
        mTimeAnsweredAt = timeRemaining;
    }

    public void setCurrentQuestion(Question question) {
        mQuestion = question;
    }

    public void setTimeAnsweredAt(long timeRemaining) {
        mTimeAnsweredAt = timeRemaining;
    }

    public Question getQuestion() {
        return mQuestion;
    }

    public long getTimeRemaining() {
        return mTimeAnsweredAt;
    }



}
