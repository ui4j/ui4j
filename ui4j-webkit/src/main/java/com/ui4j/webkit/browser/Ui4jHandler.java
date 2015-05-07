package com.ui4j.webkit.browser;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Map;

import com.ui4j.api.interceptor.Interceptor;
import com.ui4j.api.interceptor.Request;

public class Ui4jHandler extends URLStreamHandler {

	private String context;

	private static final String UI4J_PROTOCOL = "ui4j";

	private Interceptor interceptor;
	
	private URLConnection contextConnection;

	private CookieHandler cookieHandler;

	public Ui4jHandler(Interceptor interceptor) {
		this.interceptor = interceptor;
	}

	@Override
	protected URLConnection openConnection(URL u) throws IOException {
		String protocol = u.getProtocol();

		if (!protocol.startsWith(UI4J_PROTOCOL)) {
			return null;
		}

		// url without ui4j prefix
		String url = u.toString().substring(protocol.length() + 1, u.toString().length());


		if (!url.startsWith("/") && context != null && !context.endsWith("/")) {
			String f = u.getFile().replaceAll("https://", "");
			url = context + "/" + f;
		}

		boolean isContext = false;
		if (context == null && url.startsWith("http")) {
			context = url;
			isContext = true;
		}

		URLConnection connection = new URL(url).openConnection();

		if (isContext) {
			contextConnection = connection;
		}

		Request request = new Request();
		interceptor.beforeLoad(url, request);
		if (request != null) {
			for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
				connection.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		
		return connection;
	}

	public URLConnection getConnection() {
		return contextConnection;
	}

	public CookieHandler getCookieHandler() {
		return cookieHandler;
	}
}
