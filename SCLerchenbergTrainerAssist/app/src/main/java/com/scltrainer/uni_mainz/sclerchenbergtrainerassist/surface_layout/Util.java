package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout;

import android.app.Activity;
import android.util.DisplayMetrics;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.QuadVBO;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.TextureFactory;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.TriangleVBO;

import org.joml.Vector2f;
import org.joml.Vector4f;

/**
 * Created by Julian on 13.03.2017.
 */

public class Util {

    public static Vector2f pixelDensity;
    public static final float LINE_WIDTH_INCH = 0.03f;
    public static final float ARROW_SIZE_INCH = 0.12f;
    public static final float SCALE_SELECTED = 0.7f;
    public static final float MIN_SCALE_SELECTED = 1.1f;
    public static final float ALPHA_SELECTED = 0.5f;
    public static final float REMOVE_LAYER_SCALE_NDC = 0.1f;
    public static final float REMOVE_LAYER_GREY = 0.5f;
    public static final Vector4f REMOVE_COLOR = new Vector4f(1,0f,0f,1);
    public static final float MAX_ZOOM = 8.0f;
    public static final float PATH_ALPHA = 0.65f;
    public static final float TOUCH_SIZE_INCH = 0.2f;
    public static final float POINT_SIZE_INCH = 0.06f;

    private Util() {
    }

    public static void init(Activity activity) {
        pixelDensity = getPixelDensity(activity);
    }

    private static Vector2f getPixelDensity(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return new Vector2f(metrics.xdpi, metrics.ydpi);
    }

    public static void forceReloadResources() {
        TextureFactory.reset();
        QuadVBO.reset();
        TriangleVBO.reset();
    }
}
