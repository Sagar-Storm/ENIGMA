package com.kodbale.dkode.database;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sagar on 3/7/18.
 */

public class StatusManager {

    public static final String TAG = "Status Manager";

    private FirebaseAuth mAuth = null;
    private FirebaseUser mUser = null;
    private FirebaseDatabase mFirebaseDatabase = null;
    private FirebaseHelper mFirebaseHelper = null;

    private OkHttpClient client;


    private CurrentQuestion mCurrentQuestion;
    private int mTimeRemaining;
    private ArrayList<Integer> mAnsweredListIds;
    private Long mTimeStamp;
    public int allSet = -1;
    private Solutions mSolution ;


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
        if(mUser != null) Log.i("i", mUser.getEmail());
        mAnsweredListIds = new ArrayList<>();
        mCurrentQuestion  = new CurrentQuestion(null, 300);
        mFirebaseDatabase = null;
        mFirebaseHelper = new FirebaseHelper();
        mTimeStamp = null;
        mSolution = new Solutions();
        client = new OkHttpClient();
    }


    public Solutions getSolutions() {
        return mSolution;
    }

    public void initializeGameLogin() {

        mFirebaseHelper.getTimeStamp(mUser);

    }

    public void setTimeRemaining(int timeRemaining) {
        this.mTimeRemaining = timeRemaining ;
    }

    public int getTimeRemaining() {
        return mTimeRemaining;
    }

    public static StatusManager get(Context c) {
        if(mStatusManager == null) {
            mStatusManager = new StatusManager(c);
            Log.i("i", "initializing, initializing");
        }
        return mStatusManager;
    }

    public String getCurrentTime(){
        try {

            String str = run("https://google.com/");
            //Toast.makeText(mAppContext,str,Toast.LENGTH_LONG).show();
            return str;

        } catch (IOException e) {
            //Toast.makeText(mAppContext,"Could not get time!",Toast.LENGTH_SHORT).show();
            return "";
        }


    }
    String run(String url) throws IOException{
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return response.header("Date");
    }

    public void setTimeStamp(Long timeStamp) {
        mTimeStamp = timeStamp;
    }

    public void writeTimeStampToFirebase(Long timestamp) {
        mFirebaseHelper.writeTimeStamp(mUser, timestamp);
    }

    public Long getTimeStamp() {
        return mTimeStamp ;
    }

    public void setAllToNull() {
        mAnsweredListIds = null;
        mCurrentQuestion = null;
        mStatusManager = null;
    }

    public void setAnsweredListIds(ArrayList<Integer> answeredListIds) {
        this.mAnsweredListIds = answeredListIds;
    }


    public ArrayList<Integer> getAnsweredListIds() {
        return mAnsweredListIds;
    }

    public void initializeAnsweredList() {
        try {
            mFirebaseHelper.fetchAnsweredList(mUser);
        } catch(Exception e) {
            System.out.println("error fetching favorite list" + e.getMessage());
        }
    }

    public void writeScoreToFirebase() {
        try {
            Question question = getQuestion();
            int score = question.getScore();
            int questionId = question.getQuestionId();
            mFirebaseHelper.writeScore(mTotalScore, score, questionId, mUser);
        } catch(Exception e) {

        }
    }

    public void writeAnsweredListToFirebase() {
        Question question = getQuestion();
        mFirebaseHelper.writeQuestionIdToAnsweredList(mAnsweredListIds, mUser);
    }

    public Question getQuestion() {
        return mCurrentQuestion.getQuestion();
    }


    public void setCurrentQuestion(Question question) {
        mCurrentQuestion.setCurrentQuestion(question);
    }


    public void updateScoreForCurrentQuestion() {
        int timeAnsweredAt = 300 - mTimeRemaining;
        mCurrentQuestion.setTimeAnsweredAt(timeAnsweredAt);
        int score = 250 / mCurrentQuestion.getQuestion().getNumberOfTries();
        mCurrentQuestion.getQuestion().setScore(score);
        updateTotalScore(score);
        writeScoreToFirebase();
    }

    public void updateAnsweredStatusForCurrentQuestion (){
        mCurrentQuestion.getQuestion().setIsAnswered(true);
        mAnsweredListIds.add(getQuestion().getQuestionId());
        writeAnsweredListToFirebase();
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
        mFirebaseHelper.setFirebaseDatabase(mFirebaseDatabase, mAppContext);
    }

    public void updateNumberOfTriesInFirebase() {
        mFirebaseHelper.updateNumberOfTriesInFirebase(getCurrentQuestion().getQuestion(), getCurrentQuestion().getQuestion().getNumberOfTries(), mUser);
    }

    public void initializeNumberOfTriesListInFirebase() {
        mFirebaseHelper.fetchNumberOfTriesInFirebase(mUser);
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
