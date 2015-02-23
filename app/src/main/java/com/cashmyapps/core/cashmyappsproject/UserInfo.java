package com.cashmyapps.core.cashmyappsproject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;


public class UserInfo extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private String resultado;
    private String resultado_final;
    private JSONObject jObject;
    private JSONObject jData;
    private JSONArray jArray;
    private String nombre;
    private String mail;
    private String refer;
    private Account[] accounts;
    private Bundle extras;
    private Boolean success = false;





    public UserInfo() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        final View rootView = lf.inflate(R.layout.fragment_user_info, container, false);

        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        accounts = AccountManager.get(this.getActivity().getApplicationContext()).getAccounts();
        List<String> cuentas = new ArrayList<String>();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                if(!cuentas.contains(possibleEmail))
                    cuentas.add(possibleEmail);
            }

        }

        TextView txNombre = (TextView)rootView.findViewById(R.id.txUENombre);
        TextView txMail = (TextView)rootView.findViewById(R.id.txUEcorreo);
        TextView txCod_refer = (TextView)rootView.findViewById(R.id.txUERefer);
        Button btAceptar = (Button)rootView.findViewById(R.id.btUEAceptar);


        try {

            for(int s=0;s<cuentas.size();s++)
            {
                resultado = new JSONParser(Constantes.URL_GET_BBDD_JSON+"?mail="+cuentas.get(s)).execute(this,"foo").get();

                if(resultado.contains("1"))
                {
                    success = true;
                    resultado_final = resultado;

                }
            }

            jObject = new JSONObject(resultado_final);
            jArray = jObject.getJSONArray("usuarios");
            nombre = jArray.getJSONObject(0).getString("NOMBRE");
            mail = jArray.getJSONObject(0).getString("MAIL");

            txNombre.setText(nombre);
            txMail.setText(mail);
            txCod_refer.setText(refer);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return lf.inflate(R.layout.fragment_user_info, container, false);
    }

    public static UserInfo newInstance(int sectionnumber){

        UserInfo frag = new UserInfo();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionnumber);
        frag.setArguments(args);
        return frag;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }


}
