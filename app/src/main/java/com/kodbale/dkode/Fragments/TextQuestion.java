package com.kodbale.dkode.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kodbale.dkode.Database.Question;
import com.kodbale.dkode.R;

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
        Question question=new Question("Who are you?","DC",1);
        text = (TextView) view.findViewById(R.id.display_question);
        setQuestion(question);
        return view;
    }

    private void setQuestion(Question question) {
        String q = question.getQuestionText();
        text.setText(q);
    }

}
