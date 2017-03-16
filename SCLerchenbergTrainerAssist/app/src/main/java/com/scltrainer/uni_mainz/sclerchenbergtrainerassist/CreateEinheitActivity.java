package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
}
