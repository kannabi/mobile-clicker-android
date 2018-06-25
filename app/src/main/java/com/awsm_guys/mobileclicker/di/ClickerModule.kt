package com.awsm_guys.mobileclicker.di

import android.content.Context
import com.awsm_guys.mobileclicker.clicker.IClickerModel
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
    fun provideClickerPresenter(context: Context, clickerModel: IClickerModel): IClickerPresenter =
            ClickerPresenter(context, clickerModel)

    @MainScope
    @Provides
    fun provideClickerModel(): IClickerModel = ClickerModel()
}