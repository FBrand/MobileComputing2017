package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl;


import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;


import static android.opengl.GLES20.glUniformMatrix4fv;
import static com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.Layer2DRenderer.*;

public class Path extends Layer {
    public Line line;
    public TriangleVBO triangle;
    public Texture2D texture;
    public static float arrowScale = 1.0f;

    private float getArrowRotation() {
        int end = line.end();
        Vector2f v = line.getPoint(end).sub(line.getPoint(end-1), new Vector2f());
        return (float) ((-v.x > 0? 1 : -1) * Math.acos(v.y/v.length()));
    }

    private Matrix4f modelMatrix() {
        BoundingBox bb = line.getBoundingBox();
        Vector2f center = bb.getCenter();
        Matrix4f M = new Matrix4f();
        M.translate(pos.x,pos.y,0);
        M.translate(center.x,center.y,0);
        M.scale(scale, scale, 1);
        M.translate(-center.x,-center.y,0);
        return M;
    }

    @Override
    protected void doDraw(Matrix4f PV, Matrix4f M, ShaderProgramInfo info) {
        Matrix4f Mnow = PV.mul(M, new Matrix4f());
        Mnow.mul(modelMatrix());
        texture.bind();
        glUniformMatrix4fv(info.location(MVP_MAT_NAME), 1, false, Mnow.get(new float[16]), 0);
        int positionLoc = info.location(POSITION_NAME);
        int texcoordLoc = info.location(TEXCOORD_NAME);
        line.draw(positionLoc, texcoordLoc);
        if (line.numPoints() >= 2) {
            Mnow.translate(new Vector3f(line.getPoint(line.end()), 0));
            Mnow.scale(arrowScale,arrowScale,1);
            Mnow.rotate(getArrowRotation(),0,0,1);
            glUniformMatrix4fv(info.location(MVP_MAT_NAME), 1, false, Mnow.get(new float[16]), 0);
            triangle.draw(positionLoc, texcoordLoc);
        }
    }

    @Override
    protected BoundingBox localBoundingBox() {
        return line.getBoundingBox();
    }

    /**
     * @return the global position of the ith point in the path
     */
    public Vector2f getPoint(int i) {
        return line.getPoint(i).add(pos, new Vector2f());
    }

    public int size() {
        return line.numPoints();
    }
}
