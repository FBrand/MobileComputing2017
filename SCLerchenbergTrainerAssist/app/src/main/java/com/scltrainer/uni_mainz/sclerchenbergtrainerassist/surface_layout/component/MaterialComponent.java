package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component;

import android.content.Context;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.BoundingBox;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.Layer;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.QuadVBO;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.TextureFactory;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.TexturedQuad;

import org.joml.Vector2f;

import java.util.List;

public class MaterialComponent extends Component<TexturedQuad, MaterialComponent> {

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
    public void init(Context context) {
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
}
