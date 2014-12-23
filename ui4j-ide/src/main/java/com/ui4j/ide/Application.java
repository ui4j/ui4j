package com.ui4j.ide;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.Enumeration;
import java.util.Locale;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.ide.action.AboutAction;
import com.ui4j.ide.action.ExecuteAction;
import com.ui4j.ide.action.ExitAction;
import com.ui4j.ide.action.InspectAction;
import com.ui4j.ide.action.OpenFileAction;
import com.ui4j.ide.action.SaveAction;
import com.ui4j.ide.action.SaveAsAction;

public class Application extends JFrame implements PageManager, EditorManager, FileManager, Runnable {

	private static final long serialVersionUID = 2401407900556583752L;

	private Page currentPage;

	private RSyntaxTextArea area;

	private JFXPanel fxPanel;

	private ScriptManager scriptManager = new DefaultScriptManager();

	private Ui4jSplitPane splitPane;

	private File currentFile;

	private JEditorPane console;

	public Application() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Ui4j");

		JMenuBar menubar = new JMenuBar();
		menubar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic('F');
		JMenu menuRun = new JMenu("Run");
		menuRun.setMnemonic('R');

		menubar.add(menuFile);
		menubar.add(menuRun);

		menuFile.add(new OpenFileAction(this, this, this));
		menuFile.addSeparator();
		menuFile.add(new SaveAction(this, this, this));
		menuFile.add(new SaveAsAction(this, this, this));
		menuFile.addSeparator();
		menuFile.add(new ExitAction());
		
		menuRun.add(new ExecuteAction(this, this, this, scriptManager));
		menuRun.add(new InspectAction(this, this, this, scriptManager));

		JMenu menuHelp = new JMenu("Help");
		menuHelp.setMnemonic('H');
		JMenuItem about = new JMenuItem(new AboutAction(this));
		menuHelp.add(about);

		menubar.add(menuHelp);

		setLayout(new BorderLayout());

		setJMenuBar(menubar);

		splitPane = new Ui4jSplitPane();

		splitPane.setBorder(BorderFactory.createEmptyBorder());

		area = new RSyntaxTextArea(120, 40);
		area.setBorder(BorderFactory.createEmptyBorder());
		area.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
		RTextScrollPane rpane = new RTextScrollPane(area);
		rpane.setBorder(BorderFactory.createEmptyBorder());
		rpane.setMinimumSize(new Dimension(400, 400));

		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		for (Font font : env.getAllFonts()) {
			if (font.getName().equals("Consolas")) {
				area.setFont(Font.decode("Consolas-PLAIN-12"));
				break;
			}
		}

		splitPane.setLeftComponent(rpane);

		fxPanel = new JFXPanel();
		
		Platform.runLater(this);

		setLayout(new BorderLayout());
		
		add(splitPane, BorderLayout.CENTER);

		console = new JEditorPane();
		console.setPreferredSize(new Dimension(400, 100));
		console.setBackground(new Color(0xEEEEEE));

		ConsolePipe.redirectErr(console);
		ConsolePipe.redirectOut(console);
		ConsolePipe.redirectOutput(console);

		JScrollPane consoleScroll = new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
											JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		consoleScroll.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xCCCCCC)));
		add(consoleScroll, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
	}

	@Override
	public Page getActivePage() {
		return currentPage;
	}

	@Override
	public String getText() {
		return area.getText();
	}

	@Override
	public String getSelection() {
		return area.getSelectedText();
	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		String os = System.getProperty("os.name");
		if (os.toLowerCase(Locale.ENGLISH).contains("windows")) {
			UIManager.put("MenuItem.border", BorderFactory.createEmptyBorder());
			UIManager.put("Menu.border", BorderFactory.createEmptyBorder());
			UIManager.put("PopupMenu.border", BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0xCCCCCC)));
			Font font = Font.decode("Segoe UI-Plain-12");
			Enumeration<Object> keys = UIManager.getDefaults().keys();
			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();
				Object value = UIManager.get (key);
				if (value != null && value instanceof javax.swing.plaf.FontUIResource)
					UIManager.put (key, font);
			}			
		}
        UIManager.put("Ui4jSplitPane.background", new Color(0xCCCCCC));
		Application application = new Application();
		application.pack();
		application.setVisible(false);
	}

	@Override
	public void run() {
		BrowserEngine webkit = BrowserFactory.getWebKit();
		currentPage = webkit.navigate("about:blank");
		WebView view = (WebView) currentPage.getView();
		BorderLayout layout = new BorderLayout();
		fxPanel.setLayout(layout);
		fxPanel.setBorder(BorderFactory.createEmptyBorder());
		fxPanel.setScene(new Scene(view));
		splitPane.setRightComponent(fxPanel);
		setVisible(true);
	}

	@Override
	public void setText(String text) {
		area.setText(text);
	}

	@Override
	public File getCurrentFile() {
		return currentFile;
	}

	@Override
	public void setCurrentFile(File file) {
		this.currentFile = file;
	}

	@Override
	public void focus() {
		area.requestFocus();
	}
}
