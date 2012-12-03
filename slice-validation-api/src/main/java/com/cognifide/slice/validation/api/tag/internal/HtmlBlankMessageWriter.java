package com.cognifide.slice.validation.api.tag.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.cognifide.slice.api.tag.SliceTagUtils;

/*
 * #%L Slice - Core API $Id:$ $HeadURL:$ %% Copyright (C) 2012 Cognifide Limited %% Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License. #L%
 */

/**
 * Writes blank message to a writer as HTML element. It can be blank HTML defined by developer or generic
 * blank rectangle provided by CQ.
 * 
 * @author Jakub Małecki
 * 
 */
public class HtmlBlankMessageWriter implements BlankMessageWriter {

	/** default blank message to display, when no blank message for component is defined */
	private static final String DEFAULT_BLANK_MESSAGE = "<img src=\"/libs/cq/ui/resources/0.gif\" "
			+ "class=\"cq-text-placeholder\" alt=\"\">\n";

	/** blank node where markup for blank message is stored */
	private static final String BLANK_MESSAGE_NODE = "blank.html";

	private static final String JCR_CONTENT = "jcr:content";

	private static final String JCR_DATA = "jcr:data";

	private PageContext pageContext;

	public HtmlBlankMessageWriter(PageContext pageContext) {
		this.pageContext = pageContext;
	}

	@Override
	public void writeBlankMessage() throws IOException, RepositoryException {
		final Writer out = pageContext.getOut();
		final String blankMessage = prepareBlankMessage();
		out.write(blankMessage);
	}

	protected String prepareBlankMessage() throws IOException, RepositoryException {
		final SlingHttpServletRequest request = SliceTagUtils.slingRequestFrom(pageContext);
		final Resource resource = request.getResource();

		if (null == resource || StringUtils.isBlank(resource.getResourceType())) {
			return DEFAULT_BLANK_MESSAGE;
		}

		final ResourceResolver resourceResolver = resource.getResourceResolver();
		final String resourceType = resource.getResourceType();
		final Resource resourceTypeResource = resourceResolver.getResource(resourceType);

		if (null == resourceTypeResource) {
			return DEFAULT_BLANK_MESSAGE;
		}

		final Node resourceTypeNode = resourceTypeResource.adaptTo(Node.class);
		if (!resourceTypeNode.hasNode(BLANK_MESSAGE_NODE)) {
			return DEFAULT_BLANK_MESSAGE;
		}

		final Node blankMessageNode = resourceTypeNode.getNode(BLANK_MESSAGE_NODE);
		InputStream blankMessageStream = null;
		try {
			blankMessageStream = blankMessageNode.getNode(JCR_CONTENT).getProperty(JCR_DATA).getBinary().getStream();
			return IOUtils.toString(blankMessageStream);
		} finally {
			if (blankMessageStream != null) {
				blankMessageStream.close();
			}
		}

	}
}
