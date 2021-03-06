package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.R;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.Thumbnail;

/**
 * A list adapter for Thumbnails.
 * Its purpose is to create the view of list items.
 */
public class ThumbnailListAdapter extends ArrayAdapter<Thumbnail> {
    private final Context context;
    private final Thumbnail[] values;
    public int selected = -1;
    public int selectionColor;
    public int defaultColor;
    private ImageView[] views;

    public ThumbnailListAdapter(Context context, Thumbnail[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.views = new ImageView[values.length];

        selectionColor = ContextCompat.getColor(getContext(), R.color.colorAccent);
        defaultColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
    }

    public View getView(int position) {
        return views[position];
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView != null && convertView instanceof ImageView) {
            views[position] = (ImageView) convertView;
        } else {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.component_item, parent, false);
            views[position] = (ImageView) rowView.findViewById(R.id.component_item);
        }
        views[position].setImageResource(values[position].getThumbnailResID());
        views[position].setLayoutParams(new ViewGroup.LayoutParams(parent.getWidth(), parent.getWidth()));
        views[position].setBackgroundColor(position == selected? selectionColor : defaultColor);
        return views[position];
    }
}
