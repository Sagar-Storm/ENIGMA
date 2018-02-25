package com.kodbale.dkode.Database;



import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by sagar on 2/24/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "dkode.sqlite";
    public static final int VERSION = 1;
    private static final String TABLE_QUESTIONS = "questions";
    private static final String COLUMN_QUESTION_NO = "question_no";
    private static final String COLUMN_QUESTION_UUID = "question_uuid";
    private static final String COLUMN_QUESTION_TEXT = "question_text";
    private static final String COLUMN_ANSWER_TEXT = "answer_text";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null,  VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table questions (" + "question_no integer primary key autoincrement, question_text varchar(1000), answer_text varchar(1000), question_uuid int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    public long  insertQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_QUESTION_TEXT, question.getQuestionText());
        cv.put(COLUMN_ANSWER_TEXT, question.getAnswerText());
        Log.i("inserted", "inserted mate");
        return getReadableDatabase().insert(TABLE_QUESTIONS, null, cv);
    }

    public QuestionCursor queryQuestions() {
        Cursor wrapped = getReadableDatabase().query(TABLE_QUESTIONS, null, null, null, null, null, null);
        return new QuestionCursor(wrapped);
    }


    public static class QuestionCursor extends CursorWrapper {
        public QuestionCursor(Cursor c) {
            super(c);
        }

        public Question getQuestion() {
            Question question = new Question();
            int questionId = getInt(getColumnIndex((COLUMN_QUESTION_NO)));
            String questionText = getString(getColumnIndex(COLUMN_QUESTION_TEXT));
            String answerText = getString(getColumnIndex((COLUMN_ANSWER_TEXT)));
            question.setAnswerText(answerText);
            question.setQuestionText(questionText);
            question.setQuestionId(questionId);
            return question;
        }
    }

}
