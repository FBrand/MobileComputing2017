package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl;

import android.opengl.GLSurfaceView;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Julian on 12.03.2017.
 * Klasse zum Rendern von OpenGL Anwendungen
 */
public class GLRenderer implements GLSurfaceView.Renderer {

    private List<GLRendererListener> listeners = new ArrayList<>();

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        for (GLRendererListener listener : listeners) {
            listener.onCreateGL(eglConfig);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        for (GLRendererListener listener : listeners) {
            listener.onDrawGL();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int w, int h) {
        for (GLRendererListener listener : listeners) {
            listener.onResizeGL(w,h);
        }
    }

    /**
     * Fügt einen GLRendererListener hinzu
     * @param listener
     */
    public void addListener(GLRendererListener listener) {
        this.listeners.add(listener);
    }

}
