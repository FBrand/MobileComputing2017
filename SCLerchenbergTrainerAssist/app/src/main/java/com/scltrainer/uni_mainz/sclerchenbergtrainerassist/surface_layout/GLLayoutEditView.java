package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.R;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.Component;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.ComponentGroup;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.MaterialComponent;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.MaterialType;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.PathComponent;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.PathType;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.BoundingBox;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.Layer;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.Path;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.QuadVBO;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.TextureFactory;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.TexturedQuad;

import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;

import static com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.Util.*;

/**
 * The GLSurface to view and edit the layout.
 * It handles several touch gestures to edit the layout.
 * @see GLLayoutView
 */
public class GLLayoutEditView extends GLLayoutView {

    private Layer removeLayer;

    private LayoutEditGestureListener layoutListener;
    private final DragAndDropListener<MaterialComponent> materialDragListener;
    private final DragAndDropListener<PathComponent> pathDragListener;
    private final PathPointGestureListener pathPointListener;
    public final MaterialOnClickListener materialOnClickListener;
    public final PathOnClickListener pathOnClickListener;
    public final PathEditOnClickListener pathEditOnClickListener;

    /**
     * Creates a new GLLayoutEditView.
     * @param context The app context.
     */
    public GLLayoutEditView(Context context){
        super(context);

        materialDragListener = new DragAndDropListener<>(context);
        pathDragListener = new DragAndDropListener<>(context);
        pathPointListener = new PathPointGestureListener(context);
        materialOnClickListener = new MaterialOnClickListener();
        pathOnClickListener = new PathOnClickListener();
        pathEditOnClickListener = new PathEditOnClickListener();
    }

    private void createRemoveLayer() {
        TexturedQuad removeLayer = new TexturedQuad();
        removeLayer.texture = TextureFactory.loadTexture(getContext(), android.R.drawable.ic_delete);
        removeLayer.color = new Vector4f(Util.REMOVE_LAYER_GREY);
        removeLayer.scaleX = Util.REMOVE_LAYER_SCALE_NDC/renderer.getAspect();
        removeLayer.scaleY = Util.REMOVE_LAYER_SCALE_NDC;
        removeLayer.pos = new Vector2f(1-removeLayer.scaleX,1-Util.REMOVE_LAYER_SCALE_NDC);
        removeLayer.drawInNDCSpace = true;
        removeLayer.quadVBO = QuadVBO.getUnitQuad();
        this.removeLayer = removeLayer;
    }

    private float getScaleSelected(Layer layer) {
        return Math.max(Util.MIN_SCALE_SELECTED, Util.SCALE_SELECTED/renderer.getZoom()/layer.boundingBox().getExtent());
    }

    @Override
    protected LayoutEditGestureListener doMakeLayoutGetureListener(Context context) {
        return layoutListener = new LayoutEditGestureListener(context);
    }

    @Override
    public void onCreateGL(EGLConfig eglConfig) {
        super.onCreateGL(eglConfig);
        queueEvent(new Runnable() {
            public void run() {
                createRemoveLayer();
            }});
    }

    /**
     * Handles the clicks on material thumbnails within the selection list.
     */
    public class MaterialOnClickListener implements OnItemMultiClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            layoutListener.selectedMaterialType = MaterialType.values()[position];
            layoutListener.resetPathComponent();
            layoutListener.selectionType = SelectionType.MATERIAL;
            setOnTouchListener(layoutListener);
        }

        @Override
        public void onItemReClick(AdapterView<?> var1, View var2, int var3, long var4) {
            layoutListener.selectedMaterialType = null;
            layoutListener.selectionType = SelectionType.NONE;
            setOnTouchListener(layoutListener);
        }
    }
    /**
     * Handles the clicks on path thumbnails within the selection list.
     */
    public class PathOnClickListener implements OnItemMultiClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            layoutListener.selectedPathType = PathType.values()[position];
            layoutListener.selectionType = SelectionType.PATH;
            layoutListener.resetPathComponent();
            setOnTouchListener(layoutListener);
        }

        @Override
        public void onItemReClick(AdapterView<?> var1, View var2, int var3, long var4) {
            layoutListener.selectionType = SelectionType.NONE;
            layoutListener.resetPathComponent();
            setOnTouchListener(layoutListener);
        }
    }

    /**
     * Handles the clicks on path edit thumbnails within the selection list.
     */
    public class PathEditOnClickListener implements OnItemMultiClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            layoutListener.selectionType = SelectionType.PATH_EDIT;
            layoutListener.resetPathComponent();
            setOnTouchListener(layoutListener);
        }

        @Override
        public void onItemReClick(AdapterView<?> var1, View var2, int var3, long var4) {
            layoutListener.resetPathComponent();
            layoutListener.selectionType = SelectionType.NONE;
            setOnTouchListener(layoutListener);
        }
    }


    /**
     * Handles drag and drop gestures of a arbitrary component, e.g. a material or path.
     */
    private class DragAndDropListener<T extends Component<?, T>> extends
            GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {

        private GestureDetector gestureDetector;
        private boolean remove;
        private float transparency, scale;
        private Vector4f color;
        private Layer selectedLayer;
        private T selectedComponent;
        private ComponentGroup<T> parent;
        private Vector2f lastPos;


        public DragAndDropListener(Context context) {
            gestureDetector = new GestureDetector(context, this);
        }

        public void setSelected(T selectedComponent, ComponentGroup<T> parent, MotionEvent e) {
            this.selectedComponent = selectedComponent;
            this.parent = parent;
            selectedLayer = selectedComponent.getLayer();
            transparency = selectedLayer.transparency;
            color = new Vector4f(selectedLayer.color);
            scale = selectedLayer.scale;
            lastPos = worldCoord(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            final Vector2f end = new Vector2f(e2.getX(), e2.getY());
            Vector2f endNDC = renderer.ndcCoords(end);
            Vector2f endWorld = renderer.worldCoords(endNDC);

            BoundingBox removeBB = removeLayer.boundingBox();
            if (removeBB.contains(endNDC) || endNDC.x >= 1 || endNDC.y >= 1) {
                removeLayer.color =  Util.REMOVE_COLOR;
                selectedLayer.color = Util.REMOVE_COLOR;
                remove = true;
            } else {
                removeLayer.color = new Vector4f(Util.REMOVE_LAYER_GREY);
                selectedLayer.color = color;
                remove = false;
            }
            selectedComponent.translate(endWorld.sub(lastPos, new Vector2f()));
            updateLayer(selectedComponent);
            lastPos = endWorld;
            return true;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (remove) {
                    parent.remove(selectedComponent);
                    updateLayer(parent);
                    layoutListener.resetPathComponent();
                } else {
                    Layer layer = selectedComponent.getLayer();
                    layer.scale = scale;
                    layer.transparency = transparency;
                    layer.color = color;
                    updateLayer(selectedComponent);
                }

                removeLayer.color = new Vector4f(Util.REMOVE_LAYER_GREY);
                renderer.removeLayer(removeLayer);
                setOnTouchListener(layoutListener);
            }
            return gestureDetector.onTouchEvent(motionEvent);
        }
    }



    /**
     * Handles several Layout edit and view gestures and delegates them to other listeners if not handled directly.
     */
    private class LayoutEditGestureListener extends LayoutGestureListener {

        public SelectionType selectionType = SelectionType.NONE;
        public MaterialType selectedMaterialType;
        public PathType selectedPathType;
        private PathComponent pathComponent;


        public LayoutEditGestureListener(Context context) {
            super(context, renderer);
        }

        private void selectLayer(Component<?,?> component, float scale) {
            Layer layer = component.getLayer();
            layer.scale *= scale;
            layer.transparency = Util.ALPHA_SELECTED;
            renderer.addLayer(removeLayer);
            updateLayer(component);
        }

        private boolean selectPathForDrag(BoundingBox touchBB, MotionEvent e) {
            List<PathComponent> pathSelection = new ArrayList<>();
            if (layout.paths.select(touchBB, pathSelection)) {
                PathComponent pathComponent = pathSelection.get(pathSelection.size()-1);
                pathDragListener.setSelected(pathComponent, layout.paths, e);
                selectLayer(pathComponent, MIN_SCALE_SELECTED);
                setOnTouchListener(pathDragListener);
                return true;
            }
            return false;
        }

        private boolean selectMaterialForDrag(BoundingBox touchBB, MotionEvent e) {
            List<MaterialComponent> materialSelection = new ArrayList<>();
            if (layout.materials.select(touchBB, materialSelection)) {
                MaterialComponent materialComponent = materialSelection.get(materialSelection.size() - 1); // select highest component only
                materialDragListener.setSelected(materialComponent, layout.materials, e);
                selectLayer(materialComponent, getScaleSelected(materialComponent.getLayer()));
                setOnTouchListener(materialDragListener);
                return true;
            }
            return false;
        }

        private boolean selectPathPointForDrag(BoundingBox touchBB, MotionEvent e) {
            if (pathComponent != null && pathComponent.size() >= 2) {
                // check for point selection
                Path path = pathComponent.getLayer();
                for (int i = 0; i < path.size(); i++) {
                    if (touchBB.contains(path.getPoint(i))) {
                        pathPointListener.setSelected(path.line.getPoint(i), pathComponent, e);
                        setOnTouchListener(pathPointListener);
                        return true;
                    }
                }
            }
            return false;
        }

        public void resetPathComponent() {
            if (pathComponent != null) {
                pathComponent.getLayer().transparency = Util.PATH_ALPHA;
                pathComponent.getLayer().line.showPoints = false;
                updateLayer(pathComponent);
            }
            pathComponent = null;
        }

        public void setPathComponent(PathComponent component) {
            resetPathComponent();
            pathComponent = component;
            pathComponent.getLayer().transparency = 1.0f;
            if (selectionType == SelectionType.PATH_EDIT) {
                pathComponent.getLayer().line.showPoints = true;
            }
            updateLayer(pathComponent);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            switch (selectionType) {
                case NONE: return false;
                case MATERIAL:
                    // create and add material
                    final Vector2f pos = worldCoord(e);
                    MaterialComponent component = new MaterialComponent(selectedMaterialType, pos);
                    layout.materials.add(component);
                    updateLayer(layout.materials);
                    return true;
                case PATH:
                    if (pathComponent == null) {
                        setPathComponent(new PathComponent(selectedPathType));
                    }
                    // add point
                    pathComponent.addPoint(worldCoord(e).sub(pathComponent.getPosition()));
                    updateLayer(pathComponent);
                    if (pathComponent.size() == 2) { // a valid path has at least 2 points
                        layout.paths.add(pathComponent);
                        updateLayer(layout.paths);
                    }
                    return true;
            }
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            switch (selectionType) {
                case PATH_EDIT:
                    if (pathComponent != null){
                        // add point
                        pathComponent.addPoint(worldCoord(e).sub(pathComponent.getPosition()));
                        updateLayer(pathComponent);
                    }
                    return true;
            }
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            switch (selectionType) {
                case PATH_EDIT:
                    List<PathComponent> pathSelection = new ArrayList<>();
                    if (layout.paths.select(touchBB(e), pathSelection)) { // check for path selection
                        setPathComponent(pathSelection.get(pathSelection.size()-1)); // select highest component only
                    }
                    return true;
                default: return false;
            }
        }

        @Override
        public void onLongPress(MotionEvent e) {
            BoundingBox touchBB = touchBB(e);
            switch (selectionType) {
                case NONE:
                case MATERIAL:
                    if (!selectMaterialForDrag(touchBB, e))
                        selectPathForDrag(touchBB, e);
                    break;
                case PATH:
                    if (!selectPathForDrag(touchBB, e))
                        selectMaterialForDrag(touchBB, e);
                    break;
                case PATH_EDIT:
                    if (!selectPathPointForDrag(touchBB, e))
                        if (!selectPathForDrag(touchBB, e))
                            selectMaterialForDrag(touchBB, e);
            }
        }

    }

    /**
     * Handles gestures to drag a path point.
     */
    private class PathPointGestureListener
            extends GestureDetector.SimpleOnGestureListener implements
            View.OnTouchListener {

        private GestureDetector gestureDetector;
        private Vector2f selectedPoint,lastPos;
        private PathComponent pathComponent;

        public PathPointGestureListener(Context context) {
            gestureDetector = new GestureDetector(context, this);
        }

        public void setSelected(Vector2f selectedPoint, PathComponent parent, MotionEvent e) {
            this.selectedPoint = selectedPoint;
            this.pathComponent = parent;
            lastPos = worldCoord(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Vector2f p = worldCoord(e2);
            Vector2f t = p.sub(lastPos, new Vector2f());
            selectedPoint.x += t.x;
            selectedPoint.y += t.y;
            pathComponent.getLayer().line.update();
            updateLayer(pathComponent);
            lastPos = p;
            return true;
        }

        @Override
        public boolean onTouch(View view, MotionEvent e) {
            gestureDetector.onTouchEvent(e);
            if (e.getAction() == MotionEvent.ACTION_UP) {
                setOnTouchListener(layoutListener);
                return false;
            }
            return true;
        }
    }

    /**
     * The several types that can be selected from the selection list.
     */
    private enum SelectionType {
        NONE,MATERIAL,PATH,PATH_EDIT;
    }
}
