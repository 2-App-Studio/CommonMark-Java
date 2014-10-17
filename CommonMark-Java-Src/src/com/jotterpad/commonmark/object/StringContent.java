package com.jotterpad.commonmark.object;

public class StringContent extends Content {
	
	private String _content;
	
	public StringContent(String content) {
		_content = content;
	}
	
	public String getContent() {
		return _content;
	}
}
