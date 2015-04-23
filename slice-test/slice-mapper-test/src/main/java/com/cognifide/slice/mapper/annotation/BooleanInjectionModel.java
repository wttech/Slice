package com.cognifide.slice.mapper.annotation;

/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 4/23/15 1:17 PM
 */
@SliceResource(MappingStrategy.ANNOTATED)
public class BooleanInjectionModel {

    @JcrProperty
    private Boolean boolProp;

    @JcrProperty("primitiveBoolProp")
    private boolean primitiveBoolProp;

    public Boolean getBoolProp() {
        return boolProp;
    }

    public boolean isPrimitiveBoolProp() {
        return primitiveBoolProp;
    }
}
