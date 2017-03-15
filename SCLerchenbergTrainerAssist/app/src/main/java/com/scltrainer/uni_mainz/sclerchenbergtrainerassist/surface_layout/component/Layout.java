package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Julian on 09.03.2017.
 */

public class Layout {
    public Background background;
    public ComponentGroup<MaterialComponent> materials = new ComponentGroup<>();
    public ComponentGroup<PathComponent> paths = new ComponentGroup<>();


    /**
     * Creates a material list from this layout.
     * @return A list of pairs (type, count), sorted by count.
     */
    public List<Pair<MaterialType, Integer>> materialList() {
        Map<MaterialType, Integer> materialMap = new HashMap<>();
        for(MaterialComponent material : materials) {
            MaterialType materialType = material.getType();
            if (materialMap.containsKey(materialType)) {
                int count = materialMap.get(materialType);
                materialMap.put(materialType, ++count);
            } else {
                materialMap.put(materialType, 1);
            }
        }
        List<Pair<MaterialType, Integer>> materialList = new ArrayList<>();
        for (Map.Entry<MaterialType, Integer> e : materialMap.entrySet()) {
            materialList.add(new Pair<>(e.getKey(), e.getValue()));
        }
        Collections.sort(materialList, new Comparator<Pair<MaterialType, Integer>>() {
            @Override
            public int compare(Pair<MaterialType, Integer> p0, Pair<MaterialType, Integer> p1) {
                return p1.second - p0.second;
            }
        });
        return materialList;
    }
}
