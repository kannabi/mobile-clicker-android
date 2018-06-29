package com.awsm_guys.mobileclicker.di

import android.content.Context
import com.awsm_guys.mobileclicker.clicker.model.controller.DesktopControllerFactory

class ComponentProvider(applicationContext: Context) {

    var desktopControllerFactoryCache: DesktopControllerFactory? = null
        get() {
            val value = field
            field = null
            return value
        }

    private var appComponent: AppComponent = DaggerAppComponent
                                        .builder()
                                        .appModule(AppModule(applicationContext))
                                        .build()

    fun getMobileClickerComponent() =
            appComponent.plusClickerComponent(ClickerModule(desktopControllerFactoryCache))

    fun getConnectionComponent() = appComponent.plusConnectionComponent(ConnectionModule())
}