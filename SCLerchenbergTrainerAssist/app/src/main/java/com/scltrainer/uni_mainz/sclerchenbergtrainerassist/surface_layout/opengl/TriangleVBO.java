package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl;

import org.joml.Vector2f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glDeleteBuffers;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by Julian on 13.03.2017.
 * Klasse um Dreieicke zu zeichnen
 */

public class TriangleVBO {

    private int id;
    private static TriangleVBO unitTriangle;

    public TriangleVBO(Vector2f a, Vector2f b, Vector2f c) {
        int[] ids = new int[1];
        glGenBuffers(1, ids, 0);
        id = ids[0];
        glBindBuffer(GL_ARRAY_BUFFER, id);
        ByteBuffer vboBuffer = ByteBuffer.allocateDirect(24);
        vboBuffer.order(ByteOrder.nativeOrder());
        vboBuffer.asFloatBuffer()
                .put(a.x).put(a.y)
                .put(b.x).put(b.y)
                .put(c.x).put(c.y).rewind();
        glBufferData(GL_ARRAY_BUFFER, 24, vboBuffer, GL_STATIC_DRAW);
    }

    /**
     * Erstllt ein einheitliches Dreieick
     * @return
     */
    public static TriangleVBO getUnitTriangle() {
        if (unitTriangle == null) {
            float cosP = (float) Math.cos(Math.toRadians(120));
            float sinP = (float) Math.sin(Math.toRadians(120));
            float cosN = (float) Math.cos(Math.toRadians(-120));
            float sinN = (float) Math.sin(Math.toRadians(-120));

            unitTriangle = new TriangleVBO(
                    new Vector2f(0,1),
                    new Vector2f(-1,0),
                    new Vector2f(1,0));

        }
        return unitTriangle;
    }

    public static void reset() {
        unitTriangle = null;
    }

    /**
     * Zeichnet ein Dreieck
     * @param posLocation
     * @param texLocation
     */
    public void draw(int posLocation, int texLocation) {
        glBindBuffer(GL_ARRAY_BUFFER, id);
        if (posLocation >= 0) {
            glEnableVertexAttribArray(posLocation);
            glVertexAttribPointer(posLocation, 2, GL_FLOAT, false, 8, 0);
        }
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }

    /**
     * Löscht die Dreiecke
     */
    public void delete() {
        glDeleteBuffers(1, new int[] {id}, 0);
    }
}
