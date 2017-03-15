package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * Created by Julian on 12.03.2017.
 */

public interface GLRendererListener {
    void onCreateGL(EGLConfig eglConfig);
    void onDrawGL();
    void onResizeGL(int w, int h);
}
