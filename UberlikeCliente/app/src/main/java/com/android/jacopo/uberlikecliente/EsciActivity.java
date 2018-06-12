package com.android.jacopo.uberlikecliente;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EsciActivity extends AppCompatActivity {



    SharedPreferences msp;
    SharedPreferences.Editor editor ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esci);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

/*
        // mostra l'icona della freccia per tornare indietro a sinistra della toolbar
        toolbar.setNavigationIcon(this.getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // quando viene premuta la freccia si verrà rimandati alla pagina precedente

                onBackPressed();
            }
        });

*/



        int mode = Activity.MODE_PRIVATE;
        msp = getSharedPreferences("SP", mode);

        editor = msp.edit();


        TextView tw = (TextView)findViewById(R.id.codice);
        Button bt = (Button)findViewById(R.id.btesci);


        tw.setText(msp.getString("username", "defValue"));  // metto nella TextView dell'activity presente il nome dell'utente autenticato


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                editor.putString("username", "");  // resetto la chiave che memorizza il nome utente
                editor.commit();  // Salvare le modifiche

                editor.putString("password", "");  // resetto la chiave che memorizza la password dell'utente
                editor.commit();  // Salvare le modifiche

                editor.putBoolean("session", false);  // imposto "false" la chiave che fa riferimento alla sessione
                editor.commit();

                startActivity(new Intent(EsciActivity.this, AutenticazioneActivity.class));  // Avvio l'activity di Autenticazione

            }
        });
    }



}
