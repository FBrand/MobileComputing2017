package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout;

import android.view.View;
import android.widget.AdapterView;

/**
 * Provides a method to detect a re-click on an item.
 */
public interface OnItemMultiClickListener extends AdapterView.OnItemClickListener {
    void onItemReClick(AdapterView<?> var1, View var2, int var3, long var4);
}
