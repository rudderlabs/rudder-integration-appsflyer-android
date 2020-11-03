package com.rudderstack.android.integrations.appsflyer;

import android.text.TextUtils;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AFLogger;
import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.rudderstack.android.sdk.core.MessageType;
import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderIntegration;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderMessage;
import com.rudderstack.android.sdk.core.ecomm.ECommerceEvents;
import com.rudderstack.android.sdk.core.ecomm.ECommerceParamNames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AppsFlyerIntegrationFactory extends RudderIntegration<AppsFlyerLib> implements AppsFlyerConversionListener {
    private static final String APPSFLYER_KEY = "AppsFlyer";

    public static RudderIntegration.Factory FACTORY = new Factory() {
        @Override
        public RudderIntegration<?> create(Object settings, RudderClient client, RudderConfig config) {
            return new AppsFlyerIntegrationFactory(settings, config);
        }

        @Override
        public String key() {
            return APPSFLYER_KEY;
        }
    };

    private AppsFlyerIntegrationFactory(Object config, RudderConfig rudderConfig) {
        Map<String, Object> destConfig = (Map<String, Object>) config;
        if (destConfig != null && destConfig.containsKey("devKey")) {
            String appsFlyerKey = (String) destConfig.get("devKey");
            if (!TextUtils.isEmpty(appsFlyerKey)) {
                AppsFlyerLib.getInstance().init(appsFlyerKey, this, RudderClient.getApplication());
                AppsFlyerLib.getInstance().setLogLevel(
                        rudderConfig.getLogLevel() >= RudderLogger.RudderLogLevel.DEBUG ?
                                AFLogger.LogLevel.VERBOSE : AFLogger.LogLevel.NONE);
                AppsFlyerLib.getInstance().startTracking(RudderClient.getApplication());
            }
        }
    }

    private void processEvents(RudderMessage message) {
        String eventType = message.getType();
        String afEventName;
        Map<String, Object> afEventProps = new HashMap<>();
        if (eventType != null) {
            switch (eventType) {
                case MessageType.TRACK:
                    String eventName = message.getEventName();
                    if (eventName != null) {
                        Map<String, Object> property = message.getProperties();
                        if (property != null) {
                            switch (eventName) {
                                case ECommerceEvents.PRODUCTS_SEARCHED:
                                    if (property.containsKey(ECommerceParamNames.QUERY)) {
                                        afEventProps.put(AFInAppEventParameterName.SEARCH_STRING, property.get("query"));
                                    }
                                    afEventName = AFInAppEventType.SEARCH;
                                    break;
                                case ECommerceEvents.PRODUCT_VIEWED:
                                    if (property.containsKey(ECommerceParamNames.PRICE))
                                        afEventProps.put(AFInAppEventParameterName.PRICE, property.get(ECommerceParamNames.PRICE));
                                    if (property.containsKey(ECommerceParamNames.PRODUCT_ID))
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_ID, property.get(ECommerceParamNames.PRODUCT_ID));
                                    if (property.containsKey(ECommerceParamNames.CATEGORY))
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_TYPE, property.get(ECommerceParamNames.CATEGORY));
                                    if (property.containsKey(ECommerceParamNames.CURRENCY))
                                        afEventProps.put(AFInAppEventParameterName.CURRENCY, property.get(ECommerceParamNames.CURRENCY));
                                    afEventName = AFInAppEventType.CONTENT_VIEW;
                                    break;
                                case ECommerceEvents.PRODUCT_LIST_VIEWED:
                                    if (property.containsKey(ECommerceParamNames.CATEGORY)) {
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_TYPE, property.get(ECommerceParamNames.CATEGORY));
                                    }
                                    if (property.containsKey(ECommerceParamNames.PRODUCTS)) {
                                        ArrayList<String> products = new ArrayList<>();
                                        for (Map<String, Object> product : ((ArrayList<Map<String, Object>>) property.get(ECommerceParamNames.PRODUCTS))) {
                                            if (product.containsKey("product_id")) {
                                                products.add((String) product.get("product_id"));
                                            }
                                        }
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_LIST, products.toArray());
                                    }
                                    afEventName = "af_list_view";
                                    break;
                                case ECommerceEvents.PRODUCT_ADDED_TO_WISH_LIST:
                                    if (property.containsKey(ECommerceParamNames.PRICE))
                                        afEventProps.put(AFInAppEventParameterName.PRICE, property.get(ECommerceParamNames.PRICE));
                                    if (property.containsKey(ECommerceParamNames.PRODUCT_ID))
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_ID, property.get(ECommerceParamNames.PRODUCT_ID));
                                    if (property.containsKey(ECommerceParamNames.CATEGORY))
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_TYPE, property.get(ECommerceParamNames.CATEGORY));
                                    if (property.containsKey(ECommerceParamNames.CURRENCY))
                                        afEventProps.put(AFInAppEventParameterName.CURRENCY, property.get(ECommerceParamNames.CURRENCY));
                                    afEventName = AFInAppEventType.ADD_TO_WISH_LIST;
                                    break;
                                case ECommerceEvents.PRODUCT_ADDED:
                                    if (property.containsKey(ECommerceParamNames.PRICE))
                                        afEventProps.put(AFInAppEventParameterName.PRICE, property.get(ECommerceParamNames.PRICE));
                                    if (property.containsKey(ECommerceParamNames.PRODUCT_ID))
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_ID, property.get(ECommerceParamNames.PRODUCT_ID));
                                    if (property.containsKey(ECommerceParamNames.CATEGORY))
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_TYPE, property.get(ECommerceParamNames.CATEGORY));
                                    if (property.containsKey(ECommerceParamNames.CURRENCY))
                                        afEventProps.put(AFInAppEventParameterName.CURRENCY, property.get(ECommerceParamNames.CURRENCY));
                                    if (property.containsKey(ECommerceParamNames.QUANTITY))
                                        afEventProps.put(AFInAppEventParameterName.QUANTITY, property.get(ECommerceParamNames.QUANTITY));
                                    afEventName = AFInAppEventType.ADD_TO_CART;
                                    break;
                                case ECommerceEvents.CHECKOUT_STARTED:
                                    if (property.containsKey(ECommerceParamNames.TOTAL))
                                        afEventProps.put(AFInAppEventParameterName.PRICE, property.get(ECommerceParamNames.TOTAL));
                                    if (property.containsKey(ECommerceParamNames.PRODUCTS)) {
                                        ArrayList<String> pIds = new ArrayList<>();
                                        ArrayList<String> pCats = new ArrayList<>();
                                        ArrayList<String> pQnts = new ArrayList<>();
                                        for (Map<String, Object> product : ((ArrayList<Map<String, Object>>) property.get(ECommerceParamNames.PRODUCTS))) {
                                            if (product.containsKey("product_id") && product.containsKey("category") && product.containsKey("quantity")) {
                                                pIds.add((String) product.get("product_id"));
                                                pCats.add((String) product.get("category"));
                                                pQnts.add((String) product.get("quantity"));
                                            }
                                        }
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_ID, pIds.toArray());
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_TYPE, pCats.toArray());
                                        afEventProps.put(AFInAppEventParameterName.QUANTITY, pQnts.toArray());
                                    }
                                    if (property.containsKey(ECommerceParamNames.CURRENCY))
                                        afEventProps.put(AFInAppEventParameterName.CURRENCY, property.get(ECommerceParamNames.CURRENCY));
                                    afEventName = AFInAppEventType.INITIATED_CHECKOUT;
                                    break;
                                case ECommerceEvents.ORDER_COMPLETED:
                                    if (property.containsKey(ECommerceParamNames.TOTAL))
                                        afEventProps.put(AFInAppEventParameterName.PRICE, property.get(ECommerceParamNames.REVENUE));
                                    if (property.containsKey(ECommerceParamNames.PRODUCTS)) {
                                        ArrayList<String> pIds = new ArrayList<>();
                                        ArrayList<String> pCats = new ArrayList<>();
                                        ArrayList<String> pQnts = new ArrayList<>();
                                        for (Map<String, Object> product : ((ArrayList<Map<String, Object>>) property.get(ECommerceParamNames.PRODUCTS))) {
                                            if (product.containsKey("product_id") && product.containsKey("category") && product.containsKey("quantity")) {
                                                pIds.add((String) product.get("product_id"));
                                                pCats.add((String) product.get("category"));
                                                pQnts.add((String) product.get("quantity"));
                                            }
                                        }
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_ID, pIds.toArray());
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_TYPE, pCats.toArray());
                                        afEventProps.put(AFInAppEventParameterName.QUANTITY, pQnts.toArray());
                                    }
                                    if (property.containsKey(ECommerceParamNames.CURRENCY))
                                        afEventProps.put(AFInAppEventParameterName.CURRENCY, property.get(ECommerceParamNames.CURRENCY));
                                    if (property.containsKey(ECommerceParamNames.ORDER_ID)) {
                                        afEventProps.put(AFInAppEventParameterName.RECEIPT_ID, property.get(ECommerceParamNames.ORDER_ID));
                                        afEventProps.put("af_order_id", property.get(ECommerceParamNames.ORDER_ID));
                                    }
                                    afEventName = AFInAppEventType.PURCHASE;
                                    break;
                                case ECommerceEvents.PRODUCT_REMOVED:
                                    if (property.containsKey(ECommerceParamNames.PRODUCT_ID))
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_ID, property.get(ECommerceParamNames.PRODUCT_ID));
                                    if (property.containsKey(ECommerceParamNames.CATEGORY))
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_TYPE, property.get(ECommerceParamNames.CATEGORY));
                                    afEventName = "remove_from_cart";
                                    break;
                                default:
                                    afEventName = eventName.toLowerCase().replace(" ", "_");
                            }
                        } else {
                            afEventName = eventName.toLowerCase().replace(" ", "_");
                        }
                        AppsFlyerLib.getInstance().trackEvent(RudderClient.getApplication(), afEventName, afEventProps);
                    }
                    break;
                case MessageType.SCREEN:
                    AppsFlyerLib.getInstance().trackEvent(RudderClient.getApplication(), "screen", message.getProperties());
                    break;
                case MessageType.IDENTIFY:
                    String userId = message.getUserId();
                    AppsFlyerLib.getInstance().setCustomerUserId(userId);
                    if (message.getTraits().containsKey("email")) {
                        AppsFlyerLib.getInstance().setUserEmails((String) message.getTraits().get("email"));
                    }
                    break;
                default:
                    RudderLogger.logWarn("Message type is not supported");
            }
        }
    }

    @Override
    public void onConversionDataSuccess(Map<String, Object> map) {

    }

    @Override
    public void onConversionDataFail(String s) {

    }

    @Override
    public void onAppOpenAttribution(Map<String, String> map) {
    }

    @Override
    public void onAttributionFailure(String s) {
    }

    @Override
    public void reset() {

    }

    @Override
    public void dump(RudderMessage element) {
        if (element != null) {
            this.processEvents(element);
        }
    }

    @Override
    public AppsFlyerLib getUnderlyingInstance() {
        return AppsFlyerLib.getInstance();
    }
}
