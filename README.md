# What is RudderStack?

[RudderStack](https://rudderstack.com/) is a **customer data pipeline** tool for collecting, routing and processing data from your websites, apps, cloud tools, and data warehouse.

More information on RudderStack can be found [here](https://github.com/rudderlabs/rudder-server).

## Integrating AppsFlyer with RudderStack's Android SDK

1. Add [AppsFlyer](https://www.appsflyer.com) as a destination in the [Dashboard](https://app.rudderlabs.com/) and define ```devKey```

2. Add these lines to your ```app/build.gradle```
```
repositories {
    maven { url "https://dl.bintray.com/rudderstack/rudderstack" }
}
```
3. Add the dependency under ```dependencies```
```
implementation 'com.rudderstack.android.sdk:core:[1.2.1,)'
implementation 'com.rudderstack.android.integration:appsflyer:[1.0.4,)'

// appsflyer dependencies
implementation 'com.appsflyer:af-android-sdk:6.4.3'
implementation 'com.android.installreferrer:installreferrer:1.1.1'  // for attribution
```

## Initialize ```RudderClient```

```
val rudderClient: RudderClient = RudderClient.getInstance(
    this,
    <WRITE_KEY>,
    RudderConfig.Builder()
        .withDataPlaneUrl(<DATA_PLANE_URL>)
        .withFactory(AppsFlyerIntegrationFactory.FACTORY)
        .build()
)
```

## How to get AppsFlyer ID

Use this snippet after initialising `RudderClient`
```
rudderClient.onIntegrationReady("AppsFlyer") {
    val appsflyerId = AppsFlyerLib.getInstance().getAppsFlyerUID(this)
    println("=====================================================")
    println("AppsFlyer ID: $appsflyerId")
    println("=====================================================")
}
```

## Send Events

Follow the steps from the [RudderStack Android SDK](https://github.com/rudderlabs/rudder-sdk-android).

## Contact Us

If you come across any issues while configuring or using this integration, please feel free to start a conversation on our [Slack](https://resources.rudderstack.com/join-rudderstack-slack) channel. We will be happy to help you.
