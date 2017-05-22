Ui4j
====
[![Gitter](https://badges.gitter.im/Join Chat.svg)](https://gitter.im/ui4j/ui4j?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![License](http://img.shields.io/badge/license-MIT-blue.svg)](http://opensource.org/licenses/MIT) [![Coverage Status](https://img.shields.io/coveralls/ui4j/ui4j.svg)](https://coveralls.io/r/ui4j/ui4j?branch=master)

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

Ui4j is released under the terms of the MIT License (MIT).

You are free to use Ui4j or any of its constituent parts in any other project (even commercial projects) so long as its copyright headers are left intact.

Stability
---------

This library is suitable for use in production systems.

Integration with Maven
----------------------

To use the official release of Ui4j, please use the following snippet in your pom.xml

Add the following to your POM's `<repositories>` tag:

```xml
<repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
</repository>
```

then add the following dependency:

```xml
<dependency>
    <groupId>com.github.ui4j.ui4j</groupId>
    <artifactId>ui4j-all</artifactId>
    <version>2.2.5</version>
</dependency>
```

Using Ui4j without Maven
------------------------
If you use Ui4j without Maven, download [pre built jar file](https://jitpack.io/com/github/ui4j/ui4j/ui4j-all/2.2.5/ui4j-all-2.2.5.jar).


Supported Platforms
-------------------

Ui4j has been tested under Windows 10 but should work on any platform where a Java 8 JRE or JDK is available.


Headless Mode
-------------

Ui4j can be run in "headless" mode using [Xfvb](http://en.wikipedia.org/wiki/Xvfb) or with using [Monocle](https://wiki.openjdk.java.net/display/OpenJFX/Monocle).

**Headless Mode with Xfvb**

Sample configuration for ubuntu running with *headless* mode:

1. First install these packages:
```sudo apt-get install xvfb x11-xkb-utils libxrender-dev libxtst-dev libgtk2.0-0```

2. Then start the xvfb
```nohup xvfb :99 -ac &```

3. Then export the DISPLAY variable
```export DISPLAY=:99.0```

**Headless Mode with Monocle**

1. Download and add the maven dependency of latest [openjfx-monocle](https://github.com/ui4j/ui4j/blob/master/ui4j-webkit/lib/openjfx-monocle-8u76-b04.jar?raw=true)
2. Add **-Dui4j.headless** Java system parameter from command line or with using api ```System.setProperty("ui4j.headless", "true");```

Logging
-------
Both simple logger for java (SLF4J) and Java utility logger (JUL) is supported.
If slf4j is available on classpath com.ui4j.api.util.LoggerFactory use slf4j else java utility logger is used.

CSS Selector Engine
-------------------
Ui4j use W3C selector engine which is default selector engine of WebKit. Alternatively [Sizzle](http://http://sizzlejs.com) selector engine might be used.
Sizzle is the css selector engine of JQuery and it supports extra selectors like _:has(div)_, _:text_, _contains(text)_ etc.
Check the [Sizzle.java](https://github.com/ui4j/ui4j/blob/master/ui4j-sample/src/main/java/com/ui4j/sample/Sizzle.java) for using sizzle with Ui4j.


Usage Examples
--------------

Here is a very basic sample program that uses Ui4j to display a web page with a "hello, world!" message. See the [ui4j-sample](https://github.com/ui4j/ui4j/tree/master/ui4j-sample/src/main/java/com/ui4j/sample) project for more sample code snippets.

```java
package com.ui4j.sample;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;

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
package com.ui4j.sample;

import static com.ui4j.api.browser.BrowserFactory.getWebKit;

import com.ui4j.api.browser.Page;

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
mvn install

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
--------------------------------------------------------------------------------------------

![WebFolder](https://raw.githubusercontent.com/webfolderio/cdp4j/master/images/logo.png)

ui4j is an MIT licensed open source project and completely free to use. However, the amount of effort needed to maintain and develop new features for the project is not sustainable without proper financial backing. You can support ui4j development by 
**buying** support package. Please [contact us](https://webfolder.io/support) for support packages & pricing.

