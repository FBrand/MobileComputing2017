package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

/**
 * Created by Yorrick on 13.03.2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class UebungenListeAdapter extends CursorAdapter {

    private LayoutInflater inflator;
    //private int s;
    String TAG = "UebungenListeAdapter";

    /**
     * Dient zum Speichern der Reihenfolge der Ids der ListView-Einträge
     * um die Detailansicht öffnen zu können.
     */
    private ArrayList<Integer> idList;

    public UebungenListeAdapter(Context context, Cursor c) {
        super(context, c);
        inflator = LayoutInflater.from(context);
        //s = c.getCount();
        idList = new ArrayList<Integer>();
    }

    public int getIdListEntry(int entry){
        return idList.get(entry);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        if(cursor == null) return;



        TextView uebungenListeName = (TextView) view.findViewById(R.id.uebungListeName);//nameEinheit-1
        TextView uebungenListeAutor = (TextView) view.findViewById(R.id.uebungenListeAutor);//nameAutor-2
        TextView uebungenListeSchlagwoerter = (TextView) view.findViewById(R.id.uebungListeSchlagwoerter);//Schlagwörter-3
        //TextView uebungenListeSchwerpunkt = (TextView) view.findViewById(R.id.uebungenListeSchwerpunkt);//Schwerpunkt-
        RatingBar uebungenListeFavoriten = (RatingBar) view.findViewById(R.id.uebungListeFavoriten);//Favorit-4
        TextView uebungenListeDauer = (TextView) view.findViewById(R.id.uebungListeZeitaufwand);//Dauer-5

        uebungenListeName.setText(cursor.getString(1));
        uebungenListeAutor.setText(cursor.getString(2));
        uebungenListeSchlagwoerter.setText(cursor.getString(3));
        //uebungenListeSchwerpunkt.setText(cursor.getString());
        uebungenListeFavoriten.setNumStars((int)cursor.getDouble(4));
        uebungenListeDauer.setText(cursor.getString(5));

        //TODO: Prüfen ob hier die von der Tabelle gesetzten ID übernommen wird oder die von Hand gesetzte.
        idList.add(cursor.getInt(0));
        //Log.i(TAG, "_id: " + idList.get(cursor.getInt(0)-1));

        cursor.moveToNext();


    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        //Log.i(TAG, "newView");
        //return inflator.inflate(R.layout.vertretungsplan2, null);
        return LayoutInflater.from(context).inflate(R.layout.fragment_uebungen_liste_element, parent, false);
    }

}


