package com.ui4j.jxbrowser;

import java.awt.BorderLayout;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import com.ui4j.api.browser.BrowserType;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.SelectorEngine;
import com.ui4j.api.dialog.AlertHandler;
import com.ui4j.api.dialog.ConfirmHandler;
import com.ui4j.api.dialog.PromptHandler;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Window;
import com.ui4j.api.event.DocumentListener;

public class JxPage implements Page {

	private BrowserView view;

	private Browser browser;

	private Document jsDdocument;

	private JFrame frame;

	public JxPage(Browser browser, SelectorEngine selectorEngine) {
		this.browser = browser;
		this.jsDdocument = new JxDocument(browser, selectorEngine);
	}

	@Override
	public Object getEngine() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Object executeScript(String script) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Object getView() {
		return view;
	}

	@Override
	public void addDocumentListener(DocumentListener listener) {
		throw new MethodNotSupportedException();
	}

	@Override
	public void removeListener(DocumentListener listener) {
		throw new MethodNotSupportedException();
	}

	@Override
	public void waitUntilDocReady() {
		throw new MethodNotSupportedException();
	}

	@Override
	public void waitUntilDocReady(int timeout, TimeUnit unit) {
		throw new MethodNotSupportedException();
	}

	@Override
	public void wait(int milliseconds) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Document getDocument() {
		return jsDdocument;
	}

	@Override
	public Window getWindow() {
		return null;
	}

	@Override
	public void show(boolean maximized) {
		view = new BrowserView(browser);

        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        if (maximized) {
        	frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}

	@Override
	public void show() {
		show(false);
	}

	@Override
	public void hide() {
		if (frame != null) {
			frame.setVisible(false);
		}
	}

	@Override
	public void setAlertHandler(AlertHandler handler) {
		throw new MethodNotSupportedException();
	}

	@Override
	public void setPromptHandler(PromptHandler handler) {
		throw new MethodNotSupportedException();
	}

	@Override
	public void setConfirmHandler(ConfirmHandler handler) {
		throw new MethodNotSupportedException();
	}

	@Override
	public BrowserType getBrowserType() {
		return BrowserType.JxBrowser;
	}

	@Override
	public void close() {
		if (frame != null) {
			frame.setVisible(false);
			frame.dispose();
		}
		browser.stop();
		browser.dispose();
	}
}
