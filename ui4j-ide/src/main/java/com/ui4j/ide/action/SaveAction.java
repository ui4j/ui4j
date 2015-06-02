package com.ui4j.ide.action;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import com.ui4j.ide.EditorManager;
import com.ui4j.ide.FileManager;

public class SaveAction extends AbstractSaveAction {

    private static final long serialVersionUID = 8702930647091407509L;

    public SaveAction(Component parent, EditorManager editorManager, FileManager fileManager) {
        super(parent, editorManager, fileManager);

        putValue(NAME, "Save");
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/com/ui4j/ide/icon/small/save.png")));
        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, mask));
        putValue(MNEMONIC_KEY, "s".codePointAt(0));
    }

    @Override
    protected void save(File file) {
        super.save(file);
        if (file != null) {
            getFileManager().setCurrentFile(file);
        }
    }

    @Override
    protected File showSaveDialog() {
        File file = getFileManager().getCurrentFile();
        if (file == null) {
            return super.showSaveDialog();
        } else {
            return file;
        }
    }
}
