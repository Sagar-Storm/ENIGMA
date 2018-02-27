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
    private static final String COLUMN_IS_ANSWERED = "is_answered";
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_IS_TEXT = "is_text";
    private static final String COLUMN_IS_IMAGE = "is_image";



    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null,  VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table questions (" + "question_no integer primary key autoincrement, question_text varchar(1000), answer_text varchar(1000), question_uuid int, is_answered int, score int, is_text int, is_image int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int isTrue(boolean field) {
        if(field == true) return 1;
        return 0;
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    public long  insertQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_QUESTION_TEXT, question.getQuestionText());
        cv.put(COLUMN_ANSWER_TEXT, question.getAnswerText());
        cv.put(COLUMN_QUESTION_UUID, question.getQuestionId());
        cv.put(COLUMN_IS_ANSWERED, isTrue(question.isIsAnswerd()));
        cv.put(COLUMN_SCORE, question.getScore());
        cv.put(COLUMN_IS_IMAGE, isTrue(question.isIsImage()));
        cv.put(COLUMN_IS_TEXT, isTrue(question.isIsText()));
        Log.i("inserted", "inserted mate");
        return getReadableDatabase().insert(TABLE_QUESTIONS, null, cv);
    }

    public QuestionCursor queryQuestions() {
        Cursor wrapped = getReadableDatabase().query(TABLE_QUESTIONS, null, null, null, null, null, null);
        return new QuestionCursor(wrapped);
    }

    public QuestionCursor queryNotAnswered() {
      //  Cursor wrapped = getReadableDatabase().rawQuery("select *from questions where is_answered = ?", new String[] { "0"});
        Cursor wrapped = getReadableDatabase().query(TABLE_QUESTIONS, null, null, null, null, null, null);
        return new QuestionCursor(wrapped);
    }

    public QuestionCursor queryAnswered() {
        Cursor wrapped = getReadableDatabase().rawQuery("SELECT *FROM " + TABLE_QUESTIONS + " where " + COLUMN_IS_ANSWERED + " =  1", null);
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
            boolean isAnswered = (getInt(getColumnIndex(COLUMN_IS_ANSWERED)) != 0) ? true: false;
            int score = getInt(getColumnIndex(COLUMN_SCORE));
            boolean isText = (getInt(getColumnIndex(COLUMN_IS_TEXT)) != 0) ? true: false;
            boolean isImage = (getInt(getColumnIndex(COLUMN_IS_TEXT)) != 0) ? true: false;
            question.setQuestionId(0);
            question.setQuestionText(questionText);
            question.setAnswerText(answerText);
            question.setIsAnswered(isAnswered);
            question.setScore(score);
            question.setIsImage(isImage);
            question.setIsText(isText);
            return question;
        }
    }

}
