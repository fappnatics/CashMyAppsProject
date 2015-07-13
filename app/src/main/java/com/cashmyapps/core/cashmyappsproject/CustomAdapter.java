package com.cashmyapps.core.cashmyappsproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutionException;

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
    private final String correo;
    private String result="";
    private List<String> lista_apps;
    private Double saldo;
    private Double auxiliar;
    private  String cod_pago;
    private  String strAux;
    private  int finalRound;


    public CustomAdapter(Activity context, String[] nombre_apps, Bitmap[] imagen, String[] resumen_apps, String[] ppi_app, Integer[] imageId,Uri[] url, String cuenta) {
        super(context, R.layout.elementolista, nombre_apps);
        this.context = context;
        this.nombre_apps = nombre_apps;
        this.imagen = imagen;
        this.resumen_apps = resumen_apps;
        this.ppi_app = ppi_app;
        this.imageId = imageId;
        this.uris = url;
        this.correo = cuenta;

    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.elementolista, null, true);
        TextView nombre = (TextView)rowView.findViewById(R.id.txTitulo);
        ImageView icono = (ImageView)rowView.findViewById(R.id.img);
        TextView resumen_app = (TextView)rowView.findViewById(R.id.txResumen);
        TextView ppi = (TextView)rowView.findViewById(R.id.txt_ppi);
        ImageView imgEstrellas = (ImageView)rowView.findViewById(R.id.imgEstrellas);
        TextView botones = (TextView)rowView.findViewById(R.id.btInstalar);
        Double valor = Double.parseDouble(ppi_app[position]);




        nombre.setText(nombre_apps[position]);
        icono.setImageBitmap(imagen[position]);
        if(resumen_apps[position].contains("."))
        resumen_app.setText(resumen_apps[position].substring(0,resumen_apps[position].indexOf(".")) + ".");
        else
        resumen_app.setText(resumen_apps[position]);

        Log.i("PPI: ",ppi_app[position]);




        if(String.format("%.2f",Double.parseDouble(ppi_app[position])*0.2*100).equals("0,00"))
            ppi.setText("PPI: 10 COIN");
        else
        {
            auxiliar = Double.parseDouble(ppi_app[position])*0.2;
            strAux = String.format("%.2f", auxiliar);
            auxiliar = Double.parseDouble(strAux.replace(",", "."));
            auxiliar=auxiliar*1000;
            finalRound = new Double(auxiliar).intValue();
            ppi.setText("PPI: "+finalRound+" COINS");
        }

        imgEstrellas.setImageResource(imageId[position]);
        botones.setMovementMethod(LinkMovementMethod.getInstance());
        if(uris[position].toString().equals("nolink")){
            botones.setText(context.getResources().getString(R.string.boton_instalada));
            botones.setEnabled(false);
            botones.setBackgroundColor(Color.DKGRAY);
            botones.setTextColor(Color.WHITE);
        }
        else {
            botones.setText(context.getResources().getString(R.string.boton_instalar));
        }


        botones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launcher = new Intent(Intent.ACTION_VIEW,uris[position]);
                String pais = Locale.getDefault().getCountry();
                Date d = new Date();
                String fecha_alta = new SimpleDateFormat("dd-MM-yyyy").format(d);
                try {
                    cod_pago = new GeneradorCodigos().generarCodigos("IN");
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String consulta= (Constantes.REGISTRAR_INSTALACION+
                        "COD_PAGO="+cod_pago+
                        "&MAIL="+correo+
                        "&GIFT="+saldo.intValue()+
                        "&CUENTA="+correo+
                        "&APP_INSTALADA="+nombre_apps[position]+
                        "&PAIS="+pais+
                        "&LINK_PLAYSTORE=NO LINK"+
                        "&LINK_REFERIDO="+uris[position].toString().replace("http://","") +
                        "&FECHA_INSTALACION="+fecha_alta).replace(" ", "%20");
                try {

                        result = new JSONParser(consulta).execute(this,"foo").get();

                    //De mmomento, hasta que no esté hecho el módulo de pagos, estará aquí ubicada la inserción en la TAB_CAJA





                     if(result.contains("{\"success\":1}")){
                        Log.i("INTENT_LAUNCHER: ",launcher.toString());
                        getContext().startActivity(launcher);}
                    else
                        Toast.makeText(context, context.getResources().getString(R.string.dialogo_error_instalacion), Toast.LENGTH_LONG).show();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e("ERROR_CUSTOMADAPTER",e.getMessage());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    Log.e("ERROR_CUSTOMADAPTER",e.getMessage());

                }

            }
        });



        return rowView;
    }




   }