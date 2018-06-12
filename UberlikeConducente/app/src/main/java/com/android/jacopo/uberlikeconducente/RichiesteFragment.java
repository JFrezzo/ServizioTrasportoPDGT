package com.android.jacopo.uberlikeconducente;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * A simple {@link Fragment} subclass.
 */
public class RichiesteFragment extends Fragment {


    public RichiesteFragment() {
        // Required empty public constructor
    }



    TaskAsincrono Task ;
    String[] str = null ;
    String c = "" ;
    private ArrayAdapter adapter = null;

    ListView lv ;

    SharedPreferences msp;
    SharedPreferences.Editor editor ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_richieste, container, false);

        lv = (ListView)view.findViewById(R.id.listView);
        adapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1);
        lv.setAdapter(adapter);

        int mode = Activity.MODE_PRIVATE;
        msp = getContext().getSharedPreferences("SP", mode);
        editor = msp.edit();







        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {




                String elemento[] = parent.getItemAtPosition(position).toString().split("\n");


                Intent invia = new Intent(getContext(), MappaActivity.class);
                invia.putExtra("latitudine", elemento[6]);
                invia.putExtra("longitudine", elemento[5]);
                invia.putExtra("id", elemento[2]);

                if(elemento[4].equals("0")) {
                    startActivity(invia);
                }



            }
        });



        Task = new TaskAsincrono();
        Task.execute((Void) null);


        return view ;
    }

    public static String getLocationName(Activity activity, String longitudine, String latitudine){

        String nome = "Indirizzo non presente";

        Geocoder geocoder = new Geocoder(activity);
        try {
            Address address = geocoder.getFromLocation(Double.parseDouble(latitudine), Double.parseDouble(longitudine), 1).get(0);
            nome = address.getThoroughfare();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return nome;
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

                URL url = new URL("http://uberlikeapp.altervista.org/richieste_conducente.php?" +
                        "targa="+msp.getString("targa", "defValue")+
                        "&password="+msp.getString("password","defValue"));


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
                str = new String[jsonArray.length()];

                c = "" ;

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jo = jsonArray.getJSONObject(i);

                    String destinazione = getLocationName(getActivity(), jo.getString("Longitudine_arrivo"), jo.getString("Latitudine_arrivo"));

                    str[i] = "to:" + destinazione+
                            "\n" + "Stato richiesta: "+(jo.getString("Stato_servizio").equals("0") ? "Non ancora accettata" : "Accettata")+
                            "\n" + jo.getString("Id") +
                            "\n" +
                            "\n" + jo.getString("Stato_servizio") +
                            "\n" + jo.getString("Longitudine_partenza")+
                            "\n" + jo.getString("Latitudine_partenza");
                }

            } catch (Exception e)
            {

            }

            return null;
        }


        @Override
        protected void onPostExecute(final Boolean success) {


            for(int i = 0 ; i < str.length ; i++)
            {
                adapter.add(str[i]);
            }

            c = "" ;
        }

        @Override
        protected void onCancelled() {
            Task = null;
            c = "" ;
        }
    }

}


