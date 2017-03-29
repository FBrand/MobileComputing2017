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

/**
 * The Viewable class provides method to create a layer that can be rendered using the Layer2DRenderer.
 * The layer is cached and updated automatically.
 * Furthermore a Viewable can be selected by passing a single touch point or a touch bounding box.
 *
 * @param <L> The layer type of this viewable.
 * @param <C> The concrete viewable.
 */
public abstract class Viewable<L extends Layer, C extends Viewable<?, C>> {

    private L layer;
    private boolean initialized;
    private Collection<LayerUpdate> layerUpdates = new LinkedHashSet<>();

    protected abstract L doMakeLayer();
    protected abstract void doInit(Context context);

    /**
     * Adds a new LayerUpdate to the update queue.
     * @param update The layer update to plan.
     */
    protected void planUpdate(LayerUpdate update) {
        if (hasLayer() && initialized) {
            synchronized (layerUpdates) {
                layerUpdates.add(update);
            }
        }
    }

    /**
     * Initializes the viewable layer, e.g. loads textures.
     * @param context The app context.
     */
    public final void init(Context context) {
        doInit(context);
        initialized = true;
    }

    /**
     * Checks whether the viewable can be selected from a touch position.
     * @param v The position of the touch in world coordinates.
     * @param selection A list of Viewables that are selected by the touch.
     * @return true if at least one viewable has been selected.
     */
    public boolean select(Vector2f v, List<? super C> selection) {
        return false;
    }
    /**
     * Checks whether the viewable can be selected from a touch bounding box.
     * @param v The position of the touch in world coordinates.
     * @param selection A list of Viewables that are selected by the touch.
     * @return true if at least one viewable has been selected.
     */
    public boolean select(BoundingBox box, List<? super C> selection) {
        return false;
    }

    /**
     * Creates the viewable layer if necessary.
     * @return the layer.
     */
    public L getLayer() {
        if (layer == null)
            layer = doMakeLayer();
        return layer;
    }

    /**
     *
     * @return true if a layer has benn created.
     */
    public boolean hasLayer() {
        return layer != null;
    }

    /**
     * Updates the layer by performing all planed LayerUpdates.
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
