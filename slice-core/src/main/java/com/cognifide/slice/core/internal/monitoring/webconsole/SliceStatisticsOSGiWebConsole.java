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
package com.cognifide.slice.core.internal.monitoring.webconsole;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.cognifide.slice.api.injector.InjectorsRepository;
import com.cognifide.slice.core.internal.monitoring.SliceStatistics;
import com.cognifide.slice.core.internal.monitoring.InjectorStatisticsRepository;

@Component
@Service
@Properties({ @Property(name = "felix.webconsole.title", value = "Slice Injections Report"),
		@Property(name = "felix.webconsole.label", value = "slicemon") })
public class SliceStatisticsOSGiWebConsole extends HttpServlet {

	private static final long serialVersionUID = -7025333484150354044L;

	@Reference
	private InjectorsRepository injectorsRepository;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("reset") != null) {
			reset();
			response.sendRedirect("/system/console/slicemon");
		} else {
			printInjectionHistory(response);
		}
	}

	private void printInjectionHistory(HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();

		SliceStatistics report = SliceStatistics.fromInjectors(injectorsRepository);
		if (report.isEmpty()) {
			writer.write("No injection history available.");
		} else {
			writer.print(new StatisticsHtmlTablesRenderer(report));
		}
	}

	private void reset() {
		for (String injectorName : injectorsRepository.getInjectorNames()) {
			injectorsRepository.getInjector(injectorName).getInstance(InjectorStatisticsRepository.class).clearHistory();
		}
	}
}