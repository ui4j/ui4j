package com.ui4j.ide;

import javax.script.Bindings;

public interface ScriptManager {

    Object execute(String script, Bindings bindings);
}
