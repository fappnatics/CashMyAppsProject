package com.cashmyapps.core.cashmyappsproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;




public class Login extends ActionBarActivity {


    private Spinner spinner;
    private Button btAceptar;
    private EditText etNombre;
    private TextView titulo;
    private ProgressDialog pd;
    private String cuenta;
    private String nombre;
    private String saldo;
    private String fecha_alta;
    private String cuenta_seleccionada="";
    private Context context;
    private String consulta;
    private String cod_refer;
    private Intent intent;
    private List<String> cuentas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();
        cuentas = intent.getExtras().getStringArrayList("cuentas");


        try {
            getSupportActionBar().hide();
            setContentView(R.layout.activity_login);
            spinner = (Spinner) findViewById(R.id.spinner_cuentas);

            titulo = (TextView) findViewById(R.id.txTituloSelec);
            titulo.setText(getResources().getString(R.string.cont_login_cabecera_nuevo_cashmaker));


            String[] arraySpinner = cuentas.toArray(new String[cuentas.size()]);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getApplicationContext(),
                    R.layout.elemento_spinner, arraySpinner);
            spinner.setAdapter(adapter);

            btAceptar = (Button) findViewById(R.id.btAceptar);
            btAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                        Date d = new Date();
                        etNombre = (EditText)findViewById(R.id.etNombre);
                        cuenta = spinner.getSelectedItem().toString();
                        //Si el nombre es nulo, le ponemos su correo sin dominio.
                        if(etNombre.getText().toString().equals("") || etNombre.getText().toString() == null)
                            nombre = cuenta.substring(0,cuenta.indexOf("@"));
                        else
                            nombre = etNombre.getText().toString().replace(" ", "%20");



                        fecha_alta = new SimpleDateFormat("dd-MM-yyyy").format(d);
                        Log.i("FECHA: ", fecha_alta);
                        cod_refer = generarReferido();
                        //TODO Hay que consultar la base de datos para evitar códigos duplicados.
                        consulta = Constantes.ALTA_USUARIO + "NOMBRE=" + nombre + "&MAIL=" + cuenta + "&SALDO=0&FECHA_ALTA=" + fecha_alta + "&PAIS=" + getLocalizacion() + "&ESTADO_CUENTA=A" + "&COD_REFER=" + cod_refer;
                        cuenta_seleccionada = spinner.getSelectedItem().toString();
                        AltaUser au = new AltaUser(consulta);
                        au.execute(this, "foo");

                }
            });

        } catch (Exception s) {
            Log.e("ERROR_LOGIN", s.getMessage());

        }


    }

    @Override
    public void onResume(){
        super.onResume();

        context = getApplicationContext();


    }

    private class AltaUser extends AsyncTask<Object, Void, String> {

        //
        InputStream inputStream = null;
        String result = "";

        private String url_select;

        public AltaUser(String url) {
            this.url_select = url;

        }

        @Override
        protected void onPreExecute() {

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

            if (v.contains("{\"success\":1}")) {

                Intent i = new Intent(Login.this, MainActivity.class);
               // i.putExtra("cuenta", spinner.getSelectedItem().toString());
                i.putExtra("cuenta", cuenta_seleccionada);
                startActivity(i);


            } else {
                Log.i("ERROR PARSER", "!!");
                return;
            }


        }


    }


    private String generarReferido() {


        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();

        return output;
    }

    private String getLocalizacion() {
        // Localización del usuario.
        LocationManager localizacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean activado_localizacion = localizacion.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        String codigoPais="";

        if (activado_localizacion) {
            Location net_loc = localizacion.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Log.i("COORDENADAS", "Longitud: " + net_loc.getLongitude() + " Latitud: " + net_loc.getLatitude());
            Geocoder geocoder = new Geocoder(this.getApplicationContext(), Locale.getDefault());

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