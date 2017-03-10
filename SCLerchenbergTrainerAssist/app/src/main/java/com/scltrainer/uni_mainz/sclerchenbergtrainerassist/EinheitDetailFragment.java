package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class EinheitDetailFragment extends Fragment {

    public EinheitDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_einheit_detail, container, false);
    }

    private void doTransaction(int frameID, android.app.Fragment fragment, String tag){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(frameID, fragment, tag);
        ft.addToBackStack(tag);
        ft.commit();
    }
}
