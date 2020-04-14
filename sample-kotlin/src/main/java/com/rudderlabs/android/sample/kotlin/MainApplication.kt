package com.rudderlabs.android.sample.kotlin

import android.app.Application
import com.appsflyer.AppsFlyerLib
import com.rudderstack.android.integrations.appsflyer.AppsFlyerIntegrationFactory
import com.rudderstack.android.sdk.core.RudderClient
import com.rudderstack.android.sdk.core.RudderConfig
import com.rudderstack.android.sdk.core.RudderLogger

class MainApplication : Application() {
    companion object {
        private const val WRITE_KEY = "1ZDlYiFCh4wwGZc2o0Ua8pDYDLy"
        private const val DATA_PLANE_URL = "https://a0a95e46.ngrok.io"
        lateinit var rudderClient: RudderClient
    }

    override fun onCreate() {
        super.onCreate()
        rudderClient = RudderClient.getInstance(
            this,
            WRITE_KEY,
            RudderConfig.Builder()
                .withDataPlaneUrl(DATA_PLANE_URL)
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