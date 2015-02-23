package com.cashmyapps.core.cashmyappsproject;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 22/01/2015.
 */
public class DownloadImageTask extends AsyncTask<List<String>, Void, List<Bitmap>> {

    private ProgressDialog progressDialog;
    private Context context;

    public DownloadImageTask(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
    }


    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Cargando lista de ofertas");
        progressDialog.show();
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface arg0) {
                DownloadImageTask.this.cancel(true);
            }
        });
    }


    protected List<Bitmap> doInBackground(List<String>... urls) {
        List<String> urldisplay = urls[0];
        List<Bitmap> imagenes = new ArrayList<>();
        for (int i = 0; i < urldisplay.size(); i++)
            try {
                InputStream in = new java.net.URL(urldisplay.get(i)).openStream();
                imagenes.add(BitmapFactory.decodeStream(in));

            } catch (Exception e) {
                e.printStackTrace();
            }

        progressDialog.dismiss();

        return imagenes;
    }

    @Override
    protected void onPostExecute(List<Bitmap> resultado) {
        super.onPostExecute(resultado);
      progressDialog.dismiss();

    }
}
