package com.cashmyapps.core.cashmyappsproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 21/04/2015.
 */
public class ServicioPostback extends Service {

    private static ServicioPostback instance  = null;
    private final int DELAY = 3000;
    private int i=0;
    private String cuenta;
    private String cod_refer;
    private String stPagar="";
    private JSONObject jObject;
    private JSONArray jArray;
    private List<String> lsPagos;

    public static boolean isRunning() {
        return instance != null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onCreate() {
        Toast.makeText(getApplicationContext(), "Servicio ServicioPostback creado", Toast.LENGTH_LONG).show();
        instance=this;
        }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(), "Servicio ServicioPostback destruido", Toast.LENGTH_LONG).show();
        System.out.println( "Servicio MyService destruido");
        instance = null;
    }

    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(getApplicationContext(), "Servicio ServicioPostback iniciado!!", Toast.LENGTH_LONG).show();
        System.out.println( "Servicio MyService iniciado");
        lanzarNotificacion();

    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {
        cuenta = intent.getStringExtra("cuenta");
        cod_refer = intent.getStringExtra("cod_refer");
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
               new Thread()
        {
            public void run() {
                buscaPagos();
            }
        }.start();

        return START_STICKY;
    }





    void lanzarNotificacion(){
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager notManager = (NotificationManager) getSystemService(ns);

        //Configuramos la notificacion
        Notification notif = new Notification(R.drawable.cashmyapps_logo, "CashMaker", System.currentTimeMillis());

        //Configuramos el Intent
        Context contexto = ServicioPostback.this;
        CharSequence titulo = "Nuevo pago CashMAker";
        CharSequence descripcion = "Has recibido un nuevo pago, pulsa esta notificación para refrescar el marcador.";

        //Intent que se abrira al clickear la notificacion
        Intent resultIntent =new Intent(ServicioPostback.this, SplashScreen.class);;

        PendingIntent contIntent = PendingIntent.getActivity(contexto, 0, resultIntent, 0);
        notif.setLatestEventInfo(contexto, titulo, descripcion, contIntent);
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.defaults |= Notification.DEFAULT_VIBRATE;
        notManager.notify(1, notif);
    }

    public void buscaPagos(){

        while(true)
        {
            try{
               //Log.i("HILO DE EJECUCION: ", i++ +"" );
                i++;
                Thread.sleep(DELAY);
               String usuario = new JSONParser(Constantes.GET_POSTBACK_USER.replace("[CODREFER]",cod_refer)).execute(this,"foo").get();
                jObject = new JSONObject(usuario);
                jArray = jObject.getJSONArray("PAGOS");
                lsPagos = new ArrayList<>();

                if(jArray.length()>0) {
                    for (int i = 0; i < jArray.length(); i++) {
                        stPagar += jArray.getJSONObject(i).getString("GEE_APP")+", ";
                    }
                    stPagar = stPagar.substring(0,stPagar.length()-1);
                }




                if(i==10)
                    lanzarNotificacion();
            }
            catch(InterruptedException s){
                Log.i("SLEEP: ",s.getMessage() );
            }
            catch(Exception s){
                Log.i("SLEEP: ",s.getMessage() );
            }
        }
    }


}
