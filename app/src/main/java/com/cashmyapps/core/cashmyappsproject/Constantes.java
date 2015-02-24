package com.cashmyapps.core.cashmyappsproject;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David on 13/02/2015.
 */
@SuppressWarnings("unused")
public final class Constantes {

   public static final String URL_GEENAPP ="http://offer.geenapptool.com/162/?device=android&country=ES&lang=ES";
   public static  final String URL_GET_BBDD_JSON="http://www.cashmyapps.net/get_datos_usuario.php";
   public static  final String ALTA_USUARIO="http://www.cashmyapps.net/set_nuevo_usuario.php/?";
   public static final String PAGAR_RECOMPENSA="http://www.cashmyapps.net/set_usuario_pagar_recompensa.php?";


/*
        @SuppressLint("SimpleDateFormat")
        public String url_hoy(){

            String url_completa=URL_HOY;

            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            String fecha = date.format(new Date());

            url_completa += fecha+"T00:00:00&amp";


            return url_completa;
        }*/
    }



