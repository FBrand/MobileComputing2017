package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component;


import android.content.Context;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.BoundingBox;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.Layer;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.LayerUpdate;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public abstract class Viewable<L extends Layer, C extends Viewable<?, C>> {

    private L layer;
    private boolean initialized;
    private Collection<LayerUpdate> layerUpdates = new LinkedHashSet<>();

    protected abstract L doMakeLayer();
    protected abstract void doInit(Context context);

    protected void planUpdate(LayerUpdate update) {
        if (hasLayer() && initialized) {
            synchronized (layerUpdates) {
                layerUpdates.add(update);
            }
        }
    }


    public final void init(Context context) {
        doInit(context);
        initialized = true;
    }
    public boolean select(Vector2f v, List<? super C> selection) {
        return false;
    }
    public boolean select(BoundingBox box, List<? super C> selection) {
        return false;
    }

    /**
     * Creates the layer if necessary.
     * @return the layer.
     */
    public L getLayer() {
        if (layer == null)
            layer = doMakeLayer();
        return layer;
    }

    public boolean hasLayer() {
        return layer != null;
    }

    /**
     * GL context may be required.
     */
    public void updateLayer() {
        List<LayerUpdate> updates;
        synchronized (layerUpdates) {
            updates = new ArrayList<>(layerUpdates);
            layerUpdates.clear();
        }
        for (LayerUpdate update : updates) {
            update.run();
        }
    }
}
