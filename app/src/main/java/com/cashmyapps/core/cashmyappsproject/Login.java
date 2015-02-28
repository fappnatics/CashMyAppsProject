package com.cashmyapps.core.cashmyappsproject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;



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
    private String id_user;
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
    private String cod_refer;
    private String estado_cuenta;
    private List<DatosUsuario> list_usuarios;
    private DatosUsuario usuario;
    private JSONArray jArray;
    private JSONObject jObject;


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
            list_usuarios = new ArrayList<>();


            for (int i = 0; i < cuentas.size(); i++) {
                resultado = new JSONParser(Constantes.URL_GET_BBDD_JSON + "?mail=" + cuentas.get(i)).execute(this, "foo").get();
                if (resultado.contains("1")) {

                    usuario = new DatosUsuario();
                    num_cuentas++;
                    multicuenta.add(cuentas.get(i));
                    jArray = new JSONObject(resultado).getJSONArray("usuarios");
                    jObject = (JSONObject) jArray.get(i);
                    usuario.setId_usuario(jObject.getString("ID_USUARIO"));
                    usuario.setNombre(jObject.getString("NOMBRE"));
                    usuario.setMail(jObject.getString("MAIL"));
                    usuario.setSaldo(jObject.getString("SALDO"));
                    usuario.setPais(jObject.getString("PAIS"));
                    usuario.setEstado_cuenta(jObject.getString("ESTADO_CUENTA"));
                    usuario.setCod_refer(jObject.getString("COD_REFER"));
                    fecha_alta = jObject.getString("FECH_ALTA");
                    list_usuarios.add(usuario);
                }
            }




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

                String usuario_activo = list_usuarios.get(0).getEstado_cuenta();

                if(usuario_activo.equals("B")){

                    LayoutInflater factory = LayoutInflater.from(this);
                    final View view = factory.inflate(R.layout.alerta_icono,null);
                    AlertDialog.Builder alerta = new AlertDialog.Builder(this);
                    alerta.setView(view);


                    alerta.setTitle("Atención");
                    alerta.setMessage("La cuenta está bloqueada, por favor, póngase en contacto con nuestro soporte en soportecashmyapps@gmail.com");

                    alerta.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

                    alerta.show();

                }

               if(usuario_activo.equals("A"))
               {
                    Intent i = new Intent(Login.this, MainActivity.class);
                    Log.i("CUENTA", cuentas.get(0));
                    i.putExtra("cuenta", multicuenta.get(0));
                    startActivity(i);
                    finish();
               }

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
                        cod_refer = generarReferido();
                        //TODO Hay que consultar la base de datos para evitar códigos duplicados.
                        consulta = Constantes.ALTA_USUARIO + "NOMBRE=" + nombre + "&MAIL=" + cuenta + "&SALDO=0&FECHA_ALTA=" + fecha_alta + "&PAIS="+pais + "&ESTADO_CUENTA=A"+"&COD_REFER="+cod_refer;
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


    private String generarReferido(){


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
 }