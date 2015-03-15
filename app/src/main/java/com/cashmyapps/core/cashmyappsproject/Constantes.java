package com.cashmyapps.core.cashmyappsproject;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David on 13/02/2015.
 */
@SuppressWarnings("unused")
public final class Constantes {

   public static final String URL_GEENAPP2 ="http://offer.geenapptool.com/155/?device=android&country=[PAIS]&lang=[LANG]";
   public static final String URL_GEENAPP ="http://offer.geenapptool.com/162/?device=android&country=[PAIS]&lang=[LANG]";
   public static  final String URL_GET_BBDD_JSON="http://www.cashmyapps.net/get_datos_usuario.php";
   public static  final String ALTA_USUARIO="http://www.cashmyapps.net/set_nuevo_usuario.php/?";
   public static final String PAGAR_RECOMPENSA="http://www.cashmyapps.net/set_usuario_pagar_recompensa.php?";
   public static final String CONTROL_INSTALACIONES="http://www.cashmyapps.net/get_instalaciones.php?";
   public static final String CONEXION_USUARIO="http://www.cashmyapps.net/set_conexion_usuario.php?MAIL=[MAIL]&CONECTADO=[CONECTADO]";
   public static final String ERRORES_APP="http://cashmyapps.net/insert_error_log.php?MAIL=[CUENTA]&ERROR=[ERROR]&FECHA=[FECHA]";
   public static final String USUARIOS_CONECTADOS="http://cashmyapps.net/get_usuarios_conectados.php";
   public static final String GET_COD_REFER="http://cashmyapps.net/get_usuarios_conectados.php";
   public static final String GET_RANKING_USERS="http://www.cashmyapps.net/get_ranking_usuarios.php";
   public static final String GET_SALDO="http://www.cashmyapps.net/get_saldo.php?MAIL=[MAIL]";
   public static final String SOLICITAR_COBRO="http://www.cashmyapps.net/solicitudes_cobro.php?MAIL=[MAIL]&COINS=[COINS]";
   public static final String INSERTAR_SOLICITAR_COBRO="http://www.cashmyapps.net/insert_solicitud_cobro.php?MAIL=[MAIL]&COINS=[COINS]&FECHA=[FECHA]&PAGADO=[PAGADO]";





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



