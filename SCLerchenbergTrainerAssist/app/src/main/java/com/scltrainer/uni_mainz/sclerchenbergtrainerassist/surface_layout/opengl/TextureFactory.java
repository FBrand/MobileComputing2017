package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Julian on 07.03.2017.
 * Klasse um Texturen zu Erstellen
 */

public class TextureFactory {

    private static Map<Integer, Texture2D> texMap = new HashMap<>();

    /**
     * Lädt eine Texture
     * @param context
     * @param resourceId
     * @return
     */
    public static Texture2D loadTexture(final Context context, final int resourceId)
    {
        if (texMap.containsKey(resourceId))
            return texMap.get(resourceId);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;   // No pre-scaling

        // Read in the resource
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

        Texture2D texture = new Texture2D(bitmap);

        texMap.put(resourceId, texture);
        // Recycle the bitmap, since its data has been loaded into OpenGL.
        bitmap.recycle();


        return texture;
    }

    /**
     * Setzt die Texturen HashMap zurück
     */
    public static void reset() {
        texMap.clear();
    }
}
