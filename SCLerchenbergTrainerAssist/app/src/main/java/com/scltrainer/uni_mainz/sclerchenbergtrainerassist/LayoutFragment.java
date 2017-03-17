package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.GLLayoutView;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.Util;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.Layout;
import static com.scltrainer.uni_mainz.sclerchenbergtrainerassist.UebungActivity.EXTRA_LOCALID;

/**
 * Created by Julian on 15.03.2017.
 */

public class LayoutFragment extends Fragment {

    private GLLayoutView glView;
    private int entryID;
    private Layout layout;

    public static LayoutFragment newInstance(int entryID) {
        LayoutFragment fragment = new LayoutFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_LOCALID, entryID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_uebung, container, false);
        Util.forceReloadResources();

        Bundle extras = getArguments();
        if (extras == null) {
            layout = Util.defaultLayout();
        } else {
            entryID = extras.getInt(EXTRA_LOCALID);
            layout = Util.loadLayoutFromDB(entryID, getContext());
        }

        // Create a GLSurfaceView instance
        glView = new GLLayoutView(getContext());
        glView.setLayout(layout);

        ((FrameLayout) rootView.findViewById(R.id.gl_view)).addView(glView);

        return rootView;
    }

    @Override
    public void onResume() {
        // The activity must call the GL surface view's onResume() on activity onResume().
        super.onResume();
        glView.onResume();

        Bundle extras = getArguments();
        if (extras == null) {
            layout = Util.defaultLayout();
        } else {
            entryID = extras.getInt(EXTRA_LOCALID);
            layout = Util.loadLayoutFromDB(entryID, getContext());
        }
        glView.setLayout(layout);
    }

    @Override
    public void onPause() {
        // The activity must call the GL surface view's onPause() on activity onPause().
        super.onPause();
        glView.onPause();
    }
}
