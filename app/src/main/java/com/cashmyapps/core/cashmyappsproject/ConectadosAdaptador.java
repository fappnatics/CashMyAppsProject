package com.cashmyapps.core.cashmyappsproject;

import android.app.Activity;
import android.graphics.BitmapFactory;
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
    private String[] nombre_user;
    /*private String[] mail_user;
    private String[] saldo_user;*/


    public ConectadosAdaptador(Activity context,String[] nombres){
        super(context,R.layout.elemento_lista_usuarios_conectados,nombres);

        this.actividad = context;
        this.nombre_user = nombres;
        /*this.mail_user = mails;
        this.saldo_user = saldos;*/


    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        LayoutInflater inflater = actividad.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.elemento_lista_usuarios_conectados, null, true);
        TextView nombre = (TextView)rowView.findViewById(R.id.txUsuConect);

        nombre.setText(nombre_user[position]);






        return rowView;
    }

}
