package io.webfolder.ui4j.api.dom;

public interface Window {

    Document getDocument();

    String getLocation();

    void setLocation(String location);

    void back();

    void forward();

    void reload();
}
