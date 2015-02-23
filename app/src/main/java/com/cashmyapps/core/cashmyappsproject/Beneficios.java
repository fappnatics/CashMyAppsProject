package com.cashmyapps.core.cashmyappsproject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Beneficios.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Beneficios#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Beneficios extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public Beneficios() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_beneficios, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    public static Beneficios newInstance(int sectionnumber){

        Beneficios frag = new Beneficios();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionnumber);
        frag.setArguments(args);
        return frag;

    }


}
