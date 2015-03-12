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
import java.util.concurrent.ExecutionException;


public class Ranking extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private Activity contexto;
    private TabHost mTabHost;
    private TabHost.TabSpec spec;
    private ListView listAPP;
    private String cuenta="";
    private Fechas fechas = new Fechas();
    private String result;
    private JSONObject jObject;
    private JSONArray jArray;
    private String[] lista_nombre;
    private String[] lista_mail;
    private String[] lista_saldo;

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
            listAPP = (ListView) rootView.findViewById(R.id.listaAPP);
            TextView correo = (TextView)getActivity().findViewById(R.id.txCorreo);
            cuenta = correo.getText().toString();

            result = new JSONParser(Constantes.GET_RANKING_USERS).execute(this,"").get();
            jArray = new JSONObject(result).getJSONArray("usuarios");
            lista_nombre = new String[jArray.length()];
            lista_mail = new String[jArray.length()];
            lista_saldo = new String[jArray.length()];

            for(int i=0;i<jArray.length();i++){
                lista_nombre[i] = jArray.getJSONObject(i).getString("NOMBRE");
                lista_mail[i] = jArray.getJSONObject(i).getString("MAIL");
                lista_saldo[i] = jArray.getJSONObject(i).getString("SALDO");
                Log.i("LISTA_NOMBRES",lista_nombre[i]);
            }
            //TODO Arreglar la visualizacion de la lista de usuarios. No se ve.



            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, lista_nombre);
            //ConectadosAdaptador adp = new ConectadosAdaptador(getActivity(),lista_nombre,lista_mail,lista_saldo);
            listAPP.setAdapter(adapter1);



        }
        catch (ExecutionException e) {
            String error = Constantes.ERRORES_APP.replace("[CUENTA]",cuenta).replace("[ERROR]", e.getMessage()).replace("[FECHA",fechas.getFechaActual()).replace(" ","%20");
            new JSONParser(error).execute(this,"foo");
        }
        catch(Exception e){
            Log.i("LISTAUSER", "ERRORRRRRRR!!!!: "+e.getMessage());
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
        Log.i("ACTIVITY",contexto.toString());
    }


    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

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
