package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.content.Context;
import android.database.Cursor;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class UebungDetailAdapter extends CursorAdapter {
	
	private LayoutInflater inflator;
	//private int s;
	String TAG = "UebungDetailAdapter";

	
	public UebungDetailAdapter(Context context, Cursor c) {
		super(context, c);
		inflator = LayoutInflater.from(context);
		//s = c.getCount();
	}
	
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		if(cursor == null) return;
		TextView uebungName = (TextView) view.findViewById(R.id.uebungName);//nameUebung-0
		TextView uebungAutor = (TextView) view.findViewById(R.id.uebungAutor);//nameAutor-1
		TextView uebungBeschreibung = (TextView) view.findViewById(R.id.uebungBeschreibung);//Beschreibung-2
		TextView uebungTechnikPunkte = (TextView) view.findViewById(R.id.uebungTechnikPunkte);//Technik-3
		TextView uebungTaktikPunkte = (TextView) view.findViewById(R.id.uebungTaktikPunkte);//Taktik-4
		TextView uebungPhysisPunkte = (TextView) view.findViewById(R.id.uebungPhysisPunkte);//Physis-5
		RatingBar uebungFavoriten = (RatingBar) view.findViewById(R.id.uebungFavoriten);//Favorit-6
		TextView uebungDauer = (TextView) view.findViewById(R.id.uebungDauer);//Dauer-7
		TextView uebungAltersklasse = (TextView) view.findViewById(R.id.uebungAltersklassen);//Altersklassen-8
		TextView uebungStichpunkte = (TextView) view.findViewById(R.id.uebungSchlagwoerter);//Schlagwörter-9
		TextView uebungLink = (TextView) view.findViewById(R.id.uebungVideoURL);//Link-10
		TextView uebungGruppe = (TextView) view.findViewById(R.id.uebungGruppengroesse);//Gruppengroesse

		cursor.moveToFirst();
		uebungName.setText(cursor.getString(1));
		uebungAutor.setText(cursor.getString(2));
		uebungBeschreibung.setText(cursor.getString(3));
		uebungTechnikPunkte.setText(cursor.getString(4));
		uebungTaktikPunkte.setText(cursor.getString(5));
		uebungPhysisPunkte.setText(cursor.getString(6));
		uebungFavoriten.setNumStars((int)cursor.getDouble(7));
		uebungDauer.setText(cursor.getString(8));
		uebungAltersklasse.setText(cursor.getString(9));
		uebungStichpunkte.setText(cursor.getString(10));
		uebungLink.setText(cursor.getString(11));
		uebungGruppe.setText(cursor.getString(12));
		
		cursor.moveToNext();
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent){
		//Log.i(TAG, "newView");
		//return inflator.inflate(R.layout.vertretungsplan2, null);
		return null;
	}
	
	public View setLayout1(Context context, Cursor cursor, ViewGroup parent){
		//Log.i(TAG, "newView");
		//return inflator.inflate(R.layout.vertretungsplan2, null);
		return null;
	}

}

