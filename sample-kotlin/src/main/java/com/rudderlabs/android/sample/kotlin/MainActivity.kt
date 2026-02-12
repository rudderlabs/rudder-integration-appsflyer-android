package com.rudderlabs.android.sample.kotlin

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.rudderstack.android.sdk.core.RudderProperty
import com.rudderstack.android.sdk.core.RudderTraits

class MainActivity : AppCompatActivity() {

    private val rudderClient get() = MainApplication.rudderClient

    private val productA = mapOf(
        "product_id" to "product_id_a",
        "category" to "clothing",
        "quantity" to 1
    )

    private val productB = mapOf(
        "product_id" to "product_id_b",
        "category" to "electronics",
        "quantity" to 2
    )

    private val orderProperties
        get() = RudderProperty()
            .putValue("order_id", "order_001")
            .putValue("currency", "USD")
            .putValue("total", 12.99)
            .putValue("revenue", 10.99)
            .putValue("products", listOf(productA, productB))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupIdentifyButtons()
        setupECommerceButtons()
        setupTrackButtons()
        setupScreenButton()
    }

    private fun setupIdentifyButtons() {
        findViewById<Button>(R.id.btnIdentifyUserId).setOnClickListener {
            rudderClient.identify("test_user_id")
        }

        findViewById<Button>(R.id.btnIdentifyWithTraits).setOnClickListener {
            rudderClient.identify(
                "test_user_id",
                RudderTraits()
                    .putEmail("test@example.com")
                    .putName("Test User")
                    .putPhone("1234567890"),
                null
            )
        }
    }

    private fun setupECommerceButtons() {
        findViewById<Button>(R.id.btnProductsSearched).setOnClickListener {
            rudderClient.track(
                "Products Searched",
                RudderProperty().putValue("query", "Product-key-1")
            )
        }

        findViewById<Button>(R.id.btnProductViewed).setOnClickListener {
            rudderClient.track(
                "Product Viewed",
                RudderProperty()
                    .putValue("product_id", "product_id_a")
                    .putValue("price", 2.99)
                    .putValue("category", "clothing")
                    .putValue("currency", "USD")
            )
        }

        findViewById<Button>(R.id.btnProductListViewed).setOnClickListener {
            rudderClient.track(
                "Product List Viewed",
                RudderProperty()
                    .putValue("category", "clothing")
                    .putValue("products", listOf(productA, productB))
            )
        }

        findViewById<Button>(R.id.btnProductAddedToWishlist).setOnClickListener {
            rudderClient.track(
                "Product Added to Wishlist",
                RudderProperty()
                    .putValue("product_id", "product_id_a")
                    .putValue("price", 2.99)
                    .putValue("category", "clothing")
                    .putValue("currency", "USD")
            )
        }

        findViewById<Button>(R.id.btnProductAdded).setOnClickListener {
            rudderClient.track(
                "Product Added",
                RudderProperty()
                    .putValue("product_id", "product_id_a")
                    .putValue("price", 2.99)
                    .putValue("category", "clothing")
                    .putValue("currency", "USD")
                    .putValue("quantity", 5)
            )
        }

        findViewById<Button>(R.id.btnCheckoutStarted).setOnClickListener {
            rudderClient.track(
                "Checkout Started",
                RudderProperty()
                    .putValue("total", 12.99)
                    .putValue("currency", "USD")
                    .putValue("products", listOf(productA, productB))
            )
        }

        findViewById<Button>(R.id.btnOrderCompleted).setOnClickListener {
            rudderClient.track("Order Completed", orderProperties)
        }

        findViewById<Button>(R.id.btnFirstPurchase).setOnClickListener {
            rudderClient.track("first_purchase", orderProperties)
        }

        findViewById<Button>(R.id.btnProductRemoved).setOnClickListener {
            rudderClient.track(
                "Product Removed",
                RudderProperty()
                    .putValue("product_id", "product_id_a")
                    .putValue("category", "clothing")
            )
        }

        findViewById<Button>(R.id.btnPromotionViewed).setOnClickListener {
            rudderClient.track(
                "Promotion Viewed",
                RudderProperty()
                    .putValue("creative", "email")
                    .putValue("currency", "USD")
            )
        }

        findViewById<Button>(R.id.btnPromotionClicked).setOnClickListener {
            rudderClient.track(
                "Promotion Clicked",
                RudderProperty()
                    .putValue("creative", "email")
                    .putValue("currency", "USD")
            )
        }

        findViewById<Button>(R.id.btnPaymentInfoEntered).setOnClickListener {
            rudderClient.track("Payment Info Entered")
        }

        findViewById<Button>(R.id.btnProductShared).setOnClickListener {
            rudderClient.track(
                "Product Shared",
                RudderProperty()
                    .putValue("share_message", "Check this out!")
            )
        }

        findViewById<Button>(R.id.btnCartShared).setOnClickListener {
            rudderClient.track(
                "Cart Shared",
                RudderProperty()
                    .putValue("share_message", "Look at my cart")
            )
        }

        findViewById<Button>(R.id.btnProductReviewed).setOnClickListener {
            rudderClient.track(
                "Product Reviewed",
                RudderProperty()
                    .putValue("product_id", "product_id_a")
                    .putValue("rating", 4.5)
            )
        }
    }

    private fun setupTrackButtons() {
        findViewById<Button>(R.id.btnCustomTrackWithProps).setOnClickListener {
            rudderClient.track(
                "Custom Event",
                RudderProperty()
                    .putValue("key_1", "value_1")
                    .putValue("key_2", 42)
                    .putValue("key_3", true)
            )
        }

        findViewById<Button>(R.id.btnCustomTrackNoProps).setOnClickListener {
            rudderClient.track("Custom Event No Props")
        }
    }

    private fun setupScreenButton() {
        findViewById<Button>(R.id.btnScreen).setOnClickListener {
            rudderClient.screen(
                "Home",
                RudderProperty()
                    .putValue("source", "navigation")
                    .putValue("section", "main")
            )
        }
    }
}
