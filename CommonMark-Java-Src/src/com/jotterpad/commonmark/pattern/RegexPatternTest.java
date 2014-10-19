package com.jotterpad.commonmark.pattern;

import static org.junit.Assert.*;

import org.junit.Test;

public class RegexPatternTest {

	@Test
	public void testIsBlank() {
		RegexPattern regex = RegexPattern.getInstance();
		assertTrue(regex.isBlank(""));
		assertTrue(regex.isBlank("  "));
		assertTrue(regex.isBlank(" "));
		assertTrue(regex.isBlank("\t"));
		assertTrue(regex.isBlank("  \t"));
		assertTrue(regex.isBlank("  \n"));
		assertTrue(regex.isBlank("  \n "));
		assertFalse(regex.isBlank("something"));
		assertFalse(regex.isBlank(" s\n "));
		assertFalse(regex.isBlank("_\n "));
	}

	@Test
	public void testNormalizeReference() {
		RegexPattern regex = RegexPattern.getInstance();
		assertEquals("HELLO", regex.normalizeReference("hello"));
		assertEquals("HELLO", regex.normalizeReference(" hello"));
		assertEquals("HELLO", regex.normalizeReference("hello\n"));
		assertEquals("HEL_LO", regex.normalizeReference("hel_lo"));
		assertEquals("HEL_LO! !", regex.normalizeReference("hel_lo! !"));

	}

	@Test
	public void testDeTabLine() {
		RegexPattern regex = RegexPattern.getInstance();
		assertEquals("    hello", regex.deTabLine("\thello"));
		assertEquals("        hello", regex.deTabLine("\t\thello"));
		assertEquals("        hello", regex.deTabLine("    \thello"));

	}

	@Test
	public void testGetHtmlTag() {
		RegexPattern regex = RegexPattern.getInstance();
		assertTrue(regex.getHtmlTag().matcher("<br>").matches());
		assertTrue(regex.getHtmlTag().matcher("</br>").matches());
		assertTrue(regex.getHtmlTag().matcher("<br />").matches());
		assertTrue(regex.getHtmlTag().matcher("<br class=\"good\">").matches());

		assertFalse(regex.getHtmlTag().matcher("br").matches());
		assertFalse(regex.getHtmlTag().matcher("<br").matches());
		assertFalse(regex.getHtmlTag().matcher("<br<").matches());
		assertFalse(regex.getHtmlTag().matcher("2 < 2 > 5").matches());
	}

	@Test
	public void testGetHtmlBlockOpen() {
		RegexPattern regex = RegexPattern.getInstance();
		assertTrue(regex.getHtmlBlockOpen().matcher("<h1>").matches());
		assertTrue(regex.getHtmlBlockOpen().matcher("</h1>").matches());

		assertFalse(regex.getHtmlBlockOpen().matcher("h1").matches());
		assertFalse(regex.getHtmlBlockOpen().matcher("<h1 >").matches());
		assertFalse(regex.getHtmlBlockOpen().matcher("< h1>").matches());
		assertFalse(regex.getHtmlBlockOpen().matcher("<h1/>").matches());
		assertFalse(regex.getHtmlBlockOpen().matcher("<h1 />").matches());
		assertFalse(regex.getHtmlBlockOpen().matcher("< h1 />").matches());
		assertFalse(regex.getHtmlBlockOpen().matcher("< 2 >").matches());
	}

	@Test
	public void testGetEscapable() {
		RegexPattern regex = RegexPattern.getInstance();
		assertTrue(regex.getEscapable().matcher("'").matches());
		assertTrue(regex.getEscapable().matcher("^").matches());
		assertTrue(regex.getEscapable().matcher("$").matches());

		assertFalse(regex.getEscapable().matcher("0").matches());
		assertFalse(regex.getEscapable().matcher("hello").matches());

	}
	
	@Test
	public void testGetEscapableChar() {
		RegexPattern regex = RegexPattern.getInstance();
		assertTrue(regex.getEscapabledChar().matcher("\\'").matches());
		assertTrue(regex.getEscapabledChar().matcher("\\^").matches());
		assertTrue(regex.getEscapabledChar().matcher("\\$").matches());

		assertFalse(regex.getEscapabledChar().matcher("\'").matches());
		assertFalse(regex.getEscapabledChar().matcher("^").matches());

	}
	
	@Test
	public void testGetLinkTitle() {
		RegexPattern regex = RegexPattern.getInstance();
		assertTrue(regex.getLinkTitle().matcher("\"L\"").matches());
		assertTrue(regex.getLinkTitle().matcher("\'L\'").matches());
		assertTrue(regex.getLinkTitle().matcher("(L)").matches());
	}
	
	@Test
	public void testGetLinkDestinationBraces() {
		RegexPattern regex = RegexPattern.getInstance();
		assertTrue(regex.getLinkDestinationBraces().matcher("<a href=\"abc.com\">").matches());
		assertTrue(regex.getLinkDestinationBraces().matcher("<a href=\"\">").matches());
		assertTrue(regex.getLinkDestinationBraces().matcher("<a href=\"/this\">").matches());
	}
	
	@Test
	public void testGetLinkDestination() {
		RegexPattern regex = RegexPattern.getInstance();
		assertTrue(regex.getLinkDestination().matcher("<a href=\"abc.com\">").matches());
		assertTrue(regex.getLinkDestination().matcher("<a href=\"\">").matches());
		assertTrue(regex.getLinkDestination().matcher("<a href=\"/this\">").matches());
	}
}
