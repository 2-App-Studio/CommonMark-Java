package com.jotterpad.commonmark.test;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.jotterpad.commonmark.DocParser;
import com.jotterpad.commonmark.HTMLRenderer;
import com.jotterpad.commonmark.object.Block;

public class HtmlRendererTest2 {


	@Test
	public void issue1Test() {
		assertEquals(tester("# test#"), "<h1>test#</h1>\n");
		assertEquals(tester("# test## # #"), "<h1>test## #</h1>\n");
		assertEquals(tester("# test## # ## ###"), "<h1>test## # ##</h1>\n");
		assertEquals(tester("# test#####"), "<h1>test#####</h1>\n");
		assertEquals(tester("## test##### # ## # ####"), "<h2>test##### # ## #</h2>\n");
		assertEquals(tester("## # test##### # ## # ####"), "<h2># test##### # ## #</h2>\n");
	}
	
	public String tester(String markdown) {
		DocParser parser = new DocParser();
		HTMLRenderer renderer = new HTMLRenderer(null);
		Date date = new Date();

		Block block = parser.parse(markdown);
		Block.dumpAST(block, 0);
		Date tempDate = new Date();
		System.out.println("PARSE: "
				+ (tempDate.getTime() - date.getTime()) + "ms");
		date = tempDate;
		String s = renderer.render(block);
		System.out.println("RENDER: "
				+ (new Date().getTime() - date.getTime()) + "ms");
		date = tempDate;
		return s;
	}
}
