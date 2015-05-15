package com.cashmyapps.core.cashmyappsproject;

import android.graphics.Bitmap;
import android.net.Uri;


/**
 * Created by David on 13/05/2015.
 */
public class Oferta {


    //Atributos
    private String NOM_APP;
    private String DESC;
    private String PPI;
    private Integer ESTRELLAS;
    private Uri URL;
    private Bitmap IMG;

    public String getNOM_APP() {
        return NOM_APP;
    }

    public String getDESC() {
        return DESC;
    }

    public String getPPI() {
        return PPI;
    }

    public Integer getESTRELLAS() {
        return ESTRELLAS;
    }

    public Uri getURL() {
        return URL;
    }

    public Bitmap getIMG() {
        return IMG;
    }


    public void setNOM_APP(String NOM_APP) {
        this.NOM_APP = NOM_APP;
    }

    public void setDESC(String DESC) {
        this.DESC = DESC;
    }

    public void setPPI(String PPI) {
        this.PPI = PPI;
    }

    public void setESTRELLAS(Integer ESTRELLAS) {
        this.ESTRELLAS = ESTRELLAS;
    }

    public void setURL(Uri URL) {
        this.URL = URL;
    }

    public void setIMG(Bitmap IMG) {
        this.IMG = IMG;
    }
}
