package com.awsm_guys.mobileclicker.di

import android.content.Context

class ComponentProvider(applicationContext: Context) {
    private var appComponent: AppComponent = DaggerAppComponent
                                        .builder()
                                        .appModule(AppModule(applicationContext))
                                        .build()

    fun getMobileClickerComponent() = appComponent.plusClickerComponent(ClickerModule())
}