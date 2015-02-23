package com.cashmyapps.core.cashmyappsproject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class SplashScreen extends ActionBarActivity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

       try {
           setContentView(R.layout.activity_splash_screen);
           ConnectivityManager conMgr = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
           NetworkInfo i = conMgr.getActiveNetworkInfo();
           if(i==null){
               Toast.makeText(this, "Conexión de datos no disponible, por favor, conéctate a una red y vuelve a intentarlo.", Toast.LENGTH_LONG).show();
               this.finish();
               return;
           }
           if(!i.isConnected()||!i.isAvailable()){
               Toast.makeText(this, "Conexión de datos no disponible, por favor, conéctate a una red y vuelve a intentarlo.", Toast.LENGTH_LONG).show();
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
                Intent i = new Intent(SplashScreen.this, Login.class);
                startActivity(i);

                overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);




    }



}
