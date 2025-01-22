package net.sf.jdnp3.dnp3.stack.layer.application.service;

public interface OutstationApplicationStateHandler {
    void dataLinkStarted(String name);
    void dataLinkStopped(String name);
}
