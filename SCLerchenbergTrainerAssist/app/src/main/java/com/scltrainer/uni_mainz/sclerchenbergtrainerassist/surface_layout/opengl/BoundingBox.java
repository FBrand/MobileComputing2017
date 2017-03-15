package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl;

import org.joml.Vector2f;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Julian on 13.03.2017.
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

    private void update() {
        this.center = new Vector2f(min);
        center.add(max).mul(0.5f);
        extent = max.sub(min, new Vector2f()).length();
    }

    private void enclosePoint(Vector2f p) {
        min.x = Math.min(min.x, p.x);
        min.y = Math.min(min.y, p.y);
        max.x = Math.max(max.x, p.x);
        max.y = Math.max(max.y, p.y);
    }

    public void reset() {
        min.x = min.y = Float.MAX_VALUE;
        max.x = max.y = -Float.MAX_VALUE;
    }

    public boolean contains(Vector2f v) {
        return v.x >= min.x && v.y >= min.y && v.x <= max.x && v.y <= max.y;
    }

    public boolean intersect(BoundingBox bb) {
        if (max.x < bb.min.x) return false; // is left of bb
        if (min.x > bb.max.x) return false; // is right of bb
        if (max.y < bb.min.y) return false; // is above bb
        if (min.y > bb.max.y) return false; // is below bb
        return true; // boxes overlap
    }

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

    public void enclose(Vector2f... points) {
        enclose(Arrays.asList(points));
    }

    public void enclose(Collection<Vector2f> points) {
        for (Vector2f p : points) {
            enclosePoint(p);
        }

        update();
    }

    public void enclose(BoundingBox boundingBox) {
        enclose(boundingBox.min, boundingBox.max);
    }

    public void translate(Vector2f t) {
        min.add(t);
        max.add(t);
        center.add(t);
    }

    public Vector2f getCenter() {
        return center;
    }

    public Vector2f getMax() {
        return max;
    }

    public Vector2f getMin() {
        return min;
    }

    public float getExtent() {
        return extent;
    }
}
