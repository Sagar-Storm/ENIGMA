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

import com.kodbale.dkode.R;

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
        View v =inflater.inflate(R.layout.fragment_pic, container, false);
        img = (ImageView) v.findViewById(R.id.setImageQ);
        return v;
    }

    public void setImageQuestion(Bitmap bits){
        img.setImageBitmap(bits);
    }

}
