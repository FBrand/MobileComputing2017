package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.R;

/**
 * Created by Julian on 09.03.2017.
 */

public enum MaterialType implements Textured, Thumbnail, Scaled {
    GOAL(R.drawable.goal, 0.06f),
    CONE(R.drawable.cone, 0.02f),
    PYLON(R.drawable.pylon, 0.04f),
    BALL(R.drawable.ball, 0.02f),
    FLAG(R.drawable.flag, 0.03f),
    DUMMY(R.drawable.dummy, 0.02f),
    BAR(R.drawable.bar, 0.05f),
    RING(R.drawable.ring, 0.035f),
    BENCH(R.drawable.bench, 0.03f),
    LADDER(R.drawable.ladder, 0.03f),
    MAT(R.drawable.mat, 0.05f);


    private final int texResID;
    private final float scale;

    private MaterialType(int texResID, float scale) {
        this.texResID = texResID;
        this.scale = scale;
    }

    @Override
    public int getThumbnailResID() {
        return texResID;
    }

    @Override
    public int getTextureResID() {
        return texResID;
    }

    @Override
    public float getScale() {
        return scale;
    }

    public static int length() {
        return values().length;
    }
}
