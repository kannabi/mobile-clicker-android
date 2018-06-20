package com.awsm_guys.mobileclicker.primitivestore

import android.content.Context
import com.awsm_guys.mobileclicker.SHARED_PREFERENCES_FILE_NAME

class SharedPreferencesPrimitiveStore(context: Context): PrimitiveStore {
    override fun store(map: Map<String, String>) {
        with(sharedPreferences.edit()) {
            map.forEach { key, value ->  putString(key, value) }
            apply()
        }
    }

    private val sharedPreferences =
            context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)

    override fun store(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    override fun find(key: String): String? =
            sharedPreferences.getString(key, null)
}