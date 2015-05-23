package com.ui4j.jxbrowser.proxy;

import static com.teamdev.jxbrowser.chromium.JSValue.create;
import static com.teamdev.jxbrowser.chromium.JSValue.createNull;
import static java.util.Arrays.asList;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

import com.fasterxml.classmate.Filter;
import com.fasterxml.classmate.MemberResolver;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.ResolvedTypeWithMembers;
import com.fasterxml.classmate.TypeBindings;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.RawMethod;
import com.fasterxml.classmate.members.ResolvedMethod;
import com.teamdev.jxbrowser.chromium.JSFunction;
import com.teamdev.jxbrowser.chromium.JSObject;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.ui4j.jxbrowser.js.JsFunction;
import com.ui4j.jxbrowser.js.JsObject;
import com.ui4j.jxbrowser.js.JsProperty;

public class JsInterceptor {

	private static final TypeResolver RESOLVER = new TypeResolver();

	private JSObject object;

	private Class<?> klass;

	public JsInterceptor(JSObject object, Class<?> klass) {
		this.object = object;
		this.klass = klass;
	}

	@RuntimeType
    public Object execute(@This Object that,
							@Origin Method method,
							@AllArguments Object[] arguments) throws Exception {

		if (method.getDeclaringClass().equals(JsObject.class)) {
			return object;
		}

		if (method.isAnnotationPresent(JsProperty.class)) {
			return handleProperty(method, arguments);
		} else if (method.isAnnotationPresent(JsFunction.class)) {
			return handleFunction(method, arguments);
		}

		return null;
	}

	protected Object handleFunction(Method method, Object[] arguments) {
		String name = method.getAnnotation(JsFunction.class).value();
		JSValue value = object.get(name);
		if (!value.isFunction()) {
			return null;
		}
		JSFunction function = (JSFunction) value;
		JSValue[] jsArguments = new JSValue[arguments.length];
		int i = 0;
		for (Object argument : arguments) {
			JSValue jsArgument = null;
			if (argument instanceof String) {
				jsArgument = create(((String) argument));
			} else if (argument instanceof Boolean) {
				jsArgument = create(((Boolean) argument));
			} else if (argument instanceof Double) {
				jsArgument = create((((Double) argument)));
			} else if (argument instanceof Integer) {
				jsArgument = create((((Integer) argument).intValue()));
			} else if (argument instanceof Float) {
				jsArgument = create((((Float) argument).floatValue()));
			} else if (argument instanceof Short) {
				jsArgument = create((((Short) argument).shortValue()));
			} else if (argument instanceof JSValue) {
				jsArgument = (JSValue) argument;
			}
			jsArguments[i++] = jsArgument;
		}

		JSValue returnValue = function.invokeAndReturnValue(object, jsArguments);

		Class<?> returnType = method.getReturnType();

		if (!void.class.equals(returnType)) {
			if (returnType.isAssignableFrom(List.class)) {
				returnType = getErasedType(method);
			}
			return convert(returnValue, returnType);
		} else {
			return null;
		}
	}

	protected Object handleProperty(Method method, Object[] arguments) {
		Class<?> returnType = method.getReturnType();

		boolean setter = void.class.equals(returnType)
									&& method.getParameterCount() > 0;

		String property = method.getAnnotation(JsProperty.class).value();

		String rightMostProperty = getProperty(arguments, property, setter);
		JSObject target = getTarget(arguments, property);

		if (setter) {
			handleSetter(target, rightMostProperty, arguments);
		} else {
			JSValue returnValue = target.get(rightMostProperty);

			if (returnType.isAssignableFrom(List.class)) {
				returnType = getErasedType(method);
			}

			return convert(returnValue, returnType);
		}

		return null;
	}

	protected String getProperty(Object[] arguments, String property, boolean setter) {
		if (setter) {
			if (arguments.length == 1) {
				return property;
			}
			if (arguments.length == 2) {
				return (String) arguments[0];
			}
		} else {
			if (arguments.length == 0) {
				String[] properties = property.split(".");
				if (properties.length > 0) {
					return properties[properties.length - 1];
				}
			} else {
				return (String) arguments[0];
			}
		}
		return property;
	}

	protected JSObject getTarget(Object[] arguments, String property) {
		JSObject target = object;

		List<String> properties = new ArrayList<>();
		if (property.indexOf(".") > 0) {
			properties.addAll(asList(property.split(".")));
		} else {
			properties.add(property);
		}

		for (String next : properties) {
			JSValue value = target.get(next);
			if (value.isObject() && arguments.length > 0) {
				target = (JSObject) value;
			}
		}

		return target;
	}

	protected void handleSetter(JSObject target, String property, Object[] arguments) {
		Object value = arguments.length == 1 ? arguments[0] : arguments[1];

		if (value instanceof Boolean) {
			target.set(property, create((boolean) value));
		} else if (value instanceof Double) {
			target.set(property, create((double) value));
		} else if (value instanceof Integer) {
			target.set(property, create(((Integer) value).intValue()));
		} else if (value instanceof Short) {
			target.set(property, create(((Short) value).shortValue()));
		}  else if (value instanceof Float) {
			target.set(property, create(((Float) value).floatValue()));
		} else if (value instanceof String) {
			target.set(property, create((String) value));
		} else if (value == null) {
			target.set(property, createNull());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object convert(JSValue value, Class<?> klass) {

		if (value == null ||
					value.isNull() ||
					value.isUndefined()) {
			return null;
		}

		// Handle primitive
		if (value.isBoolean()) {
			return value.getBoolean();
		} else if (value.isFalse()) {
			return false;
		} else if (value.isNumber()) {
			double number = value.getNumber();
			if (short.class.equals(klass) || Short.class.equals(klass)) {
				return (short) number;
			} else if (int.class.equals(klass) || Integer.class.equals(klass)) {
				return (int) number;
			} else if (float.class.equals(klass) || Float.class.equals(klass)) {
				return (float) number;
			} else {
				return number;
			}
		} else if (value.isString()) {
			return value.getString();
		} else if (value.isObject()) {
			JSObject object = (JSObject) value;
				// Handle Object
			Object proxy = new JsProxy(object, klass).get();
			return proxy;
		}

		return null;
	}

	protected Class<?> getErasedType(Method method) {
		ResolvedType resolved = RESOLVER.resolve(klass);
		MemberResolver memberResolver = new MemberResolver(RESOLVER);
		memberResolver.setMethodFilter(new Filter<RawMethod>() {
			@Override
			public boolean include(RawMethod element) {
				return element.getRawMember().equals(method);
			}
		});
		ResolvedTypeWithMembers resolvedMembers = memberResolver.resolve(resolved, null, null);
		ResolvedMethod[] methods = resolvedMembers.getMemberMethods();
		if (methods.length == 1) {
			ResolvedMethod resolvedMethod = methods[0];
			ResolvedType type = resolvedMethod.getType();
			TypeBindings typeBindings = type.getTypeBindings();
			if (!typeBindings.isEmpty()) {
				ResolvedType boundType = typeBindings.getBoundType(0);
				return boundType.getErasedType();
			}
		}
		return null;
	}
}
