package com.jotterpad.commonmark.object;

import java.util.ArrayList;
import java.util.List;

public class BlocksContent extends Content {

	private ArrayList<Block> _contents;

	public BlocksContent(ArrayList<Block> contents) {
		_contents = contents;
	}

	public BlocksContent(List<Block> contents) {
		_contents = new ArrayList<Block>(contents);
	}

	public ArrayList<Block> getContents() {
		return _contents;
	}
}
