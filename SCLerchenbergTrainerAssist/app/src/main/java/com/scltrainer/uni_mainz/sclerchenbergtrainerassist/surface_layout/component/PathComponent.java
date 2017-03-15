package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component;

import android.content.Context;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.R;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.BoundingBox;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.Layer;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.LayerUpdate;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.Line;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.Path;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.TextureFactory;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.TriangleVBO;

import static com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.Util.PATH_ALPHA;

public class PathComponent extends Component<Path, PathComponent> {

    private PathType type;
    private List<Vector2f> points = new ArrayList<>();

    public PathComponent(PathType type) {
        this.type = type;
    }

    public void addPoint(Vector2f point) {
        points.add(point);
        planUpdate(new PointUpdate());
    }

    private Vector2f worldPoint(int i) {
        return pos.add(points.get(i), new Vector2f());
    }

    @Override
    protected Path doMakeLayer() {
        Path layer = new Path();
        layer.pos = pos;
        layer.line = new Line(points);
        layer.color = type.color;
        layer.transparency = PATH_ALPHA;
        return layer;
    }

    @Override
    public void init(Context context) {
        Path layer = getLayer();
        layer.triangle = TriangleVBO.getUnitTriangle();
        layer.texture = TextureFactory.loadTexture(context, R.drawable.empty);
    }

    @Override
    public boolean select(Vector2f v, List<? super PathComponent> selection) {
        Layer layer = getLayer();
        if (layer == null || !layer.boundingBox().contains(v))
            return false;

        selection.add(this);
        return true;
    }

    @Override
    public boolean select(BoundingBox box, List<? super PathComponent> selection) {
        Layer layer = getLayer();
        if (layer == null)
            return false;
        for (int i = 1; i < points.size(); i++) {
            if (box.intersect(worldPoint(i-1), worldPoint(i))) {
                selection.add(this);
                return true;
            }
        }

        return false;
    }

    public int size() {
        return points.size();
    }

    private class PointUpdate implements LayerUpdate {
        @Override
        public void run() {
            Line line = getLayer().line;
            line.update();
        }
        @Override
        public boolean equals(Object obj) {
            return (obj instanceof PointUpdate);
        }
    }
}
