package io.webfolder.ui4j.spi;

import io.webfolder.ui4j.api.dom.Node;
import io.webfolder.ui4j.api.util.NodeVisitor;

public class NodeUnbindVisitor extends NodeVisitor {

    private PageContext context;

    public NodeUnbindVisitor(PageContext context, Node root) {
        super(root);
        this.context = context;
    }

    @Override
    public void visit(Node child) {
        if (child != null && child.isHtmlElement()) {
            context.getEventManager().unbind(child);
        }
    }
}
