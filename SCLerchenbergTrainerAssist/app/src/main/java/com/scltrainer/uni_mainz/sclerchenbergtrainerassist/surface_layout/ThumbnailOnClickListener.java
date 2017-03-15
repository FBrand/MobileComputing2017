package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout;

import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;


public class ThumbnailOnClickListener implements OnItemClickListener {

    private List<ItemGroup> itemGroups = new ArrayList<>();
    private ThumbnailListAdapter adapter;

    public ThumbnailOnClickListener(ThumbnailListAdapter adapter) {
        this.adapter = adapter;
    }

    public void addItemGroup(ItemGroup group) {
        itemGroups.add(group);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int p = position;
        for (ItemGroup itemGroup : itemGroups){
            if (p >= itemGroup.numItems)
                p -= itemGroup.numItems;
            else {
                if (adapter.selected >= 0) {
                    adapter.getView(adapter.selected).setBackgroundColor(Color.WHITE);
                }
                if (adapter.selected == position) {
                    itemGroup.onItemClickListener.onItemReClick(parent, view, p, id);
                    adapter.getView(adapter.selected).setBackgroundColor(Color.WHITE);
                    adapter.selected = -1;
                } else {
                    view.setBackgroundColor(adapter.selectionColor);
                    itemGroup.onItemClickListener.onItemClick(parent, view, p, id);
                    adapter.selected = position;
                }
                break;
            }
        }
    }
}
