package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component;


import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.Layer;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.LayerUpdate;

import org.joml.Vector2f;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A component can be stored within a layout.
 * Each component has a position in world coordinates.
 * This provides an interface between different types of components, such as paths and materials.
 * It extends the viewable class to provide a layer that can be viewed by the Layer2DRenderer.
 * @param <L> The type of layer to create.
 * @param <C> The ComponentType, e.g MaterialComponent
 */
public abstract class Component<L extends Layer, C extends  Component<?, C>> extends Viewable<L, C> {

    protected Vector2f pos;

    public Component(Vector2f pos) {
        this.pos = pos;
    }

    public Component() {
        this(new Vector2f());
    }

    public void setPosition(Vector2f p) {
        this.pos = p;
        planUpdate(new LayerPositionUpdate());
    }

    public Vector2f getPosition() {
        return pos;
    }

    /**
     * Translates the component.
     * @param t The translation vector.
     */
    public void translate(Vector2f t) {
        pos.add(t);
        planUpdate(new LayerPositionUpdate());
    }

    /**
     * Converts the component to a JSON object.
     * @return The JSON Object.
     * @throws JSONException if something went wrong.
     */
    public abstract JSONObject toJSON() throws JSONException;

    /**
     * Updates the position of a layer.
     */
    private class LayerPositionUpdate implements LayerUpdate {

        @Override
        public void run() {
            getLayer().pos = pos;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj instanceof Component.LayerPositionUpdate);
        }
    }
}
