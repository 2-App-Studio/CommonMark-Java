package com.jotterpad.commonmark.object;

public class Delim {
	
	private int _numDelims;
	private boolean _canOpen, _canClose;
	
	public Delim(int numDelims, boolean canOpen, boolean canClose) {
		_numDelims = numDelims;
		_canOpen = canOpen;
		_canClose = canClose;
	}
	
	public int getNumDelims() {
		return _numDelims;
	}
	
	public boolean getCanOpen() {
		return _canOpen;
	}
	
	public boolean getCanClose() {
		return _canClose;
	}
	
	public void setNumDelims(int numDelims) {
		_numDelims = numDelims;
	}
}
