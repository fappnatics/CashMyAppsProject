package com.cashmyapps.core.cashmyappsproject;

/**
 * Created by David on 28/02/2015.
 */
public class DatosUsuario {

    private String id_usuario;
    private String nombre;
    private String mail;
    private String saldo;
    private String pais;
    private String estado_cuenta;
    private String cod_refer;
    private String referido_por;
    private String fecha_alta;
    private String conectado;








    public DatosUsuario(){




    }

    public String getId_usuario() {
        return id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMail() {
        return mail;
    }

    public String getSaldo() {
        return saldo;
    }

    public String getPais() {
        return pais;
    }

    public String getEstado_cuenta() {
        return estado_cuenta;
    }

    public String getCod_refer() {
        return cod_refer;
    }

    public String getReferido_por() {return referido_por;}

    public String getFecha_alta() {
        return fecha_alta;
    }

    public String getConectado() {return conectado;}



    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public void setEstado_cuenta(String estado_cuenta) {
        this.estado_cuenta = estado_cuenta;
    }

    public void setCod_refer(String cod_refer) {this.cod_refer = cod_refer;}

    public void setReferido_por(String referido_por) {
        this.referido_por = referido_por;
    }

    public void setFecha_alta(String fecha_alta) {
        this.fecha_alta = fecha_alta;
    }

    public void setConectado(String conectado) {this.conectado = conectado;}

}
