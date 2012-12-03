package com.cognifide.slice.validation;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Module;

/**
 * Factory for all CQ-related modules. It should be used in application's activator to register CQ-related
 * modules
 * 
 * @author maciej.majchrzak
 * 
 */
public class ValidationModulesFactory {

	private ValidationModulesFactory() {
	}

	/**
	 * Creates and returns a list of all validation-related modules. The list includes:<br>
	 * <ul>
	 * <li>{@link ValidationModule}</li>
	 * </ul>
	 * 
	 * @return list of validation-related modules
	 */
	public static List<Module> createModules() {
		List<Module> modules = new ArrayList<Module>();
		modules.add(new ValidationModule());
		return modules;
	}
}
