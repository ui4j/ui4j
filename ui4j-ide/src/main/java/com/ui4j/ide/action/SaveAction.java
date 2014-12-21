package com.ui4j.ide.action;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class SaveAction extends AbstractAction {

	private static final long serialVersionUID = 8702930647091407509L;

	public SaveAction() {
		putValue(NAME, "Save");
		int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, mask));		
		putValue(MNEMONIC_KEY, "s".codePointAt(0));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}
}
