package com.cashmyapps.core.cashmyappsproject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class SplashScreen extends ActionBarActivity {

    private static int SPLASH_TIME_OUT = 5000;
    private String id_user="";
    private String result = "";
    private String usuario = "";
    private JSONArray jArray;
    private JSONObject jObject;
    private String extra = "";
    private List<String> cuentas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

       try {

           setContentView(R.layout.activity_splash_screen);
           ConnectivityManager conMgr = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
           NetworkInfo i = conMgr.getActiveNetworkInfo();
           if(i==null){
               Toast.makeText(this, getResources().getString(R.string.dialogo_error_conexion), Toast.LENGTH_LONG).show();
               this.finish();
               return;
           }
           if(!i.isConnected()||!i.isAvailable()){
               Toast.makeText(this, getResources().getString(R.string.dialogo_error_conexion), Toast.LENGTH_LONG).show();
               this.finish();
               return;
           }



       }
       catch(Exception s)
       {
           Log.e("ERROR_SPLASH: ",s.getMessage());
       }

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */


            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                //Intent i = new Intent(SplashScreen.this, MainActivity.class);
                if(!extra.equals("")){
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    i.putExtra("cuenta",extra);
                startActivity(i);}
                else{
                    Intent i = new Intent(SplashScreen.this, Login.class);
                    i.putStringArrayListExtra("cuentas", (ArrayList<String>) cuentas);
                                startActivity(i);}

                overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);


    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            //Cuentas de usuario
            Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
            Account[] accounts = AccountManager.get(this.getApplicationContext()).getAccounts();
            cuentas = new ArrayList<String>();
            for (Account account : accounts) {
                if (emailPattern.matcher(account.name).matches()) {
                    String possibleEmail = account.name;
                    if (!cuentas.contains(possibleEmail)) {
                        cuentas.add(possibleEmail);
                        //id_user+="\""+possibleEmail+"\""+",";
                        id_user += "[" + possibleEmail + "]" + ",";
                    }

                }
            }
            id_user = id_user.substring(0, id_user.length() - 1);
            result = new JSONParser(Constantes.GET_USER_EXISTE.replace("[CUENTAS]", id_user)).execute(this, "foo").get();

            if (!result.contains("{\"success\":0,\"message\":\"No users found\"}")) {

                jArray = new JSONObject(result).getJSONArray("usuarios");

                for (int i = 0; i < jArray.length(); i++) {
                   jObject = (JSONObject) jArray.get(i);
                    usuario = jObject.getString("MAIL");
                    if (id_user.contains(usuario))
                        extra = usuario;
                }


            }


        }
        catch (Exception s){
            Log.i("ERROR_SPLASH",s.getMessage());
        }
    }



}
