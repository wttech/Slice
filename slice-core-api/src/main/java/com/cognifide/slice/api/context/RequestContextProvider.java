package com.cognifide.slice.api.context;

public interface RequestContextProvider {
	ContextProvider getContextProvider(String injectorName);
}
