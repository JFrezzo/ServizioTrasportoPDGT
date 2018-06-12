package com.android.jacopo.uberlikeconducente;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MappaActivity  extends FragmentActivity {




    SharedPreferences msp;
    SharedPreferences.Editor editor ;

    TaskAsincrono Task ;
    String c = "", latitudine, longitudine ;
    GoogleMap map ;


    Button accetta, rifiuta;

    int scelta; // 1 = Accetta, 2 = Rifiuta

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mappa);




        int mode = Activity.MODE_PRIVATE;
        msp = getSharedPreferences("SP", mode);
        editor = msp.edit();


        accetta = (Button)findViewById(R.id.btAccetta);
        rifiuta = (Button)findViewById(R.id.btRifiuta);






        latitudine = getIntent().getExtras().getString("latitudine");
        longitudine = getIntent().getExtras().getString("longitudine");
        id = getIntent().getExtras().getString("id");


        LatLng STARTING_POINT=new LatLng(Double.parseDouble(latitudine), Double.parseDouble(longitudine));


        map =((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(STARTING_POINT, 15));


        map.addMarker(new MarkerOptions().position(STARTING_POINT));





        accetta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Task = new TaskAsincrono();
                Task.execute((Void) null) ;

                scelta = 1;



            }
        });

        rifiuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Task = new TaskAsincrono();
                Task.execute((Void) null);

                scelta = 2;

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




                URL url = new URL("http://uberlikeapp.altervista.org/stato_servizio.php?" +
                        "targa="+msp.getString("targa", "defValue")+
                        "&password="+msp.getString("password","defValue")+
                        "&stato="+scelta+
                        "&id="+id);

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




            Toast.makeText(MappaActivity.this, "" + c, Toast.LENGTH_SHORT).show();


            Intent invia = new Intent(MappaActivity.this, MainActivity.class);

            startActivity(invia);


            c = "" ;
        }

        @Override
        protected void onCancelled() {
            Task = null;
            c = "" ;

        }
    }

}
