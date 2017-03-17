package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * EinheitDetailFragment zeigt eine konkrete Einheit an.
 * Wird von EinheitDetailAdapter befüllt.
 */



public class EinheitDetailFragment extends Fragment implements DeleteDialog.DeleteDialogListener{

    //TAG
//private static final String TAG = UebungDetail.class.getSimpleName();

    // wird für die Listenansicht benötigt
    private DeleteDialog.DeleteDialogListener dListener = this;
    private Cursor dbCursor1;
    private Cursor dbCursor2;

    // bildet den Cursor auf die ListView ab

    private EinheitDetailAdapter listAdapter1;
    private EinheitDetailUebersichtEnthaltenerUebungenAdapter listAdapter2;

    private int trainingsunitexercise = -1;
    private ArrayList<Integer> trainingsunitexerciseID;

    // Schnittstelle zur Datenbank
    private DBConnection dbConnection;

    private Button calendarButton;

    private int entryID;
    String TAG = "EDUEU";

    public EinheitDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    //TODO: Von T
    public Cursor selectCursorEinheitDetail(int entryID){

        String s = DBInfo.TRAININGSUNIT_COLUMN_NAME_IDLOCAL + " = ? ";
        String[] sArgs = {"" + entryID};
        Log.i("DetailFragment", "entryID in cursor: " + entryID);

        String[] sArr = {"_id", DBInfo.TRAININGSUNIT_COLUMN_NAME_NAME,
                DBInfo.TRAININGSUNIT_COLUMN_NAME_AUTORNAME,
                DBInfo.TRAININGSUNIT_COLUMN_NAME_DESCRIPTION,
                //Schwerpunkt?
                DBInfo.TRAININGSUNIT_COLUMN_NAME_RATING,
                DBInfo.TRAININGSUNIT_COLUMN_NAME_DURATION,
                DBInfo.TRAININGSUNIT_COLUMN_NAME_LASTCHANGE};

        return dbConnection.select(DBInfo.TRAININGSUNIT_TABLE_NAME, sArr, s, sArgs);
    }

    //neu
    public Cursor selectCursorEinheitDetailUebuersichtEnthaltenerUebungen(int entryID){

        String[] nameEinheit= {""+entryID};
        //Suche der Tabelleneinträge in Trainingsunitexercise zu der entsprechenden Einheit
        String s = DBInfo.TRAININGSUNITEXERCISE_COLUMN_NAME_TRAININGSUNIT + " = ? ";
        String[] sArr = new String[]{DBInfo.TRAININGSUNITEXERCISE_COLUMN_NAME_EXERCISE, DBInfo.TRAININGSUNITEXERCISE_COLUMN_NAME_DURATION, DBInfo.TRAININGSUNITEXERCISE_COLUMN_NAME_ID};

        return dbConnection.select(DBInfo.TRAININGSUNITEXERCISE_TABLE_NAME , sArr, s, nameEinheit);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_einheit_detail, container, false);

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras == null) {
            return null;
        }
        entryID = extras.getInt("_id");
        Log.i("EinheitDetailActivity", "ID: " + entryID);


        dbConnection= DBHelper.getConnection(getActivity());
        dbCursor1 = selectCursorEinheitDetail(entryID);
        dbCursor2 = selectCursorEinheitDetailUebuersichtEnthaltenerUebungen(entryID);

        //Log.i(TAG, "Cursor wurde initiiert");
        getActivity().startManagingCursor(dbCursor1);

        //Log.i(TAG, "startManagingCursor");
        listAdapter1 = new EinheitDetailAdapter(getActivity().getBaseContext(), dbCursor1);
        listAdapter1.bindView(rootView, getActivity().getBaseContext(), dbCursor1);

        if( dbCursor2.getCount()>0) {
            getActivity().startManagingCursor(dbCursor2);
            listAdapter2 = new EinheitDetailUebersichtEnthaltenerUebungenAdapter(getActivity().getBaseContext(), dbCursor2);
            //listAdapter2.bindView(rootView, getActivity().getBaseContext(), dbCursor2);
            ListView uebung = (ListView)rootView.findViewById(R.id.einheitUebungen);
            uebung.setAdapter(listAdapter2);
            uebung.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int i = listAdapter2.getIdListEntry(position);
                    Intent intent = new Intent(getActivity(), UebungActivity.class);
                    intent.putExtra("_id", i);
                    startActivity(intent);
                }
            });
            uebung.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    trainingsunitexerciseID = listAdapter2.getTrainingsunitexerciseID();
                    trainingsunitexercise = trainingsunitexerciseID.get(position);
                    DeleteDialog d = new DeleteDialog();
                    d.addListener(dListener);
                    d.show(getFragmentManager(),"DeleteDialog");
                    return true;
                }
            });
        }

        Button uploadButton = (Button) rootView.findViewById(R.id.uploadButtonEinheit);
        uploadButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHAREDPREFERENCES", Context.MODE_PRIVATE);
                if (!sharedPreferences.getString("USEREMAIL", "").equals("")){

                    DBConnection connection = DBHelper.getConnection(getActivity());
                    Cursor cursor = connection.select(DBInfo.TRAININGSUNIT_TABLE_NAME, new String[] {DBInfo.TRAININGSUNIT_COLUMN_NAME_ID}, (DBInfo.TRAININGSUNIT_COLUMN_NAME_IDLOCAL + " = ?"), new String[] {String.valueOf(entryID)});
                    cursor.moveToNext();
                    String globalID = cursor.getString(0);
                    if((globalID == null || globalID.equals("null"))) {
                        try{
                            Cursor exCusor = connection.select(DBInfo.TRAININGSUNITEXERCISE_TABLE_NAME, new String[] {DBInfo.TRAININGSUNITEXERCISE_COLUMN_NAME_ID}, (DBInfo.TRAININGSUNIT_COLUMN_NAME_IDLOCAL + " = ?"), new String[] {String.valueOf(entryID)});
                            while (exCusor.moveToNext()){
                                String id = exCusor.getString(0);
                                Cursor cursorp = connection.select(DBInfo.EXERCISE_TABLE_NAME, new String[] {DBInfo.EXERCISE_COLUMN_NAME_ID}, (DBInfo.EXERCISE_COLUMN_NAME_IDLOCAL + " = ?"), new String[] {id});
                                cursorp.moveToNext();
                                String globalIDp = cursorp.getString(0);
                                if((globalIDp == null || globalIDp.equals("null"))) {
                                    GlobalDBConnection.upload(DBInfo.EXERCISE_TABLE_NAME, Integer.parseInt(id), getActivity());
                                }
                            }
                            GlobalDBConnection.upload(DBInfo.EXERCISE_TABLE_NAME, entryID, getActivity());
                            Toast.makeText(getActivity(), "Hochladen erfolgreich!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Fehler beim Hochladen!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Diese Einheit wurde bereits hochgeladen!", Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(getActivity(), "Die Einheit wurde hochgeladen...NICHT! #Kappa", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Sie müssen zuerst Ihre Email freigeben!", Toast.LENGTH_LONG).show();
                }

            }
        });

        return rootView;
    }

    private void doTransaction(int frameID, android.app.Fragment fragment, String tag){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(frameID, fragment, tag);
        ft.addToBackStack(tag);
        ft.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createCalendarEvent(){

        TextView title = (TextView) getActivity().findViewById(R.id.einheitName);

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2017, 2, 19, 7, 30);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2017, 2, 19, 8, 30);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, title.getText().toString())
                .putExtra(CalendarContract.Events.DESCRIPTION, "Mannschaft")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "Auf dem Sportplatz")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                //.putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
        startActivity(intent);
    }


    @Override
    public void onDialogPoistiveClick(int duration) {
        DBConnection con = DBHelper.getConnection(this.getActivity().getBaseContext());
        String[] args = {""+trainingsunitexercise};
        con.delete(DBInfo.TRAININGSUNITEXERCISE_TABLE_NAME, DBInfo.TRAININGSUNITEXERCISE_COLUMN_NAME_ID+" = ?",args);
        Fragment currentFragment = this;
        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
        fragTransaction.detach(currentFragment);
        fragTransaction.attach(currentFragment);
        fragTransaction.commit();

    }

    @Override
    public void onDialogNegativeClick(int duration) {

    }
}
