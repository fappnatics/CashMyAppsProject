package com.cashmyapps.core.cashmyappsproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by David on 08/03/2015.
 */
public class ConectadosAdaptador extends ArrayAdapter {

    private Activity actividad;
    private String[] nombre_user;
    private String[] mail_user;
    private String[] saldo_user;
    private String[] user_conect;


    public ConectadosAdaptador(Activity context,String[] nombres, String[] mails, String[] saldos, String[] conectado){
        super(context,R.layout.elemento_lista_usuarios_conectados,nombres);

        this.actividad = context;
        this.nombre_user = nombres;
        this.mail_user = mails;
        this.saldo_user = saldos;
        this.user_conect = conectado;


    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        LayoutInflater inflater = actividad.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.elemento_lista_usuarios_conectados, null, true);
        TextView nombre = (TextView) rowView.findViewById(R.id.txUsuConect);
        //TextView cuentaAsociada = (TextView)rowView.findViewById(R.id.txCorreoAsociado);
        TextView ppi = (TextView) rowView.findViewById(R.id.txCoins);
        ImageView imgOnline = (ImageView) rowView.findViewById(R.id.imgOnline);
        ImageView img1st = (ImageView) rowView.findViewById(R.id.img1st);


        switch (position) {

            case 0:
                img1st.setImageResource(R.drawable.first);
                break;
            case 1:
                img1st.setImageResource(R.drawable.second);
                break;
            case 2:
                img1st.setImageResource(R.drawable.third);
                break;

            default:
                img1st.setVisibility(View.INVISIBLE);
                break;
        }


        if(user_conect[position].equals("N"))
        {
            imgOnline.setImageResource(R.drawable.offline);
        }
        else
        {
            imgOnline.setImageResource(R.drawable.online);
        }
        nombre.setText(nombre_user[position]);
        //cuentaAsociada.setText(mail_user[position]);
        ppi.setText(saldo_user[position]+" Coins");






        return rowView;
    }

}
