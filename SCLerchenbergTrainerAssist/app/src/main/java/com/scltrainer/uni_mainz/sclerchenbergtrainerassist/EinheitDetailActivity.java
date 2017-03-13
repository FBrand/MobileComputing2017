package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Übersicht einer einzelnen Einheit und ihrer Übungen.
 */
public class EinheitDetailActivity extends AppCompatActivity {

    FragmentManager fm;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("EinheitDetailActivity", "start onCreate");
        setContentView(R.layout.activity_einheit_detail);
        Log.i("EinheitDetailActivity", "starting Fragment creation");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment, new EinheitDetailFragment(),"EinheitDetail");
        //ft.addToBackStack("menu");
        ft.commit();

        /**
         * erzeugt FloatingActionButton mit "Plus"-Icon zum, je nach Fragment,
         * hinzufügen oder filtern von Übungen
         */
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getFragmentManager().getBackStackEntryCount()== 0){
                    doTransaction(R.id.fragment, new UebungenListeFragment(), "EinheitDetailUebungListe");
                    fab.setImageResource(android.R.drawable.ic_menu_search);
            }
        }});

        /**
         * verhindert "Zurück-Pfeil" oben links
         */
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    private void doTransaction(int frameID, Fragment fragment, String tag){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(frameID, fragment, tag);
        ft.addToBackStack(tag);
        ft.commit();
    }

    @Override
    public void onBackPressed(){
        if (getFragmentManager().getBackStackEntryCount()== 0){
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
            fab.setImageResource(android.R.drawable.ic_menu_add);
        }
    }

}
