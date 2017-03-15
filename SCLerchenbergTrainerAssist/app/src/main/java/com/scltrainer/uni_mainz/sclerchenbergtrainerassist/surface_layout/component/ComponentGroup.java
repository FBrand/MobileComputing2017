package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component;

import android.content.Context;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.BoundingBox;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.LayerGroup;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.LayerUpdate;

import org.joml.Vector2f;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Julian on 09.03.2017.
 */

public class ComponentGroup<T extends Component<?, T>> extends Viewable<LayerGroup, T> {

    private Set<T> components = new LinkedHashSet<>();

    public ComponentGroup(T... components) {
        for (T component : components)
            add(component);
    }

    @Override
    protected LayerGroup doMakeLayer() {
        LayerGroup layer = new LayerGroup();
        for (T component : components) {
            layer.add(component.getLayer());
        }
        return layer;
    }

    public void add(T component) {
        this.components.add(component);
        planUpdate(new LayerAddUpdate(component));
    }

    public void remove(T component) {
        this.components.remove(component);
        planUpdate(new LayerRemoveUpdate(component));
    }

    @Override
    public void init(Context context) {
        for (T component : components) {
            component.init(context);
        }
    }

    @Override
    public boolean select(Vector2f v, List<? super T> selection) {
        boolean selected = false;
        for (T component : components) {
            if (component.select(v, selection)) {
                selected = true;
            }
        }
        return selected;
    }

    @Override
    public boolean select(BoundingBox box, List<? super T> selection) {
        boolean selected = false;
        for (T component : components) {
            if (component.select(box, selection)) {
                selected = true;
            }
        }
        return selected;
    }

    public boolean isEmpty() {
        return components.isEmpty();
    }

    public int size() {
        return components.size();
    }

    private class LayerAddUpdate implements LayerUpdate {

        private T component;

        public LayerAddUpdate(T component) {
            this.component = component;
        }

        @Override
        public void run() {
            getLayer().add(component.getLayer());
        }
    }

    private class LayerRemoveUpdate implements LayerUpdate {

        private T component;

        public LayerRemoveUpdate(T component) {
            this.component = component;
        }

        @Override
        public void run() {
            getLayer().remove(component.getLayer());
        }
    }
}
