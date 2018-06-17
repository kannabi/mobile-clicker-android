package com.awsm_guys.mobileclicker.di

import com.awsm_guys.mobileclicker.clicker.IClickerPresenter
import com.awsm_guys.mobileclicker.clicker.model.ClickerModel
import com.awsm_guys.mobileclicker.clicker.presenter.ClickerPresenter
import com.awsm_guys.mobileclicker.di.scopes.MainScope
import dagger.Module
import dagger.Provides

@Module
class ClickerModule {
    @MainScope
    @Provides
    fun provideClickerPresenter(): IClickerPresenter = ClickerPresenter(ClickerModel())
}