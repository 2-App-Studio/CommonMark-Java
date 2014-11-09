# Commonmark for Java
This is a pure Java port of jgm's [stmd.js](https://github.com/jgm/stmd), a Markdown parser and renderer for the [CommonMark specification](http://jgm.github.io/stmd/spec.html) (version 2).

# Usage
```java
	DocParser parser = new DocParser();
	HTMLRenderer renderer = new HTMLRenderer(null);
	Block block = parser.parse("# Put Markdown here");
	String html = renderer.render(block);
	System.out.println(html);
```

# Please Note
This project is still in development.

# Contributors
* Yap Wei Rong (Kenny)
* Tan Zhi Rong