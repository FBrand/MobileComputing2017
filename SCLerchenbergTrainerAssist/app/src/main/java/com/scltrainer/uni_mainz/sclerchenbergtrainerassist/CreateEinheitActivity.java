package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.Util;


/**
 * Created by Yorrick on 16.03.2017.
 *
 * Activity die EinheitDetailEditFragment kapselt und funktionalität zum Speichern der Details
 * einer neuen Übung bietet.
 * Das Einfügen von Übungen findet derzeit nicht hier statt, sondern über EinheitDetailFragment.
 */

public class CreateEinheitActivity extends AppCompatActivity {

    FragmentManager fm;
    String TAG = "CreateEinheitActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.init(this);
        Log.i(TAG, "onCreate starts");

        setContentView(R.layout.activity_create_einheit_detail);

        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.create_einheit_frame, new EinheitDetailEditFragment(),"EinheitDetail");
        ft.commit();
    }

    public void insertEmptyUnit(View view) {
        EditText etName = (EditText) findViewById(R.id.editEinheitName);
        EditText etDescription = (EditText) findViewById(R.id.editEinheitBeschreibung);

        SharedPreferences s = getSharedPreferences("SHAREDPREFERENCES", Context.MODE_PRIVATE);
        DBConnection dbConnection = DBHelper.getConnection(this);

        String[] sArr = {"_id"};
        ContentValues row = new ContentValues();
        row.put(DBInfo.TRAININGSUNIT_COLUMN_NAME_AUTORMAIL, s.getString("USERMAIL",""));
        row.put(DBInfo.TRAININGSUNIT_COLUMN_NAME_AUTORNAME, s.getString("USERNAME",""));
        row.put(DBInfo.TRAININGSUNIT_COLUMN_NAME_DESCRIPTION, etDescription.getText().toString());
        row.put(DBInfo.TRAININGSUNIT_COLUMN_NAME_NAME, etName.getText().toString());
        long id = dbConnection.insert(DBInfo.TRAININGSUNIT_TABLE_NAME, row);
        row.clear();
        row.put(DBInfo.TRAININGSUNIT_COLUMN_NAME_IDLOCAL, id);
        String s_ = DBInfo.TRAININGSUNIT_COLUMN_NAME_IDLOCAL + " = ? ";
        String[] sArgs = {"" + id};
        dbConnection.update(DBInfo.TRAININGSUNIT_TABLE_NAME, row, s_, sArgs);
    }
}
