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
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.cognifide.slice.core.internal.injector.InjectorHierarchy;
import com.cognifide.slice.core.internal.monitoring.ModelUsageData;
import com.cognifide.slice.core.internal.monitoring.SliceStatistics;

@Component
@Service
@Properties({ @Property(name = "felix.webconsole.title", value = "Slice Statistics"),
		@Property(name = "felix.webconsole.category", value = "Status"),
		@Property(name = "felix.webconsole.label", value = "slicestats") })
public class SliceStatisticsOSGiWebConsole extends HttpServlet {

	private static final long serialVersionUID = -7025333484150354044L;

	private static final String SLICE_STATS_CONSOLE_URL = "/system/console/slicestats";

	private static final String ENABLED_PARAMETER_ENABLED = "enabled";

	private static final String RESET_PARAMETER_NAME = "reset";

	private static final String NO_STATISTICS_AVAILABLE_MESSAGE = "No injection statistics available.";

	@Reference
	private InjectorHierarchy injectorHierarchy;

	@Reference
	private SliceStatistics sliceStatistics;

	private final AtomicBoolean statisticsEnabled = new AtomicBoolean(false);

	@Override
	protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String enabledParam = request.getParameter(ENABLED_PARAMETER_ENABLED);
		if (enabledParam != null) {
			boolean enabledParamValue = Boolean.valueOf(enabledParam);
			if (enabledParamValue != statisticsEnabled.get()) {
				statisticsEnabled.set(enabledParamValue);
				sliceStatistics.updateStatisticsRepositories(statisticsEnabled.get());
			}
			response.sendRedirect(SLICE_STATS_CONSOLE_URL);
		} else if (request.getParameter(RESET_PARAMETER_NAME) != null) {
			sliceStatistics.reset();
			response.sendRedirect(SLICE_STATS_CONSOLE_URL);
		} else {
			printStatisticsStatusButtons(response);
			printInjectionHistory(response, statisticsEnabled.get());
		}
	}

	private void printInjectionHistory(HttpServletResponse response, boolean statisticsEnabled) throws IOException {
		PrintWriter writer = response.getWriter();

		Map<String, ModelUsageData> report = sliceStatistics.collectStatistics();
		if (report.isEmpty()) {
			writer.write(NO_STATISTICS_AVAILABLE_MESSAGE);
		} else {
			writer.print(new StatisticsHtmlTablesRenderer(report));
		}
	}

	private void printStatisticsStatusButtons(HttpServletResponse response) throws IOException {
		boolean statisticsEnabledLocal = statisticsEnabled.get();
		String paramValue = statisticsEnabledLocal ? "false" : "true";
		String labelValue = statisticsEnabledLocal ? "Stop" : "Start";
		response.getWriter()
				.write(String.format(
						"<form action='' method='get'><input type='hidden' name='enabled' value='%s'><button type='submit'>%s</button></form>",
						paramValue, labelValue));
	}
}