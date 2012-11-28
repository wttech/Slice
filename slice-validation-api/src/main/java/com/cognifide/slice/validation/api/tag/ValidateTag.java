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
import java.io.Writer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.slice.api.tag.SliceTagUtils;
import com.cognifide.slice.validation.api.Validatable;
import com.cognifide.slice.validation.api.ValidationResult;
import com.cognifide.slice.validation.api.Validator;
import com.cognifide.slice.validation.api.tag.internal.ErrorMessageListWriter;
import com.cognifide.slice.validation.api.tag.internal.HtmlCommentErrorMessageListWriter;
import com.cognifide.slice.validation.api.tag.internal.HtmlErrorMessageListWriter;

/**
 * If given object is valid - does nothing but evaluates body. If given model is
 * blank - displays "blank model" message. If model is invalid, displays all
 * validation errors.
 * 
 * It can also write result of validation to variable accessible in JSP code.
 * 
 * @author Albert Cenkier
 * @author Jan Kuźniak
 * @author Rafał Malinowski
 */
public class ValidateTag extends BodyTagSupport {

	private static final long serialVersionUID = -8408025060913758496L;

	private static final Logger LOG = LoggerFactory
			.getLogger(ValidateTag.class);

	/** mark-up to be displayed if given model is empty */
	private static final String EMPTY_MODEL_MESSAGE = "<img src=\"/libs/cq/ui/resources/0.gif\" "
			+ "class=\"cq-text-placeholder\" alt=\"\">\n";

	public static final String CLEAR_BOTH_DIV = "<div style=\"clear:both;font-size:1px\">&nbsp;</div>";

	/** model object that contains validation error messages (if any) */
	private transient Validatable object;

	/** variable on page to store validation result to */
	private String var;

	/**
	 * if true errors will be visible to users, if false, will be visible only
	 * in HTML comments
	 */
	private boolean displayErrors = false;

	/** information message - displayed to the author */
	private String title = "Validation messages:";

	private void clean() {
		var = null;
		object = null;
		displayErrors = false;
		title = "Validation messages:";
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			final Writer out = pageContext.getOut();

			final ValidationResult validationResult = validateObject(object);
			if (null != var) {
				pageContext.setAttribute(var, validationResult,
						PageContext.PAGE_SCOPE);
			}
			if (null == validationResult
					|| validationResult.getValidationState().isValid()) {
				return EVAL_BODY_INCLUDE;
			}

			if (validationResult.getValidationState().isBlank()) {
				if (displayErrors) {
					out.write(EMPTY_MODEL_MESSAGE);
				}
			} else {
				final ErrorMessageListWriter writer = displayErrors ? new HtmlErrorMessageListWriter(
						out, title) : new HtmlCommentErrorMessageListWriter(out);
				writer.writeErrorMessageList(validationResult.getErrorMessages());
			}

			return SKIP_BODY;
		} catch (IOException e) {
			// should never occur
			LOG.error("unexpected exception occured", e);
		} finally {
			clean();
		}
		return SKIP_BODY;
	}

	private ValidationResult validateObject(final Validatable object) {
		if (null == object) {
			return null;
		}

		final Validator validator = SliceTagUtils.getFromCurrentPath(
				pageContext, Validator.class);
		final ValidationResult validationResult = validator.validate(object);
		return validationResult;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public Validatable getObject() {
		return object;
	}

	public void setObject(Validatable object) {
		this.object = object;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isDisplayErrors() {
		return displayErrors;
	}

	public void setDisplayErrors(boolean displayErrors) {
		this.displayErrors = displayErrors;
	}

}
