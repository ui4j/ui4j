package com.ui4j.ide.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class ExitAction extends AbstractAction {

	private static final long serialVersionUID = -8063122795520833829L;

	public ExitAction() {
		putValue(NAME, "Exit");
		putValue(MNEMONIC_KEY, "x".codePointAt(0));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}
}
