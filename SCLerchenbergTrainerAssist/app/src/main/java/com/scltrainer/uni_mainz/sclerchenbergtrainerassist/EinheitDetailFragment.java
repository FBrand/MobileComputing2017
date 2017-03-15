package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private int entryID;

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


}
