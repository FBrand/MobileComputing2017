package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.TextView;

/**
 * EinheitDetailFragment zeigt eine konkrete Einheit an.
 * Wird von EinheitDetailAdapter befüllt.
 */



public class EinheitDetailFragment extends Fragment {

    //TAG
//private static final String TAG = UebungDetail.class.getSimpleName();

    // wird für die Listenansicht benötigt
    private Cursor dbCursor;
    // bildet den Cursor auf die ListView ab
    private EinheitDetailAdapter listAdapter;
    // Schnittstelle zur Datenbank
    private DBConnection dbConnection;

    private Button calendarButton;

    private int entryID;

    public EinheitDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*View.OnClickListener clickHandler1 = new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.N)
            public void onClick(View v) {
                Log.i("EinheitDetailFragment", "call createCalendarEvent");
                createCalendarEvent();
            }
        };

        calendarButton = (Button) getActivity().findViewById(R.id.addCalendarDateButton);
        calendarButton.setOnClickListener(clickHandler1);*/



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
        dbCursor = selectCursorEinheitDetail(entryID);
        //Log.i(TAG, "Cursor wurde initiiert");
        getActivity().startManagingCursor(dbCursor);
        //Log.i(TAG, "startManagingCursor");
        listAdapter = new EinheitDetailAdapter(getActivity().getBaseContext(), dbCursor);
        listAdapter.bindView(rootView, getActivity().getBaseContext(), dbCursor);

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


}
