package com.cognifide.slice.validation.api.tag;

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

import java.io.IOException;
import java.io.Writer;

import javax.jcr.RepositoryException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.slice.api.tag.SliceTagUtils;
import com.cognifide.slice.validation.api.Validatable;
import com.cognifide.slice.validation.api.ValidationResult;
import com.cognifide.slice.validation.api.Validator;
import com.cognifide.slice.validation.api.tag.internal.BlankMessageWriter;
import com.cognifide.slice.validation.api.tag.internal.ErrorMessageListWriter;
import com.cognifide.slice.validation.api.tag.internal.HtmlBlankMessageWriter;
import com.cognifide.slice.validation.api.tag.internal.HtmlCommentErrorMessageListWriter;
import com.cognifide.slice.validation.api.tag.internal.HtmlErrorMessageListWriter;

/**
 * If given object is valid - does nothing but evaluates body. If given model is blank - displays
 * "blank model" message. If model is invalid, displays all validation errors.
 * 
 * It can also write result of validation to variable accessible in JSP code.
 * 
 * @author Albert Cenkier
 * @author Jan Kuźniak
 * @author Rafał Malinowski
 * @author Jakub Małecki
 */
public class ValidateTag extends BodyTagSupport {

	private static final long serialVersionUID = -8408025060913758496L;

	private static final Logger LOG = LoggerFactory.getLogger(ValidateTag.class);

	public static final String CLEAR_BOTH_DIV = "<div style=\"clear:both;font-size:1px\">&nbsp;</div>";

	private transient Object object;

	/** model object that contains validation error messages (if any) */
	private transient Validatable validatable;

	/** variable on page to store validation result to */
	private String var;

	/**
	 * if true errors will be visible to users, if false, will be visible only in HTML comments
	 */
	private boolean displayErrors = false;

	/** information message - displayed to the author */
	private String title = "Validation messages:";

	private void clean() {
		var = null;
		object = null;
		validatable = null;
		displayErrors = false;
		title = "Validation messages:";
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			final Writer out = pageContext.getOut();

			final ValidationResult validationResult = validateObject(validatable);
			if (null != var) {
				pageContext.setAttribute(var, validationResult, PageContext.PAGE_SCOPE);
			}
			if (null == validationResult || validationResult.getValidationState().isValid()) {
				return EVAL_BODY_INCLUDE;
			}

			if (validationResult.getValidationState().isBlank()) {
				if (displayErrors) {
					final BlankMessageWriter writer = new HtmlBlankMessageWriter(pageContext);
					writer.writeBlankMessage();
				}
			} else {
				final ErrorMessageListWriter writer = displayErrors ? new HtmlErrorMessageListWriter(out,
						title) : new HtmlCommentErrorMessageListWriter(out);
				writer.writeErrorMessageList(validationResult.getErrorMessages());
			}

			return SKIP_BODY;
		} catch (IOException e) {
			// should never occur
			LOG.error("Unexpected exception occured", e);
		} catch (RepositoryException e) {
			// should never occur
			LOG.error("Repository exception occured", e);
		} finally {
			clean();
		}
		return SKIP_BODY;
	}

	private ValidationResult validateObject(final Validatable validatable) {
		if (null == validatable) {
			return null;
		}

		final Validator validator = SliceTagUtils.getFromCurrentPath(pageContext, Validator.class);
		final ValidationResult validationResult = validator.validate(validatable);
		return validationResult;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
		if (object instanceof Validatable) {
			this.validatable = (Validatable) object;
		}
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