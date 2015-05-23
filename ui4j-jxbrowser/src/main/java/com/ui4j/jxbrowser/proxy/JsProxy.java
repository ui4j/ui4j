package com.ui4j.jxbrowser.proxy;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static net.bytebuddy.matcher.ElementMatchers.not;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Default;
import net.bytebuddy.implementation.MethodDelegation;

import com.teamdev.jxbrowser.chromium.JSObject;
import com.ui4j.api.util.Ui4jException;

public class JsProxy <T> {

	private T instance;

	@SuppressWarnings("unchecked")
	public JsProxy(JSObject object, Class<T> klass) {
		try {
			instance = (T) new ByteBuddy()
									.subclass(Object.class)
									.implement(klass)
									.method(any()
									.and(not(isDeclaredBy(Object.class))))
									.intercept(MethodDelegation.to(new JsInterceptor(object, klass)))
									.make()
									.load(JsProxy.class.getClassLoader(), Default.WRAPPER)
									.getLoaded()
									.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new Ui4jException(e);
		}
	}

	public T get() {
		return instance;
	}
}
