package com.android.jacopo.uberlikeconducente;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AutenticazioneActivity extends AppCompatActivity {

    Button bt ;  // button accedi


    EditText targa , password ;

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;


    LoginTask login ;

    SharedPreferences msp;
    SharedPreferences.Editor editor ;

    String c = "" ;  // variabile che verrà utilizzata per contenere la risposta del server

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticazione);


        bt = (Button)findViewById(R.id.btlogin);

        targa = (EditText)findViewById(R.id.etTarga);
        password = (EditText)findViewById(R.id.etPassword);


        int mode = Activity.MODE_PRIVATE;

        // Creare o riprendere un determinato shared preference
        msp = getSharedPreferences("SP", mode);
        // Editor che consente di modificare i dati di un determinato shared preference
        editor = msp.edit();





        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login = new LoginTask();
                login.execute(targa.getText().toString(), password.getText().toString());



            }
        });



        if(msp.getBoolean("session",false))
        {

            targa.setText(msp.getString("targa", "defValue"));

            password.setText(msp.getString("password", "defValue"));

            login = new LoginTask();
            login.execute(targa.getText().toString(), password.getText().toString());

        }

    }

    @Override
    public void onBackPressed() {  // per impedire che il tasto indietro rimandi alla pagina precedente

        //  super.onBackPressed();
    }




    public class LoginTask  extends AsyncTask<String, String, String> {


        ProgressDialog pdLoading = new ProgressDialog(AutenticazioneActivity.this);
        HttpURLConnection conn;
        URL url = null;



        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();


        }


        /*
        @Override
        protected void onProgressUpdate(Void[] values) {

        };

        */


        @Override
        protected String doInBackground(String... params) {
            try {




                url = new URL("http://uberlikeapp.altervista.org/login_conducente.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {



                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");




                conn.setDoInput(true);
                conn.setDoOutput(true);


                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("targa", params[0])
                        .appendQueryParameter("password", params[1]);
                String query = builder.build().getEncodedQuery();






                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();






            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();


                if (response_code == HttpURLConnection.HTTP_OK) {


                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }




                    return(result.toString());


                }else{



                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }


        @Override
        protected void onPostExecute(String result) {






            pdLoading.dismiss();


            if(result.equals("false"))
            {

                Toast.makeText(AutenticazioneActivity.this, "Accesso rifiutato", Toast.LENGTH_SHORT).show();

                editor.putBoolean("session",false);
                editor.commit();
            }
            else
            {
                if(result.equals("true")) {



                    /* Faccio in modo, tramite l'utilizzo di SharedPreferences, che ogni contenitore contenga il dato opportuno */

                    editor.putString("targa", targa.getText().toString());
                    editor.commit();  // Salvare le modifiche

                    editor.putString("password", password.getText().toString());
                    editor.commit();  // Salvare le modifiche


                    editor.putBoolean("session", true);
                    editor.commit();

                    startActivity(new Intent(AutenticazioneActivity.this, MainActivity.class));

                }
                else
                    Toast.makeText(AutenticazioneActivity.this, "C'è stato un problema con l'autenticazione ", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {


            login = null;
        }
    }


}


