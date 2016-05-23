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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.cognifide.slice.core.internal.monitoring.ModelUsageData;
import com.cognifide.slice.core.internal.monitoring.SliceStatistics;

public class StatisticsHtmlTablesRenderer {

	private SliceStatistics report;

	public StatisticsHtmlTablesRenderer(SliceStatistics report) {
		this.report = report;
	}

	@Override
	public String toString() {
		StringBuilder tableBuilder = new StringBuilder();
		tableBuilder.append(
				"<script>$('head').append('<link rel=\"stylesheet\" type=\"text/css\" href=\"https://cdnjs.cloudflare.com/ajax/libs/jquery-treetable/3.2.0/css/jquery.treetable.css\">');</script>");
		tableBuilder.append(
				"<script src='https://cdnjs.cloudflare.com/ajax/libs/jquery-treetable/3.2.0/jquery.treetable.js'></script>");
		for (Entry<String, ModelUsageData> injectorEntry : report.getStatistics().entrySet()) {
			renderFlatTable(tableBuilder, injectorEntry.getKey(), injectorEntry.getValue());
			renderTreeTable(tableBuilder, injectorEntry.getKey(), injectorEntry.getValue());
			tableBuilder.append("<hr style='margin-bottom: 20px'>");
		}
		tableBuilder.append("<hr><hr>");
		tableBuilder.append(
				"<form action='' method='get'><input type='hidden' name='reset'><button type='submit'>RESET</button></form>");
		// remove root children indentation
		tableBuilder.append(
				"<script>$(document).ready(function() {$(\"tr\").find(\"td:first\").find(\"span[style='padding-left: 0px;']\").remove();});</script>");
		return tableBuilder.toString();
	}

	private void renderFlatTable(StringBuilder tableBuilder, String header, ModelUsageData treeItem) {
		tableBuilder.append("<table id='summary-" + header + "' class='tablesorter nicetable noauto'>");
		tableBuilder.append(renderCaption(header));
		tableBuilder.append(
				"<thead><tr><th>Class</th><th>Instances Injected</th><th>Total Init Time [ms]</th class=\"{sorter: 'floating'}\"><th>Avg. Init Time [ms]</th></tr></thead><tbody>");
		for (Entry<Class<?>, ModelUsageData> entry : flattenStats(treeItem).entrySet()) {
			ModelUsageData stats = entry.getValue();
			Long instancesCount = stats.getCount();
			Long totalTime = stats.getTotalTime();
			double avgTime = stats.getAverageTime();
			tableBuilder.append("<tr><td>");
			tableBuilder.append(entry.getKey().getName());
			tableBuilder.append("</td><td>");
			tableBuilder.append(instancesCount.toString());
			tableBuilder.append("</td><td>");
			tableBuilder.append(totalTime.toString());
			tableBuilder.append("</td><td>");
			tableBuilder.append(String.format("%.3f", avgTime));
			tableBuilder.append("</td><tr>");
		}
		tableBuilder.append("</tbody></table>");

		tableBuilder.append("<script>$(document).ready(function() {$('#summary-" + header
				+ "').tablesorter({sortList:[[2,1]]})});</script>");
	}

	private void renderTreeTable(StringBuilder tableBuilder, String header, ModelUsageData treeItem) {
		int rowCounter = 1;
		tableBuilder
				.append("<table id='hierarchy-" + header + "' class='ui-helper-hidden tablesorter nicetable noauto'>");
		tableBuilder.append(renderCaption(header));
		tableBuilder.append(
				"<thead><tr><th>Class</th><th>Instances Injected</th><th>Total Init Time [ms]</th class=\"{sorter: 'floating'}\"><th>Avg. Init Time [ms]</th></tr></thead><tbody>");

		for (Entry<Class<?>, ModelUsageData> entry : treeItem.entrySet()) {
			ModelUsageData stats = entry.getValue();
			Long instancesCount = stats.getCount();
			Long totalTime = stats.getTotalTime();
			double avgTime = stats.getAverageTime();
			tableBuilder.append("<tr data-tt-id='" + rowCounter + "'><td>");
			tableBuilder.append(entry.getKey().getName());
			tableBuilder.append("</td><td>");
			tableBuilder.append(instancesCount.toString());
			tableBuilder.append("</td><td>");
			tableBuilder.append(totalTime.toString());
			tableBuilder.append("</td><td>");
			tableBuilder.append(String.format("%.3f", avgTime));
			tableBuilder.append("</td><tr>");

			rowCounter = printChildren(rowCounter, stats, tableBuilder);
		}
		tableBuilder.append("</tbody></table>");

		tableBuilder
				.append("<script>$(document).ready(function() {$('#hierarchy-" + header + "').treetable()});</script>");
	}

	private int printChildren(int rowCounter, ModelUsageData parentNode, StringBuilder tableBuilder) {
		int parentNodeId = rowCounter;
		int localRowCounter = rowCounter;
		for (Entry<Class<?>, ModelUsageData> entry : parentNode.entrySet()) {
			ModelUsageData stats = entry.getValue();
			Long instancesCount = stats.getCount();
			Long totalTime = stats.getTotalTime();
			double avgTime = stats.getAverageTime();
			tableBuilder
					.append("<tr data-tt-id='" + ++localRowCounter + "' data-tt-parent-id='" + parentNodeId + "'><td>");
			tableBuilder.append(entry.getKey().getName());
			tableBuilder.append("</td><td>");
			tableBuilder.append(instancesCount.toString());
			tableBuilder.append("</td><td>");
			tableBuilder.append(totalTime.toString());
			tableBuilder.append("</td><td>");
			tableBuilder.append(String.format("%.3f", avgTime));
			tableBuilder.append("</td><tr>");

			localRowCounter = printChildren(localRowCounter, stats, tableBuilder);
		}
		return 1 + localRowCounter;
	}

	private String renderCaption(String header) {
		StringBuilder caption = new StringBuilder();
		caption.append(
				"<caption class=\"ui-widget-header ui-corner-top buttonGroup\"><span style=\"float: left; margin-left: 1em\">");
		caption.append(header);
		caption.append("</span>");

		caption.append(String.format(
				"<button class=\"ui-state-default ui-corner-all\" onclick=\"$('#hierarchy-%s').toggle(true);$('#summary-%s').toggle(false);\">Hierarchy Tree</button>",
				header, header));
		caption.append("<span> | </span>");
		caption.append(String.format(
				"<button class=\"ui-state-default ui-corner-all\" onclick=\"$('#hierarchy-%s').toggle(false);$('#summary-%s').toggle(true);\">Flat List</button>",
				header, header));
		caption.append("</caption>");
		return caption.toString();
	}

	private Map<Class<?>, ModelUsageData> flattenStats(ModelUsageData treeItem) {
		return flattenStats(treeItem, new HashMap<Class<?>, ModelUsageData>());
	}

	private Map<Class<?>, ModelUsageData> flattenStats(ModelUsageData treeItem, Map<Class<?>, ModelUsageData> result) {
		for (Entry<Class<?>, ModelUsageData> entry : treeItem.entrySet()) {
			if (!result.containsKey(entry.getKey())) {
				result.put(entry.getKey(), entry.getValue().copy());
			} else {
				result.get(entry.getKey()).add(entry.getValue());
			}
			flattenStats(entry.getValue(), result);
		}
		return result;
	}
}