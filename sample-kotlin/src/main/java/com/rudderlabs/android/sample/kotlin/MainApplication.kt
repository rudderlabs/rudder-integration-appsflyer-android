package com.rudderlabs.android.sample.kotlin

import android.app.Application
import com.appsflyer.AppsFlyerLib
import com.rudderstack.android.integrations.appsflyer.AppsFlyerIntegrationFactory
import com.rudderstack.android.sdk.core.RudderClient
import com.rudderstack.android.sdk.core.RudderConfig
import com.rudderstack.android.sdk.core.RudderLogger
import com.appsflyer.AFLogger;


class MainApplication : Application() {
    companion object {
        private const val WRITE_KEY = "1pAKRv50y15Ti6UWpYroGJaO0Dj"
        private const val DATA_PLANE_URL = "https://dd86-175-101-36-4.ngrok.io"
        lateinit var rudderClient: RudderClient
    }

    override fun onCreate() {
        super.onCreate()
        
        AppsFlyerLib.getInstance().init("tZGiwrAUq8xLuNYb99q2VT", null, this);
        AppsFlyerLib.getInstance().setLogLevel(AFLogger.LogLevel.VERBOSE);
        AppsFlyerLib.getInstance().setDebugLog(true);
        AppsFlyerLib.getInstance().start(this);
        rudderClient = RudderClient.getInstance(
            this,
            BuildConfig.WRITE_KEY,
            RudderConfig.Builder()
                .withDataPlaneUrl(BuildConfig.DATA_PLANE_URL)
                .withFactory(AppsFlyerIntegrationFactory.FACTORY)
                .withLogLevel(RudderLogger.RudderLogLevel.DEBUG)
                .build()
        )

        rudderClient.onIntegrationReady("AppsFlyer") {
            val appsflyerId = AppsFlyerLib.getInstance().getAppsFlyerUID(this)
            println("=====================================================")
            println("AppsFlyer ID: $appsflyerId")
            println("=====================================================")
        }
    }
}