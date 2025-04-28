package com.cursokotlin.entrenamientosconroom.data.firebase

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class AnalyticsService @Inject constructor(private val firebaseAnalytics: FirebaseAnalytics) {
    fun logEvent(eventName: String, params: Map<String, String> = emptyMap()) {
        val bundle = Bundle().apply {
            params.forEach { (key, value) ->
                putString(key, value)
            }
        }
        firebaseAnalytics.logEvent(eventName, bundle)
    }
}