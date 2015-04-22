package com.cognifide.slice.api.qualifier;

import com.cognifide.slice.mapper.annotation.SliceResource;
import com.google.inject.Inject;

import javax.jcr.query.QueryManager;

/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 4/22/15 3:40 PM
 */
@SliceResource
public class QueryManagerInjectionModel {

    private QueryManager queryManager;

    @Inject
    public QueryManagerInjectionModel(QueryManager queryManager) {
        this.queryManager = queryManager;
    }

    public QueryManager getQueryManager() {
        return queryManager;
    }
}
