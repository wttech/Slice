package com.cognifide.slice.core.internal.filter;

/*
 * #%L Slice - Core $Id:$ $HeadURL:$ %% Copyright (C) 2012 Cognifide Limited %% Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License. #L%
 */

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.Constants;

import com.cognifide.slice.api.context.Context;
import com.cognifide.slice.api.context.ContextProvider;
import com.cognifide.slice.api.context.RequestContextProvider;
import com.cognifide.slice.api.injector.InjectorsRepository;
import com.cognifide.slice.core.internal.context.SliceContextFactory;

// @formatter:off
/**
 * @author Witold Szczerba
 * @author Rafał Malinowski
 * @class ContextRequstFilter
 * 
 * This filter is run for on each request. Each time it stores a Context instance with ServletRequest and
 * ServletResponse values in per-thread variable. This Context is later accessible by getContext() and can be
 * used in Injector instances that are used during this request.
 */
@Component(immediate = true)
@Service
@Properties({
		@Property(name = Constants.SERVICE_DESCRIPTION, value = "Filter that is injected into request chain to provide access to request, resource and response."),
		@Property(name = Constants.SERVICE_VENDOR, value = "Cognifide"),
		@Property(name = Constants.SERVICE_RANKING, intValue = ContextRequstFilter.RANKING),
		@Property(name = "filter.scope", value = {"request","forward"}) })
// @formatter:on
public class ContextRequstFilter implements Filter, RequestContextProvider {

	@Reference
	private InjectorsRepository injectorsRepo;

	public static final int RANKING = -650;

	private ThreadLocal<Map<String, Context>> contexts = new ThreadLocal<Map<String, Context>>();

	/**
	 * Update Context instance for current thread to use current request and response values.
	 */
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		final Map<String, Context> previous = contexts.get();
		try {
			Map<String, Context> current = new HashMap<String, Context>();
			for (String injector : injectorsRepo.getInjectorNames()) {
				current.put(injector, createContext(injector, request, response));
			}
			current.put(SliceContextFactory.COMMON_CONTEXT_NAME,
					createContext(SliceContextFactory.COMMON_CONTEXT_NAME, request, response));
			contexts.set(current);
			chain.doFilter(request, response);
		} finally {
			contexts.set(previous);
		}
	}

	/**
	 * @returns Context instance for current thread
	 * 
	 * Return Context instance for current thread. It contains most current ServletRequest and ServletResponse
	 * instances.
	 */
	public ContextProvider getContextProvider(final String injectorName) {
		return new ContextProvider() {
			@Override
			public Context getContext() {
				return contexts.get().get(injectorName);
			}
		};
	}

	private Context createContext(String injectorName, ServletRequest request, ServletResponse response) {
		return (new SliceContextFactory()).getServletRequestContext(injectorName, request, response);
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}
