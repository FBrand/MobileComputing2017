package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl;

import org.joml.Vector2f;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Julian on 13.03.2017.
 * Klasse, die eine unsichtbare Box um geometrische Objekte bildet
 */
public class BoundingBox {

    private Vector2f min,max,center;
    private float extent;

    public BoundingBox(BoundingBox bb) {
        this(new Vector2f(bb.min), new Vector2f(bb.max));
    }

    public BoundingBox(Vector2f min, Vector2f max) {
        this.min = min;
        this.max = max;
        update();
    }

    public BoundingBox(float minX, float minY, float maxX, float maxY) {
        this(new Vector2f(minX, minY), new Vector2f(maxX, maxY));
    }

    public BoundingBox() {
        this(new Vector2f(Float.MAX_VALUE), new Vector2f(-Float.MAX_VALUE));
    }

    /**
     *
     */
    private void update() {
        this.center = new Vector2f(min);
        center.add(max).mul(0.5f);
        extent = max.sub(min, new Vector2f()).length();
    }

    /**
     * Berechnet, ob die BoundingBox ein Punkt umschließt
     * @param p
     */
    private void enclosePoint(Vector2f p) {
        min.x = Math.min(min.x, p.x);
        min.y = Math.min(min.y, p.y);
        max.x = Math.max(max.x, p.x);
        max.y = Math.max(max.y, p.y);
    }

    /**
     * Setzt die Attribute auf den Minimalwert bzw. Maximalwert zurück
     */
    public void reset() {
        min.x = min.y = Float.MAX_VALUE;
        max.x = max.y = -Float.MAX_VALUE;
    }

    /**
     * Prüft, ob ein Punkt in der BoundingBox ist
     * @param v
     * @return
     */
    public boolean contains(Vector2f v) {
        return v.x >= min.x && v.y >= min.y && v.x <= max.x && v.y <= max.y;
    }

    /**
     * Prüft ob zwei BoundingBoxen sich schneiden
     * @param bb
     * @return
     */
    public boolean intersect(BoundingBox bb) {
        if (max.x < bb.min.x) return false; // is left of bb
        if (min.x > bb.max.x) return false; // is right of bb
        if (max.y < bb.min.y) return false; // is above bb
        if (min.y > bb.max.y) return false; // is below bb
        return true; // boxes overlap
    }

    /**
     * Prüft, ob eine BoundingBox und eine Pfad sich schneiden
     * @param start
     * @param end
     * @return
     */
    public boolean intersect(Vector2f start, Vector2f end) {
        Vector2f r = end.sub(start, new Vector2f());
        float length = r.length();
        Vector2f d = end.sub(start, new Vector2f()).mul(1/length);

        float tmin = -Float.MAX_VALUE;
        float tmax = Float.MAX_VALUE;

        if (d.x != 0.0) {
            float tx1 = (min.x - start.x)/d.x;
            float tx2 = (max.x - start.x)/d.x;

            tmin = Math.max(tmin, Math.min(tx1, tx2));
            tmax = Math.min(tmax, Math.max(tx1, tx2));
        }

        if (d.y != 0.0) {
            float ty1 = (min.y - start.y)/d.y;
            float ty2 = (max.y - start.y)/d.y;

            tmin = Math.max(tmin, Math.min(ty1, ty2));
            tmax = Math.min(tmax, Math.max(ty1, ty2));
        }

        return tmax >= 0 && tmin <= length && tmax >= tmin;
    }

    /**
     * Berechnet, ob die BoundingBox mehrere Punkte umschließt
     * @param points
     */
    public void enclose(Vector2f... points) {
        enclose(Arrays.asList(points));
    }

    /**
     *Berechnet, ob die BoundingBox mehrere Punkte umschließt
     * @param points
     */
    public void enclose(Collection<Vector2f> points) {
        for (Vector2f p : points) {
            enclosePoint(p);
        }

        update();
    }

    /**
     * Berechnet, ob die BoundingBox eine andere BoundingBox umschließt
     * @param boundingBox
     */
    public void enclose(BoundingBox boundingBox) {
        enclose(boundingBox.min, boundingBox.max);
    }

    /**
     * Translatiert die BoundingBox
     * @param t
     */
    public void translate(Vector2f t) {
        min.add(t);
        max.add(t);
        center.add(t);
    }

    /**
     * Getter für center
     * @return
     */
    public Vector2f getCenter() {
        return center;
    }

    /**
     * Getter für max
     * @return
     */
    public Vector2f getMax() {
        return max;
    }

    /**
     * Getter für min
     * @return
     */
    public Vector2f getMin() {
        return min;
    }

    /**
     * Getter für Extent
     * @return
     */
    public float getExtent() {
        return extent;
    }
}
