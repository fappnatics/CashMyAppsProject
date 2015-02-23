package com.cashmyapps.core.cashmyappsproject;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by David on 12/01/2015.
 */
public class JSONParser extends AsyncTask<Object, Void, String> {

    //
    InputStream inputStream = null;
    String result = "";
    ProgressDialog progressDialog;
    Context context;
    private String url_select;
    public JSONParser(String url){
        this.url_select= url;
    }


    /*  @Override
    protected void onPreExecute() {

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Downloading your data...");
        progressDialog.show();
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface arg0) {
                JSONParser.this.cancel(true);
            }
        });
    }*/


    @Override
    protected String doInBackground(Object... params) {

        //String url_select = "http://offer.geenapptool.com/155/?device=android&country=ES&lang=es";
        //url_select = "http://offer.geenapptool.com/162/?device=android&country=ES&lang=es";

        ArrayList<NameValuePair> param = new ArrayList<>();

        try {

            // HttpClient is more then less deprecated. Need to change to URLConnection
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url_select);
            httpPost.setEntity(new UrlEncodedFormEntity(param));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");


    } catch (Exception e) {
            Log.e("Error  result ", e.getMessage());
        }
        return result;
    }

    @Override
    protected void onPostExecute(String  v){


    }


}
