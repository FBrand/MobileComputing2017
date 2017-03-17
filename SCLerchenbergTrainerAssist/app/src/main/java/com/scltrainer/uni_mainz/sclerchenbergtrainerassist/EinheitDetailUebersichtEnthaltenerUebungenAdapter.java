package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;
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

import static android.view.View.GONE;


public class EinheitDetailUebersichtEnthaltenerUebungenAdapter extends CursorAdapter {



    private ArrayList<Integer> ids = new ArrayList<Integer>();
    private ArrayList<Integer> trainingsunitexerciseID = new ArrayList<Integer>();

    private LayoutInflater inflator;

    //private int s;

    String TAG = "EDUEUAdapter";



    /**
     * Dient zum Speichern der Reihenfolge der Ids der ListView-Einträge
     * um die Detailansicht öffnen zu können.
     */
    private ArrayList<Integer> idList;



    public EinheitDetailUebersichtEnthaltenerUebungenAdapter(Context context, Cursor c) {

        super(context, c);

        inflator = LayoutInflater.from(context);

        //s = c.getCount();

        idList = new ArrayList<Integer>();

    }



    public int getIdListEntry(int entry){

        return idList.get(entry);

    }
    public ArrayList<Integer> getTrainingsunitexerciseID(){
        return trainingsunitexerciseID;
    }




    @Override
    public void bindView(View view, Context context, Cursor cursor) {



        if(cursor == null) return;





        ids.add(cursor.getInt(0));


        TextView uebungenListeName = (TextView) view.findViewById(R.id.uebungListeName);//nameUebung-1
        TextView uebungenListeDauer = (TextView) view.findViewById(R.id.uebungListeZeitaufwand);//Dauer-2
        TextView uebungenListeAutor = (TextView) view.findViewById(R.id.uebungenListeAutor);
        TextView uebungenListeKeywords = (TextView) view.findViewById(R.id.uebungListeSchwerpunkt);
        TextView schlag = (TextView) view.findViewById(R.id.uebungListeSchlagwoerter);


        DBConnection dbConnection = DBHelper.getConnection(context);
        String[]sArr = {DBInfo.EXERCISE_COLUMN_NAME_NAME};
        String[] args = {cursor.getString(0)};
        Cursor c = dbConnection.select(DBInfo.EXERCISE_TABLE_NAME , sArr, DBInfo.EXERCISE_COLUMN_NAME_IDLOCAL + " = ?", args);
        c.moveToFirst();
        uebungenListeAutor.setVisibility(GONE);
        uebungenListeKeywords.setVisibility(GONE);
        schlag.setVisibility(GONE);
        uebungenListeName.setText(c.getString(0));
        uebungenListeDauer.setText(cursor.getString(1));


        Log.i(TAG,uebungenListeName.getText().toString());



        //TODO: Prüfen ob hier die von der Tabelle gesetzten ID übernommen wird oder die von Hand gesetzte.

        idList.add(cursor.getInt(0));
        trainingsunitexerciseID.add(cursor.getInt(2));

        //Log.i(TAG, "_id: " + idList.get(cursor.getInt(0)-1));
        cursor.moveToNext();


    }



    @Override

    public View newView(Context context, Cursor cursor, ViewGroup parent){

        //Log.i(TAG, "newView");

        //return inflator.inflate(R.layout.vertretungsplan2, null);
        //cursor.moveToFirst();

        return LayoutInflater.from(context).inflate(R.layout.fragment_uebungen_liste_element, parent, false);

    }



    public ArrayList<Integer> getIds(){

        return ids;

    }

}