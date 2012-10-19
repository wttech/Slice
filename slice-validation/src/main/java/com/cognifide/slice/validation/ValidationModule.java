package com.cognifide.slice.validation;

import com.cognifide.slice.validation.api.Validator;
import com.google.inject.AbstractModule;

public class ValidationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Validator.class).to(ValidatorImpl.class);
	}

}
