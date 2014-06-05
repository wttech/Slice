package com.cognifide.slice.validation.api.tag.internal;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.cognifide.slice.validation.api.ErrorLevel;
import com.cognifide.slice.validation.api.ErrorMessage;

/**
 * Write list of error messages to a writer as a HTML div element. Messages will be grouped basing on error
 * levels.
 * 
 * @deprecated It will be removed (along with whole Validation API) in next major version - custom solution
 * required
 * @author Rafał Malinowski
 */
public class HtmlErrorMessageListWriter implements ErrorMessageListWriter {

	private static final String CLEAR_BOTH_DIV = "<div style=\"clear:both;font-size:1px\">&nbsp;</div>";

	private final Writer writer;

	private final String title;

	private final ErrorMessageWriter errorMessageWriter;

	public HtmlErrorMessageListWriter(final Writer writer, final String title) {
		this.writer = writer;
		this.title = title;
		errorMessageWriter = new HtmlErrorMessageWriter(writer);
	}

	@Override
	public void writeErrorMessageList(final List<ErrorMessage> errorMessageList) throws IOException {
		writer.write("<div class=\"validationMessages\">\n");
		writer.write("<div class=\"title\">");
		writer.write(title);
		writer.write("</div>\n");

		for (final ErrorLevel errorLevel : ErrorLevel.values()) {
			writeErrorMessageList(errorMessageList, errorLevel);
		}

		writer.write("</div>\n");
		writer.write(CLEAR_BOTH_DIV);
	}

	private void writeErrorMessageList(final List<ErrorMessage> errorMessageList, final ErrorLevel errorLevel)
			throws IOException {
		final List<ErrorMessage> filteredErrorMessagEList = filterErrorsMessage(errorMessageList, errorLevel);
		if (filteredErrorMessagEList.isEmpty()) {
			return;
		}

		writer.write(String.format("<ul class=\"%sMessages\">\n", errorLevel.getCssClassName()));
		for (final ErrorMessage errorMessage : filteredErrorMessagEList) {
			errorMessageWriter.writeErrorMessage(errorMessage);
		}
		writer.write("</ul>\n");
	}

	private List<ErrorMessage> filterErrorsMessage(final List<ErrorMessage> errorMessageList,
			final ErrorLevel errorLevel) {
		final List<ErrorMessage> result = new ArrayList<ErrorMessage>();
		for (final ErrorMessage errorMessage : errorMessageList) {
			if (errorMessage.getErrorLevel() == errorLevel) {
				result.add(errorMessage);
			}
		}
		return result;
	}

}
