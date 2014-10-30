/*-
 * #%L
 * Slice - Core
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

package com.cognifide.slice.core.internal.module;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.query.QueryManager;

import org.apache.sling.api.resource.ResourceResolver;

import com.cognifide.slice.api.scope.ContextScoped;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public final class JcrModule extends AbstractModule {

	@Override
	protected void configure() {
	}

	@Provides
	@ContextScoped
	public QueryManager getQueryManager(final ResourceResolver resourceResolver) {
		try {
			Session jcrsession = resourceResolver.adaptTo(Session.class);
			Workspace workspace = jcrsession.getWorkspace();
			return workspace.getQueryManager();
		} catch (RepositoryException e) {
			throw new IllegalStateException("cannot get query manager");
		}
	}
}
