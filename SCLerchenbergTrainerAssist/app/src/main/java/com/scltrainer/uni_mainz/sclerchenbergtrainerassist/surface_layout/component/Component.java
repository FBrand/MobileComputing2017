package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component;


import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.Layer;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.LayerUpdate;

import org.joml.Vector2f;

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

    public void translate(Vector2f t) {
        pos.add(t);
        planUpdate(new LayerPositionUpdate());
    }

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
