package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

/**
 * Created by Yorrick on 13.03.2017.
 *
 *
 * Listenansicht aller Übungen die zu einer Einheit hinzufügbar sind.
 * Kurze Klicks auf Listenelemente fügt Übung der Einheit hinzu.
 * Lange Klicks öffnen Detailansicht der Übung (UebungActivity.java) und
 * erlauben editieren der Übung.
 *
 * Leitet von der "reinen" Liste an Übungen ab
 *
 */

public class FragmentEinheitDetailUebungenListe extends UebungenListeFragment {


    /**
     * Layout und Adapter wird von Mutterklasse geerbt!
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("APP", "onActivityCreated start");


        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), UebungActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(), "test: " + position, Toast.LENGTH_LONG).show();

                return true;
            }
        });
    }


    /**
     * Fügt einer Einheit die angeklickte Liste hinzu und schließt die Ansicht wieder
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO: Verbindung zwischen Position in Liste und DB herstellen
        Toast.makeText(getActivity(), "Uebung " + position + " hinzugefuegt", Toast.LENGTH_SHORT).show();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        //ft.replace(R.id.fragment, new EinheitDetailFragment(), null);
        ft.remove(this);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        getFragmentManager().popBackStack();
        getFragmentManager().executePendingTransactions();

        /**
         * TODO: Aktion: Uebung der Einheit hinzufügen!
         * TODO: Dialog zur Abfrage der gewünschten Dauer einfügen
         */
        //Intent intent = new Intent(getActivity(), UebungActivity.class);
        //startActivity(intent);
    }


}
