package com.jotterpad.commonmark.test;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.Rule;
import org.junit.Test;

import com.jotterpad.commonmark.DocParser;
import com.jotterpad.commonmark.HTMLRenderer;
import com.jotterpad.commonmark.object.Block;

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
		

		DocParser parser = new DocParser();
		Block block = parser.parse("Hello *world*. This is a good **world**<http://abc.com> [LINK2](http://ccc.com)\n\n"
				+ "NEXT PARA **GOOD** *BAD*\n\n"
				+ "# THIS IS H1\n## THIS IS H2\n```CODEX CODEX```\n\n"
				+ "> BQ1 BQ1\n>BQ1 & BQ1\n>> BQ2 BQ2\n\n"
				+ "[foo]: http://abc.com\n***\n\n"
				+ "\t\tCODE AGAIN\n\n"
				+ "![IMAGETHIS](/url \"imagetitle\")\n\n"
				+ "1. List \n2. List\n");
		System.out.println(parser.printParser());
		HTMLRenderer renderer = new HTMLRenderer();
		String html = renderer.render(block);
		System.out.println(html);

	}
}
