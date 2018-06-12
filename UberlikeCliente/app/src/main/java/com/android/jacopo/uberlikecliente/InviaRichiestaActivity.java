package com.android.jacopo.uberlikecliente;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class InviaRichiestaActivity extends AppCompatActivity {




    Button btInvia;
    EditText dest, parametro, budget, altro, pagamento ;
    TextView par;

    SharedPreferences msp;
    SharedPreferences.Editor editor ;


    TaskAsincrono task ;
    String c = "";

    String indirizzo_destinazione = "";
    int budget_max;
    String tipo_pagamento;
    int passeggeri, kg;


    String tipo_parametro;


    final int ID_RICHIESTA_PERMISSION = 1;
    LocationManager locationManager;

    double latitudine_p, longitudine_p, latitudine_arrivo, longitudine_arrivo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invia_richiesta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        int mode = Activity.MODE_PRIVATE;
        msp = getSharedPreferences("SP", mode);
        editor = msp.edit();



        btInvia = (Button)findViewById(R.id.btCerca);
        dest = (EditText)findViewById(R.id.editDestinazione);
        parametro = (EditText)findViewById(R.id.editParametro);
        budget = (EditText)findViewById(R.id.editBudget);
        altro = (EditText)findViewById(R.id.editAltro);

        par = (TextView)findViewById(R.id.textParametro);




        tipo_parametro = getIntent().getExtras().getString("parametro");


        final Spinner tipi_pagamento = (Spinner) findViewById(R.id.spinner) ;



        if(tipo_parametro.equals("normale"))
            par.setText("Numero Passeggeri");
        else
            par.setText("Kg oggetto");



        btInvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                requestGPS();



                indirizzo_destinazione = dest.getText().toString();
                budget_max = Integer.parseInt(budget.getText().toString());

                if(tipo_parametro.equals("normale"))
                    passeggeri = Integer.parseInt(parametro.getText().toString());
                else
                    kg = Integer.parseInt(parametro.getText().toString());


                tipo_pagamento = tipi_pagamento.getSelectedItem().toString() ;

                if(tipo_pagamento.equals("Carta di credito"))
                    tipo_pagamento = "0";
                else if(tipo_pagamento.equals("Contanti"))
                    tipo_pagamento = "1";



                task = new TaskAsincrono();
                task.execute((Void) null);


            }
        });





    }


    public void requestGPS()
    {
        // controlliamo se la permission è concessa
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // se arriviamo qui è perchè la permission non è stata ancora concessa
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ID_RICHIESTA_PERMISSION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ID_RICHIESTA_PERMISSION);
            }
        }
        else
        {


            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    latitudine_p = location.getLatitude();
                    longitudine_p = location.getLongitude();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });

        }
    }




    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ID_RICHIESTA_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    requestGPS();


                } else {

                }
                return;
            }
        }
    }



    public String getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);



            latitudine_arrivo = location.getLatitude();
            longitudine_arrivo = location.getLongitude();

            return "OK";

        } catch (Exception e) {
            return null;
        }
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

                getLocationFromAddress(indirizzo_destinazione);




                URL url = new URL("http://uberlikeapp.altervista.org/" +
                        "nuova_richiesta.php?username="+msp.getString("username", "defValue")+
                        "&password="+msp.getString("password","defValue")+
                        "&budget="+budget_max+
                        "&tipo_p="+
                        tipo_pagamento+
                        "&kg="+kg+
                        "&passeggeri="+passeggeri+
                        "&lat_a="+latitudine_arrivo+
                        "&lat_p="+latitudine_p+
                        "&lon_a="+longitudine_arrivo+
                        "&lon_p="+longitudine_p);

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






            } catch (Exception e)
            {

            }

            return null;
        }


        @Override
        protected void onPostExecute(final Boolean success) {


            Toast.makeText(InviaRichiestaActivity.this, "" + c, Toast.LENGTH_SHORT).show();
            c = "" ;

        }

        @Override
        protected void onCancelled() {
            task = null;
            c = "" ;
        }
    }


}
