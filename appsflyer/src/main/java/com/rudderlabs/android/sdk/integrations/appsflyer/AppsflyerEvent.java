package com.rudderlabs.android.sdk.integrations.appsflyer;

import java.util.Map;

class AppsflyerEvent {
    private String eventType;
    private Map<String, Object> eventProps;

    public AppsflyerEvent(String eventType) {
        this.eventType = eventType;
    }

    public AppsflyerEvent(String eventType, Map<String, Object> eventProps) {
        this.eventType = eventType;
        this.eventProps = eventProps;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Map<String, Object> getEventProps() {
        return eventProps;
    }

    public void setEventProps(Map<String, Object> eventProps) {
        this.eventProps = eventProps;
    }

    public void setEventProps(String key, Object value) {
        this.eventProps.put(key, value);
    }
}
