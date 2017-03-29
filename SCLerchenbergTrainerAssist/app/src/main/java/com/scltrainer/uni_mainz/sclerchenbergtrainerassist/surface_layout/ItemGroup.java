package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout;

/**
 * Groups several items within a list together to provide a click listener for a part of a list.
 */
public class ItemGroup {
    public int numItems;
    public OnItemMultiClickListener onItemClickListener;

    public ItemGroup(OnItemMultiClickListener onItemClickListener, int numItems) {
        this.onItemClickListener = onItemClickListener;
        this.numItems = numItems;
    }
}
