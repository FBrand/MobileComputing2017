package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import static android.opengl.GLES20.glUniformMatrix4fv;
import static com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.Layer2DRenderer.*;

public class TexturedQuad extends Layer {
    public QuadVBO quadVBO;
    public Texture2D texture;
    public float phi;
    public float scaleX = 1;
    public float scaleY = 1;
    public boolean drawInNDCSpace;

    public TexturedQuad(Vector2f p) {
        super(p);
    }

    public TexturedQuad() {
        super(new Vector2f());
    }

    private Matrix4f modelMatrix() {
        Matrix4f M = new Matrix4f();
        M.translate(pos.x,pos.y,0);
        M.rotate(phi, 0, 0, 1);
        M.scale(scale*scaleX, scale*scaleY, 1);
        return M;
    }

    public void scaleToTexture() {
        if (texture != null) {
            if (texture.width > texture.height) {
                scaleX = texture.aspect;
                scaleY = 1.0f;
            } else {
                scaleX = 1.0f;
                scaleY = 1.0f / texture.aspect;
            }
        }
    }

    @Override
    protected void doDraw(Matrix4f PV, Matrix4f M, ShaderProgramInfo info) {
        Matrix4f Mnow = new Matrix4f(M);
        Mnow.mul(modelMatrix());
        if (!drawInNDCSpace)
            Mnow = PV.mul(Mnow, new Matrix4f());
        texture.bind();
        glUniformMatrix4fv(info.location(MVP_MAT_NAME), 1, false, Mnow.get(new float[16]), 0);
        quadVBO.draw(info.location(POSITION_NAME), info.location(TEXCOORD_NAME));
    }

    @Override
    protected BoundingBox localBoundingBox() {
        return new BoundingBox(
                -scale*scaleX,-scale*scaleY,
                scale*scaleX,scale*scaleY);
    }

}
