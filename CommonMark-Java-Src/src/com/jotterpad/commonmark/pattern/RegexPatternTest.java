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

}
