package com.cashmyapps.core.cashmyappsproject;

import android.util.Log;

/**
 * Created by David on 26/03/2015.
 */
public class PostbacksThread extends Thread {

    private final int DELAY = 5000;
    private int i=0;

    @Override
    public void run(){


        while(true)
        {
           try{
               Log.i("HILO DE EJECUCION: ", i++ +"" );
               Thread.sleep(DELAY);
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
