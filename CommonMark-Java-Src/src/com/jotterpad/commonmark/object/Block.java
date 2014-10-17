package com.jotterpad.commonmark.object;

import java.util.ArrayList;
import java.util.HashMap;

public class Block {

	// TODO: Data types still not confirmed
	String _tag, _destination, _stringContent, _title, _info, _startLine,
			_startColumn, _endLine;
	Content _c;
	boolean _isOpen, _lastLineBlank, _tight;
	ArrayList<Block> _children, _parent, _label;
	ArrayList<String> _strings, _inlineContent;
	HashMap<String, String> _listData;

	private Block(String tag, Content c, String destination,
			ArrayList<Block> label, String startLine, String startColumn,
			String title) {
		_tag = tag;
		_c = c;
		_destination = destination;
		_label = label;
		_isOpen = true;
		_lastLineBlank = false;
		_startLine = startLine;
		_startColumn = startColumn;
		_endLine = startLine;
		_children = new ArrayList<Block>();
		_parent = null;
		_stringContent = "";
		_strings = new ArrayList<String>();
		_inlineContent = new ArrayList<String>();
		_listData = new HashMap<String, String>();
		_title = title;
		_info = "";
		_tight = false;
	}

	public static Block makeBlock(String tag) {
		return new Block(tag, new StringContent(""), "",
				new ArrayList<Block>(), "", "", "");
	}

	public static Block makeBlock(String tag, Content c) {
		return new Block(tag, c, "", new ArrayList<Block>(), "", "", "");
	}

	public static Block makeBlock(String tag, ArrayList<Block> label,
			String destination) {
		return new Block(tag, new StringContent(""), destination, label, "",
				"", "");
	}

	public static Block makeBlock(String tag, String startLine,
			String startColumn) {
		return new Block(tag, new StringContent(""), "",
				new ArrayList<Block>(), startLine, startColumn, "");
	}

	public String getTag() {
		return _tag;
	}

	public Content getC() {
		return _c;
	}

	public String getDestination() {
		return _destination;
	}

	public ArrayList<Block> getLabel() {
		return _label;
	}

	public String getStringContent() {
		return _stringContent;
	}

	public String getTitle() {
		return _title;
	}

	public String getInfo() {
		return _info;
	}

	public boolean isOpen() {
		return _isOpen;
	}

	public boolean isLastLineBlank() {
		return _lastLineBlank;
	}

	public boolean isTight() {
		return _tight;
	}

	public String getStartLine() {
		return _startLine;
	}

	public String getStartColumn() {
		return _startColumn;
	}

	public String getEndLine() {
		return _endLine;
	}

	public ArrayList<Block> getChildrens() {
		return _children;
	}

	public ArrayList<Block> getParents() {
		return _parent;
	}

	public ArrayList<String> getStrings() {
		return _strings;
	}

	public ArrayList<String> getInlineContent() {
		return _inlineContent;
	}

	public HashMap<String, String> getListData() {
		return _listData;
	}

	public void setTag(String tag) {
		this._tag = tag;
	}

	public void setC(Content c) {
		this._c = c;
	}

	public void setDestination(String destination) {
		this._destination = destination;
	}

	public void setLabel(ArrayList<Block> label) {
		this._label = label;
	}

	public void setStringContent(String stringContent) {
		this._stringContent = stringContent;
	}

	public void setTitle(String title) {
		this._title = title;
	}

	public void setInfo(String info) {
		this._info = info;
	}

	public void setIsOpen(boolean isOpen) {
		this._isOpen = isOpen;
	}

	public void setLastLineBlank(boolean lastLineBlank) {
		this._lastLineBlank = lastLineBlank;
	}

	public void setTight(boolean tight) {
		this._tight = tight;
	}

	public void setStartLine(String startLine) {
		this._startLine = startLine;
	}

	public void setStartColumn(String startColumn) {
		this._startColumn = startColumn;
	}

	public void setEndLine(String endLine) {
		this._endLine = endLine;
	}

	public void setChildrens(ArrayList<Block> children) {
		this._children = children;
	}

	public void setPrents(ArrayList<Block> parent) {
		this._parent = parent;
	}

	public void setStrings(ArrayList<String> strings) {
		this._strings = strings;
	}

	public void setInlineContent(ArrayList<String> inlineContent) {
		this._inlineContent = inlineContent;
	}

	public void setListData(HashMap<String, String> listData) {
		this._listData = listData;
	}
}
