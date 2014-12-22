package com.ui4j.ide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;

public class ConsolePipe implements Runnable {

	private JEditorPane displayPane;

	private BufferedReader reader;

	public ConsolePipe(JEditorPane displayPane, PipedOutputStream pos) {
		this.displayPane = displayPane;

		try {
			PipedInputStream pis = new PipedInputStream(pos);
			reader = new BufferedReader(new InputStreamReader(pis));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				try {
					displayPane.getDocument().insertString(
							displayPane.getDocument().getLength(),
							line + System.lineSeparator(),
							new SimpleAttributeSet());
				} catch (BadLocationException e) {
					throw new RuntimeException(e);
				}
				displayPane.setCaretPosition(displayPane.getDocument()
						.getLength());
			}
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "Error redirecting output : "
					+ ioe.getMessage());
		}
	}

	public static void redirectOutput(JEditorPane displayPane) {
		ConsolePipe.redirectOut(displayPane);
		ConsolePipe.redirectErr(displayPane);
	}

	public static void redirectOut(JEditorPane displayPane) {
		PipedOutputStream pos = new PipedOutputStream();
		System.setOut(new PrintStream(pos, true));
		ConsolePipe console = new ConsolePipe(displayPane, pos);
		new Thread(console).start();
	}

	public static void redirectErr(JEditorPane displayPane) {
		PipedOutputStream pos = new PipedOutputStream();
		System.setErr(new PrintStream(pos, true));
		ConsolePipe console = new ConsolePipe(displayPane, pos);
		new Thread(console).start();
	}
}
