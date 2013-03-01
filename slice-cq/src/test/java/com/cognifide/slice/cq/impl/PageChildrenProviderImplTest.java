package com.cognifide.slice.cq.impl;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.cognifide.slice.api.execution.ExecutionContextStack;
import com.cognifide.slice.cq.RecursiveMode;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.foundation.List;

public class PageChildrenProviderImplTest {

	@Mock
	private ExecutionContextStack currentExecutionContext;

	@Mock
	private PageManager pageManager;

	private PageChildrenProviderImpl pageChildrenProvider;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		pageChildrenProvider = new PageChildrenProviderImpl(pageManager, currentExecutionContext);
	}

	@Test
	public void shouldReturnChildPages() {
		Page page = mock(Page.class);

		when(currentExecutionContext.getAbsolutePath("page")).thenReturn("absolutePath");
		when(pageManager.getPage("absolutePath")).thenReturn(page);

		Page mockPage = mock(Page.class);
		when(mockPage.getPath()).thenReturn("page");

		ArrayList<Page> pageList = new ArrayList<Page>();

		pageList.add(mockPage);
		pageList.add(mockPage);

		when(page.listChildren(Mockito.any(PageFilter.class))).thenReturn(pageList.iterator());

		ArrayList<String> pathPages = new ArrayList<String>();
		pathPages.add("page");
		pathPages.add("page");

		Assert.assertArrayEquals(pageChildrenProvider.getChildPages("page", RecursiveMode.NON_RECURSIVE).toArray(),
				pathPages.toArray());

	}

	@Test
	public void shouldReturnChildPagesRecursively() {
		Page page = mock(Page.class);

		when(currentExecutionContext.getAbsolutePath("page")).thenReturn("absolutePath");
		when(pageManager.getPage("absolutePath")).thenReturn(page);

		Page mockPage = mock(Page.class);
		when(mockPage.getPath()).thenReturn("page");
		when(mockPage.listChildren(Mockito.any(PageFilter.class))).thenReturn(new ArrayList<Page>().iterator());

		ArrayList<Page> pageList = new ArrayList<Page>();

		pageList.add(mockPage);
		pageList.add(mockPage);

		ArrayList<Page> pageWithChildrenList = new ArrayList<Page>();
		pageWithChildrenList.add(mockPage);
		pageWithChildrenList.add(mockPage);

		Page mockPageWithChildren = mock(Page.class);
		when(mockPageWithChildren.getPath()).thenReturn("pageWithChildren");
		when(mockPageWithChildren.listChildren(Mockito.any(PageFilter.class))).thenReturn(
				pageWithChildrenList.iterator());

		pageList.add(mockPageWithChildren);

		when(page.listChildren(Mockito.any(PageFilter.class))).thenReturn(pageList.iterator());

		ArrayList<String> expectedPathPages = new ArrayList<String>();
		expectedPathPages.add("page");
		expectedPathPages.add("page");
		expectedPathPages.add("pageWithChildren");
		expectedPathPages.add("page");
		expectedPathPages.add("page");

		Assert.assertArrayEquals(pageChildrenProvider.getChildPages("page", RecursiveMode.RECURSIVE).toArray(),
				expectedPathPages.toArray());
	}

	@Test
	public void shouldReturnFilteredChildPages() {
		Page page = mock(Page.class);

		when(currentExecutionContext.getAbsolutePath("page")).thenReturn("absolutePath");
		when(pageManager.getPage("absolutePath")).thenReturn(page);

		Page mockPage = mock(Page.class);
		when(mockPage.getPath()).thenReturn("page");

		ArrayList<Page> pageList = new ArrayList<Page>();

		pageList.add(mockPage);
		pageList.add(mockPage);

		PageFilter mockPageFilter = mock(PageFilter.class);
		mockPageFilter.includes(mockPage);

		when(page.listChildren(mockPageFilter)).thenReturn(pageList.iterator());

		ArrayList<String> pathPages = new ArrayList<String>();
		pathPages.add("page");
		pathPages.add("page");

		Assert.assertArrayEquals(pageChildrenProvider
				.getChildPages("page", mockPageFilter, RecursiveMode.NON_RECURSIVE).toArray(), pathPages.toArray());
	}
}
