package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class EinheitenListeFragment extends ListFragment implements OnItemClickListener {


    //TAG
    private static final String TAG = EinheitenListeFragment.class.getSimpleName();

    // wird für die Listenansicht benötigt
    private Cursor dbCursor;
    // bildet den Cursor auf die ListView ab
    private EinheitenListeAdapter listAdapter;
    // Schnittstelle zur Datenbank
    private DBConnection dbConnection;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_einheit_detail2, container, false);
        dbConnection= DBHelper.getConnection(getActivity());
        dbCursor = selectCursorEinheitenListe();
        Log.i(TAG, "Cursor wurde initiiert");
        Log.i(TAG, "startManagingCursor");
        listAdapter = new EinheitenListeAdapter(getActivity().getBaseContext(), dbCursor);
        setListAdapter(listAdapter);
        

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("APP", "onActivityCreated start");

        // TODO: Eigenen ArrayAdapter schreiben um eine schoenere Liste zu erzeugen
        /**ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.einheitenTest, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);*/

        Log.i("APP", "onActivityCreated ende");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        //TODO: Verbindung zwischen Position in Liste und DB herstellen
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), EinheitDetailActivity.class);
        startActivity(intent);
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
}