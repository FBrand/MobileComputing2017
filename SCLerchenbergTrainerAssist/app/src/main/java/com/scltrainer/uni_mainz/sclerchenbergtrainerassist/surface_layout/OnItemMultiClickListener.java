package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Julian on 12.03.2017.
 */

public interface OnItemMultiClickListener extends AdapterView.OnItemClickListener {
    void onItemReClick(AdapterView<?> var1, View var2, int var3, long var4);
}
