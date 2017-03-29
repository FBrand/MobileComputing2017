package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl;

import org.joml.Vector2f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINE_STRIP;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by Julian on 07.03.2017.
 * Klasse, um Linien darzustellen
 */

public class Line implements Iterable<Vector2f> {

    private List<Vector2f> points;
    private ByteBuffer buffer;
    private BoundingBox boundingBox;
    public boolean showPoints;

    public Line() {
        this(new ArrayList<Vector2f>());
    }

    public Line(List<Vector2f> points) {
        this.points = points;
        boundingBox = new BoundingBox();
        updateBoundingBox();
        updateBuffer();
    }

    /**
     * Erneuert den Buffer für neue Punkte
     */
    private void updateBuffer() {
        int bytes = 8*this.points.size();
        buffer = ByteBuffer.allocateDirect(bytes);
        buffer.order(ByteOrder.nativeOrder());

        for (Vector2f point : points) {
            buffer.putFloat(point.x);
            buffer.putFloat(point.y);
        }
        buffer.rewind();

    }

    /**
     * Berechnet eine neue BoundingBox
     */
    private void updateBoundingBox() {
        boundingBox.reset();
        boundingBox.enclose(points);
    }

    /**
     * Erneuert den Buffer für neue Punkte und berechnet eine neue BoundingBox
     */
    public void update() {
        updateBuffer();
        updateBoundingBox();
    }
/*
    public void addPoint(Vector2f point) {
        points.add(point);
        boundingBox.enclose(point);
        updateBuffer();
    }
*/

    /**
     * Gibt den i-ten Punkt zurück
     * @param i
     * @return
     */
    public Vector2f getPoint(int i) {
        return points.get(i);
    }

    /**
     * Getter für die BoundingBox
     * @return
     */
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    /**
     * Gibt den letzen Index der Liste von Punkte zurück
     * @return
     */
    public int end() {
        return points.size()-1;
    }

    /**
     * Gibt die Anzahl der Punkte zurück
     * @return
     */
    public int numPoints() {
        return points.size();
    }

    /**
     * Zeichnet die Linie
     * @param posLocation
     * @param texLocation
     */
    public void draw(int posLocation, int texLocation) {

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        if (posLocation >= 0) {
            glEnableVertexAttribArray(posLocation);
            glVertexAttribPointer(posLocation, 2, GL_FLOAT, false, 8, buffer);
        }
        glDrawArrays(GL_LINE_STRIP, 0, points.size());
        if (showPoints && points.size() > 0) {
            glDrawArrays(GL_POINTS, 0 , points.size()-1);
        }
    }

    @Override
    public Iterator<Vector2f> iterator() {
        return points.iterator();
    }
}
