package com.cashmyapps.core.cashmyappsproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Ranking extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private Context contexto;
    private TabHost mTabHost;
    private TabHost.TabSpec spec;
    private ListView listAPP;
    private String cuenta="";
    private Fechas fechas = new Fechas();
    private List<String> listaUsers;
    private String result;
    private JSONObject jObject;
    private JSONArray jArray;
    private List<String> lista_user;

    // TODO: Rename and change types of parameters
;


    public Ranking() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ranking,container,false);

        try {
            listAPP = (ListView) getActivity().findViewById(R.id.listaAPP);
            TextView correo = (TextView)getActivity().findViewById(R.id.txCorreo);
            listaUsers = new ArrayList<>();
            cuenta = correo.getText().toString();
            result = new JSONParser(Constantes.USUARIOS_CONECTADOS).execute(this,"").get();

            jArray = new JSONObject(result).getJSONArray("usuarios");

            for(int i=0;i<jArray.length();i++){

                lista_user.add(jArray.getJSONObject(i).getString("NOMBRE"));
                Log.i("ARRAY USER: ",jArray.getJSONObject(i).getString("NOMBRE"));

            }

            ListAdapter adp = new ArrayAdapter<String>(getActivity(),R.layout.elemento_lista_usuarios_conectados,lista_user);
            listAPP.setAdapter(adp);









        }
        catch(Exception e){
            String error = Constantes.ERRORES_APP.replace("[CUENTA]",cuenta).replace("[ERROR]", e.getMessage()).replace("[FECHA",fechas.getFechaActual()).replace(" ","%20");
            new JSONParser(error).execute(this,"foo");
        }




               // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ranking, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        contexto = getActivity();
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    public static Ranking newInstance(int sectionnumber){

        Ranking frag = new Ranking();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionnumber);
        frag.setArguments(args);
        return frag;

    }

}
