package com.cashmyapps.core.cashmyappsproject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;


public class UserInfo extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private String resultado;
    private String resultado_final;
    private JSONObject jObject;
    private JSONObject jData;
    private JSONArray jArray;
    private String nombre;
    private String mail;
    private String refer;
    private String saldo_coins;
        private Account[] accounts;
    private Bundle extras;
    private Boolean success = false;
    private TextView txNombre;
    private TextView txMail ;
    private TextView txCod_refer;
    private Button btAceptar ;
    private Context contexto;
    private static final int HELLO_ID = 1;
    private static final int NOTIFY_ME_ID=1337;
    private PostbacksThread pb;
    private ProgressDialog progressDialog;






    public UserInfo() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        final View rootView = lf.inflate(R.layout.fragment_user_info, container, false);

        TextView cuenta_main = (TextView)getActivity().findViewById(R.id.txCorreo);
        String prueba = cuenta_main.getText().toString();
        new MainParser(Constantes.URL_GET_BBDD_JSON+"?mail="+cuenta_main.getText()).execute(this,"foo");





        return lf.inflate(R.layout.fragment_user_info, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
       /* if(pb.isAlive())
            pb.interrupt();*/

    }

    @Override
    public void onResume(){
        super.onResume();




    }

    public static UserInfo newInstance(int sectionnumber){

        UserInfo frag = new UserInfo();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionnumber);
        frag.setArguments(args);
        return frag;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    private Notification getOldNotification() {
        Notification notif = new Notification(R.drawable.ic_launcher, "Optional ticker text", System.currentTimeMillis());
        notif.setLatestEventInfo(contexto, "Old title", "Old notification content text", PendingIntent.getActivity(contexto, 0, new Intent(), 0));
        return notif;
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
            progressDialog.setMessage("Cargando datos de usuario...");
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


                        TextView txNombre = (TextView)getActivity().findViewById(R.id.txNombre);
                        TextView txSaldo = (TextView)getActivity().findViewById(R.id.txSaldo);

                        txNombre.setText(nombre);
                        txSaldo.setText(saldo_coins);



                        //Invocamos al servicio
                        Intent in = new Intent(getActivity(),ServicioPostback.class);
                        in.putExtra("cuenta",mail);
                        in.putExtra("cod_refer",refer);

                        if(!ServicioPostback.isRunning())
                            getActivity().startService(in);


                    }


                }catch (Exception s){
                    Log.i("Error carga: ",s.getMessage());
                }


                contexto = getActivity().getApplicationContext();
                txNombre = (TextView)getActivity().findViewById(R.id.txUENombre);
                txMail = (TextView)getActivity().findViewById(R.id.txUEcorreo);
                txCod_refer = (TextView)getActivity().findViewById(R.id.txUERefer);

                pb = new PostbacksThread();
                txNombre.setGravity(Gravity.CENTER);
                txMail.setGravity(Gravity.CENTER);
                txCod_refer.setGravity(Gravity.CENTER);

                txNombre.setText(nombre);
                txMail.setText(mail);
                txCod_refer.setText(refer);

                progressDialog.dismiss();


            }


        }


}
