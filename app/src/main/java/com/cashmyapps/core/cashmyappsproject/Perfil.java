package com.cashmyapps.core.cashmyappsproject;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Perfil extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ProgressDialog progressDialog;
    private JSONObject jObject;
    private JSONObject jData;
    private JSONArray jArray;
    private String nombre;
    private String mail;
    private String refer;
    private String saldo_coins;
    private TextView txNombre;
    private TextView txMail ;
    private TextView txPayPal ;
    private TextView txCod_refer;
    private TextView lbMensajeFinal;
    private Button btPayPasl;
    private TextView txRefer;
    private TextView txCambioCorreo;
    private EditText txReferir;
    private Context contexto;
    private TextView txCorreo;
    private TextView txSaldo;
    private Button btReferido;
    private String cuenta_referente;
    private String cuenta_referido;
    private String cuenta_paypal;
    private String cod_refer;
    private String pais;
    private String fecha;
    private AlertDialog.Builder alerta_paypal;
    private AlertDialog.Builder error;
    private SoundPool sp;
    private MediaPlayer mp;




    public Perfil() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        TextView cuenta_main = (TextView)getActivity().findViewById(R.id.txCorreo);
        String prueba = cuenta_main.getText().toString();
        new MainParser(Constantes.URL_GET_BBDD_JSON+"?mail="+cuenta_main.getText()).execute(this,"foo");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }


    public static Perfil newInstance(int sectionnumber){

        Perfil frag = new Perfil();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionnumber);
        frag.setArguments(args);
        return frag;

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
            progressDialog.setMessage(getActivity().getResources().getString(R.string.dialogo_carga_perfil));
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


            try {

                if(v.contains("\"success\":1"))
                {

                    jObject = new JSONObject(v);
                    jArray = jObject.getJSONArray("usuarios");
                    nombre = jArray.getJSONObject(0).getString("NOMBRE");
                    mail = jArray.getJSONObject(0).getString("MAIL");
                    saldo_coins = jArray.getJSONObject(0).getString("SALDO")+" Coins";
                    refer = jArray.getJSONObject(0).getString("COD_REFER");
                    cuenta_paypal = jArray.getJSONObject(0).getString("MAIL_PAYPAL");
                    lbMensajeFinal = (TextView)getActivity().findViewById(R.id.lbMensajeFinal);

                    TextView txNombre = (TextView)getActivity().findViewById(R.id.txNombre);
                    txSaldo = (TextView)getActivity().findViewById(R.id.txSaldo);
                    txPayPal = (TextView)getActivity().findViewById(R.id.txPayPal);

                    txNombre.setText(nombre);
                    txSaldo.setText(saldo_coins);
                    txPayPal.setText(cuenta_paypal);

                    //new JSONParser(Constantes.CONEXION_USUARIO.replace("[MAIL]",mail).replace("[CONECTADO]","S"));

                    txCorreo = (TextView)getActivity().findViewById(R.id.txCorreo);
                    fecha = new Fechas().getFechaActual();
                    cuenta_referido = txCorreo.getText().toString();
                    txRefer = (TextView)getActivity().findViewById(R.id.txReferido);
                    txReferir = (EditText)getActivity().findViewById(R.id.txReferir);
                    btReferido = (Button)getActivity().findViewById(R.id.btReferidoP);
                    btPayPasl = (Button)getActivity().findViewById(R.id.btPayPal);
                    String resultado = new JSONParser(Constantes.URL_GET_BBDD_JSON+"?mail="+txCorreo.getText()).execute(this,"foo").get();
                    JSONObject jObject = new JSONObject(resultado);
                    JSONArray jArray = jObject.getJSONArray("usuarios");

                    txRefer.setText(jArray.getJSONObject(0).getString("COD_REFER"));

                    pais = jArray.getJSONObject(0).getString("PAIS");

                    if(!jArray.getJSONObject(0).getString("REFERIDO_POR").equals("") && jArray.getJSONObject(0).getString("REFERIDO_POR")!=null){

                        txReferir.setText(jArray.getJSONObject(0).getString("REFERIDO_POR"));
                        btReferido.setVisibility(View.INVISIBLE);
                        txReferir.setEnabled(false);
                        txReferir.setBackgroundColor(Color.DKGRAY);
                        txReferir.setTextColor(Color.LTGRAY);

                    }

                    btReferido.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            cod_refer = txRefer.getText().toString();

                            if (!cod_refer.equals(txReferir.getText().toString()) && txReferir.getText().toString() != null && !txReferir.getText().toString().equals("")) {

                                new GetCodRefer(Constantes.GET_CODREFER_EXISTE.replace("[MAIL]", cuenta_referido).replace("[COD_REFER]", txReferir.getText().toString())).execute();

                                }
                        }
                    });


                btPayPasl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LayoutInflater paypal = LayoutInflater.from(getActivity());
                        final View view = paypal.inflate(R.layout.alerta_paypal_cambio,null);
                        txCambioCorreo = (TextView)view.findViewById(R.id.txNuevoCorreo);
                        txCambioCorreo.setText(txPayPal.getText());
                        alerta_paypal = new AlertDialog.Builder(getActivity());
                        alerta_paypal.setView(view);
                        alerta_paypal.setPositiveButton(getActivity().getResources().getString(R.string.boton_confirmar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                new JSONParser(Constantes.SET_CUENTA_PAYPAL.replace("[MAIL]",cuenta_referido).replace("[PAYPAL]",txPayPal.getText())).execute();


                                LayoutInflater factory = LayoutInflater.from(getActivity());
                                final View view = factory.inflate(R.layout.alerta_icono,null);
                                AlertDialog.Builder alerta = new AlertDialog.Builder(getActivity());
                                alerta.setView(view);
                                alerta.setMessage(getResources().getString(R.string.layout_cambio_correopaypal_realizado));

                                alerta.setPositiveButton(getResources().getString(R.string.boton_aceptar), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                alerta.show();

                            }
                        });
                        alerta_paypal.setNegativeButton(getActivity().getResources().getString(R.string.dialogo_cancelar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        if(isValidEmail(txPayPal.getText()))
                            alerta_paypal.show();

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

                }

            }catch (Exception s){
                Log.i("Error carga: ",s.getMessage());
            }


            contexto = getActivity().getApplicationContext();
            txNombre = (TextView)getActivity().findViewById(R.id.txUENombre);
            txMail = (TextView)getActivity().findViewById(R.id.txUEcorreo);
            txCod_refer = (TextView)getActivity().findViewById(R.id.txUERefer);


            txNombre.setGravity(Gravity.CENTER);
            txMail.setGravity(Gravity.CENTER);


            txNombre.setText(nombre);
            txMail.setText(mail);


            progressDialog.dismiss();


        }



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

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getActivity().getResources().getString(R.string.dialogo_solicitud_cobro_procesando));
            progressDialog.show();

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

                if(res.contains("\"success\":1}")) {

                    JSONObject jObject2 = new JSONObject(res);
                    JSONArray jArray2 = jObject2.getJSONArray("usuarios");
                    cuenta_referente = jArray2.getJSONObject(0).getString("MAIL");

                    //Se da de alta en la tabla TAB_CAJA
                    new JSONParser(Constantes.PAGAR_REFERIDO.replace("[REFERENTE]", cuenta_referente).replace("[REFERIDO]", cuenta_referido).replace("[FECHA]", new Fechas().getFechaActual()).replace("[COD_PAGO]", new GeneradorCodigos().generarCodigos("RF")).replace("[COD_REFERIDO]", cod_refer).replace("[COD_REFERENTE]", txReferir.getText())).execute(this, "foo");

                   }

                if(res.contains("\"success\":1}")) {
                    progressDialog.dismiss();
                    btReferido.setVisibility(View.INVISIBLE);
                    txReferir.setEnabled(false);
                    txReferir.setBackgroundColor(Color.DKGRAY);
                    txReferir.setTextColor(Color.LTGRAY);
                    txSaldo.setText(Integer.parseInt(txSaldo.getText().toString().substring(0, txSaldo.getText().toString().indexOf(" "))) + 200 + " coins");
                    //Sonido de éxito
                    mp = MediaPlayer.create(getActivity(),R.raw.success);
                    mp.start();

                    lbMensajeFinal.setVisibility(View.VISIBLE);
                    lbMensajeFinal.setText(getResources().getString(R.string.dialogo_peticion_refer_ok));
                    lbMensajeFinal.setTextColor(Color.WHITE);



                }
                else{
                    progressDialog.dismiss();
                    lbMensajeFinal.setVisibility(View.VISIBLE);
                    lbMensajeFinal.setTextColor(Color.RED);
                    lbMensajeFinal.setText(getResources().getString(R.string.dialogo_peticion_refer_ko));
                    //Sonido de éxito
                    mp = MediaPlayer.create(getActivity(),R.raw.error);
                    mp.start();

                }


            } catch (Exception e) {
                e.printStackTrace();

            }


        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }



}
