package com.kodbale.dkode.database;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sagar on 3/10/18.
 */


public class FirebaseHelper {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference ;

    FirebaseHelper() {
        mFirebaseDatabase = null;
    }

    public void setFirebaseDatabase(FirebaseDatabase firebaseDatabase) {
        mFirebaseDatabase = firebaseDatabase;
        mDatabaseReference = mFirebaseDatabase.getReference("userData/scores/" );
    }


    public void writeScore(int finalScore, int score, int questionID, FirebaseUser user) {

        Log.i("score", "writescore called");

        JSONObject mJSONObject = new JSONObject();

        String _questionID = Integer.toString(questionID);

        String _score = Integer.toString(score);

        try {
            mJSONObject.put("questionID", _questionID);
            mJSONObject.put("score", _score);
        } catch(Exception e) {
            Log.i("error", "error in json exception");
        }
        Log.i("score", Integer.toString(score));
        Log.i("questoinID", Integer.toString(questionID));
        mDatabaseReference.child(user.getUid() + "/emailId").setValue(user.getEmail());
        mDatabaseReference.child(user.getUid()).push().setValue(new ScoreObject( questionID, score )) ;
        Map<String, Integer> finalScoreMap = new HashMap<>();
        finalScoreMap.put("finalScore", finalScore);
        mDatabaseReference.child(user.getUid() + "/finalScore").setValue(finalScoreMap);
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
