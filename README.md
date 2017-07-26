Ui4j
====

[![License](http://img.shields.io/badge/license-AGPL-blue.svg)](https://github.com/webfolderio/ui4j/blob/master/LICENSE) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/3e29f77ded47488f9ae4a1f609def42f)](https://www.codacy.com/app/WebFolder/ui4j?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=webfolderio/ui4j&amp;utm_campaign=Badge_Grade)
[![circleci](https://img.shields.io/circleci/project/github/webfolderio/ui4j.svg?label=linux)](https://circleci.com/gh/webfolderio/ui4j)
[![CLA assistant](https://cla-assistant.io/readme/badge/webfolderio/ui4j)](https://cla-assistant.io/webfolderio/ui4j)

Ui4j is a web-automation library for Java. It is a thin wrapper library around the JavaFx WebKit Engine, and can be used for automating the use of web pages and for testing web pages.

cdp4j (Automation for Chrome & Chromium)
=============================
Use [cdp4j](https://github.com/webfolderio/cdp4j) Java library if you need to automate  Chrome or Chromium based browsers.

Supported Java Versions
-----------------------

Oracle Java 8.

Both the JRE and the JDK are suitable for use with this library.


Licensing
---------

Ui4j is licensed as [AGPL](https://github.com/webfolderio/ui4j/blob/master/LICENSE) software.

Buying a license is __mandatory__ as soon as you develop commercial activities distributing the
ui4j software inside your product or deploying it on a network without disclosing the source code of your own applications under the AGPL license.

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
    <version>3.1.0</version>
</dependency>
```

Download
--------
[ui4j-webkit-3.1.0.jar](https://search.maven.org/remotecontent?filepath=io/webfolder/ui4j-webkit/3.0.0/ui4j-webkit-3.0.0.jar) - 394 KB

[ui4j-webkit-3.1.0-sources.jar](https://search.maven.org/remotecontent?filepath=io/webfolder/ui4j-webkit/3.0.0/ui4j-webkit-3.0.0-sources.jar) - 198 KB


Supported Platforms
-------------------

Ui4j has been tested under Windows 10 but should work on any platform where a Java 8 JRE or JDK is available.


Headless Mode
-------------

Ui4j can be run in "headless" mode using [Xfvb](http://en.wikipedia.org/wiki/Xvfb) or with using [Monocle](https://wiki.openjdk.java.net/display/OpenJFX/Monocle).

**Headless Mode with Monocle**

1. [Download](https://search.maven.org/remotecontent?filepath=org/testfx/openjfx-monocle/8u76-b04/openjfx-monocle-8u76-b04.jar) or add [maven dependency](https://search.maven.org/#artifactdetails%7Corg.testfx%7Copenjfx-monocle%7C8u76-b04%7Cjar) of the latest openjfx-monocle.
2. Add **-Dui4j.headless** Java system parameter from command line or with using api ```System.setProperty("ui4j.headless", "true");```

Logging
-------
Both simple logger for java (SLF4J) and Java utility logger (JUL) is supported.
If slf4j is available on classpath io.webfolder.ui4j.api.util.LoggerFactory use slf4j else java utility logger is used.

CSS Selector Engine
-------------------
Ui4j use W3C selector engine which is default selector engine of WebKit. Alternatively [Sizzle](http://http://sizzlejs.com) selector engine might be used.
Sizzle is the css selector engine of JQuery and it supports extra selectors like _:has(div)_, _:text_, _contains(text)_ etc.
Check the [Sizzle.java](https://github.com/ui4j/ui4j/blob/master/ui4j-sample/src/main/java/com/ui4j/sample/Sizzle.java) for using sizzle with Ui4j.


Usage Examples
--------------

Here is a very basic sample program that uses Ui4j to display a web page with a "hello, world!" message. See the [ui4j-sample](https://github.com/ui4j/ui4j/tree/master/ui4j-sample/src/main/java/com/ui4j/sample) project for more sample code snippets.

```java
package io.webfolder.ui4j.sample;

import io.webfolder.ui4j.api.browser.BrowserEngine;
import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.Page;

public class HelloWorld {

    public static void main(String[] args) {
        // get the instance of the webkit
        BrowserEngine browser = BrowserFactory.getWebKit();

        // navigate to blank page
        Page page = browser.navigate("about:blank");

        // show the browser page
        page.show();

        // append html header to the document body
        page.getDocument().getBody().append("<h1>Hello, World!</h1>");
    }
}
```

Here is another sampe code that list all front page news from Hacker News.

```java
package io.webfolder.ui4j.sample;

import static io.webfolder.ui4j.api.browser.BrowserFactory.getWebKit;

import io.webfolder.ui4j.api.browser.Page;

public class HackerNews {

    public static void main(String[] args) {

        try (Page page = getWebKit().navigate("https://news.ycombinator.com")) {
            page
                .getDocument()
                .queryAll(".title a")
                .forEach(e -> {
                    System.out.println(e.getText().get());
                });
        }
    }
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

See [UserAgent.java](https://github.com/ui4j/ui4j/blob/master/ui4j-sample/src/main/java/com/ui4j/sample/UserAgent.java) sample.

#### How can i execute javascript?

See [JavaScriptExecution.java](https://github.com/ui4j/ui4j/blob/master/ui4j-sample/src/main/java/com/ui4j/sample/JavaScriptExecution.java) sample.

#### How can i handle browser login, prompt or confirmation dialog?

See [DialogTest.java](https://github.com/ui4j/ui4j/blob/master/ui4j-webkit/src/test/java/com/ui4j/test/DialogTest.java) for custom handlers or
use default handlers from [Dialogs.java](https://github.com/ui4j/ui4j/blob/master/ui4j-api/src/main/java/com/ui4j/api/dialog/Dialogs.java).

#### What is the easiest way clear all input elements?

Use [clear](https://github.com/ui4j/ui4j/blob/master/ui4j-api/src/main/java/com/ui4j/api/dom/Form.java#L13) method of the Form class.

Getting Help
------------

![WebFolder](https://raw.githubusercontent.com/webfolderio/cdp4j/master/images/logo.png)

ui4j is an AGPL licensed open source project and completely free to use. However, the amount of effort needed to maintain and develop new features for the project is not sustainable without proper financial backing. You can support ui4j development by 
**buying** support package. Please [contact us](https://webfolder.io/support) for support packages & pricing.

