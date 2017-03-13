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
        //ft.addToBackStack(null);
        ft.commit();

        /**
         * erzeugt FloatingActionButton mit "Plus"-Icon zum, je nach Fragment,
         * hinzufügen oder filtern von Übungen
         */
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("EinheitenDetailActivity", "BackstackEntryCount for FAB: " +fm.getBackStackEntryCount());
                if (fm.getBackStackEntryCount()== 0){
                    doTransaction(R.id.fragment, new FragmentEinheitDetailUebungenListe(), "EinheitDetailUebungListe");
                    fab.setImageResource(android.R.drawable.ic_menu_search);
                } else {
                    /**
                     * TODO: Hier soll der Filterdialog hin!
                     */
                    int index = fm.getBackStackEntryCount() - 1;
                    FragmentManager.BackStackEntry backEntry = getFragmentManager().getBackStackEntryAt(index);
                    Log.i("EinheitDetailActivity", "Backstack last TAG: " +backEntry.getName());
                    Toast.makeText(getApplicationContext(), "Filter an", Toast.LENGTH_SHORT).show();
                }

        }});

        /**
         * verhindert "Zurück-Pfeil" oben links
         */
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);


    }

    /**
     * Benötigt globalen FragmentManager
     * @param frameID
     * @param fragment
     * @param tag
     */
    private void doTransaction(int frameID, Fragment fragment, String tag){
       // fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(frameID, fragment, tag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(tag);
        ft.commit();
        fm.executePendingTransactions();
        Log.i("EinheitenDetailActivity", "BackstackEntryCount: " +fm.getBackStackEntryCount());
        Log.i("EinheitenDetailActivity", "do Transaction fertig");



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
