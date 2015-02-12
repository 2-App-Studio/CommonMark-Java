package com.jotterpad.commonmark.object;

import java.util.ArrayList;

import com.jotterpad.commonmark.library.CollectionUtils;

public class Block {

	private String _tag, _destination, _stringContent, _title, _info;
	private Content _c;
	private boolean _isOpen, _lastLineBlank, _tight;
	private Block _parent;
	private ArrayList<Block> _children, _label, _inlineContent;
	private ArrayList<String> _strings;
	private ListData _listData;
	private int _startLine, _startColumn, _endLine;

	private int _fenceOffset, _fenceLength, _level;
	private char _fenceChar;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public static void dumpAST(Block obj, int ind) {
		String levels = "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";
		String indChar = ind != 0 ? (levels.substring(0, ind)) + "->" : "";
		System.out.println(indChar + "[" + obj.getTag() + "]");

		if (!obj.getTitle().isEmpty()) {
			System.out.println("\t" + indChar + "Title: " + obj.getTitle());
		}
		if (!obj.getInfo().isEmpty()) {
			System.out.println("\t" + indChar + "Info: " + obj.getInfo());
		}
		if (!obj.getDestination().isEmpty()) {
			System.out.println("\t" + indChar + "Destination: " + obj.getDestination());
		}
		if (obj.isOpen()) {
			System.out.println("\t" + indChar + "Open: "
					+ String.valueOf(obj.isOpen()));
		}
		if (obj.isLastLineBlank()) {
			System.out.println("\t" + indChar + "Last line blank: "
					+ String.valueOf(obj.isLastLineBlank()));
		}
		if (obj.getStartLine() > 0) {
			System.out.println("\t" + indChar + "Start line: "
					+ obj.getStartLine());
		}
		if (obj.getStartColumn() > 0) {
			System.out.println("\t" + indChar + "Start column: "
					+ obj.getStartColumn());
		}
		if (obj.getEndLine() > 0) {
			System.out.println("\t" + indChar + "End line: " + obj.getEndLine());
		}
		if (!obj.getStringContent().isEmpty()) {
			System.out.println("\t" + indChar + "String content: "
					+ obj.getStringContent());
		}
		if (!obj.getInfo().isEmpty()) {
			System.out.println("\t" + indChar + "Info: " + obj.getInfo());
		}
		if (obj.getStrings().size() > 0) {
			System.out.println("\t" + indChar + "Strings['"
					+ CollectionUtils.join(obj.getStrings(), "', '") + "'']");
		}
		if (obj.getC() != null) {
			if (obj.getC() instanceof BlocksContent) {
				System.out.println("\t" + indChar + "c: ");
				ArrayList<Block> blocks = ((BlocksContent) obj.getC()).getContents();
				for (Block block : blocks) {
					dumpAST(block, ind + 2);
				}
			} else {
				System.out.println("\t" + indChar + "c: "
						+ ((StringContent) obj.getC()).getContent());
			}
		}
		if (obj.getLabels().size() > 0) {
			System.out.println("\t" + indChar + "Label:");
			for (Block block : obj.getLabels()) {
				dumpAST(block, ind + 2);
			}
		}
		if (obj.getListData() != null && obj.getListData().getType() != null) {
			System.out.println("\t" + indChar + "List Data:");
			System.out.println("\t\t" + indChar + "[type] = "
					+ obj.getListData().getType());
			if (obj.getListData() instanceof UnorderedListData
					&& ((UnorderedListData) obj.getListData()).getBullet() != null) {
				System.out.println("\t\t" + indChar + "[bullet_char] = "
						+ ((UnorderedListData) obj.getListData()).getBullet());
			}
            if (obj.getListData() != null){ // int return, can't be null
                System.out.println("\t\t" + indChar + "[start] = "
                        + (obj.getListData()).getStart());
                System.out.println("\t\t" + indChar + "[padding] = "
                        + (obj.getListData()).getPadding());
                System.out.println("\t\t" + indChar + "[marker_offset] = "
                        + (obj.getListData()).getMarkerOffset());
            }
            if (obj.getListData() instanceof OrderedListData
                    && (((OrderedListData) obj.getListData()).getDelim() != null)) {
                System.out.println("\t\t" + indChar + "[delimiter] = "
                        + ((OrderedListData) obj.getListData()).getDelim());
            }
		}
		if (obj.getInlineContent().size() > 0) {
			System.out.println("\t" + indChar + "Inline content:");
			for(Block block : obj.getInlineContent()) {
				dumpAST(block, ind + 2);
			}
		}
		if (obj.getChildren().size() > 0) {
			System.out.println("\t" + indChar + "Children:");
			for(Block block : obj.getChildren()) {
				dumpAST(block, ind + 2);
			}
		}
	}

	public static String printBlock(int level, Block block) {
		String s = "";

		String levels = "*****************************************************************"
				.substring(0, level * 4);
		s += "|" + levels;

		if (block.getC() instanceof StringContent) {
			s += " " + ((StringContent) block.getC()).getContent();
		}
		if (block.getC() instanceof BlocksContent) {
			ArrayList<Block> blocks = ((BlocksContent) block.getC())
					.getContents();
			for (Block b : blocks) {
				if (b.getC() instanceof StringContent) {
					s += " " + ((StringContent) b.getC()).getContent() + " | ";
				}
			}
		}
		if (!block.getStringContent().isEmpty()) {
			s += " " + block.getStringContent();
		}
		if (!block.getDestination().isEmpty()) {
			s += " " + block.getDestination();
		}
		if (!block.getInfo().isEmpty()) {
			s += " " + block.getInfo();
		}
		if (block.getStrings().size() > 0) {
			s += " [SS: " + block.getStrings().size() + "]";
		}

		s += " (" + block.getTag() + ")" + "\n";

		ArrayList<Block> inlines = block.getInlineContent();
		for (Block inline : inlines) {
			s += Block.printBlock(level + 1, inline);
		}

		ArrayList<Block> children = block.getChildren();
		for (Block child : children) {
			s += Block.printBlock(level + 1, child) + "\n";
		}

		return s;
	}

	private Block(String tag, Content c, String destination,
			ArrayList<Block> label, int startLine, int startColumn, String title) {
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
		_inlineContent = new ArrayList<Block>();
		_listData = null;
		_title = title;
		_info = "";
		_tight = false;

		// Additional
		_fenceOffset = 0;
		_fenceLength = 0;
		_level = 0;
	}

	public static Block makeBlock(String tag) {
		return new Block(tag, new StringContent(""), "",
				new ArrayList<Block>(), -1, -1, "");
	}

	public static Block makeBlock(String tag, Content c) {
		return new Block(tag, c, "", new ArrayList<Block>(), -1, -1, "");
	}

	public static Block makeBlock(String tag, String dest, String title,
			ArrayList<Block> label) {
		return new Block(tag, new StringContent(""), dest, label, -1, -1, title);
	}

	public static Block makeBlock(String tag, ArrayList<Block> label,
			String destination) {
		return new Block(tag, new StringContent(""), destination, label, -1,
				-1, "");
	}

	public static Block makeBlock(String tag, int startLine, int startColumn) {
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

	public ArrayList<Block> getLabels() {
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

	public int getStartLine() {
		return _startLine;
	}

	public int getStartColumn() {
		return _startColumn;
	}

	public int getEndLine() {
		return _endLine;
	}

	public ArrayList<Block> getChildren() {
		return _children;
	}

	public Block getParent() {
		return _parent;
	}

	public ArrayList<String> getStrings() {
		return _strings;
	}

	public ArrayList<Block> getInlineContent() {
		return _inlineContent;
	}

	public ListData getListData() {
		return _listData;
	}

	public int getFenceOffset() {
		return _fenceOffset;
	}

	public int getFenceLength() {
		return _fenceLength;
	}

	public char getFenceChar() {
		return _fenceChar;
	}

	public int getLevel() {
		return _level;
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

	public void setStartLine(int startLine) {
		this._startLine = startLine;
	}

	public void setStartColumn(int startColumn) {
		this._startColumn = startColumn;
	}

	public void setEndLine(int endLine) {
		this._endLine = endLine;
	}

	public void setChildrens(ArrayList<Block> children) {
		this._children = children;
	}

	public void setPrent(Block parent) {
		this._parent = parent;
	}

	public void setStrings(ArrayList<String> strings) {
		this._strings = strings;
	}

	public void setInlineContent(ArrayList<Block> inlineContent) {
		this._inlineContent = inlineContent;
	}

	public void setListData(ListData listData) {
		this._listData = listData;
	}

	public void setFenceOffset(int fenceOffset) {
		_fenceOffset = fenceOffset;
	}

	public void setFenceLength(int fenceLength) {
		_fenceLength = fenceLength;
	}

	public void setFenceChar(char fenceChar) {
		_fenceChar = fenceChar;
	}

	public void setLevel(int level) {
		_level = level;
	}
}
