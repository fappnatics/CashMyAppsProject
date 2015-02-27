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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class Compartir extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private TextView txRefer;
    private Context contexto;


    public Compartir() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_compartir, container, false);


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compartir, container, false);

    }

    @Override
    public void onResume(){
        super.onResume();

        TextView txCorreo = (TextView)getActivity().findViewById(R.id.txCorreo);

        txRefer = (TextView)getActivity().findViewById(R.id.txReferido);
        contexto = Compartir.this.getActivity();
        try {
            String resultado = new GetCodRefer(Constantes.URL_GET_BBDD_JSON+"?mail="+txCorreo.getText()).execute(this,"foo").get();
           JSONObject jObject = new JSONObject(resultado);
           JSONArray jArray = jObject.getJSONArray("usuarios");

           txRefer.setText(jArray.getJSONObject(0).getString("COD_REFER"));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


    }


    public static Compartir newInstance(int sectionnumber){

        Compartir frag = new Compartir();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionnumber);
        frag.setArguments(args);
        return frag;

    }

    public class GetCodRefer extends AsyncTask<Object, Void, String> {

        //
        InputStream inputStream = null;
        String result = "";
        ProgressDialog progressDialog;
        private String url_select;

        public GetCodRefer(String url) {
            this.url_select = url;
        }


  /*    @Override
    protected void onPreExecute() {

        progressDialog = new ProgressDialog(contexto);
        progressDialog.setMessage("Downloading your data...");
        progressDialog.show();
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface arg0) {
                GetCodRefer.this.cancel(true);
            }
        });
    }*/


        @Override
        protected String doInBackground(Object... params) {

            //String url_select = "http://offer.geenapptool.com/155/?device=android&country=ES&lang=es";
            //url_select = "http://offer.geenapptool.com/162/?device=android&country=ES&lang=es";

            ArrayList<NameValuePair> param = new ArrayList<>();

            try {

                // HttpClient is more then less deprecated. Need to change to URLConnection
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url_select);
                httpPost.setEntity(new UrlEncodedFormEntity(param));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");


            } catch (Exception e) {
                Log.e("Error  result ", e.getMessage());
            }
            return result;
        }

      /*  @Override
        protected void onPostExecute(String v) {
            super.onPostExecute(v);


            JSONObject jObject = null;
            try {
                jObject = new JSONObject(v);
                JSONArray jArray = jObject.getJSONArray("usuarios");
                txRefer.setText(jArray.getJSONObject(0).getString("COD_REFER"));
                progressDialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }*/
    }

}
