package com.awsm_guys.mobileclicker

import com.awsm_guys.mobileclicker.primitivestore.PrimitiveStore

class TestPrimitiveStore: PrimitiveStore {
    private val cache = mutableMapOf<String, String>()

    override fun store(key: String, value: String) {
        cache[key] = value
    }

    override fun store(map: Map<String, String>) {
        cache.putAll(map)
    }

    override fun find(key: String): String? = cache[key]
}