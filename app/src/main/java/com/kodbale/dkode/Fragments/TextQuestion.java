package com.kodbale.dkode.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kodbale.dkode.Database.Question;
import com.kodbale.dkode.Database.QuestionManager;
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
        Log.i("d", "size is " + notAnsweredList.size());
       // Collections.shuffle(notAnsweredList);
        Question question = new Question(notAnsweredList.get(0));
        text = (TextView) view.findViewById(R.id.display_question);
        setQuestion(question);
        return view;
    }

    public void setQuestion(Question question) {
        String q = question.getQuestionText();
        text.setText(q);
    }


}
