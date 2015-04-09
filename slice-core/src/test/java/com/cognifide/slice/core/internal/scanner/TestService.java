package com.cognifide.slice.core.internal.scanner;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.cognifide.slice.api.annotation.OsgiService;
import com.cognifide.slice.api.qualifier.Nullable;

/**
 * @author Mariusz Kubis
 */
@Component(immediate = true)
@Service(value = TestService.class)
public class TestService {

	public String getValue() {
		return null;
	}

	public class InnerClass {
		public InnerClass(@Nullable String parameter, @Nullable @OsgiService Integer parameter2) {
		}
	}
}
