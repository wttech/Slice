package com.cognifide.slice.validation.api.tag;

/*
 * #%L
 * Slice - Core API
 * $Id:$
 * $HeadURL:$
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


import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.slice.validation.api.ErrorLevel;
import com.cognifide.slice.validation.api.ErrorMessage;
import com.cognifide.slice.validation.api.ValidatableObject;

/**
 * If given model is valid - does nothing but evaluates body. If given model is empty - displays "empty model"
 * message. If model is invalid, displays all validation errors.
 * 
 * @author Albert Cenkier
 * @author Jan Kuźniak
 */
public class CheckErrorsTag extends BodyTagSupport {

	private static final long serialVersionUID = -8408025060913758496L;

	private static final Logger LOG = LoggerFactory.getLogger(CheckErrorsTag.class);

	/** mark-up to be displayed if given model is empty */
	private static final String EMPTY_MODEL_MESSAGE = "<img src=\"/libs/cq/ui/resources/0.gif\" "
			+ "class=\"cq-text-placeholder\" alt=\"\">\n";

	public static final String CLEAR_BOTH_DIV = "<div style=\"clear:both;font-size:1px\">&nbsp;</div>";

	/** model object that contains validation error messages (if any) */
	private transient ValidatableObject model;

	/** information message - displayed to the author */
	private String title = "Validation messages:";

	/** if false, tag does not render */
	private boolean render = true;

	@Override
	public int doStartTag() throws JspException {
		int result = SKIP_BODY;
		try {
			if (isRender()) {
				result = renderStartTag();
			} else if ((model != null) && !model.isValid()) {
				// does not render as plain HTML, but as HTML comment instead.
				boolean useHtmlComment = true;
				writeErrors(pageContext.getOut(), model.getErrorMessages(), useHtmlComment);
				result = SKIP_BODY;
			}
		} catch (IOException e) {
			// should never occur
			LOG.error("unexpected exception occured", e);
		}
		return result;
	}

	/**
	 * Renders start tag as HTML mark-up directly to the JSP writer.
	 * 
	 * @return Tag.EVAL_BODY_INCLUDE or BodyTag.EVAL_BODY_BUFFERED to indicate that the body of the action
	 * should be evaluated or SKIP_BODY to indicate otherwise.
	 * @throws IOException if an IO exception occurs
	 */
	private int renderStartTag() throws IOException {
		int result;
		Writer out = pageContext.getOut();
		if (model == null) {
			result = EVAL_BODY_INCLUDE;
		} else {
			if (model.isBlank()) {
				out.write(EMPTY_MODEL_MESSAGE);
				result = SKIP_BODY;
			} else if (model.isValid()) {
				result = EVAL_BODY_INCLUDE;
			} else {
				boolean useHtmlComment = false;
				writeErrors(pageContext.getOut(), model.getErrorMessages(), useHtmlComment);
				result = SKIP_BODY;
			}
		}
		return result;
	}

	/**
	 * Writes error messages either as plain HTML, or using HTML comment (depending on the
	 * <code>useHtmlComment</code> parameter.
	 * 
	 * @param out a writer to write to
	 * @param errorMessages list of error messages to be written
	 * @param useHtmlComment if true, an HTML comment will be used, plain HTML otherwise
	 */
	private void writeErrors(Writer out, List<ErrorMessage> errorMessages, boolean useHtmlComment) {
		if ((errorMessages != null) && (!errorMessages.isEmpty())) {
			try {
				if (useHtmlComment) {
					out.write("\n<!--");
				} else {
					out.write("<div class=\"validationMessages\">\n");
					out.write("<div class=\"title\">");
					out.write(title);
					out.write("</div>\n");
				}

				writeMessageList(out, errorMessages, useHtmlComment, ErrorLevel.ERROR);
				writeMessageList(out, errorMessages, useHtmlComment, ErrorLevel.WARNING);
				writeMessageList(out, errorMessages, useHtmlComment, ErrorLevel.INFORMATION);

				if (useHtmlComment) {
					out.write("\n-->\n");
				} else {
					out.write("</div>\n");
					out.write(CLEAR_BOTH_DIV);
				}
			} catch (IOException e) {
				// should never happen
				LOG.error("Unexpected exception occured", e);
			}
		}
	}

	private void writeMessageList(Writer out, List<ErrorMessage> errorMessages, boolean useHtmlComment,
			ErrorLevel errorLevel) throws IOException {
		boolean first = true;
		for (ErrorMessage errorMessage : errorMessages) {
			if (errorMessage.getErrorLevel().equals(errorLevel)) {
				if (first) {
					out.write(String.format("<ul class=\"%sMessages\">\n", errorLevel.getCssClassName()));
					first = false;
				}
				writeErrorMessage(out, errorMessage, useHtmlComment);
			}
		}
		if (!first) {
			out.write("</ul>\n");
		}
	}

	/**
	 * Writes single error message, either as plain HTML, or using HTML comment (depending on the
	 * <code>useHtmlComment</code> parameter.
	 * 
	 * @param out a writer to write to
	 * @param errorMessages list of error messages to be written
	 * @param useHtmlComment if true, an HTML comment will be used, plain HTML otherwise
	 */
	private void writeErrorMessage(Writer out, ErrorMessage message, boolean useHtmlComment)
			throws IOException {
		if (!useHtmlComment) {
			out.write("<li>");
		}
		out.write("\n");
		out.write(StringEscapeUtils.escapeHtml(message.getMessage()));
		{
			Throwable throwable = message.getThrowable();
			if (throwable != null) {
				out.write("\n<!--\n");
				throwable.printStackTrace(new PrintWriter(out));
				out.write("\n-->\n");
			}
		}
		if (!useHtmlComment) {
			out.write("</li>\n");
		}
	}

	/** @return the model */
	public ValidatableObject getModel() {
		return model;
	}

	/** @param model the model to set */
	public void setModel(ValidatableObject model) {
		this.model = model;
	}

	/** @return the title */
	public String getTitle() {
		return title;
	}

	/** @param title the title to set */
	public void setTitle(String title) {
		this.title = title;
	}

	/** @return the render */
	public boolean isRender() {
		return render;
	}

	/** @param render the render to set */
	public void setRender(boolean render) {
		this.render = render;
	}
}
