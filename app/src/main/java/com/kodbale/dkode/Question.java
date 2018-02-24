package com.kodbale.dkode;

import java.util.UUID;

/**
 * Created by sagar on 2/25/18.
 */

public class Question {
    private long mQuestionId;
    private  String mQuestionText ;
    private String mAnswerText;

    public Question() {
        mQuestionText = "";
        mAnswerText = "";
        mQuestionId = -1;
    }

    public Question(String mQuestionText, String mAnswerText, long mQuestionId) {
        this.mQuestionText = mQuestionText;
        this.mAnswerText  = mAnswerText;
        this.mQuestionId = mQuestionId;
    }

    public String getQuestionText() {
        return new String(mQuestionText);
    }
    public String getAnswerText() {
        return new String(mAnswerText);
    }

    public long getQuestionId() {
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

}
