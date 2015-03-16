package com.cashmyapps.core.cashmyappsproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class Beneficios extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private  Button btSolicitarIngresos;
    private View rootView;
    private String cuenta;
    private String resultado;
    private JSONArray jArray;
    private String saldo;
    private TextView saldo_user;
    private TextView correo;


    public Beneficios() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     rootView = inflater.inflate(R.layout.fragment_beneficios,container,false);




        return inflater.inflate(R.layout.fragment_beneficios, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();

        btSolicitarIngresos = (Button)getActivity().findViewById(R.id.btSolicitarBeneficios);
        correo = (TextView)getActivity().findViewById(R.id.txCorreo);
        saldo_user = (TextView)getActivity().findViewById(R.id.txCobroSaldo);
        cuenta = correo.getText().toString();
        try {
            resultado = new JSONParser(Constantes.GET_SALDO.replace("[MAIL]",cuenta)).execute(this,"foo").get();
            jArray = new JSONObject(resultado).getJSONArray("usuarios");
            saldo = jArray.getJSONObject(0).getString("SALDO");
            saldo_user.setText(saldo+" coins");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        btSolicitarIngresos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View view = factory.inflate(R.layout.alerta_icono,null);
                AlertDialog.Builder alerta = new AlertDialog.Builder(getActivity());
                alerta.setView(view);

                try {
                    resultado = new JSONParser(Constantes.SOLICITAR_COBRO.replace("[MAIL]",cuenta).replace("[COINS]",saldo)).execute(this,"foo").get();


                    if(resultado.contains("{\"success\":1}")){

                        resultado = new JSONParser(Constantes.INSERTAR_SOLICITAR_COBRO.replace("[MAIL]",cuenta).replace("[COINS]",saldo).replace("[FECHA]",new Fechas().getFechaActual()).replace("[PAGADO]","N")).execute(this,"foo").get();
                        Log.i("SOLICITUD: ",resultado);
                        if(resultado.contains("{\"success\":1}")){
                        alerta.setTitle("Éxito");
                        alerta.setMessage("Su solicitud de cobro ha sido tramitada y se realizará en 24/48h, si tiene alguna duda, por favor, contáctenos.");

                        alerta.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        alerta.show();
                        }
                        else{
                            alerta.setTitle("Error");
                            alerta.setMessage("Ha ocurrido un error durante su tramitación o el servicio no está disponible. Por favor, inténtelo de nuevo más tarde.");

                            alerta.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            alerta.show();
                        }
                    }
                    else{
                        alerta.setTitle("Error");
                        alerta.setMessage("Ha ocurrido un error durante su tramitación o el servicio no está disponible. Por favor, inténtelo de nuevo más tarde.");

                        alerta.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        alerta.show();
                    }

                    } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });



    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


    }


    public static Beneficios newInstance(int sectionnumber){

        Beneficios frag = new Beneficios();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionnumber);
        frag.setArguments(args);
        return frag;

    }


}
