package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform4f;
import static com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.Layer2DRenderer.COLOR_NAME;
import static com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.Layer2DRenderer.TEXTURE_NAME;
import static com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.Layer2DRenderer.TRANSPARENCY_NAME;

/**
 * Created by Julian on 09.03.2017.
 * Klasse um die Schichte eines Objektes darzustellen
 */

public abstract class Layer {

    public Vector2f pos;
    public Vector4f color = new Vector4f(1,1,1,0);
    public float transparency = 1.0f;
    public float scale;

    /**
     * Zeichnet den Editor
     * @param PV
     * @param M
     * @param info
     */
    protected abstract void doDraw(Matrix4f PV, Matrix4f M, ShaderProgramInfo info);

    protected abstract BoundingBox localBoundingBox();

    /**
     * Setzt Defaultwerte für das Zeichnen des Editors
     * @param PV
     * @param M
     * @param info
     */
    public final void draw(Matrix4f PV, Matrix4f M, ShaderProgramInfo info) {
        glUniform1i(info.location(TEXTURE_NAME), 0);
        glUniform4f(info.location(COLOR_NAME), color.x, color.y, color.z, color.w);
        glUniform1f(info.location(TRANSPARENCY_NAME), transparency);
        doDraw(PV,M,info);
    }

    /**
     * Erstellt die BoundingBox einer Schicht
     * @return
     */
    public BoundingBox boundingBox() {
        BoundingBox bb = new BoundingBox(localBoundingBox());
        bb.translate(pos);
        return bb;
    }

    public Layer() {
        this(new Vector2f(), 1.0f);
    }

    public Layer(Vector2f p) {
        this(p, 1.0f);
    }
    public Layer(Vector2f p, float scale) {
        this.pos = p;
        this.scale = scale;
    }
}
