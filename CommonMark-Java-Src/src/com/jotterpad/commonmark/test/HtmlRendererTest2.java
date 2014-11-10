package com.jotterpad.commonmark.test;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.jotterpad.commonmark.DocParser;
import com.jotterpad.commonmark.HTMLRenderer;
import com.jotterpad.commonmark.object.Block;

public class HtmlRendererTest2 {

	@Test
	public void issue3Test() {
//		assertEquals(
//				tester("Test various identifiers:\n\n[link-a]\n\n"
//						+ "[link-1]\n\n[link--2]\n\n[link--a]\n\n[link---1]\n\n"
//						+ "[link-a]: http://path.to/link-a.html\n"
//						+ "[link-1]: http://path.to/link-1.html\n"
//						+ "[link--a]: http://path.to/link--a.html\n"
//						+ "[link--2]: http://path.to/link--2.html\n"
//						+ "[link---1]: http://path.to/link---1.html\n"),
//				"<p>Test various identifiers:</p>\n"
//						+ "<p><a href=\"http://path.to/link-a.html\">link-a</a></p>\n"
//						+ "<p><a href=\"http://path.to/link-1.html\">link-1</a></p>\n"
//						+ "<p><a href=\"http://path.to/link--2.html\">link--2</a></p>\n"
//						+ "<p><a href=\"http://path.to/link--a.html\">link--a</a></p>\n"
//						+ "<p><a href=\"http://path.to/link---1.html\">link---1</a></p>\n");

		assertEquals(
				tester("And [the link][link], [link] , and [][link] again."
						+ "\n\n[the link][link]"
						+ "\n\n[image]: http://path.to/image \"Image title\" "
						+ "width=\"40px\" height=\"400px\""
						+ "\n[image2]: http://path.to/image \"Image title\" height=\"400px\""
						+ "\n[image3]: http://path.to/image \"Image title\" width=\"40px\""
						+ "\n[image4]: http://path.to/image \"Image title\""
						+ "\n[link]: http://path.to/link.html \"Some Link\" class=external"
						+ "\nstyle=\"border: solid black 1px;\""
						+ "\n\nTest link and markup ![and *this* but not **that**][image-attr]"
						+ "\n\n[image-attr]: http://path.to/image \"Test *this* and **this**\""),
				"<p>And [the link][link], [link] , and [][link] again.</p>"
						+ "\n<p>[the link][link]</p>"
						+ "\n<p>[image]: http://path.to/image &quot;Image title&quot; width=&quot;40px&quot; height=&quot;400px&quot;"
						+ "\n[image2]: http://path.to/image &quot;Image title&quot; height=&quot;400px&quot;"
						+ "\n[image3]: http://path.to/image &quot;Image title&quot; width=&quot;40px&quot;"
						+ "\n[image4]: http://path.to/image &quot;Image title&quot;"
						+ "\n[link]: http://path.to/link.html &quot;Some Link&quot; class=external"
						+ "\nstyle=&quot;border: solid black 1px;&quot;</p>"
						+ "\n<p>Test link and markup <img src=\"http://path.to/image\" alt=\"and this but not that\" title=\"Test *this* and **this**\" /></p>");
	}

//	@Test
//	public void issue1Test() {
//		assertEquals(tester("# test#"), "<h1>test#</h1>\n");
//		assertEquals(tester("# test## # #"), "<h1>test## #</h1>\n");
//		assertEquals(tester("# test## # ## ###"), "<h1>test## # ##</h1>\n");
//		assertEquals(tester("# test#####"), "<h1>test#####</h1>\n");
//		assertEquals(tester("## test##### # ## # ####"),
//				"<h2>test##### # ## #</h2>\n");
//		assertEquals(tester("## # test##### # ## # ####"),
//				"<h2># test##### # ## #</h2>\n");
//	}

	public String tester(String markdown) {
		DocParser parser = new DocParser();
		HTMLRenderer renderer = new HTMLRenderer(null);
		Date date = new Date();

		Block block = parser.parse(markdown);
		Block.dumpAST(block, 0);
		Date tempDate = new Date();
		System.out.println("PARSE: " + (tempDate.getTime() - date.getTime())
				+ "ms");
		date = tempDate;
		String s = renderer.render(block);
		System.out.println("RENDER: " + (new Date().getTime() - date.getTime())
				+ "ms");
		date = tempDate;
		return s;
	}
}
