package com.cashmyapps.core.cashmyappsproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by David on 21/04/2015.
 */
public class ServicioPostback extends Service {

    private static ServicioPostback instance  = null;

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
        System.out.println( "Servicio MyService creado");
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

    void lanzarNotificacion(){
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager notManager = (NotificationManager) getSystemService(ns);

        //Configuramos la notificacion
        Notification notif = new Notification(android.R.drawable.ic_menu_agenda, "MyService", System.currentTimeMillis());

        //Configuramos el Intent
        Context contexto = ServicioPostback.this;
        CharSequence titulo = "MyService Notification";
        CharSequence descripcion = "Hora de visitar GarabatosLinux";

        //Intent que se abrira al clickear la notificacion
        Intent resultIntent =new Intent(Intent.ACTION_VIEW, Uri.parse("http://garabatoslinux.net"));;

        PendingIntent contIntent = PendingIntent.getActivity(contexto, 0, resultIntent, 0);
        notif.setLatestEventInfo(contexto, titulo, descripcion, contIntent);
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.defaults |= Notification.DEFAULT_VIBRATE;
        notManager.notify(1, notif);
    }


}

