package com.cashmyapps.core.cashmyappsproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by David on 08/03/2015.
 */
public class ConectadosAdaptador extends ArrayAdapter {

    private Activity actividad;
    private List<String> usuarios;


    public ConectadosAdaptador(Activity activity,List<String> users){
        super(activity,R.layout.elemento_lista_usuarios_conectados,users);

        this.actividad = activity;
        this.usuarios = users;

    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        LayoutInflater inflater = actividad.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.elemento_lista_usuarios_conectados, null, true);
        TextView txUsuConect = (TextView)rowView.findViewById(R.id.txUsuConect);
        ImageView imUsuConect = (ImageView)rowView.findViewById(R.id.imgUsuConect);

        txUsuConect.setText(usuarios.get(position));





        return rowView;
    }

}
