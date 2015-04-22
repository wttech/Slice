package com.cognifide.slice.api.qualifier;

import com.cognifide.slice.mapper.annotation.SliceResource;
import com.google.inject.Inject;

/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 4/22/15 1:57 PM
 */
@SliceResource
public class InjectorNameInjectionModel {

    private String injectorName;

    @Inject
    public InjectorNameInjectionModel(@InjectorName String injectorName) {
        this.injectorName = injectorName;
    }

    String getInjectorName() {
        return injectorName;
    }
}
