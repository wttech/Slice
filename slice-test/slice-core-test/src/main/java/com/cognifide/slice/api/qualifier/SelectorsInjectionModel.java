package com.cognifide.slice.api.qualifier;

import com.cognifide.slice.mapper.annotation.SliceResource;
import com.google.inject.Inject;

import java.util.List;

/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 4/22/15 11:30 AM
 */
@SliceResource
public class SelectorsInjectionModel {

    private String[] selectors;

    private List<String> selectorsAsList;

    private String selectorsAsString;

    @Inject
    public SelectorsInjectionModel(@Selectors String[] selectors, @Selectors List<String> selectorsAsList, @SelectorString @Nullable String selectorsAsString) {
        this.selectors = selectors;
        this.selectorsAsList = selectorsAsList;
        this.selectorsAsString = selectorsAsString;
    }

    public String[] getSelectors() {
        return selectors;
    }

    public List<String> getSelectorsAsList() {
        return selectorsAsList;
    }

    public String getSelectorsAsString() {
        return selectorsAsString;
    }

}
