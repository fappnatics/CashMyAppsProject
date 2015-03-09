package com.cashmyapps.core.cashmyappsproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.net.URI;
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
    private JSONArray jArray;

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

                String resultado = new JSONParser(Constantes.URL_GET_BBDD_JSON+"?mail="+cuenta).execute(this,"foo").get();
                JSONObject jObject = new JSONObject(resultado);
                JSONArray jArray = jObject.getJSONArray("usuarios");

                cod_refer = jArray.getJSONObject(0).getString("COD_REFER");

                Log.i("CODREFER",cod_refer);



            market = Constantes.URL_GEENAPP2.replace("[PAIS]",codigoPais).replace("[LANG]", Locale.getDefault().getLanguage());

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
            progressDialog.setMessage("Cargando lista de ofertas");
            //Código para poner un logo animado. El archivo /drawable/animacion.xml incluye las imágenes a animar.
            /*progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.animacion));*/
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
                    guardarImagen(context,i+"",BitmapFactory.decodeStream(in));

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



            for(int s=0;s<nombre_app.size();s++)
            {
                nom_apps[s]=nombre_app.get(s);
                img_apps[s]=imagenes_apps.get(s);
                desc_corta[s]=descripcion_app.get(s);
                ppi_array[s]=ppi_apps.get(s);
                //ppi_array[s]="PPI: Unknown";
                url_apps[s]= Uri.parse(url.get(s));
                Log.i("URLS",url.get(s)+"?gee_postback="+cod_refer);

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


            cu = new CustomAdapter(getActivity(),nom_apps,img_apps,desc_corta,ppi_array,estrellas,url_apps,cuenta);
            lsApps = (ListView)rootView.findViewById(R.id.listaAPP);
            lsApps.setAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.abc_fade_in));
            progressDialog.dismiss();
            lsApps.setAdapter(cu);


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


