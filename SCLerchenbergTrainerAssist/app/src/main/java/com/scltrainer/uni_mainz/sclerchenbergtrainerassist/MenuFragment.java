package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;



public class MenuFragment extends ListFragment implements OnItemClickListener {


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
                R.array.menu, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
        Log.i("APP", "onActivityCreated ende");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();

        replaceMenuWithList(position);
        //Hochkant

    }

    public void replaceMenuWithList(int position){
        switch (position){
            case 0:
                doTransaction(R.id.menu_frame, new UebungenListeFragment(), "uebungsliste");
                break;
            case 1:
                doTransaction(R.id.menu_frame, new EinheitenListeFragment(), "einheitenliste");
                break;
            case 2:
                break;
            default:
                Toast.makeText(getActivity(), "default switch case", Toast.LENGTH_LONG);
        }
    }

    private void doTransaction(int frameID, ListFragment fragment, String tag){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(frameID, fragment, tag);
        ft.addToBackStack(tag);
        ft.commit();
    }


    public void onBackPressed(){
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }
}