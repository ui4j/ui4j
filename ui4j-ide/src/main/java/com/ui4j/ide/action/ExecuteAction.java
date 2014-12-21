package com.ui4j.ide.action;

import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.script.Bindings;
import javax.script.SimpleBindings;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.ui4j.api.browser.Page;
import com.ui4j.ide.EditorManager;
import com.ui4j.ide.PageManager;
import com.ui4j.ide.ScriptManager;

public class ExecuteAction extends AbstractAction {

	private static final long serialVersionUID = -8282610682521986612L;

	private PageManager pageManager;

	private EditorManager editorManager;

	private ScriptManager scriptManager;


	public ExecuteAction(PageManager pageManager, EditorManager editorManager, ScriptManager scriptManager) {
		this.pageManager = pageManager;
		this.editorManager = editorManager;
		this.scriptManager = scriptManager;

		putValue(NAME, "Execute");
		int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, mask));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Page page = pageManager.getActivePage();
		if (page == null) {
			return;
		}
		Bindings bindings = createBindings(page);
		String text = getText();
		if (text == null) {
			return;
		}
		execute(text, bindings);
	}

	protected Object execute(String text, Bindings bindings) {
		try {
			return scriptManager.execute(text, bindings);
		} catch (Throwable ex) {
			showMessageDialog(null, ex.getMessage(), "Execution Error", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	protected String getText() {
		String selection = editorManager.getSelection();
		String text = null;
		if (selection != null && !selection.trim().isEmpty()) {
			text = selection;
		} else {
			text = editorManager.getText();
		}

		if (text.trim().isEmpty()) {
			return null;
		}
		return text;
	}

	protected Bindings createBindings(Page page) {
		Bindings bindings = new SimpleBindings();
		bindings.put("page", page);
		bindings.put("document", page.getDocument());
		bindings.put("body", page.getDocument().getBody());
		bindings.put("window", page.getWindow());
		return bindings;
	}
}
