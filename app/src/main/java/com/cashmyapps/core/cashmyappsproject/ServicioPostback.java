package com.cashmyapps.core.cashmyappsproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
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
    private String stMensaje="";
    private Double coins = 0.00;
    private String calcular ="";
    private String gee_unique ="";
    private int pago_final;
    private JSONObject jObject;
    private JSONArray jArray;
    private List<String> lsPagos;

    private int s;

    public static boolean isRunning() {
        return instance != null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onCreate() {

        instance=this;
        }

    @Override
    public void onDestroy() {
        System.out.println( "Servicio PostbackService destruido");
        instance = null;
    }

    @Override
    public void onStart(Intent intent, int startid) {
        System.out.println( "Servicio PostbackService iniciado");
        //lanzarNotificacion();

    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {
        cuenta = intent.getStringExtra("cuenta");
        cod_refer = intent.getStringExtra("cod_refer");
        new Thread(){
            public void run(){
                buscaPagos();
            }
        }.start();

        return START_STICKY;
    }


    private void lanzarNotificacion2(String mensaje) {

        Intent resultIntent =new Intent(ServicioPostback.this, SplashScreen.class);

        PendingIntent contIntent = PendingIntent.getActivity(ServicioPostback.this, 0, resultIntent, 0);

        Notification notif = new Notification.Builder(ServicioPostback.this)
                .setContentTitle(getResources().getString(R.string.nf_nuevo_pago))
                .setSmallIcon(R.drawable.coin)
                .setLights(Color.RED,500,100)
                .setSound(Uri.parse("android.resource://"+getPackageName()+"/"+ R.raw.monedas))
                .setVibrate(new long[]{100, 1000, 1000, 1000})
                .setStyle(new Notification.BigTextStyle().bigText(mensaje))
                .setContentIntent(contIntent)
                .build();


        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(1,notif);
        }


    //Notificación antigua en desuso, conservar por si acaso.
    void lanzarNotificacion(String mensaje){
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager notManager = (NotificationManager) getSystemService(ns);

        //Configuramos la notificacion
        Notification notif = new Notification(R.drawable.cashmyapps_logo2, "CashMaker", System.currentTimeMillis());

        //Configuramos el Intent
        Context contexto = ServicioPostback.this;
        CharSequence titulo = "Nuevo pago CashMaker";
        CharSequence descripcion = mensaje;

        //Intent que se abrira al clickear la notificacion
        Intent resultIntent =new Intent(ServicioPostback.this, SplashScreen.class);

        PendingIntent contIntent = PendingIntent.getActivity(contexto, 0, resultIntent, 0);
        notif.setLatestEventInfo(contexto, titulo, descripcion, contIntent);
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.defaults |= Notification.DEFAULT_VIBRATE;
        notif.defaults |= Notification.DEFAULT_SOUND;
        notif.defaults |= Notification.DEFAULT_LIGHTS;

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
                    for (s = 0; s < jArray.length(); s++) {
                       //Efectuamos los pagos pendiente de las aplicaciones pedientes.

                        stPagar += jArray.getJSONObject(s).getString("GEE_APP")+", ";
                        gee_unique = jArray.getJSONObject(s).getString("GEE_UNIQUE");
                        calcular= String.format("%.2f",Double.parseDouble(jArray.getJSONObject(s).getString("GEE_PPI"))*0.2);
                        coins += Double.parseDouble(calcular.replace(",","."))*100;
                        //Pagamos la recompensa al usuario y registramos la transacción.
                        new JSONParser(Constantes.PAGAR_RECOMPENSA+
                                                  "GIFT="+coins+
                                                  "&MAIL="+cuenta+
                                                  "&COD_PAGO="+new GeneradorCodigos().generarCodigos("IN")+
                                                  "&COINS="+coins.intValue()+
                                                  "&PPI="+calcular.replace(",",".")+
                                                  "&FECHA="+ new Fechas().getFechaActual()+
                                                  "&COD_REFER="+cod_refer+
                                                  "&GEE_UNIQUE="+gee_unique).execute();


                    }

                    //Datos para la notificación.
                    stPagar = stPagar.substring(0,stPagar.lastIndexOf(","));
                    pago_final = coins.intValue();




                }

                if(s>1)
                {
                    stMensaje = getResources().getString(R.string.nf_app_inst_plural).replace("[APPS]",stPagar).replace("[PPI]",pago_final+"");
                    lanzarNotificacion2(stMensaje);

                }
                if(s==1) {
                    stMensaje = getResources().getString(R.string.nf_app_inst_singular).replace("[APPS]",stPagar).replace("[PPI]",pago_final+"");

                    lanzarNotificacion2(stMensaje);
                }


               //reiniciamos las variables
                coins = 0.00;
                stPagar="";
                calcular="";
                pago_final=0;


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

