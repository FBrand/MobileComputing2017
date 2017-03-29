package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component;


import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.R;

/**
 * New field types can be declared here.
 */
public enum FieldType implements Thumbnail, Textured {
    SOCCER_FIELD(R.drawable.soccer_field);

    public final int texResID; // the texture resource id

    FieldType(int texResID) {
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
