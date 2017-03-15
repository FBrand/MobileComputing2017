package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.R;

/**
 * Created by Julian on 09.03.2017.
 */

public enum SurfaceType implements Thumbnail, Textured{
    SOCCER_GRASS(R.drawable.soccer_grass);

    public final int texResID;

    SurfaceType(int texResID) {
        this.texResID = texResID;
    }


    @Override
    public int getThumbnailResID() {
        return texResID;
    }

    @Override
    public int getTextureResID() {
        return texResID;
    }

}
