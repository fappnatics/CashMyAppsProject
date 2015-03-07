package com.cashmyapps.core.cashmyappsproject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David on 07/03/2015.
 */
public class Fechas {

    private String fecha;
    private Date d = new Date();


    //Fecha actual en String de 10 posiciones.
    public String getFechaActual(){

        fecha = new SimpleDateFormat("dd-MM-yyyy").format(d);

        return fecha;
    }
}
