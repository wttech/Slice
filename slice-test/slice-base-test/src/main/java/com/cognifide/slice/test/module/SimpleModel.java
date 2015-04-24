package com.cognifide.slice.test.module;

import com.cognifide.slice.api.model.InitializableModel;
import com.cognifide.slice.mapper.annotation.JcrProperty;
import com.cognifide.slice.mapper.annotation.SliceResource;

/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 4/21/15 1:28 PM
 */
@SliceResource
public class SimpleModel implements InitializableModel {

    @JcrProperty
    private String prop1;

    public String getProp1() {
        return prop1;
    }

    @Override
    public void afterCreated() {
        // empty implementation
    }
}
