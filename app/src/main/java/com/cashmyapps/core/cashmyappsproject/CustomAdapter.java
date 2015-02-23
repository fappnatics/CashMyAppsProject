package com.cashmyapps.core.cashmyappsproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by David on 22/01/2015.
 */
public class CustomAdapter extends ArrayAdapter {

    private final Activity context;
    private final String[] nombre_apps;
    private final Bitmap[] imagen;
    private final String[] resumen_apps;
    private final String[] ppi_app;
    private final Integer[] imageId;
    private final Uri[] uris;

    public CustomAdapter(Activity context, String[] nombre_apps, Bitmap[] imagen, String[] resumen_apps, String[] ppi_app, Integer[] imageId,Uri[] url) {
        super(context, R.layout.elementolista, nombre_apps);
        this.context = context;
        this.nombre_apps = nombre_apps;
        this.imagen = imagen;
        this.resumen_apps = resumen_apps;
        this.ppi_app = ppi_app;
        this.imageId = imageId;
        this.uris = url;



    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.elementolista, null, true);
        TextView nombre = (TextView)rowView.findViewById(R.id.txTitulo);
        ImageView icono = (ImageView)rowView.findViewById(R.id.img);
        TextView resumen_app = (TextView)rowView.findViewById(R.id.txResumen);
        TextView ppi = (TextView)rowView.findViewById(R.id.txt_ppi);
        ImageView imgEstrellas = (ImageView)rowView.findViewById(R.id.imgEstrellas);
        TextView botones = (TextView)rowView.findViewById(R.id.btInstalar);
        Double valor = Double.parseDouble(ppi_app[position]);




        Log.i("PPI", valor + "");

        nombre.setText(nombre_apps[position]);
        icono.setImageBitmap(imagen[position]);
        resumen_app.setText(resumen_apps[position]);
        ppi.setText("PPI: "+ String.format("%.2f",Double.parseDouble(ppi_app[position])*0.1)+"$");
        imgEstrellas.setImageResource(imageId[position]);
        botones.setMovementMethod(LinkMovementMethod.getInstance());
        botones.setText("Instalar");


        botones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launcher = new Intent(Intent.ACTION_VIEW,uris[position]);
                getContext().startActivity(launcher);
            }
        });



        return rowView;
    }
}