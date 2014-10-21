package com.jotterpad.commonmark;

import org.junit.Test;

public class DocParserTest {

	@Test
	public void test() {
		DocParser parser = new DocParser();
		parser.parse("Hello wotlf\n* hello");
	}

}
