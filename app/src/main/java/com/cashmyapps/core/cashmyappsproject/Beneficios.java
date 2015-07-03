package com.cashmyapps.core.cashmyappsproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

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

import java.io.InputStream;
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
    private TextView txSaldo;
    private JSONObject jObject;
    private JSONArray jArray;
    private RatingBar ratingBar;
    private AlertDialog.Builder alerta_paypal;
    private AlertDialog.Builder error;
    private int tipo_pago;





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
        txSaldo = (TextView)getActivity().findViewById(R.id.txSaldo);
        saldo_user = (TextView)getActivity().findViewById(R.id.txCobroSaldo);
        cuenta = correo.getText().toString();

        try {
            resultado = new JSONParser(Constantes.GET_SALDO.replace("[MAIL]",cuenta)).execute(this,"foo").get();
            jArray = new JSONObject(resultado).getJSONArray("usuarios");
            saldo = jArray.getJSONObject(0).getString("SALDO");
            saldo_user.setText(saldo+" coins");
            txSaldo.setText(saldo);

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


        //Boton para el cobro de gift Amazon por valor de 5000 coins. Es el tipo 1 de nuestra BBDD
        btAmazon5.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                try {
                    resultado = new JSONParser(Constantes.GET_CUENTA_PAYPAL.replace("[MAIL]",cuenta)).execute(this,"foo").get();
                    LayoutInflater paypal = LayoutInflater.from(getActivity());
                    final View view = paypal.inflate(R.layout.alerta_paypal,null);
                    alerta_paypal = new AlertDialog.Builder(getActivity());
                    cuenta_paypal = (TextView)view.findViewById(R.id.txPayPal);
                    jObject = new JSONObject(resultado);
                    jArray = jObject.getJSONArray("usuarios");
                    cuenta_paypal.setText(jArray.getJSONObject(0).getString("MAIL_PAYPAL"));
                    correo_paypal = jArray.getJSONObject(0).getString("MAIL_PAYPAL");
                    alerta_paypal.setView(view);
                    alerta_paypal.setPositiveButton(getResources().getString(R.string.boton_confirmar), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                                if(isValidEmail(cuenta_paypal.getText())) {
                                    new SolicitudCobro(Constantes.SET_DESCONTAR_SALDO.replace("[MAIL]", cuenta).
                                            replace("[COINS]", "5000").
                                            replace("[FECHA]", new Fechas().getFechaActual()).
                                            replace("[TIPO]", "1")).execute();

                                    try {
                                        new EnviarMail(Constantes.SET_CUENTA_PAYPAL.replace("[MAIL]",cuenta).replace("[PAYPAL]",cuenta_paypal.getText())).execute().get();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    }

                                }
                                else {
                                    error = new AlertDialog.Builder(getActivity());
                                    error.setMessage("El correo no es correcto");
                                    error.setTitle("Error");
                                    error.setPositiveButton(getResources().getString(R.string.boton_aceptar), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    error.setIcon(R.drawable.error32);

                                    error.show();

                                }


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

                tipo_pago = 1;
            }
        });

        btAmazon2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                try {
                    resultado = new JSONParser(Constantes.GET_CUENTA_PAYPAL.replace("[MAIL]",cuenta)).execute(this,"foo").get();
                    LayoutInflater paypal = LayoutInflater.from(getActivity());
                    final View view = paypal.inflate(R.layout.alerta_paypal,null);
                    alerta_paypal = new AlertDialog.Builder(getActivity());
                    cuenta_paypal = (TextView)view.findViewById(R.id.txPayPal);
                    jObject = new JSONObject(resultado);
                    jArray = jObject.getJSONArray("usuarios");
                    cuenta_paypal.setText(jArray.getJSONObject(0).getString("MAIL_PAYPAL"));
                    correo_paypal = jArray.getJSONObject(0).getString("MAIL_PAYPAL");
                    alerta_paypal.setView(view);
                    alerta_paypal.setPositiveButton(getResources().getString(R.string.boton_confirmar), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(isValidEmail(cuenta_paypal.getText())) {
                                new SolicitudCobro(Constantes.SET_DESCONTAR_SALDO.replace("[MAIL]", cuenta).
                                        replace("[COINS]", "2000").
                                        replace("[FECHA]", new Fechas().getFechaActual()).
                                        replace("[TIPO]", "2")).execute();

                                try {
                                    new EnviarMail(Constantes.SET_CUENTA_PAYPAL.replace("[MAIL]",cuenta).replace("[PAYPAL]",cuenta_paypal.getText())).execute().get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }

                            }
                            else {
                                error = new AlertDialog.Builder(getActivity());
                                error.setMessage("El correo no es correcto");
                                error.setTitle("Error");
                                error.setPositiveButton(getResources().getString(R.string.boton_aceptar), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                error.setIcon(R.drawable.error32);

                                error.show();

                            }


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

                tipo_pago=2;

            }
        });

        btPaypal5.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                try {
                    resultado = new JSONParser(Constantes.GET_CUENTA_PAYPAL.replace("[MAIL]",cuenta)).execute(this,"foo").get();
                    LayoutInflater paypal = LayoutInflater.from(getActivity());
                    final View view = paypal.inflate(R.layout.alerta_paypal,null);
                    alerta_paypal = new AlertDialog.Builder(getActivity());
                    cuenta_paypal = (TextView)view.findViewById(R.id.txPayPal);
                    jObject = new JSONObject(resultado);
                    jArray = jObject.getJSONArray("usuarios");
                    cuenta_paypal.setText(jArray.getJSONObject(0).getString("MAIL_PAYPAL"));
                    correo_paypal = jArray.getJSONObject(0).getString("MAIL_PAYPAL");
                    alerta_paypal.setView(view);
                    alerta_paypal.setPositiveButton(getResources().getString(R.string.boton_confirmar), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(isValidEmail(cuenta_paypal.getText())) {
                                new SolicitudCobro(Constantes.SET_DESCONTAR_SALDO.replace("[MAIL]", cuenta).
                                        replace("[COINS]", "5000").
                                        replace("[FECHA]", new Fechas().getFechaActual()).
                                        replace("[TIPO]", "3")).execute();

                                try {
                                    new EnviarMail(Constantes.SET_CUENTA_PAYPAL.replace("[MAIL]",cuenta).replace("[PAYPAL]",cuenta_paypal.getText())).execute().get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }

                            }
                            else {
                                error = new AlertDialog.Builder(getActivity());
                                error.setMessage("El correo no es correcto");
                                error.setTitle("Error");
                                error.setPositiveButton(getResources().getString(R.string.boton_aceptar), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                error.setIcon(R.drawable.error32);

                                error.show();

                            }


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

                tipo_pago=3;

            }
        });

    }

    private class SolicitudCobro extends AsyncTask<Object, Void, String> {

        String result = "";
        private String url_select;

        public SolicitudCobro(String url) {
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


            if(tipo_pago==1)
                    new EnviarMail(Constantes.SOLICITAR_COBRO.replace("[MAIL]",cuenta).replace("[TIPO]","Amazon5")).execute();
            if(tipo_pago==2)
                new EnviarMail(Constantes.SOLICITAR_COBRO.replace("[MAIL]",cuenta).replace("[TIPO]","Amazon2")).execute();
            if(tipo_pago==3)
                    new EnviarMail(Constantes.SOLICITAR_COBRO.replace("[MAIL]",cuenta).replace("[TIPO]","PayPal5")).execute();


            onResume();
            progressDialog.dismiss();

            LayoutInflater factory = LayoutInflater.from(getActivity());
            final View view = factory.inflate(R.layout.alerta_icono,null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(getActivity());
            alerta.setView(view);
            if(v.contains("{\"success\":1}")) {
                alerta.setTitle(getResources().getString(R.string.dialogo_solicitud_cobro_aceptada_titulo));
                alerta.setMessage(getResources().getString(R.string.dialogo_solicitud_cobro_aceptada));

                alerta.setPositiveButton(getResources().getString(R.string.boton_aceptar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alerta.show();

            }

        }
    }

    private class EnviarMail extends AsyncTask<Object, Void, String> {

        //
        InputStream inputStream = null;
        String result = "";
        ProgressDialog progressDialog;
        Context context;
        private String url_select;
        public EnviarMail(String url){
            this.url_select= url;
        }


      @Override
    protected void onPreExecute() {
    }


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
                result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");


            } catch (Exception e) {
                Log.e("Error  result ", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String  v){


        }


    }


    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }




            @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


    }


    public static Beneficios newInstance(int sectionnumber){

        Beneficios frag = new Beneficios();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionnumber);
        frag.setArguments(args);
        return frag;

    }


}
