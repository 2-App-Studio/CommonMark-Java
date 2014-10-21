package com.jotterpad.commonmark.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexPattern {

	public static final String ESCAPABLE = "[!\"#$%&\'()*+,./:;<=>?@\\[\\\\\\]^_`{|}~-]";
	public static final String ESCAPED_CHAR = "\\\\" + ESCAPABLE;
	public static final String IN_DOUBLE_QUOTES = "\"(" + ESCAPED_CHAR
			+ "|[^\"\\x00])*\"";
	public static final String IN_SINGLE_QUOTES = "'(" + ESCAPED_CHAR
			+ "|[^'\\x00])*'";
	public static final String IN_PARENS = "\\((" + ESCAPED_CHAR
			+ "|[^)\\x00])*\\)";
	public static final String REG_CHAR = "[^\\\\()\\x00-\\x20]";
	public static final String IN_PARENS_NOSP = "\\((" + REG_CHAR + "|"
			+ ESCAPED_CHAR + ")*\\)";
	public static final String TAGNAME = "[A-Za-z][A-Za-z0-9]*";
	public static final String BLOCKTAGNAME = "(?:article|header|aside|hgroup|iframe|blockquote|hr|body|li|map|button|object|canvas|ol|caption|output|col|p|colgroup|pre|dd|progress|div|section|dl|table|td|dt|tbody|embed|textarea|fieldset|tfoot|figcaption|th|figure|thead|footer|footer|tr|form|ul|h1|h2|h3|h4|h5|h6|video|script|style)";
	public static final String ATTRIBUTENAME = "[a-zA-Z_:][a-zA-Z0-9:._-]*";
	public static final String UNQUOTEDVALUE = "[^\"'=<>`\\x00-\\x20]+";
	public static final String SINGLEQUOTEDVALUE = "'[^']*'";
	public static final String DOUBLEQUOTEDVALUE = "\"[^\"]*\"";
	public static final String ATTRIBUTEVALUE = "(?:" + UNQUOTEDVALUE + "|"
			+ SINGLEQUOTEDVALUE + "|" + DOUBLEQUOTEDVALUE + ")";
	public static final String ATTRIBUTEVALUESPEC = "(?:" + "\\s*=" + "\\s*"
			+ ATTRIBUTEVALUE + ")";
	public static final String ATTRIBUTE = "(?:" + "\\s+" + ATTRIBUTENAME
			+ ATTRIBUTEVALUESPEC + "?)";
	public static final String OPENTAG = "<" + TAGNAME + ATTRIBUTE + "*"
			+ "\\s*/?>";
	public static final String CLOSETAG = "</" + TAGNAME + "\\s*[>]";
	public static final String OPENBLOCKTAG = "<" + BLOCKTAGNAME + ATTRIBUTE
			+ "*" + "\\s*/?>";
	public static final String CLOSEBLOCKTAG = "</" + BLOCKTAGNAME + "\\s*[>]";
	public static final String HTMLCOMMENT = "<!--([^-]+|[-][^-]+)*-->";
	public static final String PROCESSINGINSTRUCTION = "[<][?].*?[?][>]";
	public static final String DECLARATION = "<![A-Z]+" + "\\s+[^>]*>";
	public static final String CDATA = "<!\\[CDATA\\[([^\\]]+|\\][^\\]]|\\]\\][^>])*\\]\\]>";
	public static final String HTMLTAG = "(?:" + OPENTAG + "|" + CLOSETAG + "|"
			+ HTMLCOMMENT + "|" + PROCESSINGINSTRUCTION + "|" + DECLARATION
			+ "|" + CDATA + ")";
	public static final String HTMLBLOCKOPEN = "<(?:" + BLOCKTAGNAME
			+ "[\\s/>]" + "|" + "/" + BLOCKTAGNAME + "[\\s>]" + "|" + "[?!])";

	public static final String main_LITERAL = "^(?:[\\n`\\[\\]\\\\!<&*_]|[^\\n`\\[\\]\\\\!<&*_]+)";

	private static RegexPattern _instance;
	private static Pattern _htmlTag, _htmlBlockOpen, _linkTitle,
			_linkDestinationBraces, _linkDestination, _escapable, _escapedChar,
			_allTab, _hRule_LITERAL;
	private static String _allEscapedChar;

	private RegexPattern() {
		_htmlTag = Pattern.compile('^' + HTMLTAG, Pattern.CASE_INSENSITIVE);
		_htmlBlockOpen = Pattern.compile('^' + HTMLBLOCKOPEN,
				Pattern.CASE_INSENSITIVE);
		_escapable = Pattern.compile(ESCAPABLE);

		_linkTitle = Pattern.compile("^(?:\"(" + ESCAPED_CHAR
				+ "|[^\"\\x00])*\"" + "|" + "'(" + ESCAPED_CHAR
				+ "|[^'\\x00])*'" + "|" + "\\((" + ESCAPED_CHAR
				+ "|[^)\\x00])*\\))");
		_linkDestinationBraces = Pattern.compile("^(?:[<](?:[^<>\\n\\\\\\x00]"
				+ "|" + ESCAPED_CHAR + "|" + "\\\\)*[>])");
		_linkDestination = Pattern.compile("^(?:" + REG_CHAR + "+|"
				+ ESCAPED_CHAR + "|" + IN_PARENS_NOSP + ")*");
		_escapedChar = Pattern.compile("^\\\\(" + ESCAPABLE + ")");
		_allTab = Pattern.compile("\t");
		_hRule_LITERAL = Pattern
				.compile("^(?:(?:\\* *){3,}|(?:_ *){3,}|(?:- *){3,}) *$");
		_allEscapedChar = "\\\\(" + ESCAPABLE + ")";
	}

	public static RegexPattern getInstance() {
		if (_instance == null) {
			_instance = new RegexPattern();
		}
		return _instance;
	}

	public Pattern getHtmlTag() {
		return _htmlTag;
	}

	public Pattern getHtmlBlockOpen() {
		return _htmlBlockOpen;
	}

	public Pattern getLinkTitle() {
		return _linkTitle;
	};

	public Pattern getLinkDestinationBraces() {
		return _linkDestinationBraces;
	}

	public Pattern getLinkDestination() {
		return _linkDestination;
	}

	public Pattern getEscapable() {
		return _escapable;
	}

	public String getAllEscapabledChar() {
		return _allEscapedChar;
	}

	public Pattern getEscapabledChar() {
		return _escapedChar;
	}

	public Pattern getAllTab() {
		return _allTab;
	}

	public Pattern getHRule() {
		return _hRule_LITERAL;
	}

	public String getUnescape(String s) {
		return s.replaceAll(_allEscapedChar, "$1");
	}

	public boolean isBlank(String s) {
		return Pattern.matches("^\\s*$", s);
	}

	public String normalizeReference(String s) {
		return s.trim().replaceAll("\\s+", " ").toUpperCase();
	}

	/**
	 * 
	 * @param pattern
	 * @param s
	 * @param offset
	 * @return -1 if not found
	 */
	public int matchAt(Pattern pattern, String s, int offset) {
		String subStr = s.substring(offset);
		Matcher matcher = pattern.matcher(subStr);
		if (matcher.find()) {
			return offset + subStr.indexOf(matcher.group(0));
		} else {
			return -1;
		}
	}

	public String deTabLine(String text) {
		if (Pattern.matches("\t", text) && text.indexOf('\t') == -1) {
			return text;
		} else {
			// TODO: Python noob here. Please help here!
			return text.replace("\t", "    ");
		}
	}
}
