package com.kodbale.dkode.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kodbale.dkode.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TextQuestion extends Fragment {


    public TextQuestion() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_text_question, container, false);
    }

}
