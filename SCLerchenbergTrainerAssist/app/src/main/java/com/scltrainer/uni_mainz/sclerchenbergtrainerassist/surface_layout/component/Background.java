package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component;

import android.content.Context;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.LayerGroup;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.LayerUpdate;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.QuadVBO;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.TextureFactory;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.TexturedQuad;

import org.joml.Vector2f;
import org.json.JSONException;
import org.json.JSONObject;


public class Background extends Viewable<LayerGroup, Background> {

    private static final String SURFACE_TYPE_JSON_NAME = "surfaceType";
    private static final String FIELD_TYPE_JSON_NAME = "fieldType";

    private SurfaceType surfaceType;
    private FieldType fieldType;
    private Vector2f size = new Vector2f(1);
    private TexturedQuad surface, field;
    private Context context;

    public Background(SurfaceType surfaceType, FieldType fieldType) {
        this.surfaceType = surfaceType;
        this.fieldType = fieldType;
    }

    public void setSurfaceSize(Vector2f size) {
        this.size.x = size.x;
        this.size.y = size.y;
        planUpdate(new LayerVBOUpdate());
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
        planUpdate(new TextureUpdate());
    }

    public void setSurfaceType(SurfaceType surfaceType) {
        this.surfaceType = surfaceType;
        planUpdate(new TextureUpdate());
    }

    @Override
    protected LayerGroup doMakeLayer() {
        surface = new TexturedQuad();
        field = new TexturedQuad();
        return new LayerGroup(surface,field);
    }

    @Override
    protected void doInit(Context context) {
        this.context = context;
        if (surface.quadVBO != null) {
            surface.quadVBO.delete();
        }
        surface.quadVBO = new QuadVBO(2*size.x, 2*size.y, size.x, size.y);
        surface.texture = TextureFactory.loadTexture(context, surfaceType.getTextureResID());
        if (field.quadVBO == null)
            field.quadVBO = QuadVBO.getUnitQuad();
        field.texture = TextureFactory.loadTexture(context, fieldType.getTextureResID());
        field.scaleToTexture();

    }

    public float getFieldAspect() {
        return field.texture.aspect;
    }

    public JSONObject toJSON() throws JSONException {
        return new JSONObject().put(SURFACE_TYPE_JSON_NAME, surfaceType.ordinal())
                .put(FIELD_TYPE_JSON_NAME, fieldType.ordinal());
    }

    public static Background fromJSON(JSONObject json) throws JSONException {
        SurfaceType surfaceType = SurfaceType.values()[json.getInt(SURFACE_TYPE_JSON_NAME)];
        FieldType fieldType = FieldType.values()[json.getInt(FIELD_TYPE_JSON_NAME)];
        return new Background(surfaceType, fieldType);
    }

    private class LayerVBOUpdate implements LayerUpdate {

        @Override
        public void run() {
            surface.quadVBO.delete();
            surface.quadVBO = new QuadVBO(2*size.x, 2*size.y, size.x, size.y);
        }
    }

    private class TextureUpdate implements LayerUpdate {
        @Override
        public void run() {
            surface.texture = TextureFactory.loadTexture(context, surfaceType.getTextureResID());
            field.texture = TextureFactory.loadTexture(context, fieldType.getTextureResID());
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof TextureUpdate;
        }
    }
}
