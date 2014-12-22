package com.ui4j.ide;

import java.util.concurrent.CountDownLatch;

import javafx.application.Platform;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class DefaultScriptManager implements ScriptManager {

	private ScriptEngineManager manager = new ScriptEngineManager();

	private ThreadLocal<ScriptEngine> scriptEngine = new ThreadLocal<ScriptEngine>() {

		@Override
		protected ScriptEngine initialValue() {
			return manager.getEngineByName("javascript");
		}
	};

	@Override
	public Object execute(String script, Bindings bindings) {
		Object[] result = new Object[1];
		CountDownLatch latch = new CountDownLatch(1);
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				ScriptEngine engine = scriptEngine.get();
				engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
				try {
					result[0] = engine.eval(script);
					latch.countDown();
				} catch (ScriptException e) {
					throw new RuntimeException(e);
				}
			}
		});
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result[0];
	}
}
