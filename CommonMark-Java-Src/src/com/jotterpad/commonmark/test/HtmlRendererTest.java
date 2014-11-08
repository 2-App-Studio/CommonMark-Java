package com.jotterpad.commonmark.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import com.jotterpad.commonmark.DocParser;
import com.jotterpad.commonmark.HTMLRenderer;
import com.jotterpad.commonmark.HTMLRenderer.HTMLListener;
import com.jotterpad.commonmark.object.Block;

public class HtmlRendererTest {

	/**
	 * Remember to edit the getContent function in ResourceFile as well as the rules path below
	 * 		for the file reading in different system
	 */
	@Rule
	public ErrorCollector collector = new ErrorCollector();
//	public ResourceFile resIn = new ResourceFile("/spec_short.txt");
	public ResourceFile resIn = new ResourceFile("C:\\Users\\User\\git\\CommonMark-Java\\CommonMark-Java-Src\\resources\\spec.txt");
	public ResourceFile beamIn = new ResourceFile("C:\\Users\\User\\git\\CommonMark-Java\\CommonMark-Java-Src\\resources\\MMD-TestSuite-Combined.text");

	@Rule
//	public ResourceFile resOut = new ResourceFile("/spec.html");
	public ResourceFile resOut = new ResourceFile("C:\\Users\\User\\git\\CommonMark-Java\\CommonMark-Java-Src\\resources\\spec.html");
	public ResourceFile beamOut = new ResourceFile("C:\\Users\\User\\git\\CommonMark-Java\\CommonMark-Java-Src\\resources\\MMD-TestSuite-Combined.html");
//	
//	@Test
//	public void test() {
//
//		DocParser parser = new DocParser();
//		HTMLRenderer renderer = new HTMLRenderer(null);
//		Date date = new Date();
//
//		try {
//			String markdown = resIn.getContent();
//			Block block = parser.parse(markdown);
//			Block.dumpAST(block, 0);
//			Date tempDate = new Date();
//			System.out.println("PARSE: "
//					+ (tempDate.getTime() - date.getTime()) + "ms");
//			date = tempDate;
//			String s = renderer.render(block);
//			System.out.println("RENDER: "
//					+ (new Date().getTime() - date.getTime()) + "ms");
//			date = tempDate;
//			assertEquals(s, resOut.getContent());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * John Gruber's test suite 
	 */
//	@Test
//	public void test2() {
////		String path = "C:\\Users\\User\\git\\CommonMark-Java\\CommonMark-Java-Src\\resources\\MarkdownTest_1.0\\Tests";
//		String path = "/MarkdownTest_1.0/Tests/";
//		File folder = new File(path);
//		File[] listOfFiles = folder.listFiles(new FilenameFilter() { 
//	         public boolean accept(File folder, String f)
//             { return f.endsWith(".html"); }
//		} );
//		for (int i = 0; i < listOfFiles.length; i++) {
//			String filename = listOfFiles[i].getName().replaceFirst("[.][^.]+$", "");
////			ResourceFile fileIn = new ResourceFile(path + "\\" + filename + ".text");
//			ResourceFile fileIn = new ResourceFile(path + filename + ".text");
////			ResourceFile fileOut = new ResourceFile(path + "\\" + listOfFiles[i].getName());
//			ResourceFile fileOut = new ResourceFile(path + listOfFiles[i].getName());
//			testEach (fileIn, fileOut);
//		}
//	}
//	
//	private void testEach (ResourceFile fileIn, ResourceFile fileOut) {
//		DocParser parser = new DocParser();
//		HTMLRenderer renderer = new HTMLRenderer();
//		Date date = new Date();
//
//		try {
//			String markdown = fileIn.getContent();
//			Block block = parser.parse(markdown);
//			Block.dumpAST(block, 0);
//			Date tempDate = new Date();
//			System.out.println("PARSE: "
//					+ (tempDate.getTime() - date.getTime()) + "ms");
//			date = tempDate;
//			String s = renderer.render(block);
//			System.out.println("RENDER: "
//					+ (new Date().getTime() - date.getTime()) + "ms");
//			date = tempDate;
////			assertEquals(s, fileOut.getContent());
//			collector.checkThat(s, equalTo(fileOut.getContent()));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * MMD-Test-Suite - https://github.com/fletcher/MMD-Test-Suite
	 */
	@Test
	public void test3() {

		DocParser parser = new DocParser();
		HTMLRenderer renderer = new HTMLRenderer(null);
		Date date = new Date();

		try {
			String markdown = beamIn.getContent();
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
			assertEquals(beamOut.getContent(), s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
