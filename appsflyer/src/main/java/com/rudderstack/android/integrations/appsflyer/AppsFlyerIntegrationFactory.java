package com.rudderstack.android.integrations.appsflyer;

import android.text.TextUtils;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.rudderstack.android.sdk.core.MessageType;
import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderIntegration;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderMessage;
import com.rudderstack.android.sdk.core.ecomm.ECommerceEvents;
import com.rudderstack.android.sdk.core.ecomm.ECommerceParamNames;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppsFlyerIntegrationFactory extends RudderIntegration<AppsFlyerLib> {
    private static final String APPSFLYER_KEY = "AppsFlyer";
    private static final String FIRST_PURCHASE = "first_purchase";
    public static final String CREATIVE = "creative";
    private Boolean isNewScreenEnabled = false;

    static final List<String> TRACK_RESERVED_KEYWORDS = Arrays.asList(ECommerceParamNames.QUERY, ECommerceParamNames.PRICE, ECommerceParamNames.PRODUCT_ID, ECommerceParamNames.CATEGORY,
            ECommerceParamNames.CURRENCY, ECommerceParamNames.PRODUCTS, ECommerceParamNames.QUANTITY, ECommerceParamNames.TOTAL,
            ECommerceParamNames.REVENUE, ECommerceParamNames.ORDER_ID, ECommerceParamNames.SHARE_MESSAGE, CREATIVE, ECommerceParamNames.RATING);

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
        if (destConfig != null) {
            if (destConfig.containsKey("useRichEventName") && destConfig.get("useRichEventName") != null) {
                isNewScreenEnabled = (Boolean) destConfig.get("useRichEventName");
            }
        }
    }

    private void processEvents(RudderMessage message) {
        String eventType = message.getType();
        String afEventName = null;
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
                                        afEventProps.put(AFInAppEventParameterName.SEARCH_STRING, property.get(ECommerceParamNames.QUERY));
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
                                        JSONArray productsJSON = getJSONArray(property.get(ECommerceParamNames.PRODUCTS));
                                        if (productsJSON != null) {
                                            ArrayList<String> products = new ArrayList<>();
                                            for (int i = 0; i < productsJSON.length(); i++) {
                                                try {
                                                    JSONObject product = (JSONObject) productsJSON.get(i);
                                                    if (product.has("product_id")) {
                                                        products.add(getString(product.get("product_id")));
                                                    }
                                                } catch (JSONException e) {
                                                    RudderLogger.logDebug("Error while getting Products: " + productsJSON);
                                                }
                                            }
                                            afEventProps.put(AFInAppEventParameterName.CONTENT_LIST, products.toArray());
                                        }
                                    }
                                    afEventName = AFInAppEventType.LIST_VIEW;
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
                                        handleProducts(property, afEventProps);
                                    }
                                    if (property.containsKey(ECommerceParamNames.CURRENCY))
                                        afEventProps.put(AFInAppEventParameterName.CURRENCY, property.get(ECommerceParamNames.CURRENCY));
                                    afEventName = AFInAppEventType.INITIATED_CHECKOUT;
                                    break;
                                case ECommerceEvents.ORDER_COMPLETED:
                                    makeOrderCompletedEvent(property, afEventProps);
                                    afEventName = AFInAppEventType.PURCHASE;
                                    break;
                                case FIRST_PURCHASE:
                                    makeOrderCompletedEvent(property, afEventProps);
                                    afEventName = FIRST_PURCHASE;
                                    break;
                                case ECommerceEvents.PRODUCT_REMOVED:
                                    if (property.containsKey(ECommerceParamNames.PRODUCT_ID))
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_ID, property.get(ECommerceParamNames.PRODUCT_ID));
                                    if (property.containsKey(ECommerceParamNames.CATEGORY))
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_TYPE, property.get(ECommerceParamNames.CATEGORY));
                                    afEventName = "remove_from_cart";
                                    break;
                                case ECommerceEvents.PROMOTION_VIEWED:
                                    if (property.containsKey(CREATIVE))
                                        afEventProps.put(AFInAppEventParameterName.AD_REVENUE_AD_TYPE, property.get(CREATIVE));
                                    if (property.containsKey(ECommerceParamNames.CURRENCY))
                                        afEventProps.put(AFInAppEventParameterName.CURRENCY, property.get(ECommerceParamNames.CURRENCY));
                                    afEventName = AFInAppEventType.AD_CLICK;
                                    break;
                                case ECommerceEvents.PROMOTION_CLICKED:
                                    if (property.containsKey(CREATIVE))
                                        afEventProps.put(AFInAppEventParameterName.AD_REVENUE_AD_TYPE, property.get(CREATIVE));
                                    if (property.containsKey(ECommerceParamNames.CURRENCY))
                                        afEventProps.put(AFInAppEventParameterName.CURRENCY, property.get(ECommerceParamNames.CURRENCY));
                                    afEventName = AFInAppEventType.AD_VIEW;
                                    break;
                                case ECommerceEvents.PAYMENT_INFO_ENTERED:
                                    afEventName = AFInAppEventType.ADD_PAYMENT_INFO;
                                    break;
                                case ECommerceEvents.PRODUCT_SHARED:
                                case ECommerceEvents.CART_SHARED:
                                    if (property.containsKey(ECommerceParamNames.SHARE_MESSAGE))
                                        afEventProps.put(AFInAppEventParameterName.DESCRIPTION, property.get(ECommerceParamNames.SHARE_MESSAGE));
                                    afEventName = AFInAppEventType.SHARE;
                                    break;
                                case ECommerceEvents.PRODUCT_REVIEWED:
                                    if (property.containsKey(ECommerceParamNames.PRODUCT_ID))
                                        afEventProps.put(AFInAppEventParameterName.CONTENT_ID, property.get(ECommerceParamNames.PRODUCT_ID));
                                    if (property.containsKey(ECommerceParamNames.RATING))
                                        afEventProps.put(AFInAppEventParameterName.RATING_VALUE, property.get(ECommerceParamNames.RATING));
                                    afEventName = AFInAppEventType.RATE;
                                    break;
                                default:
                                    afEventName = eventName.toLowerCase().replace(" ", "_");
                            }
                        } else {
                            afEventName = eventName.toLowerCase().replace(" ", "_");
                        }
                        attachAllCustomProperties(afEventProps, property);
                        AppsFlyerLib.getInstance().logEvent(RudderClient.getApplication(), afEventName, afEventProps);
                    }
                    break;
                case MessageType.SCREEN:
                    String screenName;
                    Map<String, Object> properties = message.getProperties();
                    if (isNewScreenEnabled) {
                        if (!TextUtils.isEmpty(message.getEventName())) {
                            screenName = "Viewed " + message.getEventName() + " Screen";
                        } else if (properties != null &&
                                properties.containsKey("name") &&
                                !TextUtils.isEmpty((String) properties.get("name"))) {
                            screenName = "Viewed " + properties.get("name") + " Screen";
                        } else {
                            screenName = "Viewed Screen";
                        }
                    } else {
                        screenName = "screen";
                    }
                    AppsFlyerLib.getInstance().logEvent(RudderClient.getApplication(), screenName, properties);
                    break;
                case MessageType.IDENTIFY:
                    String userId = message.getUserId();
                    AppsFlyerLib.getInstance().setCustomerUserId(userId);
                    if (message.getTraits().containsKey("email")) {
                        AppsFlyerLib.getInstance().setUserEmails(getString(message.getTraits().get("email")));
                    }
                    break;
                default:
                    RudderLogger.logWarn("Message type is not supported");
            }
        }
    }

    private void attachAllCustomProperties(Map<String, Object> afEventProps, Map<String, Object> properties) {
        if (properties == null || properties.size() == 0) {
            return;
        }
        for (String key : properties.keySet()) {
            Object value = properties.get(key);
            if (TRACK_RESERVED_KEYWORDS.contains(key) || TextUtils.isEmpty(key)) {
                continue;
            }
            afEventProps.put(key, value);
        }
    }

    private void makeOrderCompletedEvent(Map<String, Object> eventProperties, Map<String, Object> afEventProps) {
        if (eventProperties.containsKey(ECommerceParamNames.TOTAL))
            afEventProps.put(AFInAppEventParameterName.PRICE, eventProperties.get(ECommerceParamNames.TOTAL));
        if (eventProperties.containsKey(ECommerceParamNames.REVENUE))
            afEventProps.put(AFInAppEventParameterName.REVENUE, eventProperties.get(ECommerceParamNames.REVENUE));
        if (eventProperties.containsKey(ECommerceParamNames.PRODUCTS)) {
            handleProducts(eventProperties, afEventProps);
        }
        if (eventProperties.containsKey(ECommerceParamNames.CURRENCY))
            afEventProps.put(AFInAppEventParameterName.CURRENCY, eventProperties.get(ECommerceParamNames.CURRENCY));
        if (eventProperties.containsKey(ECommerceParamNames.ORDER_ID)) {
            afEventProps.put(AFInAppEventParameterName.RECEIPT_ID, eventProperties.get(ECommerceParamNames.ORDER_ID));
            afEventProps.put("af_order_id", eventProperties.get(ECommerceParamNames.ORDER_ID));
        }
    }

    private void handleProducts(Map<String, Object> property, Map<String, Object> afEventProps) {
        JSONArray products = getJSONArray(property.get(ECommerceParamNames.PRODUCTS));
        if (products != null && products.length() > 0) {
            ArrayList<String> pIds = new ArrayList<>();
            ArrayList<String> pCats = new ArrayList<>();
            ArrayList<String> pQnts = new ArrayList<>();
            for (int i = 0; i < products.length(); i++) {
                try {
                    JSONObject product = (JSONObject) products.get(i);
                    if (product.has("product_id") && product.has("category") && product.has("quantity")) {
                        pIds.add(getString(product.get("product_id")));
                        pCats.add(getString(product.get("category")));
                        pQnts.add(getString(product.get("quantity")));
                    }
                } catch (JSONException e) {
                    RudderLogger.logDebug("Error while getting Products: " + products);
                }
            }
            afEventProps.put(AFInAppEventParameterName.CONTENT_ID, pIds.toArray());
            afEventProps.put(AFInAppEventParameterName.CONTENT_TYPE, pCats.toArray());
            afEventProps.put(AFInAppEventParameterName.QUANTITY, pQnts.toArray());
        }
    }

    private JSONArray getJSONArray(Object object) {
        if (object instanceof JSONArray) {
            return (JSONArray) object;
        }
        if (object instanceof List) {
            ArrayList<Object> arrayList = new ArrayList<>();
            arrayList.addAll((Collection<?>) object);
            return new JSONArray(arrayList);
        }
        try {
            return new JSONArray((ArrayList) object);
        } catch (Exception e) {
            RudderLogger.logDebug("Error while converting the products: " + object + " to JSONArray type");
        }
        return null;
    }

    private static String getString(Object value) {
        if (value instanceof String) {
            return (String) value;
        } else if (value != null) {
            return String.valueOf(value);
        }
        return null;
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
