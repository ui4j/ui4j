package com.ui4j.ide.action;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class OpenFileAction extends AbstractAction {

	private static final long serialVersionUID = -8063122795520833829L;

	public OpenFileAction() {
		putValue(NAME, "Open File...");
		putValue(MNEMONIC_KEY, ".".codePointAt(0));
		int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, mask));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}
}
