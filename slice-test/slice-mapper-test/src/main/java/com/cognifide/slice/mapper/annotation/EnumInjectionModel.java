package com.cognifide.slice.mapper.annotation;

/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 4/23/15 1:59 PM
 */
@SliceResource
public class EnumInjectionModel {

    @JcrProperty
    private SimpleEnum enumeration;

    public SimpleEnum getEnumeration() {
        return enumeration;
    }
}
