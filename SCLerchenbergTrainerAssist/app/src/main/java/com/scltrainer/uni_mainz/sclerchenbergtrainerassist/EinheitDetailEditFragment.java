package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Yorrick on 16.03.2017.
 *
 * Fragment in dem eine Einheit editiert werden kann.
 *
 * TODO: Klasse fertig machen!
 */

public class EinheitDetailEditFragment extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_einheit_detail_edit, container, false);

        return rootView;

    }

}
