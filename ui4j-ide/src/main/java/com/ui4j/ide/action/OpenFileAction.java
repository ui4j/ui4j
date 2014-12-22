package com.ui4j.ide.action;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.ui4j.ide.EditorManager;
import com.ui4j.ide.FileManager;
import com.ui4j.ide.UIUtils;

public class OpenFileAction extends AbstractAction {

	private static final long serialVersionUID = -8063122795520833829L;

	private Component parent;

	private EditorManager editorManager;

	private FileManager fileManager;

	public OpenFileAction(Component parent, EditorManager editorManager, FileManager fileManager) {
		this.parent = parent;

		this.editorManager = editorManager;
		this.fileManager = fileManager;

		putValue(NAME, "Open File...");
		putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/com/ui4j/ide/icon/small/open.png")));
		putValue(MNEMONIC_KEY, ".".codePointAt(0));
		int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, mask));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser(new File("."));
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileFilter(new FileNameExtensionFilter("Javascript", "js"));
		int result = chooser.showOpenDialog(parent);
		if (result != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File selectedFile = chooser.getSelectedFile();
		if (selectedFile == null) {
			return;
		}
		if (selectedFile.isDirectory()) {
			return;
		}
		fileManager.setCurrentFile(selectedFile);
		List<String> lines = Collections.emptyList();
		try {
			lines = Files.readAllLines(selectedFile.toPath());
		} catch (IOException ex) {
			JLabel label = new JLabel("<html>" + String.valueOf(ex.getMessage()) + "</html>");
			label.setPreferredSize(UIUtils.getPreferredSize(String.valueOf(ex.getMessage()), true, 400));
			JOptionPane.showMessageDialog(parent, label, "Inspect Value", JOptionPane.INFORMATION_MESSAGE);
		}
		StringBuilder builder = new StringBuilder();
		for (String line : lines) {
			builder.append(line);
			builder.append(System.lineSeparator());
		}
		editorManager.setText(builder.toString());
	}
}
