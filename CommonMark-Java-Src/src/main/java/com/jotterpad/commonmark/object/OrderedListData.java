package com.jotterpad.commonmark.object;

public class OrderedListData extends ListData {

	private String _delim;

	public OrderedListData(String type, String delim) {
		super(type, 0, 0, 0);
		_delim = delim;
	}

	public OrderedListData(String type, String delim, int start) {
		super(type, 0, 0, start);
		_delim = delim;
	}

	public String getDelim() {
		return _delim;
	}

}
