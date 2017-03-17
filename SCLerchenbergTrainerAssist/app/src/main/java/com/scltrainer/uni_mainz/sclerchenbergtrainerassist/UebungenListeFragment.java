package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class UebungenListeFragment extends ListFragment implements OnItemClickListener,FilterDialog.FilterDialogListener{

    private final String selection = DBInfo.EXERCISE_COLUMN_NAME_AGE + " like ? AND " + DBInfo.EXERCISE_COLUMN_NAME_KEYWORDS + " like ? AND " + DBInfo.EXERCISE_COLUMN_NAME_GROUPSIZE + " < ? AND " +
            DBInfo.EXERCISE_COLUMN_NAME_PHYSIS + " <= ? AND " + DBInfo.EXERCISE_COLUMN_NAME_TACTIC + " <= ? AND " + DBInfo.EXERCISE_COLUMN_NAME_TECHNIC + " <= ?"
            ;
    private String[] args = {"%", "%", "100", "7", "7" ,"7"};
    //TAG
    private static final String TAG = UebungenListeFragment.class.getSimpleName();

    // wird für die Listenansicht benötigt
    private Cursor dbCursor;
    // bildet den Cursor auf die ListView ab
    private UebungenListeAdapter listAdapter;
    // Schnittstelle zur Datenbank
    private DBConnection dbConnection;

    private FilterDialog.FilterDialogListener filterDialogListener = this;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView 1");
        View rootView = inflater.inflate(R.layout.fragment_uebungen_liste, container, false);
        Button filter = (Button) rootView.findViewById(R.id.filterButton);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterDialog filter =  new FilterDialog();
                        filter.addListener(filterDialogListener);
                filter.show(getFragmentManager(),"FilterDialog");
            }
        });
        dbConnection= DBHelper.getConnection(getActivity());
        dbCursor = selectCursorUebungenListe();
        Log.i(TAG, "Cursor wurde initiiert");
        Log.i(TAG, "startManagingCursor");
        listAdapter = new UebungenListeAdapter(getActivity(), dbCursor);
        setListAdapter(listAdapter);

        Log.i(TAG, "onCreateView fertig");

        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("APP", "onActivityCreated start");

        // TODO: Eigenen ArrayAdapter schreiben um eine schoenere Liste zu erzeugen
        /*ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.uebungTest, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);*/
        dbConnection= DBHelper.getConnection(getActivity());
        dbCursor = selectCursorUebungenListe();
        Log.i(TAG, "Cursor wurde initiiert");
        getActivity().startManagingCursor(dbCursor);
        Log.i(TAG, "startManagingCursor");
        listAdapter = new UebungenListeAdapter(getActivity().getBaseContext(), dbCursor);

        setListAdapter(listAdapter);

        /**
         * Löschen einer Übung durch longclick.
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
                Toast.makeText(getActivity(), "Lösche Übung " + position, Toast.LENGTH_LONG).show();

                return true;
            }
        });


        Log.i("APP", "onActivityCreated ende");
    }

    /**
     * Wird nicht benutzt?
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        /*Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), UebungActivity.class);
        startActivity(intent);*/
    }

    public Cursor selectCursorUebungenListe(){

        String[] sArr = {"_id", DBInfo.EXERCISE_COLUMN_NAME_NAME,
                DBInfo.EXERCISE_COLUMN_NAME_AUTORNAME,
                DBInfo.EXERCISE_COLUMN_NAME_KEYWORDS,
                DBInfo.EXERCISE_COLUMN_NAME_RATING,
                DBInfo.EXERCISE_COLUMN_NAME_DURATION};

        return dbConnection.select(DBInfo.EXERCISE_TABLE_NAME, sArr, selection, args);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        super.onListItemClick(l, v, pos, id);
        int i = listAdapter.getIdListEntry(pos);
        Intent intent = new Intent(getActivity(), UebungActivity.class);
        intent.putExtra("_id", i);
        startActivity(intent);
//        Toast.makeText(getActivity(), "Item " + listAdapter.getIds().get(i) + " was clicked", Toast.LENGTH_SHORT).show();

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

    public UebungenListeAdapter getListAdapter(){
        return this.listAdapter;
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
        if(getActivity() instanceof MainActivity){
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "ÜBUNGEN", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    @Override
    public void onDialogPoistiveClick(String[] args) {
        this.args = args;
        Fragment currentFragment = this;
        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
        fragTransaction.detach(currentFragment);
        fragTransaction.attach(currentFragment);
        fragTransaction.commit();
    }

    @Override
    public void onDialogNegativeClick(String[] args) {

    }
}