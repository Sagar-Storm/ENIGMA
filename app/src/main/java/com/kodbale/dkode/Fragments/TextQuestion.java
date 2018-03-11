package com.kodbale.dkode.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kodbale.dkode.Activities.Logout;
import com.kodbale.dkode.Database.Question;
import com.kodbale.dkode.Database.QuestionManager;
import com.kodbale.dkode.Database.StatusManager;
import com.kodbale.dkode.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TextQuestion extends Fragment {

    TextView text;

    public TextQuestion() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text_question, container, false);

        ArrayList<Question> notAnsweredList = QuestionManager.get(getContext()).getNotAnsweredList();
        ArrayList<Question>  answeredList = QuestionManager.get(getContext()).getAnsweredList();

        //TODO
        //update the database to reflect that the item has been isanswered with score = 0
        Question question = notAnsweredList.get(0);
        notAnsweredList.remove(0);
        answeredList.add(question);
        StatusManager.get(getContext()).setCurrentQuestion(question);
        text = (TextView) view.findViewById(R.id.display_question);
        setQuestion(question);
        return view;
    }

    public void setQuestion(Question question) {
        String questionText = question.getQuestionText();
        text.setText(questionText);
    }



}
