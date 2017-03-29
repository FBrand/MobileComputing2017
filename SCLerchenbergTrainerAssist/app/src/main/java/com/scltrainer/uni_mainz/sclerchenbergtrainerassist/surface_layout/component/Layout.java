package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component;

import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains all parts of a layout.
 * The layout can be stored to and loaded from a JSONObject.
 * Furthermore it provides a method to generate material list from.
 */
public class Layout {
    private static final String BACKGROUND_JSON_NAME = "Background";
    private static final String MATERIALS_JSON_NAME = "Materials";
    private static final String PATHS_JSON_NAME = "Paths";



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

    /**
     * Converts the Layout to a JSON object.
     * @return The created JSON object.
     * @throws JSONException if something went wrong.
     */
    public JSONObject toJSON() throws JSONException {
        return new JSONObject().put(BACKGROUND_JSON_NAME, background.toJSON())
                .put(MATERIALS_JSON_NAME, materials.toJSONArray())
                .put(PATHS_JSON_NAME, paths.toJSONArray());
    }


    /**
     * Creates a layout from a JSON object.
     * @param json The json object to create the layout from.
     * @return The created layout.
     * @throws JSONException if something went wrong.
     */
    public static Layout fromJSON(JSONObject json) throws JSONException {
        Layout layout = new Layout();
        layout.background = Background.fromJSON(json.getJSONObject(BACKGROUND_JSON_NAME));

        JSONArray array = json.getJSONArray(MATERIALS_JSON_NAME);
        layout.materials = new ComponentGroup<>();
        for (int i = 0; i < array.length(); i++) {
            MaterialComponent material = MaterialComponent.fromJSON(array.getJSONObject(i));
            layout.materials.add(material);
        }

        array = json.getJSONArray(PATHS_JSON_NAME);
        layout.paths = new ComponentGroup<>();
        for (int i = 0; i < array.length(); i++) {
            PathComponent path = PathComponent.fromJSON(array.getJSONObject(i));
            layout.paths.add(path);
        }
        return layout;
    }
}
