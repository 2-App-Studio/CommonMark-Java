# Commonmark for Java
This is a pure Java port of jgm's [commonmark.js](https://github.com/jgm/commonmark.js), a Markdown parser and renderer for the [CommonMark specification](http://spec.commonmark.org/).

# Usage
```java
	DocParser parser = new DocParser(false);
	HTMLRenderer renderer = new HTMLRenderer(null);
	Block block = parser.parse("# Put Markdown here");
	String html = renderer.render(block);
	System.out.println(html);
```

# Project(s)
This library is currently used in **JotterPad 11** on **Android**. 

![JotterPad CommonMark](https://lh3.ggpht.com/GU8KdcQv4DKrQ2-rvvToRHRQ5R6bf1Q2SyvxxVU0jVr5dW40NJVqsm_RNy8hf0C57c4=w80)

You can download the app from the [Google Play Store](https://play.google.com/store/apps/details?id=com.jotterpad.x) or vist the [website](http://2appstudio.com/jotterpad).


# Contributors
* Yap Wei Rong (Kenny)
* Tan Zhi Rong
