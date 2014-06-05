package com.cognifide.slice.validation;

import com.cognifide.slice.validation.api.Validator;
import com.google.inject.AbstractModule;

/**
 * @deprecated It will be removed (along with whole Validation API) in next major version - custom solution
 * required
 * 
 */
public class ValidationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Validator.class).to(ValidatorImpl.class);
	}

}
