package io.webfolder.ui4j.api.dom;

public interface DataHolder {

    void removeProperty(String key);

    Object getProperty(String key);

    void setProperty(String key, Object value);
}
