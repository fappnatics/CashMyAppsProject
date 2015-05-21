package com.cashmyapps.core.cashmyappsproject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


public class ListaApps extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private ProgressDialog progressDialog;
    private List<Bitmap> imagenes_apps;
    private CustomAdapter cu;
    private ListView lsApps;
    private String[]nom_apps;
    private Bitmap[]img_apps;
    private String[]desc_corta;
    private String[]ppi_array;
    private Integer[]estrellas;
    private Uri[]url_apps;
    private View rootView;
    private String result="";
    private String result_user="";
    private String codigoPais;
    private List<String> id_app = new ArrayList<>();
    private List<String> nombre_app = new ArrayList<>();
    private List<String> imagen_app = new ArrayList<>();
    private List<String> short_desc_app = new ArrayList<>();
    private List<String> descripcion_app = new ArrayList<>();
    private List<String> url = new ArrayList<>();
    private List<String> its_free = new ArrayList<>();
    private List<String> estrellas_app = new ArrayList<>();
    private List<String> ppi_apps = new ArrayList<>();
    private List<String> currency = new ArrayList<>();
    private List<String> priority_apps = new ArrayList<>();
    private String cuenta="";
    private Context contexto;
    private String cod_refer;
    private String market;
    private JSONObject jObject;
    private Date d = new Date();
    private String fecha;
    private String resultado;
    private JSONArray jsonArray;
    private List<String> lista_apps_instaladas;
    private JSONArray jArray;
    private String nombre;
    private String mail;
    private String refer;
    private String saldo_coins;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_lista_apps, container, false);

        fecha = new SimpleDateFormat("dd-MM-yyyy").format(d);


        try {

            TextView correo = (TextView)getActivity().findViewById(R.id.txCorreo);
            cuenta = correo.getText().toString();

            codigoPais = getLocalizacion();
            contexto = ListaApps.this.getActivity();

            //Obtenemos el codigo de referido del usuario para montar los links con el postback.
                resultado = new JSONParser(Constantes.URL_GET_BBDD_JSON+"?mail="+cuenta).execute(this,"foo").get();
                JSONObject jObject = new JSONObject(resultado);
                JSONArray jArray = jObject.getJSONArray("usuarios");

                cod_refer = jArray.getJSONObject(0).getString("COD_REFER");

                Log.i("CODREFER",cod_refer);



            //Obtenemos las aplicaciones instaladas del usuario, para desactivar el botón Instalar de las que ya tenga registradas.

                resultado = new JSONParser(Constantes.CONTROL_INSTALACIONES+"&MAIL="+cuenta).execute(this,"foo").get();

            if(!resultado.equals("{\"success\":0,\"message\":\"No user apps\"}")) {
                jsonArray = new JSONObject(resultado).getJSONArray("instalaciones");
                lista_apps_instaladas = new ArrayList<>();


                for (int i = 0; i < jsonArray.length(); i++) {
                    lista_apps_instaladas.add(jsonArray.getJSONObject(i).getString("LINK_REFERIDO"));
                }
            }



            market = Constantes.URL_GEENAPP.replace("[PAIS]",codigoPais).replace("[LANG]", Locale.getDefault().getLanguage());
            Log.i("MERCADO: ",market);
            jObject= new JSONObject();
            result = new JSONParser(market).execute(this,"foo").get();

        } catch (InterruptedException e) {
            Log.e("LISTAAPPS",e.getMessage().toString());
            String error = Constantes.ERRORES_APP.replace("[CUENTA]",cuenta).replace("[ERROR]", e.getMessage()).replace("[FECHA",fecha).replace(" ","%20");
            new JSONParser(error).execute(this,"foo");
        } catch (ExecutionException e) {
            Log.e("LISTAAPPS",e.getMessage().toString());
            String error = Constantes.ERRORES_APP.replace("[CUENTA]",cuenta).replace("[ERROR]", e.getMessage()).replace("[FECHA",fecha).replace(" ","%20");
            new JSONParser(error).execute(this,"foo");
        }
        catch (Exception e)
        {
            Log.e("LISTAAPPS",e.getMessage().toString());
            String error = Constantes.ERRORES_APP.replace("[CUENTA]",cuenta).replace("[ERROR]",e.getMessage()).replace("[FECHA",fecha).replace(" ","%20");
            new JSONParser(error).execute(this,"foo");
        }


        try {
            jObject = new JSONObject(result);
        } catch (JSONException e) {
            Log.e("LISTAAPPS",e.getMessage().toString());
            String error = Constantes.ERRORES_APP.replace("[CUENTA]",cuenta).replace("[ERROR]", e.getMessage()).replace("[FECHA",fecha).replace(" ","%20");
            new JSONParser(error).execute(this,"foo");
        }

        int i=0;
        try {
            while(jObject.getJSONObject("content").getJSONObject(i+"")!=null && !jObject.getJSONObject("content").getJSONObject(i+"").equals("")){

                id_app.add(jObject.getJSONObject("content").getJSONObject(i+"").getString("idapp"));
                nombre_app.add(jObject.getJSONObject("content").getJSONObject(i+"").getString("app"));
                imagen_app.add(jObject.getJSONObject("content").getJSONObject(i + "").getString("image"));
                short_desc_app.add(jObject.getJSONObject("content").getJSONObject(i + "").getString("quote"));
                descripcion_app.add(jObject.getJSONObject("content").getJSONObject(i + "").getString("description"));
                url.add(jObject.getJSONObject("content").getJSONObject(i + "").getString("url"));

                if(jObject.getJSONObject("content").getJSONObject(i + "").toString().contains("free"))
                its_free.add(jObject.getJSONObject("content").getJSONObject(i + "").getString("free"));
                else
                its_free.add("0");

                if(jObject.getJSONObject("content").getJSONObject(i + "").toString().contains("stars"))
                estrellas_app.add(jObject.getJSONObject("content").getJSONObject(i + "").getString("stars"));
                else
                estrellas_app.add("1");

                ppi_apps.add(jObject.getJSONObject("content").getJSONObject(i + "").getString("ppi"));
                currency.add(jObject.getJSONObject("content").getJSONObject(i + "").getString("currency"));
                priority_apps.add(jObject.getJSONObject("content").getJSONObject(i + "").getString("priority"));

                i++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Ejecuta la tarea asíncrona para cargar la lista.
        try {

            DescargaImagenes di = new DescargaImagenes(getActivity());
            di.execute(imagen_app);

        }catch(Exception s){
            Log.e("LISTAAPPS",s.getMessage().toString());
            String error = Constantes.ERRORES_APP.replace("[CUENTA]",cuenta).replace("[ERROR]", s.getMessage()).replace("[FECHA",fecha).replace(" ", "%20");
            new JSONParser(error).execute(this,"foo");
        }

        return rootView;
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    public static ListaApps newInstance(int sectionnumber){

        ListaApps frag = new ListaApps();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionnumber);
        frag.setArguments(args);
        return frag;

    }

    private class DescargaImagenes extends AsyncTask<List<String>, Void, List<Bitmap>> {


        private Context context;

        public DescargaImagenes(Context context) {
            this.context = context;

        }


        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(getResources().getString(R.string.dialogo_carga_apps));
            //Código para poner un logo animado. El archivo /drawable/animacion.xml incluye las imágenes a animar.
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.animacion));
            progressDialog.show();
            progressDialog.setCancelable(false);

        }


        protected List<Bitmap> doInBackground(List<String>... urls) {
            List<String> urldisplay = urls[0];
            List<Bitmap> imagenes = new ArrayList<>();
            for (int i = 0; i < urldisplay.size(); i++)
                try {
                    InputStream in = new java.net.URL(urldisplay.get(i)).openStream();
                    imagenes.add(BitmapFactory.decodeStream(in));
                   // guardarImagen(context,i+"",BitmapFactory.decodeStream(in));

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("LISTAAPPS",e.getMessage().toString());
                    String error = Constantes.ERRORES_APP.replace("[CUENTA]",cuenta).replace("[ERROR]",e.getMessage()).replace("[FECHA",fecha).replace(" ","%20");
                    new JSONParser(error).execute(this,"foo");
                }

            return imagenes;
        }

        @Override
        protected void onPostExecute(List<Bitmap> resultado) {
            super.onPostExecute(imagenes_apps);
            imagenes_apps = resultado;

            nom_apps = new String[nombre_app.size()];
            img_apps= new Bitmap[imagenes_apps.size()];
            desc_corta = new String[descripcion_app.size()];
            ppi_array = new String[ppi_apps.size()];
            estrellas = new Integer[estrellas_app.size()];
            url_apps = new Uri[url.size()];


            TextView cuenta_main = (TextView)getActivity().findViewById(R.id.txCorreo);
            try {
                result_user = new JSONParser(Constantes.URL_GET_BBDD_JSON+"?mail="+cuenta_main.getText()).execute(this,"foo").get();

                if(result_user.contains("\"success\":1"))
                {

                    jObject = new JSONObject(result_user);
                    jArray = jObject.getJSONArray("usuarios");
                    nombre = jArray.getJSONObject(0).getString("NOMBRE");
                    mail = jArray.getJSONObject(0).getString("MAIL");
                    saldo_coins = jArray.getJSONObject(0).getString("SALDO")+" Coins";
                    refer = jArray.getJSONObject(0).getString("COD_REFER");


                    TextView txNombre = (TextView)getActivity().findViewById(R.id.txNombre);
                    TextView txSaldo = (TextView)getActivity().findViewById(R.id.txSaldo);

                    txNombre.setText(nombre);
                    txSaldo.setText(saldo_coins);

                    new JSONParser(Constantes.CONEXION_USUARIO.replace("[MAIL]",mail).replace("[CONECTADO]","S"));


                    //Invocamos al servicio
                    Intent in = new Intent(getActivity(),ServicioPostback.class);
                    in.putExtra("cuenta",mail);
                    in.putExtra("cod_refer",refer);

                    if(!ServicioPostback.isRunning())
                        getActivity().startService(in);


                }




            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            for(int s=0;s<nombre_app.size();s++)
            {
                nom_apps[s]=nombre_app.get(s);
                img_apps[s]=imagenes_apps.get(s);
                desc_corta[s]=descripcion_app.get(s);
                ppi_array[s]=ppi_apps.get(s);


               if(lista_apps_instaladas!=null && lista_apps_instaladas.contains(url.get(s).replace("http://",""))) {
                   url_apps[s] = Uri.parse("nolink");
                   Log.i("INSTALADA: ",url.get(s));
               }
                else {
                   url_apps[s] = Uri.parse(url.get(s) + "&gee_postback=" + cod_refer);
                   Log.i("NO_INSTALADA: ",url.get(s));

               }



                Log.i("URLS",url.get(s)+"&gee_postback="+cod_refer);

                if(estrellas_app.get(s).equals("1"))
                    estrellas[s]=R.drawable.unaestrellas;

                if(estrellas_app.get(s).equals("2"))
                    estrellas[s]=R.drawable.dosestrellas;

                if(estrellas_app.get(s).equals("3"))
                    estrellas[s]=R.drawable.tresestrellas;

                if(estrellas_app.get(s).equals("4"))
                    estrellas[s]=R.drawable.cuatroestrellas;

                if(estrellas_app.get(s).equals("5"))
                    estrellas[s]=R.drawable.cincoestrellas;

            }


            //Vamos a ordenar el array por PPI.
            List<Oferta> lista_desordenada = new ArrayList<>();
            List<Oferta> lista_ordenada = new ArrayList<>();
            Oferta of = new Oferta();
            Oferta of_ordenada = new Oferta();

            for(int i=0;i<nom_apps.length;i++)
            {
                of.setNOM_APP(nom_apps[i]);
                of.setDESC(desc_corta[i]);
                of.setESTRELLAS(estrellas[i]);
                of.setIMG(img_apps[i]);
                of.setPPI(ppi_array[i]);
                of.setURL(url_apps[i]);

                lista_desordenada.add(of);
                of = new Oferta();
            }

            Double aux = 0.0;
            int elemento = 0;

            while(lista_desordenada.size()!=0){

                for(int x=0;x<lista_desordenada.size();x++){

                    if(Double.parseDouble(lista_desordenada.get(x).getPPI())>aux) {
                        aux = Double.parseDouble(lista_desordenada.get(x).getPPI());
                        elemento = x;
                    }

                }
                lista_ordenada.add(lista_desordenada.get(elemento));
                lista_desordenada.remove(elemento);
                aux = 0.0;

            }

             nom_apps = new String[lista_ordenada.size()];
             img_apps = new Bitmap[lista_ordenada.size()];
             desc_corta = new String[lista_ordenada.size()];
             ppi_array = new String[lista_ordenada.size()];
             estrellas = new Integer[lista_ordenada.size()];
             url_apps = new Uri[lista_ordenada.size()];

            for(int s=0;s<lista_ordenada.size();s++){
                nom_apps[s] = lista_ordenada.get(s).getNOM_APP();
                img_apps[s] = lista_ordenada.get(s).getIMG();
                desc_corta[s] = lista_ordenada.get(s).getDESC();
                ppi_array[s] = lista_ordenada.get(s).getPPI();
                estrellas[s] = lista_ordenada.get(s).getESTRELLAS();
                url_apps[s] = lista_ordenada.get(s).getURL();
            }
            //FIN de la ordenación.


            ImageView imHeader = new ImageView(getActivity());
            imHeader.setBackgroundColor(Color.parseColor("#fec301"));
            imHeader.setImageResource(R.drawable.offers_header);


            cu = new CustomAdapter(getActivity(),nom_apps,img_apps,desc_corta,ppi_array,estrellas,url_apps,cuenta);
            lsApps = (ListView)rootView.findViewById(R.id.listaAPP);
            lsApps.setAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.abc_fade_in));
            progressDialog.dismiss();
            lsApps.setAdapter(cu);
            lsApps.addHeaderView(imHeader);

            //Invocamos al servicio
            Intent in = new Intent(getActivity(),ServicioPostback.class);
            in.putExtra("cuenta", mail);
            in.putExtra("cod_refer", refer);

            if(!ServicioPostback.isRunning())
                getActivity().startService(in);


        }


        private String guardarImagen (Context context, String nombre, Bitmap imagen){



            File dirImages = context.getDir("imagenes", Context.MODE_PRIVATE);
            File myPath = new File(dirImages, nombre + ".png");

            FileOutputStream fos = null;
            try{
                fos = new FileOutputStream(myPath);
                imagen.compress(Bitmap.CompressFormat.JPEG, 10, fos);
                fos.flush();
            }catch (FileNotFoundException ex){
                ex.printStackTrace();
            }catch (IOException ex){
                ex.printStackTrace();
            }
            return myPath.getAbsolutePath();
        }
    }


    private String getLocalizacion() {
        // Localización del usuario.
        LocationManager localizacion = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean activado_localizacion = localizacion.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        String codigoPais="";

        if (activado_localizacion) {
            Location net_loc = localizacion.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Log.i("COORDENADAS", "Longitud: " + net_loc.getLongitude() + " Latitud: " + net_loc.getLatitude());
            Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());

            try {
                List<Address> direccion = geocoder.getFromLocation(net_loc.getLatitude(), net_loc.getLongitude(), 1);
                Address address = direccion.get(0);
                codigoPais = address.getCountryCode();
                Log.i("PAIS", address.getCountryName());
            } catch (IOException e) {
                e.printStackTrace();
              }
        }
        return codigoPais;
    }

}


