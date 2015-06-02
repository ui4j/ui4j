package com.ui4j.ide.action;

import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class AboutAction extends AbstractAction {

    private static final long serialVersionUID = 1322685050838533853L;

    private Component parent;

    public AboutAction(Component parent) {
        this.parent = parent;

        putValue(NAME, "About");
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/com/ui4j/ide/icon/small/help.png")));
        putValue(MNEMONIC_KEY, "A".codePointAt(0));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String version = getClass().getPackage().getImplementationVersion();
        String about = "Ui4j IDE";
        if (version != null && !version.trim().isEmpty()) {
            about += System.lineSeparator() + "Version: " + version; 
        }
        showMessageDialog(parent, about, "About", JOptionPane.INFORMATION_MESSAGE);
    }
}
