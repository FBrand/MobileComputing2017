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
    private Collection<LayerUpdate> layerUpdates = new LinkedHashSet<>();

    protected abstract L doMakeLayer();

    protected void planUpdate(LayerUpdate update) {
        synchronized (layerUpdates) {
            layerUpdates.add(update);
        }
    }

    public void init(Context context) {

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
