package com.cashmyapps.core.cashmyappsproject;

import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Created by David on 10/04/2015.
 */
public class GeneradorCodigos {

    public String generarCodigos(String prefijo) throws ExecutionException, InterruptedException {

        Boolean code = true;
        String output="";

        while(code) {
            char[] chars = "0123456789".toCharArray();
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 8; i++) {
                char c = chars[random.nextInt(chars.length)];
                sb.append(c);
            }
            output = prefijo + "-" + sb.toString();
            String result = new JSONParser(Constantes.COMPROBAR_COD_PAGO.replace("[COD_PAGO]",output)).execute(this,"foo").get();

            if(result.contains("\"COD_PAGO\":\"0\"")){
                code=false;

            }

        }

        return output;
    }
}
