package com.awsm_guys.mobileclicker.di

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {
    fun plusClickerComponent(clickerModule: ClickerModule): ClickerComponent
}