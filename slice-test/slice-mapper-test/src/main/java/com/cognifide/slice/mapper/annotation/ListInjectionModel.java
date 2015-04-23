package com.cognifide.slice.mapper.annotation;

import java.util.List;

/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 4/23/15 3:25 PM
 */
@SliceResource
public class ListInjectionModel {

    @JcrProperty
    private List<String> list;

    public List<String> getList() {
        return list;
    }

}
