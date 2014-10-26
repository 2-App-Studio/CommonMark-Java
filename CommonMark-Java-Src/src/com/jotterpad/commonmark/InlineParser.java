package com.jotterpad.commonmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jotterpad.commonmark.object.Block;
import com.jotterpad.commonmark.object.BlocksContent;
import com.jotterpad.commonmark.object.Delim;
import com.jotterpad.commonmark.object.RefMapItem;
import com.jotterpad.commonmark.object.StringContent;
import com.jotterpad.commonmark.pattern.RegexPattern;

public class InlineParser {

	private String _subject;
	private int _labelNestLevel, _pos;
	private HashMap<String, RefMapItem> _refMap;
	private RegexPattern _regex;

	public InlineParser() {
		_subject = "";
		_labelNestLevel = 0;
		_pos = 0;
		_refMap = new HashMap<String, RefMapItem>();
		_regex = RegexPattern.getInstance();
	}

	/**
	 *
	 * @param pattern
	 * @return
	 */
	public String match(Pattern pattern) {
		Matcher match = pattern.matcher(_subject.substring(_pos));
		boolean isMatch = match.find();
		if (isMatch) {
			_pos += match.end(0);
			return match.group();
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param regex
	 * @param flags
	 * @return NULL if not found
	 */
	public String match(String regex, int flags) {
		Pattern pattern = Pattern.compile(regex, flags);
		return match(pattern);
	}

	/**
	 *
	 * @param regex
	 * @return
	 */
	public String match(String regex) {
		return match(regex, 0);
	}

	/**
	 * 
	 * @return NULL if index out of bound
	 */
	public Character peek() {
		if (_pos < _subject.length()) {
			return _subject.charAt(_pos);
		} else {
			return null;
		}
	}

	public boolean spnl() {
		match("^ *(?:\\n *)?");
		return true;
	}

	public int parseBackticks(ArrayList<Block> inlines) {
		int startPos = _pos;
		String ticks = match("^`+");
		if (ticks == null) {
			return 0;
		}
		int afterOpenTicks = _pos;
		boolean foundCode = false;
		String match = match("`+", Pattern.MULTILINE);
		while ((!foundCode) && (match != null)) {
			if (match.equals(ticks)) {
				String c = _subject.substring(afterOpenTicks,
						_pos - ticks.length());
				c = c.replaceAll("[ \\n]+", " "); // Originally Literal Regex
				c = c.trim();
				inlines.add(Block.makeBlock("Code", new StringContent(c)));
				return _pos - startPos;
			}
			match = match("`+", Pattern.MULTILINE);
		}
		inlines.add(Block.makeBlock("Str", new StringContent(ticks)));
		_pos = afterOpenTicks;

		return _pos - startPos;
	}

	public int parseEscaped(ArrayList<Block> inlines) {
		String subj = _subject;
		int pos = _pos;

		if (subj.length() > pos && subj.charAt(pos) == '\\') {
			if (subj.length() > pos + 1 && subj.charAt(pos + 1) == '\n') {
				inlines.add(Block.makeBlock("Hardbreak"));
				_pos += 2;
				return 2;
			} else if (subj.length() > pos + 2
					&& _regex.getEscapable()
							.matcher(subj.substring(pos + 1, pos + 2)).find()) {
				inlines.add(Block.makeBlock("Str",
						new StringContent(subj.substring(pos + 1, pos + 2))));
				_pos += 2;
				return 2;
			} else {
				inlines.add(Block.makeBlock("Str", new StringContent("\\")));
				_pos += 1;
				return 1;
			}
		} else {
			return 0;
		}
	}

	public int parseAutoLink(ArrayList<Block> inlines) {
		String m = match("^<([a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*)>");
		String m2 = match(
				"^<(?:coap|doi|javascript|aaa|aaas|about|acap|cap|cid|crid|data|dav|dict|dns|file|ftp|geo|go|gopher|h323|http|https|iax|icap|im|imap|info|ipp|iris|iris.beep|iris.xpc|iris.xpcs|iris.lwz|ldap|mailto|mid|msrp|msrps|mtqp|mupdate|news|nfs|ni|nih|nntp|opaquelocktoken|pop|pres|rtsp|service|session|shttp|sieve|sip|sips|sms|snmp|soap.beep|soap.beeps|tag|tel|telnet|tftp|thismessage|tn3270|tip|tv|urn|vemmi|ws|wss|xcon|xcon-userid|xmlrpc.beep|xmlrpc.beeps|xmpp|z39.50r|z39.50s|adiumxtra|afp|afs|aim|apt|attachment|aw|beshare|bitcoin|bolo|callto|chrome|chrome-extension|com-eventbrite-attendee|content|cvs|dlna-playsingle|dlna-playcontainer|dtn|dvb|ed2k|facetime|feed|finger|fish|gg|git|gizmoproject|gtalk|hcp|icon|ipn|irc|irc6|ircs|itms|jar|jms|keyparc|lastfm|ldaps|magnet|maps|market|message|mms|ms-help|msnim|mumble|mvn|notes|oid|palm|paparazzi|platform|proxy|psyc|query|res|resource|rmi|rsync|rtmp|secondlife|sftp|sgn|skype|smb|soldat|spotify|ssh|steam|svn|teamspeak|things|udp|unreal|ut2004|ventrilo|view-source|webcal|wtai|wyciwyg|xfire|xri|ymsgr):[^<>\\x00-\\x20]*>",
				Pattern.CASE_INSENSITIVE);

		if (m != null) {
			String dest = m.substring(1, m.length() - 1);
			ArrayList<Block> label = new ArrayList<Block>();
			label.add(Block.makeBlock("Str", new StringContent(dest)));
			inlines.add(Block.makeBlock("Link", label, "mailto:" + dest));
			return m.length();
		} else if (m2 != null) {
			String dest2 = m2.substring(1, m2.length() - 1);
			ArrayList<Block> label = new ArrayList<Block>();
			label.add(Block.makeBlock("Str", new StringContent(dest2)));
			inlines.add(Block.makeBlock("Link", label, dest2));
			return m2.length();
		} else {
			return 0;
		}
	}

	public int parseHtmlTag(ArrayList<Block> inlines) {
		String m = match(_regex.getHtmlTag());
		if (m != null) {
			inlines.add(Block.makeBlock("Html", new StringContent(m)));
			return m.length();
		} else {
			return 0;
		}
	}

	public Delim scanDelims(Character c) {
		int numDelims = 0;
		Character charBefore = null, charAfter = null;
		int startPos = _pos;

		charBefore = _pos == 0 ? '\n' : _subject.charAt(_pos - 1);

		// TODO: Want to check for null?
		while (peek() == c) {
			numDelims++;
			_pos++;
		}

		Character a = peek();
		charAfter = a != null ? a : '\n';

		// TODO: Correct? Check for whitespace
		boolean canOpen = (numDelims > 0) && (numDelims <= 3)
				&& (!Character.isWhitespace(charAfter));
		boolean canClose = (numDelims > 0) && (numDelims <= 3)
				&& (!Character.isWhitespace(charBefore));

		if (c == '_') {
			// TODO: Correct? Check for alphanumeric
			canOpen = canOpen
					&& (!(Character.isDigit(charBefore) && Character
							.isLetter(charBefore)));
			canClose = canClose
					&& (!(Character.isDigit(charAfter) && Character
							.isLetter(charAfter)));
		}
		_pos = startPos;

		return new Delim(numDelims, canOpen, canClose);
	}

	public int parseEmphasis(ArrayList<Block> inlines) {
		int startPos = _pos;
		int firstClose = 0;
		Character next = peek();
		char c;

		if ((next == '*') || (next == '_')) {
			c = next;
		} else {
			return 0;
		}

		Delim res = scanDelims(c);
		int numDelims = res.getNumDelims();
		_pos += numDelims;

		if (startPos > 0) {
			inlines.add(Block.makeBlock(
					"Str",
					new StringContent(_subject.substring(_pos - numDelims,
							numDelims + startPos))));
		} else {
			inlines.add(Block.makeBlock(
					"Str",
					new StringContent(_subject.substring(_pos - numDelims,
							numDelims))));
		}

		int delimPos = inlines.size() - 1;

		if (!res.getCanOpen() || numDelims == 0) {
			return 0;
		}

		int firstCloseDelims = 0;

		if (numDelims == 1) {
			while (true) {
				res = scanDelims(c);
				if (res.getNumDelims() >= 1 && res.getCanClose()) {
					_pos++;
					inlines.get(delimPos).setTag("Emph");
					inlines.get(delimPos).setC(
							new BlocksContent(inlines.subList(delimPos + 1,
									inlines.size())));
					if (inlines.size() > 1) {
						inlines = new ArrayList<Block>(inlines.subList(0,
								delimPos + 1));
					}
					break;
				} else {
					if (parseInline(inlines) == 0) {
						break;
					}
				}
			}
			return _pos - startPos;
		} else if (numDelims == 2) {
			while (true) {
				res = scanDelims(c);
				if (res.getNumDelims() >= 2 && res.getCanClose()) {
					_pos += 2;
					inlines.get(delimPos).setTag("Strong");
					inlines.get(delimPos).setC(
							new BlocksContent(inlines.subList(delimPos + 1,
									inlines.size())));
					if (inlines.size() > 1) {
						inlines = new ArrayList<Block>(inlines.subList(0,
								delimPos + 1));
					}
					break;
				} else {
					if (parseInline(inlines) == 0) {
						break;
					}
				}
			}
			return _pos - startPos;
		} else if (numDelims == 3) {
			while (true) {
				res = scanDelims(c);
				if (res.getNumDelims() >= 1 && res.getNumDelims() <= 3
						&& res.getCanClose()
						&& res.getNumDelims() != firstCloseDelims) {
					if (firstCloseDelims == 1 && numDelims > 2) {
						res.setNumDelims(2);
					} else if (firstCloseDelims == 2) {
						res.setNumDelims(1);
					} else if (res.getNumDelims() == 3) {
						res.setNumDelims(1);
					}
					_pos += res.getNumDelims();

					if (firstClose > 0) {
						String tag = firstCloseDelims == 1 ? "Strong" : "Emph";
						inlines.get(delimPos).setTag(tag);

						String temp = firstCloseDelims == 1 ? "Emph" : "Strong";
						ArrayList<Block> blocks = new ArrayList<Block>();
						blocks.add(Block.makeBlock(temp, new BlocksContent(
								inlines.subList(delimPos + 1, firstClose))));
						blocks.addAll(inlines.subList(firstClose + 1,
								inlines.size()));
						inlines.get(delimPos).setC(new BlocksContent(blocks)); // Error
																				// on
																				// 362?
						if (inlines.size() > 1) {
							inlines = new ArrayList<Block>(inlines.subList(0,
									delimPos + 1));
						}
						break;
					} else {
						inlines.add(Block.makeBlock(
								"Str",
								new StringContent(_subject.substring(
										_pos - res.getNumDelims(), _pos))));
					}
				} else {
					if (parseInline(inlines) == 0) {
						break;
					}
				}
			}
			return _pos - startPos;
		} else {
			return 1;
		}
	}

	/**
	 * 
	 * @return NULL if not found
	 */
	public String parseLinkTitle() {
		String title = match(_regex.getLinkTitle());
		if (title != null) {
			return _regex.getUnescape(title.substring(1, title.length() - 1));
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @return NULL if not found
	 */
	public String parseLinkDestination() {
		String res = match(_regex.getLinkDestinationBraces());
		if (res != null) {
			return _regex.getUnescape(res.substring(1, res.length() - 1));
		} else {
			String res2 = match(_regex.getLinkDestination());
			if (res2 != null) {
				return _regex.getUnescape(res2);
			} else {
				return null;
			}
		}
	}

	public int parseLinkLabel() {
		Character peek = peek();
		if (peek == null || peek != '[') {
			return 0;
		}

		int startPos = _pos;
		int nestLevel = 0;
		if (_labelNestLevel > 0) {
			_labelNestLevel--;
			return 0;
		}
		_pos++;

		Character c = peek();

		while (c != null && ((c != ']') || (nestLevel > 0))) {
			switch (c) {
			case '`':
				parseBackticks(new ArrayList<Block>());
				break;
			case '<':
				// TODO: Please help to check this logic
				if (parseAutoLink(new ArrayList<Block>()) == 0) {
					if (parseHtmlTag(new ArrayList<Block>()) == 0) {
						parseString(new ArrayList<Block>());
					}
				}
				break;
			case '[':
				nestLevel++;
				_pos++;
				break;
			case ']':
				nestLevel--;
				_pos++;
				break;
			case '\\':
				parseEscaped(new ArrayList<Block>());
				break;
			default:
				parseString(new ArrayList<Block>());
			}
			c = peek();
		}

		if (c != null && c == ']') {
			_labelNestLevel = 0;
			_pos++;
			return _pos - startPos;
		} else {
			if (c == null) {
				_labelNestLevel = nestLevel;
			}
			_pos = startPos;
			return 0;
		}
	}

	public ArrayList<Block> parseRawLabel(String s) {
		return new InlineParser().parse(s.substring(1, s.length() - 1));
	}

	public int parseLink(ArrayList<Block> inlines) {
		int startPos = _pos;
		int n = parseLinkLabel();

		if (n == 0) {
			return 0;
		}

		int afterLabel = _pos;
		String rawLabel = _subject.substring(startPos, startPos + n);

		Character c = peek();
		if (c != null && c == '(') {
			_pos++;

			if (spnl()) {

				String dest = parseLinkDestination();

				if (dest != null && spnl()) {
					String title = "";
					// Changed to whitespace. Correct?
					if (Character.isWhitespace(_subject.charAt(_pos - 1))) {
						title = parseLinkTitle();
					} else {
						title = "";
					}

					if (spnl() && match("^\\)") != null) {
						inlines.add(Block.makeBlock("Link", dest, title,
								parseRawLabel(rawLabel)));
						return _pos - startPos;
					} else {
						_pos = startPos;
						return 0;
					}
				} else {
					_pos = startPos;
					return 0;
				}
			} else {
				_pos = startPos;
				return 0;
			}
		}

		int savePos = _pos;
		spnl();
		int beforelabel = _pos;
		n = parseLinkLabel();
		String refLabel = "";
		if (n == 2) {
			refLabel = rawLabel;
		} else if (n > 0) {
			refLabel = _subject.substring(beforelabel, beforelabel + n);
		} else {
			_pos = savePos;
			refLabel = rawLabel;
		}

		String normalizedRefLabel = _regex.normalizeReference(refLabel);
		RefMapItem link;
		if (_refMap.containsKey(normalizedRefLabel)) {
			link = _refMap.get(normalizedRefLabel);
		} else {
			link = null;
		}

		if (link != null) {
			String title = "", destination = "";
			if (link.getTitle() != null) {
				title = link.getTitle();
			} else {
				title = "";
			}

			if (link.getDestination() != null) {
				destination = link.getDestination();
			} else {
				destination = "";
			}

			inlines.add(Block.makeBlock("Link", destination, title,
					parseRawLabel(rawLabel)));
			return _pos - startPos;
		} else {
			_pos = startPos;
			return 0;
		}
	}

	public int parseEntity(ArrayList<Block> inlines) {
		// Originally Literal String
		String m = match(
				"^&(?:#x[a-f0-9]{1,8}|#[0-9]{1,8}|[a-z][a-z0-9]{1,31});",
				Pattern.CASE_INSENSITIVE);
		if (m != null) {
			inlines.add(Block.makeBlock("Entity", new StringContent(m)));
			return m.length();
		} else {
			return 0;
		}
	}

	public int parseString(ArrayList<Block> inlines) {
		String m = match(RegexPattern.main_LITERAL, Pattern.MULTILINE);
		if (m != null) {
			inlines.add(Block.makeBlock("Str", new StringContent(m)));
			return m.length();
		} else {
			return 0;
		}
	}

	// TODO:
	// TODO:
	// TODO:
	// TODO:
	// TODO:
	// TODO:
	// TODO:
	// TODO:
	// TODO:
	public int parseNewLine(ArrayList<Block> inlines) {
		if (peek() == '\n') {
			_pos++;
			Block last = inlines.get(inlines.size() - 1);
			// Logic Modified
			if (last != null && last.getTag().equals("Str")) {
				String content = ((StringContent) last.getC()).getContent();
				if (content.length() >= 2
						&& content.substring(content.length() - 2).equals("  ")) {
					String replaceTo = content.replaceAll(" *$", "");
					last.setC(new StringContent(replaceTo));
					inlines.add(Block.makeBlock("Hardbreak"));
				} else if (content.substring(content.length() - 1).equals(" ")) {
					last.setC(new StringContent(content.substring(0,
							content.length() - 1)));
					inlines.add(Block.makeBlock("Softbreak"));
				} else {
					inlines.add(Block.makeBlock("Softbreak"));
				}
			} else {
				inlines.add(Block.makeBlock("Softbreak"));
			}
			return 1;
		} else {
			return 0;
		}
	}

	public int parseImage(ArrayList<Block> inlines) {
		if (match("^!") != null) {
			int n = parseLink(inlines);
			if (n == 0) {
				inlines.add(Block.makeBlock("Str", new StringContent("!")));
				return 1;
				// TODO: Need help
			} else if (inlines.get(inlines.size() - 1) != null
					&& inlines.get(inlines.size() - 1).getTag().equals("Link")) {
				inlines.get(inlines.size() - 1).setTag("Image");
				return n + 1;
			} else {
				throw new RuntimeException("Shouldn't happen");
			}
		} else {
			return 0;
		}
	}

	public int parseReference(String s, HashMap<String, RefMapItem> refMap) {
		_subject = s;
		_pos = 0;
		int startPos = _pos;
		String rawLabel = "";

		int matchChars = parseLinkLabel();
		if (matchChars == 0) {
			return 0;
		} else {
			rawLabel = _subject.substring(0, matchChars);
		}

		char test = peek();
		if (test == ':') {
			_pos++;
		} else {
			_pos = startPos;
			return 0;
		}

		spnl();

		String dest = parseLinkDestination();
		if (dest == null || dest.length() == 0) {
			_pos = startPos;
			return 0;
		}

		int beforeTitle = _pos;
		spnl();
		String title = parseLinkTitle();
		if (title == null) {
			title = "";
			_pos = beforeTitle;
		}

		if (match("^ *(?:\\n|$)") == null) {
			_pos = startPos;
			return 0;
		}

		String normLabel = _regex.normalizeReference(rawLabel);
		if (!refMap.containsKey(normLabel)) {
			refMap.put(normLabel, new RefMapItem(dest, title));
		}

		return _pos - startPos;
	}

	public int parseInline(ArrayList<Block> inlines) {
		Character c = peek();
		int res = 0;
		System.out.println("PEEK: " + c + " " + _pos);

		if (c != null) {
			switch (c) {
			case '\n':
				res = parseNewLine(inlines);
				break;
			case '\\':
				res = parseEscaped(inlines);
				break;
			case '`':
				res = parseBackticks(inlines);
				break;
			case '*':
			case '_':
				res = parseEmphasis(inlines);
				break;
			case '[':
				System.out.println("PARSE LINK@ POS: " + _pos);
				res = parseLink(inlines);
				break;
			case '!':
				res = parseImage(inlines);
				break;
			case '<':
				res = parseAutoLink(inlines);
				if (res == 0) {
					res = parseHtmlTag(inlines);
				}
				break;
			case '&':
				res = parseEntity(inlines);
				break;
			}
		}
		if (res == 0) {
			return parseString(inlines);
		} else {
			return res;
		}
	}

	public ArrayList<Block> parseInlines(String s,
			HashMap<String, RefMapItem> refMap) {
		_subject = s;
		_pos = 0;
		_refMap = refMap;
		ArrayList<Block> inlines = new ArrayList<Block>();
		int code = -1;

		do {
			code = parseInline(inlines);
		} while (code != 0);
		return inlines;
	}

	public ArrayList<Block> parse(String s, HashMap<String, RefMapItem> refMap) {
		return parseInlines(s, refMap);
	}

	public ArrayList<Block> parse(String s) {
		HashMap<String, RefMapItem> refMap = new HashMap<String, RefMapItem>();
		return parseInlines(s, refMap);
	}
}
