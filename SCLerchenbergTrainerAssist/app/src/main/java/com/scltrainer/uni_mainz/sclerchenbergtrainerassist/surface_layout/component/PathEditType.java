package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.R;

/**
 * New thumbnails to edit a path can be declared here.
 */
public class PathEditType implements Thumbnail {

    private static PathEditType instance;

    @Override
    public int getThumbnailResID() {
        return R.drawable.arrow_edit;
    }

    private PathEditType() {

    }

    public static PathEditType getInstance() {
        if (instance == null)
            instance = new PathEditType();
        return instance;
    }
}
