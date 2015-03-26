package com.cashmyapps.core.cashmyappsproject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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






    public UserInfo() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        final View rootView = lf.inflate(R.layout.fragment_user_info, container, false);

        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        accounts = AccountManager.get(this.getActivity().getApplicationContext()).getAccounts();
        List<String> cuentas = new ArrayList<String>();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                if(!cuentas.contains(possibleEmail))
                    cuentas.add(possibleEmail);
            }

        }

        try {

            for(int s=0;s<cuentas.size();s++)
            {
                resultado = new JSONParser(Constantes.URL_GET_BBDD_JSON+"?mail="+cuentas.get(s)).execute(this,"foo").get();

                if(resultado.contains("1"))
                {
                    success = true;
                    resultado_final = resultado;

                }
            }

            jObject = new JSONObject(resultado_final);
            jArray = jObject.getJSONArray("usuarios");
            nombre = jArray.getJSONObject(0).getString("NOMBRE");
            mail = jArray.getJSONObject(0).getString("MAIL");
            refer = jArray.getJSONObject(0).getString("COD_REFER");




        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return lf.inflate(R.layout.fragment_user_info, container, false);
    }

    @Override
    public void onPause(){
        super.onPause();


       //pb.interrupt();

    }

    @Override
    public void onResume(){
        super.onResume();

        contexto = getActivity().getApplicationContext();
        txNombre = (TextView)getActivity().findViewById(R.id.txUENombre);
        txMail = (TextView)getActivity().findViewById(R.id.txUEcorreo);
        txCod_refer = (TextView)getActivity().findViewById(R.id.txUERefer);
        btAceptar = (Button)getActivity().findViewById(R.id.btUEAceptar);

       /* final NotificationCompat.Builder builder = new NotificationCompat.Builder(contexto);
        builder.setSmallIcon(R.drawable.cashmyapps_logo2);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.cashmyapps_logo2));
        builder.setContentTitle("Mi notificación");
        builder.setContentText("Esto es una prueba para probar las notificaciones");
        Intent result = new Intent();

        TaskStackBuilder taskStack = TaskStackBuilder.create(contexto);
        taskStack.addParentStack(getActivity());
        taskStack.addNextIntent(result);

        PendingIntent resultPending = taskStack.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPending);

        final NotificationManager mNotif = (NotificationManager)contexto.getSystemService(Context.NOTIFICATION_SERVICE);*/
         pb = new PostbacksThread();

        btAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*********** Create notification ***********/
                       // mNotif.notify(0,builder.build());

                    pb.start();

                }
            });



        txNombre.setGravity(Gravity.CENTER);
        txMail.setGravity(Gravity.CENTER);
        txCod_refer.setGravity(Gravity.CENTER);

        txNombre.setText(nombre);
        txMail.setText(mail);
        txCod_refer.setText(refer);


    }

    public static UserInfo newInstance(int sectionnumber){

        UserInfo frag = new UserInfo();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionnumber);
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

    private Notification getDefaultNotification(Notification.Builder builder) {
        builder.setSmallIcon(R.drawable.cashmyapps_logo2);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.cashmyapps_logo2));
        builder.setContentTitle("Mi notificación");
        builder.setContentText("Esto es una prueba para probar las notificaciones");
        Intent result = new Intent();

        TaskStackBuilder taskStack = TaskStackBuilder.create(contexto);
        taskStack.addParentStack(getActivity());
        taskStack.addNextIntent(result);

        PendingIntent resultPending = taskStack.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPending);

        final NotificationManager mNotif = (NotificationManager)contexto.getSystemService(Context.NOTIFICATION_SERVICE);

        return builder.getNotification();

    }


}
