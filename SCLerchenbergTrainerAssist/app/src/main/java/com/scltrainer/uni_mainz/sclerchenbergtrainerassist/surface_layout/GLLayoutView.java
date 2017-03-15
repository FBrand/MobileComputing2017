package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout;

import android.content.Context;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.Layout;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.Viewable;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.BoundingBox;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.GLRenderer;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.GLRendererListener;

import org.joml.Vector2f;

import javax.microedition.khronos.egl.EGLConfig;


public class GLLayoutView extends GLSurfaceView implements GLRendererListener {


    protected Layer2DRenderer renderer;
    protected Layout layout;
    protected Vector2f touchSize;

    public GLLayoutView(Context context){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);
        touchSize = Util.pixelDensity.mul(Util.TOUCH_SIZE_INCH, new Vector2f());

        renderer = new Layer2DRenderer(getContext());
        GLRenderer glRenderer = new GLRenderer();
        glRenderer.addListener(renderer);
        glRenderer.addListener(this);
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(glRenderer);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
        setOnTouchListener(doMakeLayoutGetureListener(context));
    }

    protected LayoutGestureListener doMakeLayoutGetureListener(Context context) {
        return new LayoutGestureListener(context, renderer);
    }
    protected void updateLayer(final Viewable<?, ?> viewable) {
        queueEvent(new Runnable() {
            public void run() {
                viewable.updateLayer();
                requestRender();
            }});
    }
    protected void initLayer(final Viewable<?, ?> viewable) {
        queueEvent(new Runnable() {
            public void run() {
                viewable.init(getContext());
                requestRender();
            }});
    }

    protected Vector2f worldCoord(MotionEvent e) {
        Vector2f pixel = new Vector2f(e.getX(), e.getY());
        return renderer.pixelToWorldCoords(pixel);
    }

    protected BoundingBox touchBB(MotionEvent e) {
        Vector2f pPixel = new Vector2f(e.getX(), e.getY());
        Vector2f min = new Vector2f(pPixel.x - touchSize.x, pPixel.y + touchSize.y);
        Vector2f max = new Vector2f(pPixel.x + touchSize.x, pPixel.y - touchSize.y);
        return new BoundingBox(renderer.pixelToWorldCoords(min), renderer.pixelToWorldCoords(max));
    }


    public void setLayout(final Layout layout) {
        this.layout = layout;
    }


    @Override
    public void onCreateGL(EGLConfig eglConfig) {


        queueEvent(new Runnable() {
            public void run() {
                GLES10.glLineWidth(Util.pixelDensity.x*Util.LINE_WIDTH_INCH);
                GLES20.glUniform1f(renderer.getProgramInfo().location(Layer2DRenderer.POINT_SIZE_NAME), Util.pixelDensity.x*Util.POINT_SIZE_INCH);
                renderer.addLayer(layout.background.getLayer());
                renderer.addLayer(layout.materials.getLayer());
                renderer.addLayer(layout.paths.getLayer());

            }});
        initLayer(layout.background);
        initLayer(layout.materials);
        initLayer(layout.paths);
    }

    @Override
    public void onResizeGL(int w, int h) {
        float aspect = (float) w/h;
        queueEvent(new Runnable() {
            @Override
            public void run() {
                renderer.updateWorldBounds(1.0f/layout.background.getFieldAspect());
                layout.background.setSurfaceSize(renderer.getWorldBounds());
            }
        });
        updateLayer(layout.background);
    }

    @Override
    public void onDrawGL() {}

    /**
     * Handles Layout view gestures.
     */
    public class LayoutGestureListener extends GestureDetector.SimpleOnGestureListener
            implements ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {

        private GestureDetector gestureDetector;
        private ScaleGestureDetector scaleGestureDetector;
        private RendererBase renderer;
        protected boolean scaling = false;

        public LayoutGestureListener(Context context, RendererBase renderer) {
            scaleGestureDetector = new ScaleGestureDetector(context, this);
            gestureDetector = new GestureDetector(context, this);
            this.renderer = renderer;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // Forward the scale event to the renderer.
            final float amount = detector.getScaleFactor();
            final Vector2f zoomCenter = new Vector2f(detector.getFocusX(), detector.getFocusY());

            final Vector2f zc = renderer.pixelToWorldCoords(zoomCenter);
            renderer.zoom(amount, zc);
            requestRender();
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return scaling = true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            scaling = false;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            scaleGestureDetector.onTouchEvent(motionEvent);
            gestureDetector.onTouchEvent(motionEvent);
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {

            return true;
        }


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (scaling) // no scrolling while scaling
                return false;
            final Vector2f end = new Vector2f(e2.getX(), e2.getY());
            final Vector2f start = new Vector2f(end).sub(distanceX, distanceY);

            final Vector2f t = renderer.pixelToWorldCoords(end).sub(renderer.pixelToWorldCoords(start));

            renderer.translate(t);
            requestRender();
            return true;
        }
    }
}
