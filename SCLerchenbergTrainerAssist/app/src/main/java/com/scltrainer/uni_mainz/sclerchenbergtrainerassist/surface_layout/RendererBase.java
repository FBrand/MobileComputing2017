package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.GLRendererListener;

import org.joml.Vector2f;


/**
 * The base class of a renderer.
 * It listens to GLSurface events such as create, resize and draw.
 */
public abstract class RendererBase implements GLRendererListener {

    protected float zoom = 1.0f;
    protected Vector2f center = new Vector2f();
    protected int width, height;
    protected float aspect;

    public abstract void translate(Vector2f t);

    public abstract void zoom(float amount, Vector2f zoomCenter);

    public abstract Vector2f worldCoords(Vector2f ndc);


    /**
     * Converts pixel coordinates to normalized device coordinates.
     * @param pixel The pixel to convert.
     * @return The NDC coordinates of the pixel.
     */
    public Vector2f ndcCoords(Vector2f pixel) {
        Vector2f v = new Vector2f();
        v.x = 2.0f*pixel.x/width - 1.0f;
        v.y = 1.0f - 2.0f*pixel.y/height;
        return v;
    }

    /**
     * Converts pixel coordinates to world coordinates.
     * @param pixel The pixel to convert.
     * @return The world coordinates of the pixel.
     */
    public Vector2f pixelToWorldCoords(Vector2f pixel) {
        return worldCoords(ndcCoords(pixel));
    }

    @Override
    public void onResizeGL(int w, int h) {
        this.width = w;
        this.height = h;
        aspect = (float) w/h;
    }

    /// GETTERS

    public float getZoom() {
        return zoom;
    }

    public Vector2f getCenter() {
        return center;
    }
/*
    public int getOrientation() {
        return orientation;
    }
*/
    public float getAspect() {
        return aspect;
    }
}
