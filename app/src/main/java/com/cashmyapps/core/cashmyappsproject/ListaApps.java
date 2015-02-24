package com.cashmyapps.core.cashmyappsproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class ListaApps extends Fragment {

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_apps);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prueba_lista, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_lista_apps, container, false);
        TextView correo = (TextView)getActivity().findViewById(R.id.txCorreo);
        cuenta = correo.getText().toString();



        JSONObject jObject= new JSONObject();

        try {
            result = new JSONParser(Constantes.URL_GEENAPP).execute(this,"foo").get();
        } catch (InterruptedException e) {
            Log.e("LISTAAPPS",e.getMessage().toString());
        } catch (ExecutionException e) {
            Log.e("LISTAAPPS",e.getMessage().toString());
        }

        try {
            jObject = new JSONObject(result);
        } catch (JSONException e) {
            Log.e("LISTAAPPS",e.getMessage().toString());
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

        }catch(Exception s){}

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

                } catch (Exception e) {
                    e.printStackTrace();
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
    }


}


