package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Julian on 09.03.2017.
 */

public class ShaderProgramInfo {

    private Map<String, Integer> locations = new HashMap<>();

    public int location(String name) {
        return locations.get(name);
    }

    public ShaderProgramInfo putLocation(String name, int location) {
        locations.put(name, location);
        return this;
    }
}
