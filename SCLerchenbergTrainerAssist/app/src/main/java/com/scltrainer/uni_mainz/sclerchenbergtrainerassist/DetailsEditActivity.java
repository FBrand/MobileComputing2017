package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.scltrainer.uni_mainz.sclerchenbergtrainerassist.UebungActivity.EXTRA_LOCALID;
import static com.scltrainer.uni_mainz.sclerchenbergtrainerassist.UebungActivity.EXTRA_EXERCISE_DETAIL;

/**
 * Created by Julian on 17.03.2017.
 */

public class DetailsEditActivity extends AppCompatActivity {

    private int entryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_uebung_detail_edit);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        String[] dbValues = extras.getStringArray(EXTRA_EXERCISE_DETAIL);
        entryID = extras.getInt(EXTRA_LOCALID);


        TextView uebungName = (TextView) findViewById(R.id.uebungNameEdit);//nameUebung-0
        TextView uebungBeschreibung = (TextView) findViewById(R.id.uebungBeschreibungEdit);//Beschreibung-2
        TextView uebungTechnikPunkte = (TextView) findViewById(R.id.uebungTechnikPunkteEdit);//Technik-3
        TextView uebungTaktikPunkte = (TextView) findViewById(R.id.uebungTaktikPunkteEdit);//Taktik-4
        TextView uebungPhysisPunkte = (TextView) findViewById(R.id.uebungPhysisPunkteEdit);//Physis-5
        TextView uebungDauer = (TextView) findViewById(R.id.uebungDauerWertEdit);//Dauer-7
        TextView uebungAltersklasse = (TextView) findViewById(R.id.uebungAltersklassenEdit);//Altersklassen-8
        TextView uebungStichpunkte = (TextView) findViewById(R.id.uebungSchlagwoerterEdit);//Schlagwörter-9
        TextView uebungLink = (TextView) findViewById(R.id.uebungVideoURLEdit);//Link-10
        TextView uebungGruppe = (TextView) findViewById(R.id.uebungGruppengroesseEdit);//Gruppengroesse

        uebungName.setText(dbValues[1]);
        uebungBeschreibung.setText(dbValues[3]);
        uebungTechnikPunkte.setText(dbValues[4]);
        uebungTaktikPunkte.setText(dbValues[5]);
        uebungPhysisPunkte.setText(dbValues[6]);
        uebungDauer.setText(dbValues[8]);
        uebungAltersklasse.setText(dbValues[9]);
        uebungStichpunkte.setText(dbValues[10]);
        uebungLink.setText(dbValues[11]);
        uebungGruppe.setText(dbValues[12]);

        Button saveButton = (Button) findViewById(R.id.buttonUebungSpeichern);

        saveButton.setOnClickListener(new SaveClickListener());


    }

    private String getInputText(int viewId) {
        return ((TextView) findViewById(viewId)).getText().toString();
    }

    private class SaveClickListener implements View.OnClickListener {

        private Pair<String, Integer>[] dbUpdateMap = new Pair[]  {
                new Pair(DBInfo.EXERCISE_COLUMN_NAME_NAME, R.id.uebungNameEdit),
                new Pair(DBInfo.EXERCISE_COLUMN_NAME_DESCRIPTION, R.id.uebungBeschreibungEdit),
                new Pair(DBInfo.EXERCISE_COLUMN_NAME_TECHNIC, R.id.uebungTechnikPunkteEdit),
                new Pair(DBInfo.EXERCISE_COLUMN_NAME_TACTIC, R.id.uebungTaktikPunkteEdit),
                new Pair(DBInfo.EXERCISE_COLUMN_NAME_PHYSIS, R.id.uebungPhysisPunkteEdit),
                new Pair(DBInfo.EXERCISE_COLUMN_NAME_DURATION, R.id.uebungDauerWertEdit),
                new Pair(DBInfo.EXERCISE_COLUMN_NAME_AGE, R.id.uebungAltersklassenEdit),
                new Pair(DBInfo.EXERCISE_COLUMN_NAME_KEYWORDS, R.id.uebungSchlagwoerterEdit),
                new Pair(DBInfo.EXERCISE_COLUMN_NAME_VIDEOLINK, R.id.uebungVideoURLEdit),
                new Pair(DBInfo.EXERCISE_COLUMN_NAME_GROUPSIZE, R.id.uebungGruppengroesseEdit)

        };

        @Override
        public void onClick(View v) {
            DBConnection dbConnection = DBHelper.getConnection(DetailsEditActivity.this);
            ContentValues details = new ContentValues();
            for (int i = 0; i < dbUpdateMap.length; i++) {
                details.put(dbUpdateMap[i].first, getInputText(dbUpdateMap[i].second));
            }
            String s = DBInfo.EXERCISE_COLUMN_NAME_IDLOCAL + " = ? ";
            String[] sArgs = {"" + entryID};
            dbConnection.update(DBInfo.EXERCISE_TABLE_NAME, details, s, sArgs);
        }
    }
}
