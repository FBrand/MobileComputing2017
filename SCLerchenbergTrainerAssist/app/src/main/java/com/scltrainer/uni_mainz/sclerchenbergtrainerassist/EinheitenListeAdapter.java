package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

/**
 * Created by Yorrick on 13.03.2017.
 *
 * EinheitenListeAdapter stellt Verbindung zwischen DB und EinheitenListeFragment her.
 *
 * Verwendet fragment_einheiten_liste_element.xml als Layout.
 */

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class EinheitenListeAdapter extends CursorAdapter {

    private LayoutInflater inflator;
    //private int s;
    String TAG = "EinheitenListeAdapter";

    private ArrayList<Integer> idList;

    public EinheitenListeAdapter(Context context, Cursor c) {
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

        TextView einheitListeName = (TextView) view.findViewById(R.id.einheitListeName);//nameEinheit-1
        TextView einheitListeAutor = (TextView) view.findViewById(R.id.einheitListeAutor);//nameAutor-2
        TextView einheitListeSchlagwoerter = (TextView) view.findViewById(R.id.einheitListeSchlagwoerter);//Schlagwörter-3
        //TextView einheitListeSchwerpunkt = (TextView) view.findViewById(R.id.einheitListeSchwerpunkt);//Schwerpunkt-
        RatingBar einheitListeFavoriten = (RatingBar) view.findViewById(R.id.einheitListeFavoriten);//Favorit-4
        TextView einheitListeDauer = (TextView) view.findViewById(R.id.einheitListeZeitaufwand);//Dauer-5

        einheitListeName.setText(cursor.getString(1));
        einheitListeAutor.setText(cursor.getString(2));
        einheitListeSchlagwoerter.setText(cursor.getString(3));
        //einheitListeSchwerpunkt.setText(cursor.getString());
        einheitListeFavoriten.setNumStars(cursor.getInt(4));
        einheitListeDauer.setText(cursor.getString(5));

        idList.add(cursor.getInt(0));

        cursor.moveToNext();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        //Log.i(TAG, "newView");
        //return inflator.inflate(R.layout.vertretungsplan2, null);
        return LayoutInflater.from(context).inflate(R.layout.fragment_einheiten_liste_element, parent, false);
    }


}

