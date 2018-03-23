package com.kodbale.dkode.database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sagar on 3/10/18.
 */


public class FirebaseHelper {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference ;
    private Context mAppContext;
    FirebaseHelper() {
        mFirebaseDatabase = null;
    }

    public void setFirebaseDatabase(FirebaseDatabase firebaseDatabase, Context context) {

        mFirebaseDatabase = firebaseDatabase;
        if(mFirebaseDatabase != null) {
            mDatabaseReference = mFirebaseDatabase.getReference("userData/scores/");
        }
        mAppContext = context;
    }


    public void writeScore(int finalScore, int score, int questionID, FirebaseUser user) {

        Log.i("score", "writescore called");
        String _questionID = Integer.toString(questionID);
        String _score = Integer.toString(score);
        Log.i("score", Integer.toString(score));
        Log.i("questoinID", Integer.toString(questionID));

        String _emailId = user.getEmail().split("@")[0];

        try {
            mDatabaseReference.child(user.getUid()).child(_emailId).child("score_details").push().setValue(new ScoreObject(questionID, score));
            Map<String, Integer> finalScoreMap = new HashMap<>();
            finalScoreMap.put("finalScore", finalScore);
            mDatabaseReference.child(user.getUid()).child(_emailId).child("final_score").setValue(finalScoreMap);
        } catch(Exception e) {
            Log.i("problem with firebase", "pjiooijo");
        }
    }

    public void writeQuestionIdToAnsweredList(ArrayList answeredList, FirebaseUser user) {

        String _emailId = user.getEmail().split("@")[0];
        try {
            Map<String, ArrayList<Integer>> answeredListMap = new HashMap<>();
            answeredListMap.put("question_ids", answeredList);
            mDatabaseReference.child(user.getUid() + "/" + _emailId + "/answered_list").setValue(answeredListMap);
        } catch(Exception e) {
            Log.i("answer storing error", "exception in firebase");
        }
    }


    public String getEmailStripped(String emailId) {
        String emailIdSplit[] = emailId.split("@");
        String _emailId = emailIdSplit[0];
        return _emailId;
    }

    public void fetchAnsweredList(FirebaseUser user) {
        Log.i("i", "fetch answerd list from fb called");
        String emailId = getEmailStripped(user.getEmail());

        DatabaseReference databaseReference = mDatabaseReference.child(user.getUid()).child(emailId).child("answered_list").child("question_ids");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Integer>> t = new GenericTypeIndicator<List<Integer>>() {};
                ArrayList<Integer> answeredListIds = (ArrayList<Integer>) dataSnapshot.getValue(t);
                if(answeredListIds != null) {
                    StatusManager.get(mAppContext).setAnsweredListIds(answeredListIds);
                    System.out.println("size of firebase answerd list " + StatusManager.get(mAppContext).getAnsweredListIds().size());
                }
                QuestionManager.get(mAppContext).initializeFromFirebaseList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }


    public void writeTimeStamp(FirebaseUser user, Long timeStamp) {
        String _emailId = user.getEmail().split("@")[0];
        try {
            mDatabaseReference.child(user.getUid()).child(_emailId).child("logged_in_at").setValue(timeStamp);
        } catch(Exception e) {
            Log.i("answer storing error", "exception in firebase");
        }
    }

    public void getTimeStamp(FirebaseUser user) {
        String emailId = getEmailStripped(user.getEmail());
        System.out.println("timestamp" + emailId);
            DatabaseReference databaseReference = mDatabaseReference.child(user.getUid()).child(emailId).child("logged_in_at");

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Object o = dataSnapshot.getValue();
                    if (dataSnapshot.getValue() == null) {
                        System.out.println("set mate null");

                        StatusManager.get(mAppContext).setTimeStamp(null);
                        StatusManager.get(mAppContext).allSet = 0;
                    } else {
                        GenericTypeIndicator<Long> t = new GenericTypeIndicator<Long>(){} ;
                        System.out.println("set mate");
                        StatusManager.get(mAppContext).setTimeStamp(dataSnapshot.getValue(t));
                        StatusManager.get(mAppContext).allSet = 1;
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });

    }

    class ScoreObject {

        public int score;
        public int questionId;

        public ScoreObject() {
            score = 0;
            questionId = -1;
        }

        public ScoreObject (int questionId, int score) {
            this.score = score;
            this.questionId = questionId;
        }

    }



}
