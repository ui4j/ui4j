package com.ui4j.ide;

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
		ScriptEngine engine = this.scriptEngine.get();
		engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
		try {
			return engine.eval(script);
		} catch (ScriptException e) {
			throw new RuntimeException(e);
		}
	}
}
