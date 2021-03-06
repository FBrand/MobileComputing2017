package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.app.FragmentManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.Util;


/**
 * Hauptmenu mit dem die Anwendung startet.
 * Verlinkt zur Uebungliste, Einheitenliste und Einstellungen.
 * Enthaelt Fragmente MenuFragment, UebungListeFragment und EinheitenListeFragment.
 * In der Hochkant-Ansicht wird jeweils eine Liste über das MenuFragment gestapelt.
 * In Landscape wird links dauerhaft das MenuFragment angezeigt und rechts die jeweilige Liste, Standard ist Uebungsliste.
 */
public class MainActivity extends AppCompatActivity {

    FragmentManager fm;

    Button startdialogButton;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("APP", "onCreate start");
        super.onCreate(savedInstanceState);
        Util.init(this);
        setContentView(R.layout.activity_main);


        //Exercise Class Test
        /*Exercise e = new Exercise();
        e.setContext(getBaseContext());
        e.createTestExercise();
        Log.i("Main", "e");*/

        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.menu_frame, new MenuFragment(),"menu");
        //ft.addToBackStack("menu");
        ft.commit();

        //Startdialog
        boolean startdialogUnterdrücken = false; //Boolean um Startdialog zu unterdrücken
        SharedPreferences shared = this.getSharedPreferences("SHAREDPREFERENCES", Context.MODE_PRIVATE);
        boolean isFirstRun = shared.getBoolean("ISFIRSTRUN", true);

        if(isFirstRun && !startdialogUnterdrücken){
            this.startdialog();

            SharedPreferences.Editor edit = shared.edit();
            edit.putBoolean("ISFIRSTRUN", false);
            edit.putString("LASTDATABASEUPDATE", "0000-00-00 00:00:00");
            edit.commit();
        }

        this.fab = (FloatingActionButton) findViewById(R.id.fab_menu);

        Log.i("APP", "onCreate ende");
    }


    @Override
    public void onBackPressed(){
        getFragmentManager().popBackStack();
        fab.hide();
        if (getFragmentManager().getBackStackEntryCount() == 0){
            super.onBackPressed();
        }
    }

    //Startdialog bauen
    private void startdialog(){
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (MainActivity.this.anzahlRichtigerZeichen(input.getText().toString()) == 0) {
                    MainActivity.this.startdialogButton.setEnabled(false);
                } else {
                    MainActivity.this.startdialogButton.setEnabled(true);
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nutzername = MainActivity.this.trimmeNutzernamen(input.getText().toString());
                SharedPreferences shared = MainActivity.this.getSharedPreferences("SHAREDPREFERENCES", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("USERNAME", nutzername);
                editor.commit();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setTitle("Bitte geben Sie Ihren Nutzernamen ein:");
        dialog.setView(input);
        dialog.setCancelable(false);
        dialog.show();

        this.startdialogButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);
        this.startdialogButton.setEnabled(false);
    }

    private String trimmeNutzernamen(String string) {
        char[] array1 = string.toCharArray();
        while (array1[0] == ' '){
            int laenge = array1.length;
            char[] array2 = new char[laenge-1];
            for(int i = 1; i < laenge; i++) {
                array2[i-1] = array1[i];
            }
            array1 = array2;
        }
        while (array1[array1.length-1] == ' '){
            int laenge = array1.length;
            char[] array2 = new char[laenge-1];
            for(int i = 0; i < laenge-1; i++){
                array2[i] = array1[i];
            }
            array1 = array2;
        }
        return new String(array1);
    }

    private int anzahlRichtigerZeichen(String string) {
        int laenge = string.length();
        int result = laenge;
        for(int i = 0; i < laenge; i++){
            if(string.charAt(i) == ' '){
                result -= 1;
            }
        }
        return result;
    }

    public void resetDB(View v){
        DBHelper db = new  DBHelper(getBaseContext());
        db.onUpgrade(db.getWritableDatabase(),1,1);
        Log.i("Einstellungen", "DB reset");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Account[] accounts = AccountManager.get(this).getAccountsByType("com.google");

            String email = "";
            if(accounts.length > 0) {
                email = accounts[0].name;
            }

            SharedPreferences.Editor edit = this.getSharedPreferences("SHAREDPREFERENCES", Context.MODE_PRIVATE).edit();
            edit.putString("USEREMAIL", email);
            edit.commit();
        }
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        Spinner spinner = (Spinner) view.getRootView().findViewById(R.id.keyword);

        boolean checked = ((RadioButton) view).isChecked();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.default_array, android.R.layout.simple_spinner_item);

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_fitness:
                if (checked)
                    adapter = ArrayAdapter.createFromResource(view.getContext(),
                            R.array.fitness_array, android.R.layout.simple_spinner_item);
                break;
            case R.id.radio_taktik:
                if (checked)
                    adapter = ArrayAdapter.createFromResource(view.getContext(),
                            R.array.taktik_array, android.R.layout.simple_spinner_item);
                break;
            case R.id.radio_technik:
                if (checked)
                    adapter = ArrayAdapter.createFromResource(view.getContext(),
                            R.array.technik_array, android.R.layout.simple_spinner_item);
                break;
            case R.id.radio_torwart:
                if (checked)
                    adapter = ArrayAdapter.createFromResource(view.getContext(),
                            R.array.torwart_array, android.R.layout.simple_spinner_item);
                break;
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.performClick();
    }
}
