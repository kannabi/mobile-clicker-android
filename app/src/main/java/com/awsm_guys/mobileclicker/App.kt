package com.awsm_guys.mobileclicker

import android.app.Application
import com.awsm_guys.mobileclicker.di.ComponentProvider

class App: Application() {
    lateinit var componentProvider: ComponentProvider

    override fun onCreate() {
        super.onCreate()
        componentProvider = ComponentProvider(this.applicationContext)
    }
}