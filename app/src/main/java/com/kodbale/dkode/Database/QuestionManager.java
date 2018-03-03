package com.kodbale.dkode.Database;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by sagar on 2/25/18.
 */

public class QuestionManager {

    public static final String TAG = "QuestionManager";

    private static int mQuestionsAnswered = 0;
    private static DatabaseHelper mDatabaseHelper;
    private static QuestionManager mQuestionManager = null;
    private static ArrayList<Question> mNotAnsweredList, mAnsweredList;
    private Context mAppContext;

    private QuestionManager(Context context) {
        mAppContext = context;
        mDatabaseHelper = new DatabaseHelper(mAppContext);
        mNotAnsweredList = new ArrayList<>();
        mAnsweredList = new ArrayList<>();
    }

    public static QuestionManager get(Context c) {
        if(mQuestionManager == null) {
            mQuestionManager = new QuestionManager(c);
        }
        return mQuestionManager;
    }

    public  void incrementQuestionAnswered() {
            mQuestionsAnswered++;
    }

    public int getQuestionsAnswered() {
        return mQuestionsAnswered;
    }

    public ArrayList<Question> getNotAnsweredList() {
        return mNotAnsweredList;
    }

    public ArrayList<Question> getAnsweredList() {
        return mAnsweredList;
    }

    public void insertQuestion(Question question) {
        Log.i("what's", "called");
        mDatabaseHelper.insertQuestion(question);
    }

    public ArrayList<Question> getAllQuestions() {
        DatabaseHelper.QuestionCursor mQuestionCursor = mDatabaseHelper.queryQuestions();
        ArrayList<Question> questionsList = new ArrayList<>();
        if(mQuestionCursor.getCount() > 0) {
            for(int i = 0; i < mQuestionCursor.getCount(); i++) {
                Question question = mQuestionCursor.getQuestion();
                questionsList.add(question);
            }
        }
        mQuestionCursor.close();
        return questionsList;
    }

    public void insertAllQuestions() {
        DatabaseHelper.QuestionCursor mQuestionCursor = mDatabaseHelper.queryQuestions();
        if(mQuestionCursor.getCount() == 0 ) {
         //   mQuestionManager.insertQuestion(new Question("What is your name?", "Your name you know", 123, false, 0, false, true));
           // mQuestionManager.insertQuestion(new Question("That's it?", "answer not revealed", 123,false,0, false, true));
            mQuestionManager.insertQuestion(new Question("That's it1?", "answer not revealed", 123,false,0, false, true));
            mQuestionManager.insertQuestion(new Question("That's it2?", "answer not revealed", 123,false,0, false, true));
            mQuestionManager.insertQuestion(new Question("That's it3?", "answer not revealed", 123,false,0, false, true));
            mQuestionManager.insertQuestion(new Question("That's it4?", "answer not revealed", 123,false,0, false, true));

        }
        Log.i("i", "inserted 2 questions");
        mQuestionCursor.close();
    }

    public void initializeNotAnsweredList() {
        DatabaseHelper.QuestionCursor mQuestionCursor = mDatabaseHelper.queryNotAnswered();
        Log.i("d", "size of not answered list" + mQuestionCursor.getCount());
        mQuestionCursor.moveToFirst();
        if(mQuestionCursor.getCount() != 0) {
            for(int i = 0; i < mQuestionCursor.getCount(); i++) {
               Question question = mQuestionCursor.getQuestion();
                Log.i("i", "adding to not answeredlist");
                mNotAnsweredList.add(question);
                mQuestionCursor.moveToNext();
            }
        }
        mQuestionCursor.close();
    }

    public void initializeAnsweredList() {
        DatabaseHelper.QuestionCursor mQuestionCursor = mDatabaseHelper.queryAnswered();
        mQuestionCursor.moveToFirst();
        if(mQuestionCursor.getCount() != 0) {
            for(int i = 0; i < mQuestionCursor.getCount(); i++) {
                Question question = mQuestionCursor.getQuestion();
                mAnsweredList.add(question);
            }
        }
        mQuestionCursor.close();
    }

    public Question getNextQuestion() {
        if(mNotAnsweredList.size() == 0) {
            Question question = new Question();
            question.setQuestionText("you finished all");
            return question;
        }
        Log.i("d", "size before" + mNotAnsweredList.size());
        Question question = mNotAnsweredList.get(0);
        mAnsweredList.add(question);
        mNotAnsweredList.remove(question);
        Log.i("d", "size after" + mNotAnsweredList.size() + question.getQuestionText());

        return question;
    }


}
