package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout;

/**
 * Created by Julian on 12.03.2017.
 */

public class ItemGroup {
    public int numItems;
    public OnItemMultiClickListener onItemClickListener;

    public ItemGroup(OnItemMultiClickListener onItemClickListener, int numItems) {
        this.onItemClickListener = onItemClickListener;
        this.numItems = numItems;
    }
}
