package com.cognifide.slice.validation.api.tag.internal;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.cognifide.slice.validation.api.ErrorLevel;
import com.cognifide.slice.validation.api.ErrorMessage;

/**
 * Write list of error messages to a writer as a HTML comment. Messages will be
 * grouped basing on error levels.
 * 
 * @author Rafał Malinowski
 */
public class HtmlCommentErrorMessageListWriter implements
		ErrorMessageListWriter {

	private final Writer writer;

	private final ErrorMessageWriter errorMessageWriter;

	public HtmlCommentErrorMessageListWriter(final Writer writer) {
		this.writer = writer;
		errorMessageWriter = new HtmlCommentErrorMessageWriter(writer);
	}

	@Override
	public void writeErrorMessageList(final List<ErrorMessage> errorMessageList)
			throws IOException {
		writer.write("\n<!--");

		for (final ErrorLevel errorLevel : ErrorLevel.values()) {
			writeErrorMessageList(errorMessageList, errorLevel);
		}

		writer.write("\n-->\n");
	}

	private void writeErrorMessageList(
			final List<ErrorMessage> errorMessageList,
			final ErrorLevel errorLevel) throws IOException {
		final List<ErrorMessage> filteredErrorMessagEList = filterErrorsMessage(
				errorMessageList, errorLevel);
		if (errorMessageList.isEmpty()) {
			return;
		}

		for (final ErrorMessage errorMessage : filteredErrorMessagEList) {
			errorMessageWriter.writeErrorMessage(errorMessage);
		}
	}

	private List<ErrorMessage> filterErrorsMessage(
			final List<ErrorMessage> errorMessageList,
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
