package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

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
 */

public class FragmentEinheitDetailUebungenListe extends UebungenListeFragment {


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("APP", "onActivityCreated start");

        // TODO: Eigenen ArrayAdapter schreiben um eine schoenere Liste zu erzeugen
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.uebungTest, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
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
     * Fügt einer Einheit die angeklickte Liste hinzu
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO: Verbindung zwischen Position in Liste und DB herstellen
        Toast.makeText(getActivity(), "Uebung " + position + " hinzugefuegt", Toast.LENGTH_SHORT).show();

        /**
         * TODO: Aktion: Uebung der Einheit hinzufügen!
         * TODO: Dialog zur Abfrage der gewünschten Dauer einfügen
         */
        //Intent intent = new Intent(getActivity(), UebungActivity.class);
        //startActivity(intent);
    }


}
