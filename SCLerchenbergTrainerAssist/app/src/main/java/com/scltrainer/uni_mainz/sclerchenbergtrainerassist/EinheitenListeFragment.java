package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.app.ListFragment;
import android.content.Intent;
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
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.i("APP", "onCreateView start");
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        Log.i("APP", "onCreate ende");
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("APP", "onActivityCreated start");

        // TODO: Eigenen ArrayAdapter schreiben um eine schoenere Liste zu erzeugen
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.einheitenTest, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
        Log.i("APP", "onActivityCreated ende");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        //TODO: Verbindung zwischen Position in Liste und DB herstellen
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), EinheitDetail.class);
        startActivity(intent);
    }
}