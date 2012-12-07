package com.cognifide.slice.cq.taglib.include;

/*-
 * #%L
 * Slice - CQ Taglib
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2012 Cognifide Limited
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Arrays;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;

import com.cognifide.slice.cq.taglib.AbstractBodyTag;
import com.day.cq.wcm.api.WCMMode;
import com.day.cq.wcm.api.components.Component;
import com.day.cq.wcm.api.components.ComponentManager;
import com.day.cq.wcm.api.components.IncludeOptions;

/**
 * Extended version of the cq:include tag. In addition to the original behaviour allows controlling the WCM
 * mode inside the include, and disabling the decoration markup that's added by WCM by default. The control is
 * done either at component level - using custom properties stored in a component's node, or at the include
 * level - using tag's attributes. Tag's attributes have higher priority than component's meta-data.
 * 
 * @author Jan Kuźniak
 * @author Witold Szczerba
 * @author maciej.majchrzak
 */
public class IncludeTag extends AbstractBodyTag {

	private static final long serialVersionUID = -214258163075048064L;

	static final DecorationMode DEFAULT_DECORATION_MODE = DecorationMode.ALL;

	static final boolean DEFAULT_DISABLE_WCM = false;

	private Boolean disableWcm;

	private Boolean enableDecoration;

	private String[] additionalCssClassNames;

	/**
	 * The path to the resource object to include in the current request processing. If this path is relative
	 * it is appended to the path of the current resource whose script is including the given resource. Either
	 * resource or path must be specified. If both are specified, the resource takes precedences.
	 */
	private String path;

	/**
	 * The resource type of a resource to include. If the resource to be included is specified with the path
	 * attribute, which cannot be resolved to a resource, the tag may create a synthetic resource object out
	 * of the path and this resource type. If the resource type is set the path must be the exact path to a
	 * resource object. That is, adding parameters, selectors and extensions to the path is not supported if
	 * the resource type is set.
	 */
	private String resourceType;

	/** wrapping the original include tag to mimic it's behaviour */
	private com.day.cq.wcm.tags.IncludeTag wcmIncludeTag = new com.day.cq.wcm.tags.IncludeTag();

	/** {@inheritDoc} */
	@Override
	public int doStartTag() throws JspException {
		return wcmIncludeTag.doStartTag();
	}

	/** @param disableWcm the disableWcm to set */
	public void setDisableWcm(boolean disableWcm) {
		this.disableWcm = disableWcm;
	}

	/** @param enableDecoration the enableDecoration to set */
	public void setEnableDecoration(boolean enableDecoration) {
		this.enableDecoration = enableDecoration;
	}

	/** @param flush the flush to set */
	public void setFlush(Boolean flush) {
		this.wcmIncludeTag.setFlush(flush);
	}

	/** @param path the path to set */
	public void setPath(String path) {
		this.path = path;
		wcmIncludeTag.setPath(path);
	}

	/** @param resourceType the resourceType to set */
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
		wcmIncludeTag.setResourceType(resourceType);
	}

	/** @param script the script to set */
	public void setScript(String script) {
		wcmIncludeTag.setScript(script);
	}

	/** @param ignoreComponentHierarchy the ignoreComponentHierarchy to set */
	public void setIgnoreComponentHierarchy(boolean ignoreComponentHierarchy) {
		wcmIncludeTag.setIgnoreComponentHierarchy(ignoreComponentHierarchy);
	}

	public void setAdditionalCssClassNames(final String additionalCssClassNames) {
		this.additionalCssClassNames = StringUtils.split(additionalCssClassNames, ',');
	}

	/** {@inheritDoc} */
	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);
		wcmIncludeTag.setPageContext(pageContext);
	}

	/** {@inheritDoc} */
	@Override
	public void setParent(Tag t) {
		super.setParent(t);
		wcmIncludeTag.setParent(t);
	}

	/** {@inheritDoc} */
	@Override
	public void setId(String id) {
		super.setId(id);
		wcmIncludeTag.setId(id);
	}

	/** {@inheritDoc} */
	@Override
	public void release() {
		super.release();
		wcmIncludeTag.release();
	}

	/** {@inheritDoc} */
	@Override
	public void removeValue(String k) {
		super.removeValue(k);
		wcmIncludeTag.removeValue(k);
	}

	/** {@inheritDoc} */
	@Override
	public void setValue(String k, Object o) {
		super.setValue(k, o);
		wcmIncludeTag.setValue(k, o);
	}

	/** {@inheritDoc} */
	@Override
	public int doEndTag() throws JspException {
		// we consider removing decoration only if we're including a component
		SlingHttpServletRequest request = getRequest();
		WCMMode wcmMode = WCMMode.fromRequest(request);

		ComponentConfiguration componentConfiguration = readComponentConfiguration(request);

		boolean decorationEnabled;
		if (this.enableDecoration == null) {
			decorationEnabled = isDecorationEnabled(componentConfiguration.getDecorationModes(), wcmMode);
		} else {
			decorationEnabled = this.enableDecoration;
		}

		IncludeOptions options = IncludeOptions.getOptions(request, true);
		if (!decorationEnabled) {
			options.forceSameContext(true);
		}

		boolean wcmDisabled;
		if (this.disableWcm == null) {
			wcmDisabled = componentConfiguration.isDisableWcm(); // default is in componentConfiguration
		} else {
			wcmDisabled = this.disableWcm;
		}

		if (wcmDisabled) {
			WCMMode.DISABLED.toRequest(request);
		}
		try {
			String[] componentAdditionalCssClassNames = componentConfiguration.getAdditionalCssClassNames();
			if ((null != componentAdditionalCssClassNames) && (componentAdditionalCssClassNames.length > 0)) {
				options.getCssClassNames().addAll(Arrays.asList(componentAdditionalCssClassNames));
			}

			if ((null != additionalCssClassNames) && (additionalCssClassNames.length > 0)) {
				options.getCssClassNames().addAll(Arrays.asList(additionalCssClassNames));
			}

			wcmIncludeTag.setResourceType(resourceType);
			return wcmIncludeTag.doEndTag();
		} finally {
			if (wcmDisabled) {
				wcmMode.toRequest(request);
			}
		}
	}

	private ComponentConfiguration readComponentConfiguration(SlingHttpServletRequest request) {
		ComponentConfiguration componentConfiguration = new ComponentConfiguration(logger);

		if ((resourceType != null) && (path != null)) {
			ComponentManager componentManager = request.getResourceResolver().adaptTo(ComponentManager.class);
			if (componentManager != null) {
				Component component = componentManager.getComponent(resourceType);
				if (component != null) {
					componentConfiguration.readFromComponent(component);
				} else {
					logger.warn("Cannot read component configuration for '{}' at {}", resourceType, path);
				}
			} else {
				logger.warn("Unable to obtain component manager for '{}' at {}", resourceType, path);
			}
		}
		return componentConfiguration;
	}

	private boolean isDecorationEnabled(DecorationMode[] decorationModes, WCMMode wcmMode) {
		boolean enabled = false;
		for (int i = 0; (i < decorationModes.length) && !enabled; i++) {
			DecorationMode decorationMode = decorationModes[i];
			if (null != decorationMode) {
				enabled = decorationMode.isEnabledInWcmMode(wcmMode);
			}
		}
		return enabled;
	}

	// /////////////////////////////////////////////////////////////////////////
	// DecorationMode enum
	// ///////////////////////////////////////////////////////////////////////

	/**
	 * Represents configuration options for the {{cog:enableDecorationInModes}} property.
	 */
	enum DecorationMode {
		/** decoration is enabled in the WCM disabled mode */
		DISABLED("disabled"), //
		/** decoration is enabled in the WCM edit mode */
		EDIT("edit"), //
		/** decoration is enabled in the WCM read-only mode */
		READ_ONLY("read-only"), //
		/** decoration is enabled in the WCM preview-mode */
		PREVIEW("preview"), //
		/** decoration is enabled in the WCM design-mode */
		DESIGN("design"), //
		/** decoration is enabled in the authoring mode */
		AUTHOR("author"), //
		/** decoration is enabled in the publish mode */
		PUBLISH("publish"), //
		/** decoration is enabled in all WCM modes */
		ALL("all"), //
		/** decoration is disabled (not enabled in any mode) */
		NONE("none");

		private String name;

		private DecorationMode(String name) {
			this.name = name;
		}

		public static DecorationMode fromObject(Object o) {
			if (o == null) {
				return null;
			} else {
				return fromNotNullString(o.toString());
			}
		}

		public static DecorationMode fromString(String name) {
			if (name == null) {
				return null;
			} else {
				return fromNotNullString(name);
			}
		}

		private static DecorationMode fromNotNullString(String name) {
			for (DecorationMode b : DecorationMode.values()) {
				if (name.equals(b.name)) {
					return b;
				}
			}
			// default
			return null;
		}

		/**
		 * Returns true if the decoration mode is enabled in the current WCM Mode
		 * 
		 * @param wcmMode the WCM mode model representing current state of the WCM
		 * @return true if the decoration mode is enabled in given WCM mode, false otherwise
		 */
		public boolean isEnabledInWcmMode(WCMMode wcmMode) {
			boolean enabled = (this == DISABLED) && (wcmMode == WCMMode.DISABLED);
			enabled |= (this == EDIT) && (wcmMode == WCMMode.EDIT);
			enabled |= (this == READ_ONLY) && (wcmMode == WCMMode.READ_ONLY);
			enabled |= (this == PREVIEW) && (wcmMode == WCMMode.PREVIEW);
			enabled |= (this == DESIGN) && (wcmMode == WCMMode.DESIGN);
			enabled |= (this == AUTHOR) && ((wcmMode == WCMMode.EDIT) || (wcmMode == WCMMode.DESIGN));
			enabled |= (this == PUBLISH) && ((wcmMode != WCMMode.EDIT) && (wcmMode != WCMMode.DESIGN));
			enabled |= (this == ALL);
			return enabled;
		}
	}
}
