package com.cashmyapps.core.cashmyappsproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class Beneficios extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private  Button btSolicitarIngresos;
    private ProgressDialog progressDialog;
    private  Button btAmazon5;
    private  Button btAmazon2;
    private  Button btPaypal5;
    private View rootView;
    private String cuenta;
    private String resultado;
    private String saldo;
    private String correo_paypal;
    private TextView saldo_user;
    private TextView correo;
    private TextView cuenta_paypal;
    private JSONObject jObject;
    private JSONArray jArray;





    public Beneficios() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     rootView = inflater.inflate(R.layout.fragment_beneficios,container,false);




        return inflater.inflate(R.layout.fragment_beneficios, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();

        btSolicitarIngresos = (Button)getActivity().findViewById(R.id.btSolicitarBeneficios);
        btAmazon5 = (Button)getActivity().findViewById(R.id.btAmazon5);
        btAmazon2 = (Button)getActivity().findViewById(R.id.btAmazon2);
        btPaypal5 = (Button)getActivity().findViewById(R.id.btPayPal5);
        correo = (TextView)getActivity().findViewById(R.id.txCorreo);
        saldo_user = (TextView)getActivity().findViewById(R.id.txCobroSaldo);
        cuenta = correo.getText().toString();

        try {
            resultado = new JSONParser(Constantes.GET_SALDO.replace("[MAIL]",cuenta)).execute(this,"foo").get();
            jArray = new JSONObject(resultado).getJSONArray("usuarios");
            saldo = jArray.getJSONObject(0).getString("SALDO");
            saldo_user.setText(saldo+" coins");

            if(Integer.parseInt(saldo)<5000){

                btAmazon5.setEnabled(false);
                btPaypal5.setEnabled(false);

            }

            if(Integer.parseInt(saldo)<2000){

                btAmazon2.setEnabled(false);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        btAmazon5.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                try {
                    resultado = new JSONParser(Constantes.GET_CUENTA_PAYPAL.replace("[MAIL]",cuenta)).execute(this,"foo").get();
                    LayoutInflater paypal = LayoutInflater.from(getActivity());
                    final View view = paypal.inflate(R.layout.calificar,null);
                    AlertDialog.Builder alerta_paypal = new AlertDialog.Builder(getActivity());
                    cuenta_paypal = (TextView)view.findViewById(R.id.txPayPal);
                    jObject = new JSONObject(resultado);
                    jArray = jObject.getJSONArray("usuarios");
                    //cuenta_paypal.setText(jArray.getJSONObject(0).getString("MAIL_PAYPAL"));
                    alerta_paypal.setView(view);

                    alerta_paypal.setPositiveButton(getResources().getString(R.string.boton_confirmar), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {



                        }
                    });

                    alerta_paypal.setNegativeButton(getResources().getString(R.string.dialogo_cancelar), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });



                    alerta_paypal.show();





                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        btAmazon2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

            }
        });

        btPaypal5.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

            }
        });


        btSolicitarIngresos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View view = factory.inflate(R.layout.alerta_icono,null);
                AlertDialog.Builder alerta = new AlertDialog.Builder(getActivity());
                alerta.setView(view);

                try {
                    resultado = new JSONParser(Constantes.SOLICITAR_COBRO.replace("[MAIL]",cuenta).replace("[COINS]",saldo)).execute(this,"foo").get();


                    if(resultado.contains("{\"success\":1}")){

                        resultado = new JSONParser(Constantes.INSERTAR_SOLICITAR_COBRO.replace("[MAIL]",cuenta).replace("[COINS]",saldo).replace("[FECHA]",new Fechas().getFechaActual()).replace("[PAGADO]","N")).execute(this,"foo").get();
                        Log.i("SOLICITUD: ",resultado);
                        if(resultado.contains("{\"success\":1}")){
                        alerta.setTitle(getResources().getString(R.string.dialogo_solicitud_cobro_aceptada_titulo));
                        alerta.setMessage(getResources().getString(R.string.dialogo_solicitud_cobro_aceptada));

                            alerta.setPositiveButton(getResources().getString(R.string.boton_aceptar),new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        alerta.show();
                        }
                        else{
                            alerta.setTitle(getResources().getString(R.string.dialogo_solicitud_cobro_rechazada_titulo));
                            alerta.setMessage(getResources().getString(R.string.dialogo_solicitud_cobro_rechazada));

                            alerta.setPositiveButton(getResources().getString(R.string.boton_aceptar),new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            alerta.show();
                        }
                    }
                    else{
                        alerta.setTitle(getResources().getString(R.string.dialogo_solicitud_cobro_rechazada_titulo));
                        alerta.setMessage(getResources().getString(R.string.dialogo_solicitud_cobro_rechazada));

                        alerta.setPositiveButton(getResources().getString(R.string.boton_aceptar),new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        alerta.show();
                    }

                    } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    private class MainParser extends AsyncTask<Object, Void, String> {

        String result = "";
        private String url_select;

        public MainParser(String url) {
            this.url_select = url;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getActivity().getResources().getString(R.string.dialogo_solicitud_cobro_procesando));
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Object... params) {
            ArrayList<NameValuePair> param = new ArrayList<>();

            try {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url_select);
                httpPost.setEntity(new UrlEncodedFormEntity(param));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");


            } catch (Exception e) {
                Log.e("Error  result ", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String v) {

            //TODO aquí hay que hacer el descuento del saldo y el mensaje que confirma la transacción.








        }
    }







            @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


    }


    public static Beneficios newInstance(int sectionnumber){

        Beneficios frag = new Beneficios();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionnumber);
        frag.setArguments(args);
        return frag;

    }


}
