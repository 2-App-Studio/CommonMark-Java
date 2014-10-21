package com.jotterpad.commonmark.object;

public abstract class ListData {

	private int _padding, _markerOffset;
	private String _type;

	public ListData(String type, int padding, int markerOffset) {
		_type = type;
		_padding = padding;
		_markerOffset = markerOffset;
	}

	public String getType() {
		return _type;
	}

	public int getPadding() {
		return _padding;
	}

	public void setPadding(int padding) {
		_padding = padding;
	}

	public int getMarkerOffset() {
		return _markerOffset;
	}

	public void setMarkerOffset(int markerOffset) {
		_markerOffset = markerOffset;
	}
}
