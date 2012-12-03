package com.cognifide.slice.cq.module;

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
public class CQModulesFactory {

	private CQModulesFactory() {
	}

	/**
	 * Creates and returns a list of all CQ-related modules. The list includes:<br>
	 * <ul>
	 * <li>{@link CQModule}</li>
	 * <li>{@link CQMapperModule}</li>
	 * <li>{@link DamModule}</li>
	 * <li>{@link CurrentPageModule}</li>
	 * <li>{@link RequestedPageModule}</li>
	 * <li>{@link LinkModule}</li>
	 * <li>{@link TemplateModule}</li>
	 * </ul>
	 * 
	 * @return list of CQ-related modules
	 */
	public static List<Module> createModules() {
		List<Module> modules = new ArrayList<Module>();
		modules.add(new CQModule());
		modules.add(new CQMapperModule());
		modules.add(new DamModule());
		modules.add(new CurrentPageModule());
		modules.add(new RequestedPageModule());
		modules.add(new LinkModule());
		modules.add(new TemplateModule());
		return modules;
	}
}
