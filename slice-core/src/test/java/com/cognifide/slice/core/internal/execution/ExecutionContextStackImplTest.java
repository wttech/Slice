package com.cognifide.slice.core.internal.execution;

import com.cognifide.slice.api.exceptions.InvalidExecutionContextException;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExecutionContextStackImplTest {

	private ExecutionContextStackImpl contextStack;

	@Before
	public void setup() {
		contextStack = new ExecutionContextStackImpl("/content");
	}

	@Test
	public void testGetAbsolutePath() {
		String path = contextStack.getAbsolutePath("/examplePath");
		Assert.assertEquals(path, "/examplePath");
		path = contextStack.getAbsolutePath("examplePath");
		Assert.assertEquals(path, "/content/examplePath");

		contextStack.push(new ExecutionContextImpl("/secondContent"));
		Assert.assertEquals(contextStack.size(), 1);
		path = contextStack.getAbsolutePath("./examplePath");
		Assert.assertEquals(path, "/secondContent/examplePath");
		contextStack.pop();
		Assert.assertEquals(contextStack.size(), 0);
	}

	@Test(expected = InvalidExecutionContextException.class)
	public void testGetAbsolutePathException() {
		Assert.assertEquals(contextStack.size(), 0);
		contextStack.getAbsolutePath("./examplePath");
	}
}