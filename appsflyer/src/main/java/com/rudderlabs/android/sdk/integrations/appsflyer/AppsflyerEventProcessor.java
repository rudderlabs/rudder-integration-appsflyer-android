package com.rudderlabs.android.sdk.integrations.appsflyer;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.rudderlabs.android.sdk.core.MessageType;
import com.rudderlabs.android.sdk.core.RudderElement;
import com.rudderlabs.android.sdk.core.ecommerce.ECommerceEvents;
import com.rudderlabs.android.sdk.core.ecommerce.ECommerceParamNames;
import com.rudderlabs.android.sdk.core.ecommerce.ECommerceProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class AppsflyerEventProcessor {
    static AppsflyerEvent processRudderEvents(RudderElement element) {
        String eventType = element.getType().toLowerCase();
        switch (eventType) {
            case MessageType.TRACK:
                return processTrackEvents(element);
            case MessageType.PAGE:
                return processPageEvents(element);
            case MessageType.SCREEN:
                return processScreenEvents(element);
            case MessageType.IDENTIFY:
                return processIdentifyEvents(element);
        }
        return null;
    }

    private static AppsflyerEvent processTrackEvents(RudderElement element) {
        String eventName = element.getEventName();
        AppsflyerEvent appsflyerEvent = new AppsflyerEvent(eventName);
        Map<String, Object> property = element.getProperties();
        Map<String, Object> map = new HashMap<>();
        switch (eventName) {
            case ECommerceEvents.PRODUCTS_SEARCHED:
                if (property.containsKey(ECommerceParamNames.QUERY))
                    map.put(AFInAppEventParameterName.SEARCH_STRING, property.get("query"));
                appsflyerEvent.setEventType(AFInAppEventType.SEARCH);
                appsflyerEvent.setEventProps(map);
                break;
            case ECommerceEvents.PRODUCT_VIEWED:
                if (property.containsKey(ECommerceParamNames.PRICE))
                    map.put(AFInAppEventParameterName.PRICE, property.get(ECommerceParamNames.PRICE));
                if (property.containsKey(ECommerceParamNames.PRODUCT_ID))
                    map.put(AFInAppEventParameterName.CONTENT_ID, property.get(ECommerceParamNames.PRODUCT_ID));
                if (property.containsKey(ECommerceParamNames.CATEGORY))
                    map.put(AFInAppEventParameterName.CONTENT_TYPE, property.get(ECommerceParamNames.CATEGORY));
                if (property.containsKey(ECommerceParamNames.CURRENCY))
                    map.put(AFInAppEventParameterName.CURRENCY, property.get(ECommerceParamNames.CURRENCY));
                appsflyerEvent.setEventType(AFInAppEventType.CONTENT_VIEW);
                appsflyerEvent.setEventProps(map);
                break;
            case ECommerceEvents.PRODUCT_LIST_VIEWED:
                if (property.containsKey(ECommerceParamNames.CATEGORY)) {
                    map.put(AFInAppEventParameterName.CONTENT_TYPE, property.get(ECommerceParamNames.CATEGORY));
                }
                if (property.containsKey(ECommerceParamNames.PRODUCTS)) {
                    ArrayList<String> products = new ArrayList<>();
                    for (ECommerceProduct product : ((ArrayList<ECommerceProduct>) property.get(ECommerceParamNames.PRODUCTS))) {
                        products.add(product.getProductId());
                    }
                    map.put(AFInAppEventParameterName.CONTENT_LIST, products.toArray());
                }
                appsflyerEvent.setEventType("af_list_view");
                appsflyerEvent.setEventProps(map);
                break;
            case ECommerceEvents.PRODUCT_ADDED_TO_WISH_LIST:
                if (property.containsKey(ECommerceParamNames.PRICE))
                    map.put(AFInAppEventParameterName.PRICE, property.get(ECommerceParamNames.PRICE));
                if (property.containsKey(ECommerceParamNames.PRODUCT_ID))
                    map.put(AFInAppEventParameterName.CONTENT_ID, property.get(ECommerceParamNames.PRODUCT_ID));
                if (property.containsKey(ECommerceParamNames.CATEGORY))
                    map.put(AFInAppEventParameterName.CONTENT_TYPE, property.get(ECommerceParamNames.CATEGORY));
                if (property.containsKey(ECommerceParamNames.CURRENCY))
                    map.put(AFInAppEventParameterName.CURRENCY, property.get(ECommerceParamNames.CURRENCY));
                appsflyerEvent.setEventType(AFInAppEventType.ADD_TO_WISH_LIST);
                appsflyerEvent.setEventProps(map);
                break;
            case ECommerceEvents.PRODUCT_ADDED:
                if (property.containsKey(ECommerceParamNames.PRICE))
                    map.put(AFInAppEventParameterName.PRICE, property.get(ECommerceParamNames.PRICE));
                if (property.containsKey(ECommerceParamNames.PRODUCT_ID))
                    map.put(AFInAppEventParameterName.CONTENT_ID, property.get(ECommerceParamNames.PRODUCT_ID));
                if (property.containsKey(ECommerceParamNames.CATEGORY))
                    map.put(AFInAppEventParameterName.CONTENT_TYPE, property.get(ECommerceParamNames.CATEGORY));
                if (property.containsKey(ECommerceParamNames.CURRENCY))
                    map.put(AFInAppEventParameterName.CURRENCY, property.get(ECommerceParamNames.CURRENCY));
                if (property.containsKey(ECommerceParamNames.QUANTITY))
                    map.put(AFInAppEventParameterName.QUANTITY, property.get(ECommerceParamNames.QUANTITY));
                appsflyerEvent.setEventType(AFInAppEventType.ADD_TO_CART);
                appsflyerEvent.setEventProps(map);
                break;
            case ECommerceEvents.CHECKOUT_STARTED:
                if (property.containsKey(ECommerceParamNames.TOTAL))
                    map.put(AFInAppEventParameterName.PRICE, property.get(ECommerceParamNames.TOTAL));
                if (property.containsKey(ECommerceParamNames.PRODUCTS)) {
                    ArrayList<String> pIds = new ArrayList<>();
                    ArrayList<String> pCats = new ArrayList<>();
                    ArrayList<String> pQnts = new ArrayList<>();
                    for (ECommerceProduct product : ((ArrayList<ECommerceProduct>) property.get(ECommerceParamNames.PRODUCTS))) {
                        pIds.add(product.getProductId());
                        pCats.add(product.getCategory());
                        pQnts.add(Float.toString(product.getQuantity()));
                    }
                    map.put(AFInAppEventParameterName.CONTENT_ID, pIds.toArray());
                    map.put(AFInAppEventParameterName.CONTENT_TYPE, pCats.toArray());
                    map.put(AFInAppEventParameterName.QUANTITY, pQnts.toArray());
                }
                if (property.containsKey(ECommerceParamNames.CURRENCY))
                    map.put(AFInAppEventParameterName.CURRENCY, property.get(ECommerceParamNames.CURRENCY));
                appsflyerEvent.setEventType(AFInAppEventType.INITIATED_CHECKOUT);
                appsflyerEvent.setEventProps(map);
                break;
            case ECommerceEvents.ORDER_COMPLETED:
                if (property.containsKey(ECommerceParamNames.TOTAL))
                    map.put(AFInAppEventParameterName.PRICE, property.get(ECommerceParamNames.REVENUE));
                if (property.containsKey(ECommerceParamNames.PRODUCTS)) {
                    ArrayList<String> pIds = new ArrayList<>();
                    ArrayList<String> pCats = new ArrayList<>();
                    ArrayList<String> pQnts = new ArrayList<>();
                    for (ECommerceProduct product : ((ArrayList<ECommerceProduct>) property.get(ECommerceParamNames.PRODUCTS))) {
                        pIds.add(product.getProductId());
                        pCats.add(product.getCategory());
                        pQnts.add(Float.toString(product.getQuantity()));
                    }
                    map.put(AFInAppEventParameterName.CONTENT_ID, pIds.toArray());
                    map.put(AFInAppEventParameterName.CONTENT_TYPE, pCats.toArray());
                    map.put(AFInAppEventParameterName.QUANTITY, pQnts.toArray());
                }
                if (property.containsKey(ECommerceParamNames.CURRENCY))
                    map.put(AFInAppEventParameterName.CURRENCY, property.get(ECommerceParamNames.CURRENCY));
                if (property.containsKey(ECommerceParamNames.ORDER_ID)) {
                    map.put(AFInAppEventParameterName.RECEIPT_ID, property.get(ECommerceParamNames.ORDER_ID));
                    map.put("af_order_id", property.get(ECommerceParamNames.ORDER_ID));
                }
                appsflyerEvent.setEventType(AFInAppEventType.PURCHASE);
                appsflyerEvent.setEventProps(map);
                break;
            case ECommerceEvents.PRODUCT_REMOVED:
                if (property.containsKey(ECommerceParamNames.PRODUCT_ID))
                    map.put(AFInAppEventParameterName.CONTENT_ID, property.get(ECommerceParamNames.PRODUCT_ID));
                if (property.containsKey(ECommerceParamNames.CATEGORY))
                    map.put(AFInAppEventParameterName.CONTENT_TYPE, property.get(ECommerceParamNames.CATEGORY));
                appsflyerEvent.setEventType("remove_from_cart");
                appsflyerEvent.setEventProps(map);
                break;
            default:
                appsflyerEvent.setEventType(eventName.toLowerCase());
                appsflyerEvent.setEventProps(map);
        }
        return appsflyerEvent;
    }

    private static AppsflyerEvent processPageEvents(RudderElement element) {
        return new AppsflyerEvent("page", element.getProperties());
    }

    private static AppsflyerEvent processScreenEvents(RudderElement element) {
        return new AppsflyerEvent("screen", element.getProperties());
    }

    private static AppsflyerEvent processIdentifyEvents(RudderElement element) {
        return null;
    }

}
