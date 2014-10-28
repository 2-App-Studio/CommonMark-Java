package com.jotterpad.commonmark.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Date;

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
		
		
		DocParser parser = new DocParser();
		HTMLRenderer renderer = new HTMLRenderer();
		Date date = new Date();
				
		try {
			String markdown = resIn.getContent();
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
			assertEquals(s, resOut.getContent());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
