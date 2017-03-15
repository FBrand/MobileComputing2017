package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl;

/**
 * Created by Julian on 09.03.2017.
 */

/**
 * Describes an update of a layer.
 * Make sure to call the run method from a thread with an active OpenGL context.
 */
public interface LayerUpdate {

    public void run();
}
