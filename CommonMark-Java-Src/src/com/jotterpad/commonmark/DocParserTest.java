package com.jotterpad.commonmark;

import org.junit.Test;

public class DocParserTest {

	@Test
	public void test() {
		
		DocParser parser = new DocParser();
//		parser.parse("Hello *wotlf*\n");
		parser.parse("Hello *wotlf*. This is a good **world**<http://abc.com> [LINK2](http://ccc.com)\n\n"
				+ "NEXT PARA **GOOD** *BAD*\n\n"
				+ "# THIS IS H1\n## THIS IS H2\n```CODEX CODEX```\n\n"
				+ "> BQ1 BQ1\n>BQ1 BQ1\n>> BQ2 BQ2\n\n"
				+ "[foo]: http://abc.com\n***\n\n"
				+ "\t\tCODE AGAIN\n\n"
				+ "![IMAGETHIS](/url \"imagetitle\")");
		
		System.out.println(parser.printParser());
	}

}
