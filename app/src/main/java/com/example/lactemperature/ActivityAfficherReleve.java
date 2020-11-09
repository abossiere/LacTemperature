package com.example.lactemperature;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class ActivityAfficherReleve extends Activity {


    final String[] leLac= new String[1];
    final String[] laTemp= new String[1];
    final String[] leSigne= new String[1];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_releve);

        final EditText Date = findViewById(R.id.dateReleveAfficherReleve);
        Button buttonAfficherReleveValider = findViewById(R.id.buttonAfficherReleveValider);
        Button buttonAfficherReleveAnnuler = findViewById(R.id.buttonAfficherReleveAnnuler);

        //on place un écouteur dessus:
        View.OnClickListener ecouteur1 = new View.OnClickListener() {
            //on implémente la méthode onclick
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonAfficherReleveValider:
                        // enregistrer les données dans la base
                        //on passer les infos dans l'autre interface
                        Intent i = new Intent (ActivityAfficherReleve.this, ActivityAfficheReleve.class);
                        i.putExtra("EXTRA_LAC",leLac[0]);
                        i.putExtra("EXTRA_TEMP",laTemp[0]);
                        i.putExtra("EXTRA_DATE",Date.getText().toString());
                        i.putExtra("EXTRA_SIGNE",leSigne[0]);
                        startActivityForResult(i, 0);
                        Toast.makeText(getApplicationContext(), "Ouverture de l'affichage", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.buttonAfficherReleveAnnuler:
                        finish();
                        Toast.makeText(getApplicationContext(), "Annulation de l'affichage", Toast.LENGTH_LONG).show();
                        break;


                }

            }
        };
        //on affecte l'écouteur aux boutons:
        buttonAfficherReleveValider.setOnClickListener(ecouteur1);
        buttonAfficherReleveAnnuler.setOnClickListener(ecouteur1);

        //programmation des boutons radios
        RadioGroup radioGroupTemp = findViewById(R.id.radioGroupAfficheReleve);
        radioGroupTemp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged (RadioGroup radioGroupTemp,int i){
                switch (i) {
                    case R.id.radioButtonAffiche6:
                        Toast.makeText(getApplicationContext(), " 6h",
                                Toast.LENGTH_LONG).show();
                        laTemp[0] ="6";
                        break;
                    case R.id.radioButtonAffiche12:
                        Toast.makeText(getApplicationContext(), " 12h",
                                Toast.LENGTH_LONG).show();
                        laTemp[0] ="12";
                        break;
                    case R.id.radioButtonAffiche18:

                        Toast.makeText(getApplicationContext(), " 18h",
                                Toast.LENGTH_LONG).show();
                        laTemp[0] ="18";
                        break;
                    case R.id.radioButtonAffiche24:

                        Toast.makeText(getApplicationContext(), " 24h",
                                Toast.LENGTH_LONG).show();
                        laTemp[0] ="24";
                        break;
                }
            }

        });

//programmation des boutons radios
        RadioGroup radioGroupSigne = findViewById(R.id.radioGroupSigne);
        radioGroupSigne.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged (RadioGroup radioGroupSigne,int i){
                switch (i) {
                    case R.id.radioButton:
                        Toast.makeText(getApplicationContext(), " Degrés Celsius",
                                Toast.LENGTH_LONG).show();
                        leSigne[0] ="°C";
                        break;
                    case R.id.radioButton2:
                        Toast.makeText(getApplicationContext(), " Degrés Fahrenheit",
                                Toast.LENGTH_LONG).show();
                        leSigne[0] ="°F";
                        break;

                }
            }

        });

        //gestion de la liste déroulante des Lacs
        final Spinner spinnerAfficheLac = (Spinner) findViewById(R.id.spinnerAfficheLac);
        DAOBdd lacbdd = new DAOBdd(this);
        lacbdd.open();
        Cursor c = lacbdd.getDataLac();
        ArrayList<String> leslacs = new ArrayList<String>();
        if (c.moveToFirst()) {

            if (c.getCount() != 0) {

                while (!c.isAfterLast()) {
                    leslacs.add(c.getString(1)); //add the item
                    c.moveToNext();
                }
            }
        }
        ArrayAdapter<String> dataAdapterR = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, leslacs);
        dataAdapterR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAfficheLac.setAdapter(dataAdapterR);
        spinnerAfficheLac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                leLac[0] = String.valueOf(spinnerAfficheLac.getSelectedItem());
                Toast.makeText(ActivityAfficherReleve.this, "Vous avez choisie : " + "\n" + leLac[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





//gestion du calendrier
            final Calendar myCalendar = Calendar.getInstance();
        final EditText edittext = findViewById(R.id.dateReleveAfficherReleve);
        edittext.requestFocus();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(edittext, myCalendar);
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ActivityAfficherReleve.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel(EditText edittext, Calendar myCalendar){
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }


}