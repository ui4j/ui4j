Ui4j
====

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/webfolderio/ui4j/blob/master/LICENSE)
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fwebfolderio%2Fui4j.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2Fwebfolderio%2Fui4j?ref=badge_shield)

Ui4j is a web-automation library for Java. It is a thin wrapper library around the JavaFx WebKit Engine, and can be used for automating the use of web pages and for testing web pages.

cdp4j (Automation for Chrome & Chromium)
=============================
Use [cdp4j](https://github.com/webfolderio/cdp4j) Java library if you need to automate  Chrome or Chromium based browsers.

Supported Java Versions
-----------------------

Oracle & OpenJDK Java 8 and 11.

Both the JRE and the JDK are suitable for use with this library.

Licensing
---------

Ui4j is licensed as [MIT](https://github.com/webfolderio/ui4j/blob/master/LICENSE) software.

Stability
---------

This library is suitable for use in production systems.

Integration with Maven
----------------------

To use the official release of ui4j, please use the following snippet in your pom.xml file.

Add the following to your POM's <dependencies> tag:

```xml
<dependency>
    <groupId>io.webfolder</groupId>
    <artifactId>ui4j-webkit</artifactId>
    <version>4.0.0</version>
</dependency>
```

Download
--------
[ui4j-webkit-4.0.0.jar](https://search.maven.org/remotecontent?filepath=io/webfolder/ui4j-webkit/4.0.0/ui4j-webkit-4.0.0.jar) - 387 KB

[ui4j-webkit-4.0.0-sources.jar](https://search.maven.org/remotecontent?filepath=io/webfolder/ui4j-webkit/4.0.0/ui4j-webkit-4.0.0-sources.jar) - 196 KB


Supported Platforms
-------------------

Ui4j has been tested under Windows 10 but should work on any platform where a Java 8 or Java 11 is available.


Headless Mode
-------------

Ui4j can be run in "headless" mode using [Monocle](https://wiki.openjdk.java.net/display/OpenJFX/Monocle).

1. Add [maven dependency](https://github.com/TestFX/Monocle) of the latest openjfx-monocle.
2. Add **-Dui4j.headless** Java system parameter from command line or with using api ```System.setProperty("ui4j.headless", "true");```

Logging
-------
Both simple logger for java (SLF4J) and Java utility logger (JUL) is supported.
If slf4j is available on classpath `io.webfolder.ui4j.api.util.LoggerFactory` use slf4j else java utility logger is used.

CSS Selector Engine
-------------------
Ui4j use W3C selector engine which is default selector engine of WebKit. Alternatively [Sizzle](http://http://sizzlejs.com) selector engine might be used.
Sizzle is the css selector engine of JQuery and it supports extra selectors like _:has(div)_, _:text_, _contains(text)_ etc.
Check the [Sizzle.java](https://github.com/webfolderio/ui4j/blob/master/ui4j-sample/src/main/java/io/webfolder/ui4j/sample/Sizzle.java) for using sizzle with Ui4j.

Usage Examples
--------------

Here is a very basic sample program that uses Ui4j to display a web page with a "hello, world!" message. See the [ui4j-sample](https://github.com/webfolderio/ui4j/tree/master/ui4j-sample/src/main/java/io/webfolder/ui4j/sample) project for more sample code snippets.

```java
// get the instance of the webkit
BrowserEngine browser = BrowserFactory.getWebKit();
// navigate to blank page
Page page = browser.navigate("about:blank");
// show the browser page
page.show();
// append html header to the document body
page.getDocument().getBody().append("<h1>Hello, World!</h1>");
```

Here is another sampe code that list all front page news from Hacker News.

```java
try (Page page = getWebKit().navigate("https://news.ycombinator.com")) {
    page
        .getDocument()
        .queryAll(".title a")
        .forEach(e -> {
            System.out.println(e.getText());
        });
}
```

Building Ui4j
-------------

```bash
mvn package
```

FAQ
---

#### How can i set the user agent string?

See [UserAgent.java](https://github.com/webfolderio/ui4j/blob/master/ui4j-sample/src/main/java/io/webfolder/ui4j/sample/UserAgent.java) sample.

#### How can i execute javascript?

See [JavaScriptExecution.java](https://github.com/webfolderio/ui4j/blob/master/ui4j-sample/src/main/java/io/webfolder/ui4j/sample/JavaScriptExecution.java) sample.

#### How can i handle browser login, prompt or confirmation dialog?

See [DialogTest.java](https://github.com/webfolderio/ui4j/blob/master/ui4j-webkit/src/test/java/io/webfolder/ui4j/test/DialogTest.java) for custom handlers or
use default handlers from [Dialogs.java](https://github.com/webfolderio/ui4j/blob/master/ui4j-api/src/main/java/io/webfolder/ui4j/api/dialog/Dialogs.java).

#### What is the easiest way clear all input elements?

Use [clear](https://github.com/webfolderio/ui4j/blob/master/ui4j-api/src/main/java/io/webfolder/ui4j/api/dom/Form.java#L13) method of the Form class.

[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fwebfolderio%2Fui4j.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2Fwebfolderio%2Fui4j?ref=badge_large)
