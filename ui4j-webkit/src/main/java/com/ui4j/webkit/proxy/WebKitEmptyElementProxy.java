package com.ui4j.webkit.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;

import org.w3c.dom.Node;

import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Element;
import com.ui4j.api.util.Point;
import com.ui4j.api.util.Ui4jException;
import com.ui4j.spi.PageContext;
import com.ui4j.webkit.dom.WebKitElement;
import com.ui4j.webkit.spi.WebKitJavaScriptEngine;

public class WebKitEmptyElementProxy {

	private Element emptyElement;

	private Point emptyPoint = new Point();

    protected static class WebKitEmptyElementInterceptor {

		private WebKitEmptyElementProxy proxy;

    	public WebKitEmptyElementInterceptor(
				WebKitEmptyElementProxy webKitEmptyElementProxy) {
    		this.proxy = webKitEmptyElementProxy;
		}

		@RuntimeType
        public Object execute(@SuperCall Callable<Object> callable, @Origin Method method) {
    		Class<?> retType = method.getReturnType();
    		String name = method.getName();
    		if ("isEmpty".equals(name)) {
    			return true;
    		} else if (Element.class.isAssignableFrom(retType)) {
    			return proxy.getEmptyElement();
    		} else if (List.class.isAssignableFrom(retType)) {
    			return Collections.emptyList();
    		} else if (Point.class.isAssignableFrom(retType)) {
    			return proxy.getEmptyPoint();
    		} else if (boolean.class.isAssignableFrom(retType)) {
    			return false;
    		} else if (String.class.isAssignableFrom(retType)) {
    			return "";
    		} else if (float.class.isAssignableFrom(retType)) {
    			return 0f;
    		} else {
    			return null;
    		}
        }
    }

	public WebKitEmptyElementProxy() {
		Class<?> loaded = new ByteBuddy()
				.subclass(WebKitElement.class)
				.method(ElementMatchers.any().and(ElementMatchers.not(ElementMatchers.isDeclaredBy(Object.class))))
				.intercept(MethodDelegation.to(new WebKitEmptyElementInterceptor(this)))
				.make()
				.load(WebKitProxy.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER).getLoaded();
		Constructor<?> constructor = null;
		try {
			constructor = loaded.getConstructor(new Class[] {
			    	Node.class, Document.class,
			        PageContext.class, WebKitJavaScriptEngine.class });
		} catch (NoSuchMethodException | SecurityException e) {
			throw new Ui4jException(e);
		}
		try {
			emptyElement = (Element) constructor.newInstance(new Object[] { null, null, null, null });
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			throw new Ui4jException(e);
		}
	}

	public Element getEmptyElement() {
		return emptyElement;
	}

	public Point getEmptyPoint() {
		return emptyPoint;
	}
}
