package com.awsm_guys.mobileclicker.primitivestore

interface PrimitiveStore {
    fun store(key: String, value: String)

    fun store(map: Map<String, String>)

    fun find(key: String): String?

    fun remove(key: String)
}