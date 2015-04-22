package com.cognifide.slice.core.modules;

import com.cognifide.slice.api.qualifier.Extension;
import com.cognifide.slice.api.qualifier.SelectorString;
import com.cognifide.slice.api.qualifier.Selectors;
import com.cognifide.slice.api.qualifier.Suffix;
import com.cognifide.slice.mapper.annotation.SliceResource;
import com.google.inject.Inject;
import org.apache.sling.api.SlingHttpServletRequest;

import java.util.List;

/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 4/22/15 10:53 AM
 */
@SliceResource
public class SlingModuleInjectionsModel {

    private String suffix;

    private String extension;

    @Inject
    public SlingModuleInjectionsModel(@Suffix String suffix, @Extension String extension) {
        this.suffix = suffix;
        this.extension = extension;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getExtension() {
        return extension;
    }

}
