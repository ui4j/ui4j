package com.ui4j.ide.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.ui4j.ide.EditorManager;
import com.ui4j.ide.FileManager;
import com.ui4j.ide.UIUtils;

public class AbstractSaveAction extends AbstractAction {

	private static final long serialVersionUID = 8702930647091407509L;

	private Component parent;

	private EditorManager editorManager;

	private FileManager fileManager;

	public AbstractSaveAction(Component parent, EditorManager editorManager, FileManager fileManager) {
		this.parent = parent;
		this.editorManager = editorManager;
		this.fileManager = fileManager;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		File selectedFile = showSaveDialog();
		if (selectedFile == null) {
			return;
		}
		save(selectedFile);
	}

	protected void save(File file) {
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(editorManager.getText());
		} catch (IOException ex) {
			JLabel label = new JLabel("<html>" + String.valueOf(ex.getMessage()) + "</html>");
			label.setPreferredSize(UIUtils.getPreferredSize(String.valueOf(ex.getMessage()), true, 400));
			JOptionPane.showMessageDialog(parent, label, "Inspect Value", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	protected File showSaveDialog() {
		JFileChooser chooser = new JFileChooser(new File(".")) {

			private static final long serialVersionUID = 7353347982505742908L;

			@Override
			public void approveSelection() {
				File f = getSelectedFile();
				if (f.exists() && getDialogType() == SAVE_DIALOG) {
					int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?", "Existing file", JOptionPane.YES_NO_CANCEL_OPTION);
					switch (result) {
					case JOptionPane.YES_OPTION:
						super.approveSelection();
						return;
					case JOptionPane.NO_OPTION:
						return;
					case JOptionPane.CLOSED_OPTION:
						return;
					case JOptionPane.CANCEL_OPTION:
						cancelSelection();
						return;
					}
				}
				super.approveSelection();
			}
		};
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileFilter(new FileNameExtensionFilter("Javascript", "js"));
		int result = chooser.showSaveDialog(parent);
		if (result != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		File selectedFile = chooser.getSelectedFile();
		if (selectedFile.isDirectory()) {
			return null;
		}
		return selectedFile;
	}

	protected FileManager getFileManager() {
		return fileManager;
	}
}
