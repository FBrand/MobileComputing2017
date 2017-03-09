package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.app.FragmentManager;
import android.widget.Toast;


/**
 * Hauptmenu mit dem die Anwendung startet.
 * Verlinkt zur Uebungliste, Einheitenliste und Einstellungen.
 * Enthaelt Fragmente MenuFragment, UebungListeFragment und EinheitenListeFragment.
 * In der Hochkant-Ansicht wird jeweils eine Liste über das MenuFragment gestapelt.
 * In Landscape wird links dauerhaft das MenuFragment angezeigt und rechts die jeweilige Liste, Standard ist Uebungsliste.
 */
public class MainActivity extends AppCompatActivity {

    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("APP", "onCreate start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.menu_frame, new MenuFragment(),"menu");
        ft.addToBackStack("menu");
        ft.commit();
        Log.i("APP", "onCreate ende");
    }





}
