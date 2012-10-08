package com.cognifide.slice.validation;

/*
 * #%L
 * Slice - CQ Add-on
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

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.Constants;

import com.cognifide.slice.api.injector.InjectorServiceRunner;
import com.cognifide.slice.validation.api.ValidationModulesInstaller;

// @formatter:off
/**
 * @author Rafał Malinowski
 * @class CQModulesInstallerImpl
 */
@Component(immediate = true)
@Service
@Properties({ //
		@Property(name = Constants.SERVICE_DESCRIPTION, value = "Factory of Validation modules."), //
		@Property(name = Constants.SERVICE_VENDOR, value = "Cognifide") })
// @formatter:on
public class ValidationModulesInstallerImpl implements
		ValidationModulesInstaller {

	@Override
	public void installValidationModules(
			final InjectorServiceRunner injectorServiceRunner) {
		injectorServiceRunner.installModule(new ValidationModule());
	}

}
