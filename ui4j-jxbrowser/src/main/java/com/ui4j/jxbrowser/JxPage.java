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

	private SelectorEngine selectorEngine;

	public JxPage(BrowserView view, Browser browser, SelectorEngine selectorEngine) {
		this.view = view;
		this.browser = browser;
		this.selectorEngine = selectorEngine;
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
		return new JxDocument(browser, selectorEngine);
	}

	@Override
	public Window getWindow() {
		return null;
	}

	@Override
	public void show(boolean maximized) {
		throw new MethodNotSupportedException();
	}

	@Override
	public void show() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}

	@Override
	public void hide() {
		throw new MethodNotSupportedException();
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
		browser.stop();
	}
}
