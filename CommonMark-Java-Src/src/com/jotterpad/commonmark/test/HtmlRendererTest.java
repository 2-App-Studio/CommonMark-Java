package com.jotterpad.commonmark.test;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.Rule;
import org.junit.Test;

public class HtmlRendererTest {
	
	@Rule
	public ResourceFile resIn = new ResourceFile("/spec.txt");

	@Rule
	public ResourceFile resOut = new ResourceFile("/spec.html");

	@Test
	public void test() {
		System.out.println(StringEscapeUtils.unescapeHtml4("&amp;"));
		System.out.println(StringEscapeUtils.escapeHtml4("&"));
		// escape: & => &amp;
		// unescape: &amp; => &
	}
}
