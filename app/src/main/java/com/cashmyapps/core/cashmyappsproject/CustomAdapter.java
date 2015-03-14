package com.cashmyapps.core.cashmyappsproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
        resumen_app.setText(resumen_apps[position]);

        if(String.format("%.2f",Double.parseDouble(ppi_app[position])*0.1).equals("0,00"))
            ppi.setText("PPI: 0,01$");
        else
            ppi.setText("PPI: "+ String.format("%.2f",Double.parseDouble(ppi_app[position])*0.1*100)+"$");

        imgEstrellas.setImageResource(imageId[position]);
        botones.setMovementMethod(LinkMovementMethod.getInstance());
        botones.setText("Instalar");


        botones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launcher = new Intent(Intent.ACTION_VIEW,uris[position]);
                String pais = Locale.getDefault().getCountry();
                Date d = new Date();
                String fecha_alta = new SimpleDateFormat("dd-MM-yyyy").format(d);
                String consulta= (Constantes.PAGAR_RECOMPENSA+"MAIL="+correo+"&GIFT="+String.format("%.2f",Double.parseDouble(ppi_app[position])*0.1).replace(",",".")

                        +"&CUENTA="+correo+"&APP_INSTALADA="+nombre_apps[position]+"&PAIS="+pais+"&LINK_PLAYSTORE=NO LINK"+"&LINK_REFERIDO="+uris[position].toString().replace("http://","") +"&FECHA_INSTALACION="+fecha_alta).replace(" ","%20");
                try {



                   String control = new JSONParser(Constantes.CONTROL_INSTALACIONES+"&MAIL="+correo).execute(this,"foo").get();

                    //Si no existen instalaciones del usuario, directamente le permitimos instalar.
                    if(!control.contains("No user apps")){
                                JSONArray jsonArray = new JSONObject(control).getJSONArray("instalaciones");
                                lista_apps = new ArrayList<String>();

                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    lista_apps.add(jsonArray.getJSONObject(i).getString("LINK_REFERIDO"));
                                }

                                if( lista_apps.contains(uris[position].toString().replace("http://","")))
                                {

                                    AlertDialog.Builder alerta = new AlertDialog.Builder(context);

                                    alerta.setTitle("Atención");
                                    alerta.setMessage("Esta aplicación ya ha sido instalada");

                                    alerta.setNegativeButton("Aceptar",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            return;
                                        }
                                    });

                                    alerta.show();
                                    return;
                                }
                                else
                                {
                                    result = new JSONParser(consulta).execute(this,"foo").get();
                                }

                    }
                    else
                    {
                        result = new JSONParser(consulta).execute(this,"foo").get();
                    }





                    //TODO recuperar lista de instalaciones para impedir que instala una app 2 veces.
                    //TODO implementar el postback para no pagar inmediatamente las recompensas.


                    if(result.contains("{\"success\":1}"))
                        getContext().startActivity(launcher);
                    else
                        Toast.makeText(context, "Oooops, ha sucedido un error, por favor, inténtalo de nuevo más tarde", Toast.LENGTH_LONG);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e("ERROR_CUSTOMADAPTER",e.getMessage());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    Log.e("ERROR_CUSTOMADAPTER",e.getMessage());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



        return rowView;
    }
}