package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.R;

/**
 * New surface types can be declared here.
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
