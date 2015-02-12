package com.jotterpad.commonmark.library;

import java.util.regex.Pattern;

public class URLEscape {

	public static String escape(String input) {
		StringBuilder resultStr = new StringBuilder();
		for (char ch : input.toCharArray()) {
			if (isUnsafe(ch)) {
				resultStr.append('%');
				resultStr.append(toHex(ch / 16));
				resultStr.append(toHex(ch % 16));
			} else {
				resultStr.append(ch);
			}
		}
		return resultStr.toString();
	}

	private static char toHex(int ch) {
		return (char) (ch < 10 ? '0' + ch : 'A' + ch - 10);
	}

	public static boolean isUnsafe(char ch) {

		if (":/=*%?&)(#.-_".indexOf(ch) >= 0
				|| Pattern.compile("[a-z0-9]", Pattern.CASE_INSENSITIVE)
						.matcher(String.valueOf(ch)).find())
			return false;
		else
			return true;
	}
}
