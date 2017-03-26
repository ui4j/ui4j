package com.ui4j.spi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ui4j.api.dom.EventTarget;
import com.ui4j.api.event.EventHandler;

@SuppressWarnings("unchecked")
public class NativeEventManager implements EventManager {

    private PageContext context;

    public NativeEventManager(PageContext context) {
        this.context = context;
    }

    @Override
    public void bind(EventTarget target, String event, EventHandler handler) {
        if (target != null && List.class.isAssignableFrom(target.getClass())) {
            context.getEventRegistrar().register(target, event, handler);
            Map<String, Object> map = new HashMap<>();
            map.put("event", event);
            map.put("listener", handler);
            List<Map<String, Object>> events = (List<Map<String, Object>>) target.getProperty("events");
            if (events == null || "undefined".equals(events.toString().trim())) {
                target.setProperty("events", events = new ArrayList<>(1));
            }
            events.add(map);
        }
    }

    public void unbind(EventTarget target) {
        Object eventObject = target.getProperty("events");
        if (eventObject == null || "undefined".equals(eventObject.toString().trim())) {
            return;
        }
        if (List.class.isAssignableFrom(eventObject.getClass())) {
            List<Map<String, Object>> events = (List<Map<String, Object>>) eventObject;
            for (Map<String, Object> next : events) {
                String event = next.get("event").toString();
                EventHandler handler = (EventHandler) next.get("listener");
                context.getEventRegistrar().unregister(target, event, handler);
            }
            events.clear();
            target.removeProperty("events");
        }
    }

    @Override
    public void unbind(EventTarget target, String event) {
        Object eventObject = target.getProperty("events");
        if (eventObject == null || "undefined".equals(eventObject.toString().trim())) {
            return;
        }
        List<Map<String, Object>> events = (List<Map<String, Object>>) eventObject;
        List<Map<String, Object>> founds = new ArrayList<>();
        for (Map<String, Object> next : events) {
            String nextEvent = next.get("event").toString();
            if (nextEvent.equals(event)) {
                founds.add(next);
                EventHandler handler = (EventHandler) next.get("listener");
                context.getEventRegistrar().unregister(target, nextEvent, handler);
            }
        }
        events.removeAll(founds);
    }

    @Override
    public void unbind(EventTarget target, EventHandler handler) {
        Object eventObject = target.getProperty("events");
        if (eventObject == null || "undefined".equals(eventObject.toString().trim())) {
            return;
        }
        List<Map<String, Object>> events = (List<Map<String, Object>>) eventObject;
        Map<String, Object> found = Collections.<String, Object>emptyMap();
        for (Map<String, Object> next : events) {
            EventHandler nextHandler = (EventHandler) next.get("listener");
            if (nextHandler.equals(handler)) {
                found = next;
                String nextEvent = next.get("event").toString();
                context.getEventRegistrar().unregister(target, nextEvent, handler);
            }
        }
        events.remove(found);
    }
}
