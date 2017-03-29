package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component;

import android.content.Context;
import android.util.Log;

import org.joml.Vector2f;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

/**
 * A component representing a path.
 */
public class PathComponent extends Component<Path, PathComponent> {

    private static final String TYPE_JSON_NAME = "Type";
    private static final String POSITIONS_JSON_NAME = "Positions";
    private static final String POSITION_JSON_NAME = "Position";
    private static final String X_JSON_NAME = "x";
    private static final String Y_JSON_NAME = "y";

    private PathType type;
    private List<Vector2f> points = new ArrayList<>();

    public PathComponent(PathType type) {
        this(type, new Vector2f());
    }

    public PathComponent(PathType type, Vector2f pos) {
        this.type = type;
        this.pos = pos;
    }

    /**
     * Adds a point at the end of the path.
     * @param point The point to add.
     */
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
    protected void doInit(Context context) {
        Path layer = getLayer();
        layer.line.update();
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

    /**
     * The number of points of this path.
     * @return The point count.
     */
    public int size() {
        return points.size();
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONArray temp = new JSONArray();
        for(Vector2f point : points){
            temp.put(new JSONObject().put(X_JSON_NAME, point.x).put(Y_JSON_NAME, point.y));
        }
        return new JSONObject().put(TYPE_JSON_NAME, type.ordinal())
                .put(POSITION_JSON_NAME, new JSONObject().put(X_JSON_NAME, pos.x).put(Y_JSON_NAME, pos.y))
                .put(POSITIONS_JSON_NAME, temp);
    }

    /**
     * Creates a PathComponent from a JSON object.
     * @param json The json object to create the PathComponent from.
     * @return The created path component.
     * @throws JSONException if something went wrong.
     */
    public static PathComponent fromJSON(JSONObject json) throws JSONException {

        PathType type = PathType.values()[json.getInt(TYPE_JSON_NAME)];
        JSONObject position = json.getJSONObject(POSITION_JSON_NAME);
        float x = (float) position.getDouble(X_JSON_NAME);
        float y = (float) position.getDouble(Y_JSON_NAME);
        PathComponent path = new PathComponent(type, new Vector2f(x,y));

        JSONArray points = json.getJSONArray(POSITIONS_JSON_NAME);
        for (int i = 0; i < points.length(); i++) {
            JSONObject point = points.getJSONObject(i);
            x = (float) point.getDouble(X_JSON_NAME);
            y = (float) point.getDouble(Y_JSON_NAME);
            path.points.add(new Vector2f(x,y));
        }
        return path;
    }

    /**
     * Updates the layer if a point was added or moved.
     */
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
