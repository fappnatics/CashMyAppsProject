package com.cashmyapps.core.cashmyappsproject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Compartir extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";


    public Compartir() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_compartir, container, false);
        Button btCompartir = (Button)rootView.findViewById(R.id.btCompatir);


        btCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compartir, container, false);

    }

    @Override
    public void onResume(){
        super.onResume();

        TextView txRefer = (TextView)getActivity().findViewById(R.id.txReferido);
        txRefer.setText("MIS HUEVOS");


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
