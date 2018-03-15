package com.kodbale.dkode.fragments;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kodbale.dkode.R;
import com.kodbale.dkode.database.Question;
import com.kodbale.dkode.database.QuestionManager;
import com.kodbale.dkode.database.StatusManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PicFragment extends Fragment {


    ImageView img;
    public PicFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_pic, container, false);

        ArrayList<Question> notAnsweredList = QuestionManager.get(getContext()).getNotAnsweredList();
        ArrayList<Question>  answeredList = QuestionManager.get(getContext()).getAnsweredList();

        //TODO
        //update the database to reflect that the item has been isanswered with score = 0
        Question question = notAnsweredList.get(0);
        notAnsweredList.remove(0);
        answeredList.add(question);
        StatusManager.get(getContext()).setCurrentQuestion(question);
        img = (ImageView) view.findViewById(R.id.setImageQ);
        //setQuestion();
        return view;
    }

    public void setQuestion(Bitmap bits){
        img.setImageBitmap(bits);
    }

}
