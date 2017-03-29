package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_NEAREST;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glTexParameteri;

/**
 * Created by Julian on 07.03.2017.
 * Klasse um Teturen darstellen
 */

public class Texture2D {

    public final int id;
    public final int width, height;
    public final float aspect;

    public Texture2D(Bitmap bitmap) {
        final int[] textureHandle = new int[1];
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        aspect = (float) width/height;
        glGenTextures(1, textureHandle, 0);

        id = textureHandle[0];
        // Bind to the texture in OpenGL
        glBindTexture(GL_TEXTURE_2D, id);

        // Set filtering
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

}
