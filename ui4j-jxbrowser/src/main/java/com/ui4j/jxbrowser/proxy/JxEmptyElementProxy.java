package com.ui4j.jxbrowser.proxy;

import static net.bytebuddy.instrumentation.MethodDelegation.to;
import static net.bytebuddy.matcher.ElementMatchers.any;
import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static net.bytebuddy.matcher.ElementMatchers.not;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.ClassLoadingStrategy.Default;
import net.bytebuddy.instrumentation.method.bytecode.bind.annotation.Origin;
import net.bytebuddy.instrumentation.method.bytecode.bind.annotation.RuntimeType;
import net.bytebuddy.instrumentation.method.bytecode.bind.annotation.SuperCall;

import com.ui4j.api.dom.Element;
import com.ui4j.api.util.Point;
import com.ui4j.api.util.Ui4jException;
import com.ui4j.jxbrowser.JxElement;
import com.ui4j.jxbrowser.js.JsElement;

public class JxEmptyElementProxy {

    private Element emptyElement;

    private Point emptyPoint = new Point();

    protected static class JxEmptyElementInterceptor {

        private JxEmptyElementProxy proxy;

        public JxEmptyElementInterceptor(
                JxEmptyElementProxy jxEmptyElementProxy) {
            this.proxy = jxEmptyElementProxy;
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

    public JxEmptyElementProxy() {
        Class<?> loaded = new ByteBuddy()
                            .subclass(JxElement.class)
                            .method(any().and(not(isDeclaredBy(Object.class))))
                            .intercept(to(new JxEmptyElementInterceptor(this)))
                            .make()
                            .load(JxEmptyElementProxy.class.getClassLoader(), Default.WRAPPER).getLoaded();
        Constructor<?> constructor = null;
        try {
            constructor = loaded.getConstructor(new Class[] { JsElement.class });
        } catch (NoSuchMethodException | SecurityException e) {
            throw new Ui4jException(e);
        }
        try {
            emptyElement = (Element) constructor.newInstance(new Object[] { null });
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
