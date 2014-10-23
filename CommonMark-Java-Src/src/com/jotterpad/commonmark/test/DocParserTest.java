package com.jotterpad.commonmark.test;

import org.junit.Test;

import com.jotterpad.commonmark.DocParser;

public class DocParserTest {

	@Test
	public void test() {

		DocParser parser = new DocParser();
		parser.parse("Hello *world*. This is a good **world**<http://abc.com> [LINK2](http://ccc.com)\n\n"
				+ "NEXT PARA **GOOD** *BAD*\n\n"
				+ "# THIS IS H1\n## THIS IS H2\n```CODEX CODEX```\n\n"
				+ "> BQ1 BQ1\n>BQ1 BQ1\n>> BQ2 BQ2\n\n"
				+ "[foo]: http://abc.com\n***\n\n"
				+ "\t\tCODE AGAIN\n\n"
				+ "![IMAGETHIS](/url \"imagetitle\")\n\n"
				+ "1. List \n2. List\n");
		System.out.println(parser.printParser());
	}

}
