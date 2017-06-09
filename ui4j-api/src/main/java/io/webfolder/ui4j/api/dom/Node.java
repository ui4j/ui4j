package io.webfolder.ui4j.api.dom;

import java.util.List;

public interface Node extends EventTarget {

    List<Element> getChildren();

    boolean hasChildNodes();

    boolean isHtmlElement();

    boolean isEqualNode(Node node);

    boolean isSameNode(Node node);

    Document getDocument();
}
