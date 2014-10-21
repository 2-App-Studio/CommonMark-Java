package com.jotterpad.commonmark.object;

public class RefMapItem {
	private String _destination, _title;

	public RefMapItem(String destination, String title) {
		_destination = destination;
		_title = title;
	}

	public String getTitle() {
		return _title;
	}

	public String getDestination() {
		return _destination;
	}
}
