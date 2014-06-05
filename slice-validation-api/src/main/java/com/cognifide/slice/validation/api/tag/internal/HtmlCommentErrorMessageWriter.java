package com.cognifide.slice.validation.api.tag.internal;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import org.apache.commons.lang.StringEscapeUtils;

import com.cognifide.slice.validation.api.ErrorMessage;

/**
 * Write error message to a writer as a HTML comment. All code will be HTML escaped.
 * 
 * @deprecated It will be removed (along with whole Validation API) in next major version - custom solution
 * required
 * 
 * @author Rafał Malinowski
 */
public class HtmlCommentErrorMessageWriter implements ErrorMessageWriter {

	private final Writer writer;

	public HtmlCommentErrorMessageWriter(final Writer writer) {
		this.writer = writer;
	}

	@Override
	public void writeErrorMessage(final ErrorMessage errorMessage) throws IOException {
		writer.write("\n");
		writer.write(StringEscapeUtils.escapeHtml(errorMessage.getMessage()));

		Throwable throwable = errorMessage.getThrowable();
		if (throwable != null) {
			throwable.printStackTrace(new PrintWriter(writer));
		}
	}

}
