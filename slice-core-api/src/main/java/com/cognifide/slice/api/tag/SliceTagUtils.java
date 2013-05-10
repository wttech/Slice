package com.cognifide.slice.api.tag;

/*-
 * 
 * 
 */

import javax.servlet.ServletRequest;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;

import com.cognifide.slice.api.context.ContextProvider;
import com.cognifide.slice.api.injector.InjectorWithContext;
import com.cognifide.slice.api.injector.InjectorsRepository;
import com.cognifide.slice.api.provider.ModelProvider;
import com.cognifide.slice.util.InjectorNameUtil;

public final class SliceTagUtils {

	private final static String SLICE_INJECTOR_NAME = "SLICE_INJECTOR_NAME";

	private SliceTagUtils() {
		// hidden constructor
	}

	public static <T> T getFromCurrentPath(final SlingHttpServletRequest request,
			final InjectorsRepository injectorsRepository, final ContextProvider contextProvider,
			final Class<T> type) {
		return getFromCurrentPath(request, injectorsRepository, contextProvider, type, null);
	}

	public static <T> T getFromCurrentPath(final PageContext pageContext, final Class<T> type) {
		final SlingHttpServletRequest request = SliceTagUtils.slingRequestFrom(pageContext);
		final InjectorsRepository injectorsRepository = SliceTagUtils.injectorsRepositoryFrom(pageContext);
		final ContextProvider contextProvider = SliceTagUtils.contextProviderFrom(pageContext);

		return getFromCurrentPath(request, injectorsRepository, contextProvider, type);
	}

	public static <T> T getFromCurrentPath(final PageContext pageContext, final Class<T> type,
			final String appName) {
		final SlingHttpServletRequest request = SliceTagUtils.slingRequestFrom(pageContext);
		final InjectorsRepository injectorsRepository = SliceTagUtils.injectorsRepositoryFrom(pageContext);
		final ContextProvider contextProvider = SliceTagUtils.contextProviderFrom(pageContext);

		return getFromCurrentPath(request, injectorsRepository, contextProvider, type, appName);
	}

	public static <T> T getFromCurrentPath(final SlingHttpServletRequest request,
			final InjectorsRepository injectorsRepository, final ContextProvider contextProvider,
			final Class<T> type, final String appName) {
		final String injectorName = getInjectorName(request, appName);
		if (StringUtils.isBlank(injectorName)) {
			throw new IllegalStateException("Guice injector name not available");
		} else {
			request.setAttribute(SLICE_INJECTOR_NAME, injectorName);
		}

		if (null == contextProvider) {
			throw new IllegalStateException("ContextProvider is not available");
		}

		final InjectorWithContext injector = injectorsRepository.getInjector(injectorName);
		if (injector == null) {
			throw new IllegalStateException("Guice injector not found: " + injectorName);
		}

		injector.pushContextProvider(contextProvider);

		try {
			final ModelProvider modelProvider = injector.getInstance(ModelProvider.class);
			final Resource resource = request.getResource();
			return (T) modelProvider.get(type, resource);
		} finally {
			injector.popContextProvider();
		}
	}

	@SuppressWarnings("deprecation")
	private static String getInjectorName(final SlingHttpServletRequest request, final String appName) {
		String injectorName;
		if (StringUtils.isNotBlank(appName)) {
			injectorName = appName;
		} else {
			String cachedInjectorName = (String) request.getAttribute(SLICE_INJECTOR_NAME);
			if (StringUtils.isNotBlank(cachedInjectorName)) {
				injectorName = cachedInjectorName;
			} else {
				injectorName = InjectorNameUtil.getFromRequest(request);
			}
		}
		return injectorName;
	}

	public static SlingHttpServletRequest slingRequestFrom(final PageContext pageContext) {
		return (SlingHttpServletRequest) pageContext.getRequest();
	}

	public static ContextProvider contextProviderFrom(final PageContext pageContext) {
		final SlingScriptHelper slingScriptHelper = getSlingScriptHelper(pageContext);
		return slingScriptHelper.getService(ContextProvider.class);
	}

	public static InjectorsRepository injectorsRepositoryFrom(final PageContext pageContext) {
		final SlingScriptHelper slingScriptHelper = getSlingScriptHelper(pageContext);
		return slingScriptHelper.getService(InjectorsRepository.class);
	}

	private static SlingScriptHelper getSlingScriptHelper(final PageContext pageContext) {
		ServletRequest request = pageContext.getRequest();
		SlingBindings bindings = (SlingBindings) request.getAttribute(SlingBindings.class.getName());
		return bindings.getSling();
	}
}
