package com.android.jacopo.uberlikeconducente;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class AggiornaActivity extends AppCompatActivity {


    Button bt;

    TextView lon, lat;

    double lat_c,lon_c;


    SharedPreferences msp;
    SharedPreferences.Editor editor ;



    final int ID_RICHIESTA_PERMISSION = 1;
    LocationManager locationManager;


    TaskAsincrono Task ;


    String c="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiorna);
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


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        bt = (Button)findViewById(R.id.btposizione);

        lat = (TextView)findViewById(R.id.textlat);
        lon = (TextView)findViewById(R.id.textlon);



        int mode = Activity.MODE_PRIVATE;
        msp = getSharedPreferences("SP", mode);

        editor = msp.edit();




        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                requestGPS();


                if(lat_c != 0.0 && lon_c != 0.0) {


                    lat.setText(""+lat_c);

                    lon.setText(""+lon_c);

                    Task = new TaskAsincrono();
                    Task.execute((Void) null);
                }

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

                    lat_c = location.getLatitude();
                    lon_c = location.getLongitude();
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




                URL url = new URL("http://uberlikeapp.altervista.org/coordinate.php?" +
                        "targa="+msp.getString("targa", "defValue")+
                        "&password="+msp.getString("password","defValue")+
                        "&latitudine="+lat_c+
                        "&longitudine="+lon_c);

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

               Toast.makeText(AggiornaActivity.this, "" + c, Toast.LENGTH_SHORT).show();


            c = "" ;
        }

        @Override
        protected void onCancelled() {
            Task = null;
            c = "" ;

        }
    }




}

