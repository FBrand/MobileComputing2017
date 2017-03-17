package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import static com.scltrainer.uni_mainz.sclerchenbergtrainerassist.R.id.container;


/**
 * EinheitenListeFragment zeigt eine Liste aller Einheiten an und
 * wird von EinheitenListeAdapter befüllt.
 */
public class EinheitenListeFragment extends ListFragment implements OnItemClickListener {

    //TAG
    private static final String TAG = EinheitenListeFragment.class.getSimpleName();

    // wird für die Listenansicht benötigt
    private Cursor dbCursor;
    // bildet den Cursor auf die ListView ab
    private EinheitenListeAdapter listAdapter;
    // Schnittstelle zur Datenbank
    private DBConnection dbConnection;

    private int entryID;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_einheiten_liste, container, false);
        dbConnection= DBHelper.getConnection(getActivity());
        dbCursor = selectCursorEinheitenListe();
        Log.i(TAG, "Cursor wurde initiiert");
        Log.i(TAG, "startManagingCursor");
        listAdapter = new EinheitenListeAdapter(getActivity(), dbCursor);
        setListAdapter(listAdapter);


        return rootView;
    }

    public EinheitenListeFragment newInstance(int entryID) {
        EinheitenListeFragment fragment = new EinheitenListeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        this.entryID = entryID;
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated start");

        dbConnection= DBHelper.getConnection(getActivity());
        dbCursor = selectCursorEinheitenListe();
        Log.i(TAG, "Cursor wurde initiiert");
        getActivity().startManagingCursor(dbCursor);
        Log.i(TAG, "startManagingCursor");
        listAdapter = new EinheitenListeAdapter(getActivity().getBaseContext(), dbCursor);

        setListAdapter(listAdapter);

        /**
         * Löschen einer Einheit durch longclick.
         */
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                //TODO: Datenbankinteraktion einfügen
                //TODO: Bei Bedarf hier ein Intent zur vorgänger Activity einfügen
                /*Intent intent = new Intent(getActivity(), UebungActivity.class);
                //TODO: Anpassen dass hier nicht die Position sondern die DB ID genommen wird
                intent.putExtra("_id", position+1);
                startActivity(intent);*/
                Toast.makeText(getActivity(), "Lösche Einheit " + position, Toast.LENGTH_LONG).show();

                return true;
            }
        });

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras == null) {
            return;
        }
        entryID = extras.getInt("_id");
        Log.i(TAG, "onActivityCreated: ID: " + entryID);

        Log.i(TAG, "onActivityCreated ende");
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        super.onListItemClick(l, v, pos, id);
        Log.i(TAG, "onListItemClick 1");
        int i = listAdapter.getIdListEntry(pos);
        Intent intent = new Intent(getActivity(), EinheitDetailActivity.class);
        intent.putExtra("_id", i);
        startActivity(intent);
        Toast.makeText(getActivity(), "Item " + i + " was clicked", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onListItemClick ende");

    }

    @Override
    public void onPause(){
        super.onPause();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //outState.putString("message", "This is my message to be reloaded");
        super.onSaveInstanceState(outState);
    }



    public Cursor selectCursorEinheitenListe(){

        String[] sArr = {"_id", DBInfo.TRAININGSUNIT_COLUMN_NAME_NAME,
                DBInfo.TRAININGSUNIT_COLUMN_NAME_AUTORNAME,
                DBInfo.TRAININGSUNIT_COLUMN_NAME_DESCRIPTION,
                //Schwerpunkt?
                DBInfo.TRAININGSUNIT_COLUMN_NAME_RATING,
                DBInfo.TRAININGSUNIT_COLUMN_NAME_DURATION};

        return dbConnection.select(DBInfo.TRAININGSUNIT_TABLE_NAME, sArr, null, null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateEinheitActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }
}