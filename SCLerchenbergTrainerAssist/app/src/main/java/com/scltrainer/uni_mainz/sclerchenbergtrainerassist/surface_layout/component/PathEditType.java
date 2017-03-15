package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.R;

/**
 * Created by Julian on 14.03.2017.
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
