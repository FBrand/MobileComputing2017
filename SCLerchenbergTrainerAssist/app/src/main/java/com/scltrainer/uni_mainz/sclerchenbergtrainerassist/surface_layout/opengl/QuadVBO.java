package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glDeleteBuffers;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by Julian on 07.03.2017.
 */

public class QuadVBO {

    private int id;
    private static QuadVBO unitQuad;

    public QuadVBO(float w, float h, float texW, float texH) {
        float[] vboData = new float[]
                {-w/2, -h/2, 0, texH,
                 w/2, -h/2, texW, texH,
                 -w/2, h/2, 0, 0,
                 w/2, h/2, texW, 0};
        int[] ids = new int[1];
        glGenBuffers(1, ids, 0);
        id = ids[0];
        glBindBuffer(GL_ARRAY_BUFFER, id);
        int size = 4*vboData.length;
        ByteBuffer vboBuffer = ByteBuffer.allocateDirect(size);
        vboBuffer.order(ByteOrder.nativeOrder());
        vboBuffer.asFloatBuffer().put(vboData).rewind();
        glBufferData(GL_ARRAY_BUFFER, size, vboBuffer, GL_STATIC_DRAW);
    }

    public static QuadVBO getUnitQuad() {
        if (unitQuad == null) {
            unitQuad = new QuadVBO(2,2,1,1);
        }
        return unitQuad;
    }

    public static void reset() {
        unitQuad = null;
    }


    public void draw(int posLocation, int texLocation) {
        glBindBuffer(GL_ARRAY_BUFFER, id);
        if (posLocation >= 0) {
            glEnableVertexAttribArray(posLocation);
            glVertexAttribPointer(posLocation, 2, GL_FLOAT, false, 16, 0);
        }
        if (texLocation >= 0) {
            glEnableVertexAttribArray(texLocation);
            glVertexAttribPointer(texLocation, 2, GL_FLOAT, false, 16, 8);
        }
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    }

    public void delete() {
        glDeleteBuffers(1, new int[] {id}, 0);
    }

}
