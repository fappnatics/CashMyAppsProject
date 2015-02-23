package com.cashmyapps.core.cashmyappsproject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Compartir extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public Compartir() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compartir, container, false);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    public static Compartir newInstance(int sectionnumber){

        Compartir frag = new Compartir();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionnumber);
        frag.setArguments(args);
        return frag;

    }

}
