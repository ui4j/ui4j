package com.ui4j.ide.action;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import com.ui4j.ide.EditorManager;

public class SaveAction extends AbstractSaveAction {

	private static final long serialVersionUID = 8702930647091407509L;

	public SaveAction(Component parent, EditorManager editorManager) {
		super(parent, editorManager);

		putValue(NAME, "Save");
		putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/com/ui4j/ide/icon/small/save.png")));
		int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, mask));
		putValue(MNEMONIC_KEY, "s".codePointAt(0));
	}
}
