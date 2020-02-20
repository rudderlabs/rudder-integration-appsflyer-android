package com.rudderlabs.android.sdk.integrations.appsflyer;

import android.app.Application;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.rudderlabs.android.sdk.core.*;

import java.util.HashMap;
import java.util.Map;

public class AppsflyerIntegration extends RudderIntegrationFactory<AppsFlyerLib> implements AppsFlyerConversionListener {
    private RudderClient rudderClient;
    private Application application;

    public AppsflyerIntegration(String token) {
        this(AppsflyerSettings.getWithToken(token));
    }

    public AppsflyerIntegration(AppsflyerSettings settings) {
        super(settings);
    }

    @Override
    public void init(Application application, RudderClient client, RudderConfig config) {
        this.rudderClient = client;
        this.application = application;
        AppsFlyerLib.getInstance().init(getSettings().getToken(), this, application);
        AppsFlyerLib.getInstance().startTracking(application);
    }

    @Override
    public void identify(RudderElement element) {
        processInAppEvents(element);
    }

    @Override
    public void page(RudderElement element) {
        processInAppEvents(element);
    }

    @Override
    public void screen(RudderElement element) {
        processInAppEvents(element);
    }

    @Override
    public void track(RudderElement element) {
        processInAppEvents(element);
    }

    private void processInAppEvents(RudderElement element) {
        AppsflyerEvent appsFlyerEvent = AppsflyerEventProcessor.processRudderEvents(element);
        if (appsFlyerEvent != null) {
            AppsFlyerLib.getInstance()
                    .trackEvent(
                            application,
                            appsFlyerEvent.getEventType(),
                            appsFlyerEvent.getEventProps()
                    );
        }
    }

    @Override
    public String key() {
        return "AF";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public AppsFlyerLib getInstance() {
        return AppsFlyerLib.getInstance();
    }

    private Map<String, Object> props;

    @Override
    public Map<String, Object> getDestinationProps(RudderElement element) {
        if (props == null) {
            props = new HashMap<>();
            props.put("rl_af_uid", AppsFlyerLib.getInstance().getAppsFlyerUID(application));
        }
        return props;
    }

    @Override
    public void onInstallConversionDataLoaded(Map<String, String> map) {
        Map<String, Object> eventMap = new HashMap<>();
        for (String key : map.keySet()) {
            eventMap.put(key, eventMap.get(key));
        }
        rudderClient.track(new RudderElementBuilder().setEventName("InstallConversionDataLoaded").setProperty(eventMap).build(), false);
    }

    @Override
    public void onInstallConversionFailure(String s) {
        // ignored
    }

    @Override
    public void onAppOpenAttribution(Map<String, String> map) {
        Map<String, Object> eventMap = new HashMap<>();
        for (String key : map.keySet()) {
            eventMap.put(key, eventMap.get(key));
        }
        rudderClient.track(new RudderElementBuilder().setEventName("AppOpenAttribution").setProperty(eventMap).build(), false);
    }

    @Override
    public void onAttributionFailure(String s) {
        // ignored
    }
}
