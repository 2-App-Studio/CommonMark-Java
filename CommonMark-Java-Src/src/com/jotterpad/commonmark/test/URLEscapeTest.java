package com.jotterpad.commonmark.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.jotterpad.commonmark.library.URLEscape;

public class URLEscapeTest {

	@Test
	public void test() {
 		assertTrue(URLEscape.isUnsafe('"'));
	}

}
