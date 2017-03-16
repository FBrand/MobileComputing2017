package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component;

import android.content.Context;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.BoundingBox;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.Layer;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.QuadVBO;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.TextureFactory;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.TexturedQuad;

import org.joml.Vector2f;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MaterialComponent extends Component<TexturedQuad, MaterialComponent> {

    private static final String TYPE_JSON_NAME = "Type";
    private static final String POSITION_JSON_NAME = "Position";
    private static final String X_JSON_NAME = "x";
    private static final String Y_JSON_NAME = "y";

    private MaterialType type;

    public MaterialComponent(MaterialType type) {
        this.type = type;
    }

    public MaterialComponent(MaterialType type, Vector2f pos) {
        super(pos);
        this.type = type;
    }

    @Override
    protected TexturedQuad doMakeLayer() {
        TexturedQuad layer = new TexturedQuad();
        layer.pos = pos;
        layer.scale = type.getScale();
        return layer;
    }

    @Override
    protected void doInit(Context context) {
        TexturedQuad layer = getLayer();
        layer.quadVBO = QuadVBO.getUnitQuad();
        layer.texture = TextureFactory.loadTexture(context, type.getTextureResID());
        layer.scaleToTexture();
    }

    @Override
    public boolean select(Vector2f v, List<? super MaterialComponent> selection) {
        Layer layer = getLayer();
        if (layer == null || !layer.boundingBox().contains(v))
            return false;

        selection.add(this);
        return true;
    }

    @Override
    public boolean select(BoundingBox box, List<? super MaterialComponent> selection) {
        Layer layer = getLayer();
        if (layer == null || !layer.boundingBox().intersect(box))
            return false;

        selection.add(this);
        return true;
    }

    public MaterialType getType() {
        return type;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
            return new JSONObject().put(TYPE_JSON_NAME, type.ordinal())
                    .put(POSITION_JSON_NAME, new JSONObject().put(X_JSON_NAME, pos.x()).put(Y_JSON_NAME, pos.y()));
    }

    public static MaterialComponent fromJSON(JSONObject json) throws JSONException {
        MaterialType type = MaterialType.values()[json.getInt(TYPE_JSON_NAME)];
        JSONObject position = json.getJSONObject(POSITION_JSON_NAME);
        float x = (float) position.getDouble(X_JSON_NAME);
        float y = (float) position.getDouble(Y_JSON_NAME);
        return new MaterialComponent(type, new Vector2f(x,y));
    }
}
