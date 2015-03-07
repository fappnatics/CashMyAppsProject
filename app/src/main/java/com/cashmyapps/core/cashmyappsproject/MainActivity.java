package com.cashmyapps.core.cashmyappsproject;

import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.*;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private OnFragmentInteractionListener mListener;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Fragment fragment;
    private Intent intent;
    private Bundle savedinstance;
    private Context context;
    private Account[] accounts;
    private String[] cuentas_array;
    private View header;
    private String cuenta;
    private Intent i;


    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        savedinstance = savedInstanceState;
        context = this.getApplicationContext();


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(120,0,47)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        TextView txNombre = (TextView)findViewById(R.id.txNombre);
        i = getIntent();


        ImageView im = (ImageView)findViewById(R.id.backgrd);
        Bitmap bm = RoundedImageView.getCroppedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.user_info),180);
        im.setImageBitmap(bm);

        try {

            TextView txCorreo = (TextView)findViewById(R.id.txCorreo);
            TextView txSaldo = (TextView)findViewById(R.id.txSaldo);
            cuenta = i.getExtras().getString("cuenta");



            String resultado = new JSONParser(Constantes.URL_GET_BBDD_JSON+"?mail="+cuenta).execute(this,"foo").get();

            if(resultado.contains("{\"success\":0}")){
                Toast.makeText(context,"Oooops, ha sucedido un error en el alta de usuario, por favor, inténtalo de nuevo más tarde",Toast.LENGTH_LONG);

            }

            JSONObject jObject = new JSONObject(resultado);
            JSONArray jArray = jObject.getJSONArray("usuarios");

            txNombre.setText(jArray.getJSONObject(0).getString("NOMBRE"));
            txCorreo.setText(jArray.getJSONObject(0).getString("MAIL"));
            txSaldo.setText(jArray.getJSONObject(0).getString("SALDO")+" Coins");



        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragment = null;

        switch (position){

            case 0:

                fragment = new UserInfo();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, UserInfo.newInstance(position + 1))
                        .commit();

                getSupportActionBar().setTitle(getResources().getString(R.string.titulo_user_info));
                getSupportActionBar().setSubtitle(getResources().getString(R.string.subtitulo_user_info));

                break;

            case 1:
                fragment = new Compartir();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, Compartir.newInstance(position + 1))
                        .commit();

                getSupportActionBar().setTitle(getResources().getString(R.string.titulo_compartir));
                getSupportActionBar().setSubtitle(getResources().getString(R.string.subtitulo_compartir));

                break;
            case 2:

                fragment = new ListaApps();

                 fragmentManager.beginTransaction()
                        .replace(R.id.container, ListaApps.newInstance(position + 1))
                        .commit();
                getSupportActionBar().setTitle(getResources().getString(R.string.titulo_lista_apps));
                getSupportActionBar().setSubtitle(getResources().getString(R.string.subtitulo_compartir));

                break;

            case 3:
                fragment = new Ranking();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, Ranking.newInstance(position+1))
                        .commit();
                getSupportActionBar().setTitle(getResources().getString(R.string.titulo_ranking));
                getSupportActionBar().setSubtitle(getResources().getString(R.string.subtitulo_ranking));
                break;

            case 4:
                fragment = new Beneficios();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, Beneficios.newInstance(position + 1))
                        .commit();
                getSupportActionBar().setTitle(getResources().getString(R.string.titulo_cobros));
                getSupportActionBar().setSubtitle(getResources().getString(R.string.subtitulo_cobros));
                break;
            default:
                Toast.makeText(this.getApplicationContext(), "Opcion no disponible", Toast.LENGTH_LONG);
                fragment = new Compartir();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, Compartir.newInstance(position + 1))
                        .commit();
                break;
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.titulo_lista_apps);
                break;
            case 2:
                mTitle = getString(R.string.titulo_compartir);
                break;
            case 3:
                mTitle = getString(R.string.titulo_cobros);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.DISPLAY_USE_LOGO);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {



        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem shareOpt = menu.findItem(R.id.action_settings);
        ShareActionProvider myShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareOpt);
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, "Únete a CashMyApps " + "http://oi58.tinypic.com/24wgivr.jpg");
        myShareActionProvider.setShareIntent(i);

        return true;
    }



    @Override
    public void onBackPressed() {

        LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(R.layout.alerta_icono,null);
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setView(view);


        alerta.setTitle("Salir");
        alerta.setMessage("¿Seguro que quieres salir de la aplicación?");

        alerta.setPositiveButton("Sí",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataManagment da = new DataManagment(Constantes.CONEXION_USUARIO.replace("[MAIL]",cuenta).replace("[CONECTADO]","N"));
                da.execute(this,"foo");
            }
        });

        alerta.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        alerta.show();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
             
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    private class DataManagment extends AsyncTask<Object, Void, String> {

       String result = "";

        private String url_select;

        public DataManagment(String url) {
            this.url_select = url;

        }

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected String doInBackground(Object... params) {


            ArrayList<NameValuePair> param = new ArrayList<>();

            try {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url_select);
                httpPost.setEntity(new UrlEncodedFormEntity(param));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");


            } catch (Exception e) {
                Log.e("Error  result ", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String v) {

            if (!v.contains("{\"success\":1}")) {

                Log.i("ERROR PARSER", "!!");

            }

            finish();


        }


    }

}
