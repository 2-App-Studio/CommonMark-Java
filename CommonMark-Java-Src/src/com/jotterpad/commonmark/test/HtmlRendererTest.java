package com.jotterpad.commonmark.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;

import com.jotterpad.commonmark.DocParser;
import com.jotterpad.commonmark.HTMLRenderer;
import com.jotterpad.commonmark.object.Block;

public class HtmlRendererTest {

	@Rule
	public ResourceFile resIn = new ResourceFile("/spec_short.txt");

	@Rule
	public ResourceFile resOut = new ResourceFile("/spec.html");

	@Test
	public void test() {

		DocParser parser = new DocParser();
		HTMLRenderer renderer = new HTMLRenderer();

		try {
			String markdown = resIn.getContent();
			Block block = parser.parse(markdown);
			System.out.println(parser.printParser());
			assertEquals(renderer.render(block), resOut.getContent());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
