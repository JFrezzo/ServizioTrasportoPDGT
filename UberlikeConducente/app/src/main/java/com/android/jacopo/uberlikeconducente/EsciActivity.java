package com.android.jacopo.uberlikeconducente;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class EsciActivity extends AppCompatActivity {



    SharedPreferences msp;
    SharedPreferences.Editor editor ;


    TaskAsincrono Task ;


    String c="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esci);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // mostra l'icona della freccia per tornare indietro a sinistra della toolbar
        toolbar.setNavigationIcon(this.getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // quando viene premuta la freccia si verrà rimandati alla pagina precedente

                onBackPressed();
            }
        });





        int mode = Activity.MODE_PRIVATE;
        msp = getSharedPreferences("SP", mode);

        editor = msp.edit();


        TextView tw = (TextView)findViewById(R.id.codice);
        Button bt = (Button)findViewById(R.id.btesci);


        tw.setText(msp.getString("targa", "defValue"));  // metto nella TextView dell'activity presente il nome dell'utente autenticato


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Task = new TaskAsincrono();
                Task.execute((Void) null);


            }
        });
    }



    public class TaskAsincrono  extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute(){
        }

        @Override
        protected void onProgressUpdate(Void[] values) {

        };

        @Override
        protected Boolean doInBackground(Void... params) {
            try {




                URL url = new URL("http://uberlikeapp.altervista.org/in_servizio.php?" +
                        "targa="+msp.getString("targa", "defValue")+
                        "&password="+msp.getString("password","defValue")+
                        "&stato=0");

                HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                InputStream is= conn.getInputStream();

                Scanner file = new Scanner
                        (new InputStreamReader(is)); // Percorso file da leggere

                String s ;

                while(file.hasNext())  // Legge una riga finchè il file non è finito
                {

                    s = file.nextLine();

                    c = c + s + "\n" ;

                }

                JSONArray jsonArray = new JSONArray(c);

                c = "" ;

                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);


                    c = jo.getString("0");


                }

            } catch (Exception e)
            {

            }

            return null;
        }


        @Override
        protected void onPostExecute(final Boolean success) {

         //   Toast.makeText(EsciActivity.this, "" + c, Toast.LENGTH_SHORT).show();



            editor.putString("targa", "");  // resetto la chiave che memorizza il nome utente
            editor.commit();  // Salvare le modifiche

            editor.putString("password", "");  // resetto la chiave che memorizza la password dell'utente
            editor.commit();  // Salvare le modifiche

            editor.putBoolean("session", false);  // imposto "false" la chiave che fa riferimento alla sessione
            editor.commit();

            startActivity(new Intent(EsciActivity.this, AutenticazioneActivity.class));  // Avvio l'activity di Autenticazione





            c = "" ;
        }

        @Override
        protected void onCancelled() {
            Task = null;
            c = "" ;

        }
    }




}
