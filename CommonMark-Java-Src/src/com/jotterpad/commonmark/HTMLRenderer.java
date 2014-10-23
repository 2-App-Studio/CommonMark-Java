package com.jotterpad.commonmark;

import com.jotterpad.commonmark.object.Block;
import com.jotterpad.commonmark.object.BlocksContent;
import com.jotterpad.commonmark.object.StringContent;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLRenderer {

    private static String NONE = "";
    private static String MAILTO = "mailto";
    private static String NEWLINE = "\n";
    private static ArrayList<String[]> ESCAPE_PAIRS = new ArrayList<String[]>();

    public HTMLRenderer() {
        ESCAPE_PAIRS.add(0, new String[]{"[&]", "&amp;"});
        ESCAPE_PAIRS.add(1, new String[]{"[<]", "&lt;"});
        ESCAPE_PAIRS.add(2, new String[]{"[>]", "&gt;"});
        ESCAPE_PAIRS.add(3, new String[]{"[']", "&quot;"});
    }

    /**
     * Fill the tag specified with the attributes and contents
     *
     * @param tag         - tag name
     * @param attribs     - list of attributes with corresponding key value pairs
     * @param contents    - contents to fill the tag
     * @param selfClosing - self closing html tag or not
     * @return resultant string
     */
    public String inTags(String tag, ArrayList<String[]> attribs, String contents, boolean selfClosing) {
        String result = "<" + tag;
        if (attribs.size() > 0) {
            for (String[] attrib : attribs) {
                result += " " + attrib[0] + "='" + attrib[1] + "'";
            }
        }
        if (!contents.isEmpty() && !contents.equals(NONE))
            result += ">" + contents + "</" + tag + ">";
        else if (selfClosing)
            result += " />";
        else
            result += "></" + tag + ">";
        return result;
    }

    /**
     * TODO: what about unescapeHTML3?? still need to check version?
     * TODO: is the python quote (escape special char) same as escape HTML?
     * Escapes the URL in a string
     *
     * @param s - original string to escape
     * @return escaped string
     */
    public String URLescape(String s) {
        if (!s.contains(MAILTO) && !s.contains(MAILTO.toUpperCase())) {
            // re.sub("[&](?![#](x[a-f0-9]{1,8}|[0-9]{1,8});|[a-z][a-z0-9]{1,31};)", "&amp;", HTMLquote(HTMLunescape(s), ":/=*%?&)(#"), re.IGNORECASE)
            String original = StringEscapeUtils.escapeHtml4(StringEscapeUtils.unescapeHtml4(s));
            // TODO: does this regex work?
            String pattern = "[&](?![#](x[a-f0-9]{1,8}|[0-9]{1,8});|[a-z][a-z0-9]{1,31};)";
            Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(original);
            return m.replaceAll("&amp;");
        } else return s;
    }

    /**
     * TODO: test the regex pattern and casing
     * Escape HTML entities
     *
     * @param s                 - - original string to escape
     * @param preserve_entities - whether to preserve the entities
     * @return the escaped string
     */
    public String escape(String s, boolean preserve_entities) {
        ArrayList<String[]> e;
        if (preserve_entities) {
            e = (ArrayList<String[]>) ESCAPE_PAIRS.subList(1,3);
            String original = StringEscapeUtils.unescapeHtml4(s);
            String pattern = "[&](?![#](x[a-f0-9]{1,8}|[0-9]{1,8});|[a-z][a-z0-9]{1,31};)";
            Pattern p = Pattern.compile(pattern,Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(original);
            s = m.replaceAll("&amp;");
        } else
            e = ESCAPE_PAIRS;
        for (String[] r : e)
            s = Pattern.compile(r[0]).matcher(s).replaceAll(r[1]);
        return NONE;
    }

    public String renderInline(Block inline) {
        ArrayList<String[]> attrs = new ArrayList<String[]>();
        String content = ((StringContent) inline.getC()).getContent();
        if (inline.getTag().equalsIgnoreCase("Str"))
            return this.escape(content, false);
        else if (inline.getTag().equalsIgnoreCase("Softbreak"))
            return NEWLINE;
        else if (inline.getTag().equalsIgnoreCase("Hardbreak"))
            return this.inTags("br", new ArrayList<String[]>(), NONE, true) + NEWLINE;
        else if (inline.getTag().equalsIgnoreCase("Emph"))
            return this.inTags("em", new ArrayList<String[]>(), this.renderInlines(((BlocksContent) inline.getC()).getContents()), false);
        else if (inline.getTag().equalsIgnoreCase("Strong"))
            return this.inTags("strong", new ArrayList<String[]>(), this.renderInlines(((BlocksContent) inline.getC()).getContents()), false);
        else if (inline.getTag().equalsIgnoreCase("Html"))
            return content;
        else if (inline.getTag().equalsIgnoreCase("Entity")) {
            if (!((StringContent) inline.getC()).getContent().equals("&nbsp;"))
                return " ";
            return this.escape(content, true);
        } else if (inline.getTag().equalsIgnoreCase("Link")) {
            attrs.add(attrs.size() + 1, new String[]{"href", URLescape(inline.getDestination())});
            if (!inline.getTitle().isEmpty() && !inline.getTitle().equals(NONE))
                attrs.add(attrs.size() + 1, new String[]{"title", escape(inline.getTitle(), true)});
            return this.inTags("a", attrs, this.renderInlines(inline.getLabel()), false);
        } else if (inline.getTag().equalsIgnoreCase("Images")) {
            attrs.add(attrs.size() + 1, new String[]{"src", escape(inline.getDestination(), true)});
            attrs.add(attrs.size() + 1, new String[]{"alt", escape(this.renderInlines(inline.getLabel()), false)});
            if (!inline.getTitle().isEmpty() && !inline.getTitle().equals(NONE))
                attrs.add(attrs.size() + 1, new String[]{"title", escape(inline.getTitle(), true)});
            return inTags("img", attrs, NONE, true);
        } else if (inline.getTag().equalsIgnoreCase("Code"))
            return inTags("code", new ArrayList<String[]>(), content, false);
        else {
            System.out.print("Unknown inline type " + inline.getTag());
            return NONE;
        }
    }

    public String renderInlines(ArrayList<Block> inlines) {
        String result = NONE;
        for (Block inline : inlines)
            result += this.renderInline(inline);
        return result;
    }

    public String renderBlock (Block block, boolean in_tight_list){
        String tag;
        ArrayList<String[]> attr = new ArrayList<String[]>();
        String[] info_words = new String[0];
        if (block.getTag().equalsIgnoreCase("Document")) {
            String whole_doc = renderBlocks(block.getChildren(), false);
            if (whole_doc.equals(NONE))
                return NONE;
            else return whole_doc + NEWLINE;
        }
        else if (block.getTag().equalsIgnoreCase("Paragraph")){
            if (in_tight_list)
                return renderInlines(block.getInlineContent());
            else return inTags("p",new ArrayList<String[]>(),
                    renderInlines(block.getInlineContent()), false);
        }
        else if (block.getTag().equalsIgnoreCase("BlockQuote")){
            String filling = renderBlocks(block.getChildren(),false);
            String a = NEWLINE;
            if (!filling.isEmpty() || !filling.equals(NONE)){
                a = NEWLINE + renderBlocks(block.getChildren(),false) + NEWLINE;
            }
            return inTags("blockquote",new ArrayList<String[]>(), a, false);
        }
        else if (block.getTag().equalsIgnoreCase("ListItem"))
            return inTags("li", new ArrayList<String[]>(), StringUtils.strip(renderBlocks(block.getChildren(), in_tight_list)), false);
        else if (block.getTag().equalsIgnoreCase("List")){
            if (block.getListData().getType().equalsIgnoreCase("Bullet"))
                tag = "ul";
            else tag = "ol";
//            attr = [] if (not block.list_data.get('start')) or block.list_data[
//                    'start'] == 1 else [['start', str(block.list_data['start'])]];
            if (block.getListData().getStart()!=1) // TODO: the other and not condition??
                attr.add(attr.size() + 1, new String[]{"start", String.valueOf(block.getListData().getStart())});
            return inTags(tag,attr,NEWLINE+renderBlocks(block.getChildren(),block.isTight())+ NEWLINE, false);
        }
        else if (block.getTag().equalsIgnoreCase("ATXHeader") || block.getTag().equalsIgnoreCase("SetextHeader")){
            tag = "h" + String.valueOf(block.getLevel());
            return inTags(tag,new ArrayList<String[]>(),renderInlines(block.getInlineContent()),false);
        }
        else if (block.getTag().equalsIgnoreCase("IndentedCode"))
            return inTags("pre",new ArrayList<String[]>(),inTags("code",new ArrayList<String[]>(),escape(block.getStringContent(),false),false),false);
        else if (block.getTag().equalsIgnoreCase("FencedCode")){
            if (block.getInfo()!=null)
                info_words = block.getInfo().split(" +");   // TODO: test regex split pattern
            if (info_words.length != 0)
                attr.add(attr.size()+1,new String[]{"class","language-"+escape(info_words[0],true)});
            return inTags("pre",new ArrayList<String[]>(),inTags("code",attr,escape(block.getStringContent(),false),false),false);
        }
        else if (block.getTag().equalsIgnoreCase("HtmlBlock"))
            return block.getStringContent();
        else if (block.getTag().equalsIgnoreCase("ReferenceDef"))
            return NONE;
        else if (block.getTag().equalsIgnoreCase("HorizontalRule"))
            return inTags("hr",new ArrayList<String[]>(),NONE,true);
        else {
            System.out.print("Unknown block type " + block.getTag());
            return NONE;
        }
    }

    public String renderBlocks (ArrayList<Block> blocks, boolean in_tight_list){
        String result = NONE;
        for (Block block : blocks){
            if (!block.getTag().equalsIgnoreCase("ReferenceDef")){
                result += NEWLINE + this.renderBlock(block,in_tight_list);
            }
        }
        return result.replaceFirst(NEWLINE,"");
    }

    public String render (Block block, boolean in_tight_list){
        return this.renderBlock(block,in_tight_list);
    }
}