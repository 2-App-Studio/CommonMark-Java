package com.jotterpad.commonmark.test;

import org.junit.Rule;

public class HtmlRendererTest {
	@Rule
	public ResourceFile resIn = new ResourceFile("/spec.txt");

	@Rule
	public ResourceFile resOut = new ResourceFile("/spec.html");

}
