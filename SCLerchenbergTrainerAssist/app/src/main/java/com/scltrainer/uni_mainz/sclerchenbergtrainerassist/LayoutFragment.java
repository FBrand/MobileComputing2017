package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.GLLayoutView;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.ThumbnailListAdapter;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.Background;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.Layout;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.MaterialComponent;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.MaterialType;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.PathComponent;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.PathType;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.QuadVBO;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.TextureFactory;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.TriangleVBO;

import org.joml.Vector2f;

import static com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.FieldType.SOCCER_FIELD;
import static com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.SurfaceType.SOCCER_GRASS;

/**
 * Created by Julian on 15.03.2017.
 */

public class LayoutFragment extends Fragment {

    private GLLayoutView glView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_uebung, container, false);
        forceReloadResources();

        // for testing
        Layout layout = new Layout();
        layout.background = new Background(SOCCER_GRASS, SOCCER_FIELD);
        layout.materials.add(new MaterialComponent(MaterialType.BALL));
        PathComponent path = new PathComponent(PathType.PASS);
        path.addPoint(new Vector2f());
        path.addPoint(new Vector2f(1,0));
        layout.paths.add(path);
        // Create a GLSurfaceView instance
        glView = new GLLayoutView(getContext());
        glView.setLayout(layout);

        ((FrameLayout) rootView.findViewById(R.id.gl_view)).addView(glView);

        return rootView;
    }

    private void forceReloadResources() {
        TextureFactory.reset();
        QuadVBO.reset();
        TriangleVBO.reset();
    }

    @Override
    public void onResume() {
        // The activity must call the GL surface view's onResume() on activity onResume().
        super.onResume();
        glView.onResume();
    }

    @Override
    public void onPause() {
        // The activity must call the GL surface view's onPause() on activity onPause().
        super.onPause();
        glView.onPause();
    }
}
