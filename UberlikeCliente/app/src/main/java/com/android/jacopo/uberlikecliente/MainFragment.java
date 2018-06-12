package com.android.jacopo.uberlikecliente;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    public MainFragment() {
        // Required empty public constructor
    }



    Button btnormale, btogg;


    SharedPreferences msp;
    SharedPreferences.Editor editor ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        btnormale = (Button)view.findViewById(R.id.btnormale);

        btogg = (Button)view.findViewById(R.id.btOggetti);



        int mode = Activity.MODE_PRIVATE;
        msp = getContext().getSharedPreferences("SP", mode);
        editor = msp.edit();




        btnormale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent invia = new Intent(getContext(), InviaRichiestaActivity.class);
                invia.putExtra("parametro", "normale");
                startActivity(invia);

            }
        });

        btogg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent invia = new Intent(getContext(), InviaRichiestaActivity.class);
                invia.putExtra("parametro", "oggetto");
                startActivity(invia);

            }
        });





        return view;
    }

}
