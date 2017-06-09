package io.webfolder.ui4j.api.util;

import io.webfolder.ui4j.api.dom.Element;
import io.webfolder.ui4j.api.dom.Node;

public abstract class NodeVisitor {

    private Node root;

    public NodeVisitor(Node root) {
        if (root == null) {
            return;
        }
        this.root = root;
    }

    public void walk() {
        walk(root);
    }

    public abstract void visit(Node child);

    private void walk(Node node) {
        for (Element next : node.getChildren()) {
            if (next.hasChildNodes()) {
                walk(next);
            }
            visit(next);
        }
    }
}
