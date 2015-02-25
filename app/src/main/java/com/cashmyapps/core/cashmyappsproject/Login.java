package com.cashmyapps.core.cashmyappsproject;

import android.accounts.Account;
import android.accounts.AccountManager;
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
import android.util.Patterns;
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
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.concurrent.ExecutionException;



public class Login extends ActionBarActivity {

    private String resultado;
    private Spinner spinner;
    private int num_cuentas = 0;
    private List<String> multicuenta;
    private Button btInstalar;
    private TextView txTituloAmigo;
    private EditText etAmigo;
    private EditText etNombre;
    private TextView titulo;
    private ProgressDialog pd;
    private String cuenta;
    private String nombre;
    private String saldo;
    private String fecha_alta;
    private String cod_usuario;
    private Context context;
    private String consulta;
    ProgressDialog progressDialog;
    private String pais;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        try {
            getSupportActionBar().hide();
            setContentView(R.layout.activity_login);
            spinner = (Spinner) findViewById(R.id.spinner_cuentas);

            //Cuentas de usuario
            Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
            Account[] accounts = AccountManager.get(this.getApplicationContext()).getAccounts();
            List<String> cuentas = new ArrayList<String>();
            for (Account account : accounts) {
                if (emailPattern.matcher(account.name).matches()) {
                    String possibleEmail = account.name;
                    if (!cuentas.contains(possibleEmail))
                        cuentas.add(possibleEmail);

                }
            }

            multicuenta = new ArrayList<>();

            for (int i = 0; i < cuentas.size(); i++) {
                resultado = new JSONParser(Constantes.URL_GET_BBDD_JSON + "?mail=" + cuentas.get(i)).execute(this, "foo").get();
                if (resultado.contains("1")) {
                    num_cuentas++;
                    multicuenta.add(cuentas.get(i));
                }
            }

           /*JSONObject jObject = new JSONObject(resultado);
            String estado_cuenta = jObject.getJSONObject("usuarios").getString("ESTADO_CUENTA");*/


            titulo = (TextView) findViewById(R.id.txTituloSelec);
            titulo.setText("Selecciona una cuenta de acceso.");

            txTituloAmigo = (TextView) findViewById(R.id.txTituloAmigo);


            etAmigo = (EditText) findViewById(R.id.etAmigo);


            etNombre = (EditText) findViewById(R.id.etNombre);

            if (num_cuentas == 0) {
                TextView titulo = (TextView) findViewById(R.id.txTituloSelec);
                titulo.setText("¿Nuevo en CashMyApps? \n Selecciona una cuenta de registro");


                String[] arraySpinner = cuentas.toArray(new String[cuentas.size()]);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getApplicationContext(),
                        R.layout.elemento_spinner, arraySpinner);
                spinner.setAdapter(adapter);


            }

            if (num_cuentas == 1) {
                Intent i = new Intent(Login.this, MainActivity.class);
                Log.i("CUENTA", cuentas.get(0));
                i.putExtra("cuenta", multicuenta.get(0));
                startActivity(i);
                finish();
            }

            if (num_cuentas > 1) {

                String[] arraySpinner = multicuenta.toArray(new String[multicuenta.size()]);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getApplicationContext(),
                        R.layout.elemento_spinner, arraySpinner);
                spinner.setAdapter(adapter);


            }

            btInstalar = (Button) findViewById(R.id.btAceptar);
            btInstalar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (num_cuentas == 0) {


                        Date d = new Date();
                        pais = Locale.getDefault().getCountry();
                        cuenta = spinner.getSelectedItem().toString();
                        nombre = etNombre.getText().toString().replace(" ", "%20");
                        fecha_alta = new SimpleDateFormat("dd-MM-yyyy").format(d);
                        Log.i("FECHA: ", fecha_alta);
                        consulta = Constantes.ALTA_USUARIO + "NOMBRE=" + nombre + "&MAIL=" + cuenta + "&SALDO=0&FECHA_ALTA=" + fecha_alta + "&PAIS="+pais + "&ESTADO_CUENTA=A";
                        AltaUser au = new AltaUser(consulta);
                        au.execute(this, "foo");


                    }
                    if (num_cuentas == 1) {

                        Intent i = new Intent(Login.this, MainActivity.class);
                        i.putExtra("cuenta", spinner.getSelectedItem().toString());
                        startActivity(i);
                        finish();

                    }
                    if (num_cuentas > 1) {

                        Intent i = new Intent(Login.this, MainActivity.class);
                        i.putExtra("cuenta", spinner.getSelectedItem().toString());
                        startActivity(i);
                        etAmigo.setVisibility(View.INVISIBLE);
                        txTituloAmigo.setVisibility(View.INVISIBLE);
                        finish();

                    }

                }
            });

        } catch (Exception s) {
            Log.e("ERROR_LOGIN", s.getMessage());
            System.exit(0);
        }


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
                i.putExtra("cuenta", spinner.getSelectedItem().toString());
                startActivity(i);
                System.exit(0);

            } else {
                Log.i("ERROR PARSER", "!!");
                return;
            }


        }


    }

}