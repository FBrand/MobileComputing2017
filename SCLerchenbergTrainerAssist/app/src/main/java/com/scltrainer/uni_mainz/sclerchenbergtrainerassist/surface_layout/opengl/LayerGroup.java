package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl;


import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class LayerGroup extends Layer {

    private LinkedHashSet<Layer> layers = new LinkedHashSet<>();

    public LayerGroup(Vector2f p, Layer... layers) {
        super(p);
        this.layers.addAll(Arrays.asList(layers));
    }

    public LayerGroup(Layer... layers) {
        this(new Vector2f(), layers);
    }

    public void add(Layer layer) {
        synchronized (layers) {
            layers.add(layer);
        }
    }

    public void remove(Layer layer) {
        synchronized (layers) {
            layers.remove(layer);
        }
    }

    public Layer getLayer(int i) {
        Iterator<Layer> it = layers.iterator();
        for (int j = 0; j < i-1; j++) {
            it.next();
        }
        return it.next();
    }

    public Matrix4f modelMatrix() {
        Matrix4f M = new Matrix4f();
        M.translate(pos.x, pos.y, 0);
        M.scale(scale, scale, 1);
        return M;
    }

    @Override
    protected void doDraw(Matrix4f PV, Matrix4f M, ShaderProgramInfo info) {
        Matrix4f Mnow = new Matrix4f(M);
        Mnow.mul(modelMatrix());
        synchronized (layers) {
            for (Layer layer : layers) {
                layer.draw(PV, Mnow, info);
            }
        }
    }

    @Override
    protected BoundingBox localBoundingBox() {
        BoundingBox boundingBox = new BoundingBox();
        for (Layer layer : layers) {
            boundingBox.enclose(layer.boundingBox());
        }
        return boundingBox;
    }

    public int size() {
        return layers.size();
    }

    public boolean isEmpty() {
        return layers.isEmpty();
    }

}
