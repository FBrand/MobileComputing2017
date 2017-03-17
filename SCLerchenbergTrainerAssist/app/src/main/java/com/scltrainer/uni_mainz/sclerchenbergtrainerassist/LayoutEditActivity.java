package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.GLLayoutEditView;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.ItemGroup;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.ThumbnailListAdapter;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.ThumbnailOnClickListener;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.Util;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.Layout;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.MaterialType;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.PathEditType;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.PathType;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.Thumbnail;

import org.w3c.dom.Text;

import static com.scltrainer.uni_mainz.sclerchenbergtrainerassist.UebungActivity.EXTRA_EXERCISE_NAME;
import static com.scltrainer.uni_mainz.sclerchenbergtrainerassist.UebungActivity.EXTRA_LOCALID;

/**
 * Created by Julian on 15.03.2017.
 */

public class LayoutEditActivity extends Activity {
    private GLLayoutEditView glView;
    private Layout layout;
    private int entryID;
    private String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.forceReloadResources();

        Bundle extras = getIntent().getExtras();
        assert extras != null;

        if (extras == null) {
            layout = Util.defaultLayout();
        } else {
            entryID = extras.getInt(EXTRA_LOCALID);
            name = extras.getString(EXTRA_EXERCISE_NAME);
            layout = Util.loadLayoutFromDB(entryID, this);
        }

        // Create a GLSurfaceView instance
        glView = new GLLayoutEditView(this);
        glView.setLayout(layout);

        setContentView(R.layout.activity_edit_layout);
        ((FrameLayout) findViewById(R.id.gl_view)).addView(glView);

        ThumbnailListAdapter adapter = new ThumbnailListAdapter(this, thumbnails());
        ListView componentList = (ListView) findViewById(R.id.component_list);
        componentList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        componentList.setAdapter(adapter);
        ThumbnailOnClickListener listener = new ThumbnailOnClickListener(adapter);
        listener.addItemGroup(new ItemGroup(glView.materialOnClickListener, MaterialType.length()));
        listener.addItemGroup(new ItemGroup(glView.pathOnClickListener, PathType.length()));
        listener.addItemGroup(new ItemGroup(glView.pathEditOnClickListener, 1));

        componentList.setOnItemClickListener(listener);

        AppCompatImageButton saveButton = (AppCompatImageButton) findViewById(R.id.save_layout_button);
        saveButton.setOnClickListener(new SaveOnClickListener());
    }

    private Thumbnail[] thumbnails() {
        Thumbnail[] thumbnails = new Thumbnail[MaterialType.length() + PathType.length()+1];
        int i = 0;
        for (MaterialType materialType : MaterialType.values())
            thumbnails[i++] = materialType;
        for (PathType pathType : PathType.values())
            thumbnails[i++] = pathType;
        thumbnails[i++] = PathEditType.getInstance();
        return thumbnails;
    }

    @Override
    protected void onResume()
    {
        // The activity must call the GL surface view's onResume() on activity onResume().
        super.onResume();
        glView.onResume();
    }

    @Override
    protected void onPause()
    {
        // The activity must call the GL surface view's onPause() on activity onPause().
        super.onPause();
        glView.onPause();
    }

    private class SaveOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (entryID >= 0) {
                boolean success = Util.saveLayoutToDB(entryID, layout, LayoutEditActivity.this);
                Log.e("SAVE", ""+success);
            }
        }
    }

}
