package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Yorrick on 13.03.2017.
 */

public class EinheitenDetailAdapter extends CursorAdapter {

    private LayoutInflater inflator;
    //private int s;
    String TAG = "EinheitDetailAdapter";

    public EinheitenDetailAdapter(Context context, Cursor c) {
        super(context, c);
        inflator = LayoutInflater.from(context);

        //s = c.getCount();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        if(cursor == null) return;

        TextView einheitName = (TextView) view.findViewById(R.id.einheitName);//nameEinheit-1
        TextView einheitAutor = (TextView) view.findViewById(R.id.einheitAutor);//nameAutor-2
        TextView einheitBeschreibung = (TextView) view.findViewById(R.id.einheitBeschreibung);//Beschreibung-3
        //TextView einheitSchwerpunkt = (TextView) view.findViewById(R.id.einheitSchwerpunkt);//Schwerpunkt-
        RatingBar einheitFavoriten = (RatingBar) view.findViewById(R.id.einheitFavoriten);//Favorit-4
        TextView einheitDauer = (TextView) view.findViewById(R.id.einheitDauer);//Dauer-5
        TextView einheitLetzteAenderung = (TextView) view.findViewById(R.id.einheitLetzteAenderung);//letzte Änderung-6

        cursor.moveToFirst();

        einheitName.setText(cursor.getString(1));
        einheitAutor.setText(cursor.getString(2));
        einheitBeschreibung.setText(cursor.getString(3));
        //einheitSchwerpunkt.setText(cursor.getString());
        einheitFavoriten.setNumStars(cursor.getInt(4));
        einheitDauer.setText(cursor.getString(5));
        einheitLetzteAenderung.setText(cursor.getString(6));

        cursor.moveToNext();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return null;
    }

}
