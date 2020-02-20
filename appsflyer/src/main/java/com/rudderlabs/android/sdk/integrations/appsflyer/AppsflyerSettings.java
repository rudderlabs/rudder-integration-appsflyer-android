package com.rudderlabs.android.sdk.integrations.appsflyer;

import com.rudderlabs.android.sdk.core.RudderIntegrationSettings;

public class AppsflyerSettings extends RudderIntegrationSettings {
    private String token;

    public AppsflyerSettings(String token) {
        this.token = token;
    }

    public static AppsflyerSettings getWithToken(String token) {
        return new AppsflyerSettings(token);
    }

    @Override
    public String getToken() {
        return token;
    }
}
