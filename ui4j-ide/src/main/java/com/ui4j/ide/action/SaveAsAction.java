package com.ui4j.ide.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import com.ui4j.ide.EditorManager;

public class SaveAsAction extends AbstractSaveAction {

	private static final long serialVersionUID = -3724420288262935505L;

	public SaveAsAction(Component parent, EditorManager editorManager) {
		super(parent, editorManager);

		putValue(NAME, "Save As...");
		putValue(MNEMONIC_KEY, "A".codePointAt(0));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		File file = showSaveDialog();
		if (file != null) {
			save(file);
		}
	}
}
