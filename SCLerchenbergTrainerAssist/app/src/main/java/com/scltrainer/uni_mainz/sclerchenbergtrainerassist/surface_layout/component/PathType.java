package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.R;

import org.joml.Vector4f;

/**
 * Created by Julian on 09.03.2017.
 */

public enum PathType implements Thumbnail {
    TRAVEL(R.drawable.arrow_orange, new Vector4f(1,0.5f,0,1)),
    PASS(R.drawable.arrow_blue, new Vector4f(0,0.3f,1,1));

    private final int texResID;
    public final Vector4f color;

    PathType(int texResID, Vector4f color) {
        this.texResID = texResID;
        this.color = color;
    }

    @Override
    public int getThumbnailResID() {
        return texResID;
    }

    public static int length() {
        return values().length;
    }
}
