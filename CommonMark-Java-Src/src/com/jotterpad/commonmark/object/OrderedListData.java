package com.jotterpad.commonmark.object;

public class OrderedListData extends ListData {

	private int _start;
	private String _delim;

	public OrderedListData(String type, int start, String delim) {
		super(type, 0, 0);
		_start = start;
		_delim = delim;
	}

	public int getStart() {
		return _start;
	}

	public String getDelim() {
		return _delim;
	}

}
