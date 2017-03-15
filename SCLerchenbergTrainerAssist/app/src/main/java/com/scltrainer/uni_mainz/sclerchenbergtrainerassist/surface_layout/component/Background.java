package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component;

import android.content.Context;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.LayerGroup;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.LayerUpdate;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.QuadVBO;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.TextureFactory;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.TexturedQuad;

import org.joml.Vector2f;


public class Background extends Viewable<LayerGroup, Background> {

    private SurfaceType surfaceType;
    private FieldType fieldType;
    private Vector2f size = new Vector2f(1);
    private TexturedQuad surface, field;

    public Background(SurfaceType surfaceType, FieldType fieldType) {
        this.surfaceType = surfaceType;
        this.fieldType = fieldType;
    }

    public void setSurfaceSize(Vector2f size) {
        this.size.x = size.x;
        this.size.y = size.y;
        planUpdate(new LayerVBOUpdate());
    }

    @Override
    protected LayerGroup doMakeLayer() {
        surface = new TexturedQuad();
        field = new TexturedQuad();
        return new LayerGroup(surface,field);
    }

    @Override
    public void init(Context context) {
        surface.quadVBO = new QuadVBO(2*size.x, 2*size.y, size.x, size.y);
        surface.texture = TextureFactory.loadTexture(context, surfaceType.getTextureResID());
        field.quadVBO = QuadVBO.getUnitQuad();
        field.texture = TextureFactory.loadTexture(context, fieldType.getTextureResID());
        field.scaleToTexture();
    }

    public float getFieldAspect() {
        return field.texture.aspect;
    }

    private class LayerVBOUpdate implements LayerUpdate {


        @Override
        public void run() {
            surface.quadVBO.delete();
            surface.quadVBO = new QuadVBO(2*size.x, 2*size.y, size.x, size.y);
        }
    }
}
