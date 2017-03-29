package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl;


import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * Organisiert die Schichten von geometrischen Objekten
 */
public class LayerGroup extends Layer {

    private LinkedHashSet<Layer> layers = new LinkedHashSet<>();

    public LayerGroup(Vector2f p, Layer... layers) {
        super(p);
        this.layers.addAll(Arrays.asList(layers));
    }

    public LayerGroup(Layer... layers) {
        this(new Vector2f(), layers);
    }

    /**
     * Fügt ein neue Schicht hinzu
     * @param layer
     */
    public void add(Layer layer) {
        synchronized (layers) {
            layers.add(layer);
        }
    }

    /**
     * Entfernt eine Schicht
     * @param layer
     */
    public void remove(Layer layer) {
        synchronized (layers) {
            layers.remove(layer);
        }
    }

    /**
     * Gibt alle Schichten aus
     * @param i
     * @return
     */
    public Layer getLayer(int i) {
        Iterator<Layer> it = layers.iterator();
        for (int j = 0; j < i-1; j++) {
            it.next();
        }
        return it.next();
    }

    /**
     * Erstellt eine Modelmatrix
     * @return
     */
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

    /**
     * Gibt die Anzahl der gespeicherten Schichten aus
     * @return
     */
    public int size() {
        return layers.size();
    }

    /**
     * Prüft ob es Schichten gibt
     * @return
     */
    public boolean isEmpty() {
        return layers.isEmpty();
    }

    /**
     * Löscht alle Schichten
     */
    public void clear() {
        layers.clear();
    }
}
