package com.cashmyapps.core.cashmyappsproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.EditText;
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
import java.util.Random;
import java.util.concurrent.ExecutionException;


public class Compartir extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private TextView txRefer;
    private EditText txReferir;
    private Context contexto;
    private TextView txCorreo;
    private Button btReferido;
    private String cuenta_referente;
    private String cuenta_referido;
    private String cod_refer;
    private String pais;
    private String fecha;


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



        txCorreo = (TextView)getActivity().findViewById(R.id.txCorreo);
        fecha = new Fechas().getFechaActual();
        cuenta_referido = txCorreo.getText().toString();
        txRefer = (TextView)getActivity().findViewById(R.id.txReferido);
        txReferir = (EditText)getActivity().findViewById(R.id.txReferir);
        btReferido = (Button)getActivity().findViewById(R.id.btReferido);

        contexto = Compartir.this.getActivity();
        try {


            String resultado = new JSONParser(Constantes.URL_GET_BBDD_JSON+"?mail="+txCorreo.getText()).execute(this,"foo").get();
           JSONObject jObject = new JSONObject(resultado);
           JSONArray jArray = jObject.getJSONArray("usuarios");

           txRefer.setText(jArray.getJSONObject(0).getString("COD_REFER"));
            pais = jArray.getJSONObject(0).getString("PAIS");

           if(!jArray.getJSONObject(0).getString("REFERIDO_POR").equals("") && jArray.getJSONObject(0).getString("REFERIDO_POR")!=null){

               txReferir.setText(jArray.getJSONObject(0).getString("REFERIDO_POR"));
               btReferido.setEnabled(false);
               btReferido.setText(" Referido ");
               btReferido.setBackgroundColor(Color.rgb(138, 138, 138));
               btReferido.setTextColor(Color.DKGRAY);
               txReferir.setEnabled(false);
               txReferir.setBackgroundColor(Color.DKGRAY);
               txReferir.setTextColor(Color.LTGRAY);

           }

            btReferido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    cod_refer = txRefer.getText().toString();

                    if(!cod_refer.equals(txReferir.getText().toString()) && txReferir.getText().toString()!=null && !txReferir.getText().toString().equals("") ){

                      new GetCodRefer(Constantes.GET_CODREFER_EXISTE.replace("[MAIL]",cuenta_referido).replace("[COD_REFER]",txReferir.getText().toString())).execute();

                    }
                    else{
                        Toast.makeText(contexto,"El texto no es correcto, revíselo e inténtelo de nuevo",Toast.LENGTH_LONG).show();

                    }


                }
            });



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


    @Override
    protected void onPreExecute() {

        progressDialog = new ProgressDialog(contexto);
        progressDialog.setMessage("Procesando petición...");
        progressDialog.show();
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface arg0) {
                GetCodRefer.this.cancel(true);
            }
        });
    }


        @Override
        protected String doInBackground(Object... params) {



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

        @Override
        protected void onPostExecute(String v) {
            super.onPostExecute(v);


            JSONObject jObject = null;
            try {



                String res = new JSONParser(Constantes.GET_CUENTA_REFERENTE.replace("[COD_REFER]",txReferir.getText())).execute(this,"foo").get();
                JSONObject jObject2 = new JSONObject(res);
                JSONArray jArray2 = jObject2.getJSONArray("usuarios");
                cuenta_referente = jArray2.getJSONObject(0).getString("MAIL");



                String ok = new JSONParser(Constantes.PAGAR_REFERIDO.replace("[REFERENTE]",cuenta_referente)
                                                                    .replace("[REFERIDO]",cuenta_referido)
                                                                    .replace("[FECHA]",new Fechas().getFechaActual())
                                                                    .replace("[COD_PAGO]",generarCodigos())).execute(this,"foo").get();

                Thread.sleep(2000);
                progressDialog.dismiss();

                LayoutInflater factory = LayoutInflater.from(contexto);
                final View view = factory.inflate(R.layout.alerta_icono,null);
                AlertDialog.Builder alerta = new AlertDialog.Builder(contexto);
                alerta.setView(view);


                alerta.setTitle("Salir");
                alerta.setMessage("La petición se ha procesado correctamente.");

                alerta.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       return;
                    }
                });

                if(v.equals("{\"success\":1}")){
                    btReferido.setEnabled(false);
                    btReferido.setText("Referido");
                    btReferido.setBackgroundColor(Color.DKGRAY);
                    txReferir.setEnabled(false);
                    txReferir.setBackgroundColor(Color.DKGRAY);
                    txReferir.setTextColor(Color.LTGRAY);
                }



                alerta.show();


            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }


        }
    }

    private String generarCodigos() throws ExecutionException, InterruptedException {

        Boolean code = true;
        String output="";

        while(code) {
            char[] chars = "0123456789".toCharArray();
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 8; i++) {
                char c = chars[random.nextInt(chars.length)];
                sb.append(c);
            }
            output = "RF-" + sb.toString();
            String result = new JSONParser(Constantes.COMPROBAR_COD_PAGO.replace("[COD_PAGO]",output)).execute(this,"foo").get();

            if(result.contains("LIBRE")){
                code=false;
            }

        }



        return output;
    }



}
