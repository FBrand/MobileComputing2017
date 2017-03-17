package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Übersicht einer einzelnen Einheit und ihrer Übungen.
 */
public class EinheitDetailActivity extends AppCompatActivity {

    FragmentManager fm;
    FloatingActionButton fab;

    public int entryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("EinheitDetailActivity", "start onCreate");
        setContentView(R.layout.activity_einheit_detail);
        Log.i("EinheitDetailActivity", "starting Fragment creation");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        entryID = extras.getInt("_id");
        Log.i("UebungActivity", "ID: " + entryID);

        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment, new EinheitDetailFragment(),"EinheitDetail");
        ft.commit();

        /**
         * erzeugt FloatingActionButton mit "Plus"-Icon zum, je nach Fragment,
         * hinzufügen oder filtern von Übungen
         */
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("EinheitenDetailActivity", "BackstackEntryCount for FAB: " +fm.getBackStackEntryCount());
                if (fm.getBackStackEntryCount() <2){
                    doTransaction(R.id.fragment, new FragmentEinheitDetailUebungenListe(), "EinheitDetailUebungListe");
                    //fab.setImageResource(android.R.drawable.ic_menu_search);
                    fab.hide();
                } else {
                    /**
                     * TODO: Hier soll der Filterdialog hin!
                     */
                    int index = fm.getBackStackEntryCount() - 1;
                    FragmentManager.BackStackEntry backEntry = getFragmentManager().getBackStackEntryAt(index);
                    Log.i("EinheitDetailActivity", "Backstack last TAG: " +backEntry.getName());
                    Toast.makeText(getApplicationContext(), "Filter an", Toast.LENGTH_SHORT).show();
                }

        }});

        /**
         * verhindert "Zurück-Pfeil" oben links
         */
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);


    }

    /**
     * Benötigt globalen FragmentManager
     * @param frameID
     * @param fragment
     * @param tag
     */
    private void doTransaction(int frameID, Fragment fragment, String tag){
       // fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(frameID, fragment, tag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(tag);
        ft.commit();
        fm.executePendingTransactions();
        Log.i("EinheitenDetailActivity", "BackstackEntryCount: " +fm.getBackStackEntryCount());
        Log.i("EinheitenDetailActivity", "do Transaction fertig");



    }

    public FloatingActionButton getFAB(){
        return this.fab;
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onBackPressed(){
        if (getFragmentManager().getBackStackEntryCount()== 0){
            super.onBackPressed();
            //fab.show();
        } else {
            getFragmentManager().popBackStack();
            //fab.setImageResource(android.R.drawable.ic_menu_add);
            fab.show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createCalendarEvent(View b){

        TextView title = (TextView) findViewById(R.id.einheitName);
        TextView description = (TextView) findViewById(R.id.einheitBeschreibung);

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2017, 2, 19, 7, 30);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2017, 2, 19, 8, 30);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                /*.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())*/
                .putExtra(CalendarContract.Events.TITLE, title.getText().toString())
                .putExtra(CalendarContract.Events.DESCRIPTION, description.getText().toString())
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "Auf dem Sportplatz")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        //.putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
        startActivity(intent);
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        Spinner spinner = (Spinner) view.getRootView().findViewById(R.id.keyword);

        boolean checked = ((RadioButton) view).isChecked();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.default_array, android.R.layout.simple_spinner_item);

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_fitness:
                if (checked)
                    adapter = ArrayAdapter.createFromResource(view.getContext(),
                            R.array.fitness_array, android.R.layout.simple_spinner_item);
                break;
            case R.id.radio_taktik:
                if (checked)
                    adapter = ArrayAdapter.createFromResource(view.getContext(),
                            R.array.taktik_array, android.R.layout.simple_spinner_item);
                break;
            case R.id.radio_technik:
                if (checked)
                    adapter = ArrayAdapter.createFromResource(view.getContext(),
                            R.array.technik_array, android.R.layout.simple_spinner_item);
                break;
            case R.id.radio_torwart:
                if (checked)
                    adapter = ArrayAdapter.createFromResource(view.getContext(),
                            R.array.torwart_array, android.R.layout.simple_spinner_item);
                break;
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.performClick();
    }

}
