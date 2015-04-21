package com.cashmyapps.core.cashmyappsproject;

import android.app.Activity;
import android.util.Log;

/**
 * Created by David on 26/03/2015.
 */
public class PostbacksThread extends Thread {


   /* public PostbacksThread (Activity act){
        this.actividad = act;

    }*/

    private String comp;
    private final int DELAY = 3000;
    private int i=0;
    private Activity actividad;

    @Override
    public void run(){


        while(!this.isInterrupted())
        {
           try{
               Log.i("HILO DE EJECUCION: ", i++ +"" );
               Thread.sleep(DELAY);
               /*String prueba = new JSONParser(Constantes.GET_RANKING_USERS).execute(this,"foo").get();
               Log.i("HILO DE EJECUCION: ",prueba);*/



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
