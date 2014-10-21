package com.jotterpad.commonmark.object;

public class UnorderedListData extends ListData {

	private String _bullet;

	public UnorderedListData(String type, String bullet) {
		super(type, 0, 0);
		_bullet = bullet;
	}

	public String getBullet() {
		return _bullet;
	}

}
