package com.ui4j.jxbrowser.proxy;

import static java.util.Collections.synchronizedMap;
import static net.bytebuddy.matcher.ElementMatchers.any;
import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static net.bytebuddy.matcher.ElementMatchers.not;

import java.util.Map;
import java.util.WeakHashMap;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.ClassLoadingStrategy.Default;
import net.bytebuddy.instrumentation.MethodDelegation;

import com.teamdev.jxbrowser.chromium.JSObject;
import com.ui4j.api.util.Ui4jException;

public class JsProxy {

    private static final Map<Class<?>, Class<?>> PROXIES = synchronizedMap(new WeakHashMap<>());

    @SuppressWarnings("unchecked")
    public static <T> T to(JSObject object, Class<T> klass) {
        if (!klass.isInterface()) {
            throw new Ui4jException("Class must be interface");
        }
        Class<?> proxyKlass = PROXIES.get(klass);
        if (proxyKlass == null) {
            proxyKlass = new ByteBuddy()
                                .subclass(Object.class)
                                .implement(klass)
                                .method(any()
                                .and(not(isDeclaredBy(Object.class))))
                                .intercept(MethodDelegation.to(new JsInterceptor(object, klass)))
                                .make()
                                .load(JsProxy.class.getClassLoader(), Default.WRAPPER)
                                .getLoaded();
        }
        Object instance = null;
        try {
            instance = proxyKlass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new Ui4jException(e);
        }
        return (T) instance;
    }
}
