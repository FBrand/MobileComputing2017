package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;


/**
 * Enthält das Hauptmenü und die Verknüpfungen zu ÜbungenListe, EinheitenListe und Einstellungen
 */
public class MenuFragment extends ListFragment implements OnItemClickListener {

    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.i("APP", "onCreateView start");
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_menu);
        Log.i("APP", "onCreate ende");
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("APP", "onActivityCreated start");

        // TODO: Eigenen ArrayAdapter schreiben um eine schoenere Liste zu erzeugen
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.menu, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
        Log.i("APP", "onActivityCreated ende");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        //Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();

        replaceMenuWithList(position);
        //Hochkant

    }

    public void replaceMenuWithList(int position){
        switch (position){
            case 0:
                doTransaction(R.id.menu_frame, new UebungenListeFragment(), "uebungsliste");
                fab.show();
                break;
            case 1:
                doTransaction(R.id.menu_frame, new EinheitenListeFragment(), "einheitenliste");
                fab.show();
                break;
            case 2:
                doTransaction(R.id.menu_frame, new EinstellungenFragment(), "einstellungen");
                break;
            case 3:
                this.downloadDiaglog();
                break;
            default:
                Toast.makeText(getActivity(), "default switch case", Toast.LENGTH_LONG);
        }
    }

    private void aktualisiereDatenbank() {
        SharedPreferences shared = getActivity().getSharedPreferences("SHAREDPREFERENCES", Context.MODE_PRIVATE);
        String lastUpdate = shared.getString("LASTDATABASEUPDATE", "");

        try {
            GlobalDBConnection.fetch(DBInfo.EXERCISE_TABLE_NAME, getActivity(), lastUpdate);
            GlobalDBConnection.fetch(DBInfo.TRAININGSUNIT_TABLE_NAME, getActivity(), lastUpdate);
            GlobalDBConnection.fetch(DBInfo.TRAININGSUNITEXERCISE_TABLE_NAME, getActivity(), lastUpdate);
            Toast.makeText(getActivity(), "Aktualisieren erfolgreich.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Fehler beim Aktualisieren der Datenbank.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        SharedPreferences.Editor editor = shared.edit();

        java.util.Calendar c = new java.util.GregorianCalendar();
        String year = String.valueOf(c.get(java.util.Calendar.YEAR));
        int monthInt = c.get(java.util.Calendar.MONTH) +1;
        String month = myToString(monthInt);
        int dayInt = c.get(java.util.Calendar.DAY_OF_MONTH);
        String day = myToString(dayInt);
        int hourInt = c.get(java.util.Calendar.HOUR_OF_DAY);
        String hour = myToString(hourInt);
        int minuteInt = c.get(java.util.Calendar.MINUTE);
        String minute = myToString(minuteInt);
        int secondInt = c.get(java.util.Calendar.SECOND);
        String second = myToString(secondInt);

        String currentDateTime = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
        editor.putString("LASTDATABASEUPDATE", currentDateTime);

    }

    private void downloadDiaglog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                aktualisiereDatenbank();
            }
        });
        builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        AlertDialog dialog = builder.create();
        dialog.setTitle("Hinweis");
        dialog.setMessage("Beim Aktualisieren der Daten wird Internetzugang benötigt. Dies kann unter Umständen Ihr mobiles Datenvolumen in Anspruch nehmen. Wollen Sie die Aktualisierung starten?");
        dialog.setCancelable(false);
        dialog.show();
    }

    private String myToString(int value){
        String result = "";
        if(value < 10){
            result = "0" + String.valueOf(value);
        } else {
            result = String.valueOf(value);
        }
        return result;
    }

    private void doTransaction(int frameID, Fragment fragment, String tag){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(frameID, fragment, tag);
        ft.addToBackStack(tag);
        ft.commit();
    }


    public void onBackPressed(){
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }
}