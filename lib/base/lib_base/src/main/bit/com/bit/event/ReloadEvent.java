package com.bit.event;

/**
 * author       : frog
 * time         : 2019-07-24 18:24
 * desc         :
 * version      : 1.3.0
 */
public class ReloadEvent {

    private String viewId;

    public ReloadEvent(String viewId) {
        this.viewId = viewId;
    }

    public String getViewId() {
        return viewId;
    }
}
