package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

public class FragmentEinheitDetailUebungenListe extends UebungenListeFragment implements DurationDialog.DurationDialogListener{
    private int exercise;
    private int trainingsunit;

    FloatingActionButton fab;

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
                Intent intent = new Intent(getActivity(), UebungActivity.class);
                //TODO: Anpassen dass hier nicht die Position sondern die DB ID genommen wird
                intent.putExtra("_id", position+1);
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
     * @param posUebung
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int posUebung, long id) {
        //TODO: Verbindung zwischen Position in Liste und DB herstellen

        //neu
        //TODO:Intent von EinheitDetailActivity mit position der Einheit schicken

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras == null) {
            Log.e("FragmentEDUL", "FEHLER");
        }
        int posEinheit = extras.getInt("_id");

        addUebungToEinheit(posEinheit, this.getListAdapter().getIds().get(posUebung), getActivity().getBaseContext());

    }

    //neu
    public void addUebungToEinheit(int einheit, int position, Context context){

        exercise = position;
        trainingsunit = einheit;

        DurationDialog d = new DurationDialog();
        d.addListener(this);
        d.show(getFragmentManager(),"Übungsdauer eingeben");

    }


    @Override
    public void onDialogPoistiveClick(int duration) {
        DBConnection dbConnection = DBHelper.getConnection(this.getActivity().getBaseContext());
        ContentValues row = new ContentValues();
        row.put(DBInfo.TRAININGSUNITEXERCISE_COLUMN_NAME_TRAININGSUNIT, trainingsunit);
        row.put(DBInfo.TRAININGSUNITEXERCISE_COLUMN_NAME_EXERCISE, exercise);
        row.put(DBInfo.TRAININGSUNITEXERCISE_COLUMN_NAME_DURATION, duration);
        dbConnection.insert(DBInfo.TRAININGSUNITEXERCISE_TABLE_NAME, row);

        Toast.makeText(getActivity(), "Uebung " + exercise + " hinzugefuegt", Toast.LENGTH_SHORT).show();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        //ft.replace(R.id.fragment, new EinheitDetailFragment(), null);
        ft.remove(this);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.show();
        getFragmentManager().popBackStack();
        getFragmentManager().executePendingTransactions();
    }

    @Override
    public void onDialogNegativeClick(int duration) {

    }
}
