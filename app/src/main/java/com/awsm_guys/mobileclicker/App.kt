package com.awsm_guys.mobileclicker

import android.app.Application
import com.awsm_guys.mobileclicker.di.ComponentProvider
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

class App: Application() {
    lateinit var componentProvider: ComponentProvider

    override fun onCreate() {
        super.onCreate()
        componentProvider = ComponentProvider(this.applicationContext)
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
        }
    }
}