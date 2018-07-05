package com.awsm_guys.mobileclicker.di

import com.awsm_guys.mobileclicker.clicker.IClickerPresenter
import com.awsm_guys.mobileclicker.clicker.view.ClickerFragment
import com.awsm_guys.mobileclicker.di.scopes.MainScope
import com.kannabi.simplelifecycleapilibrary.lifecycleapi.ProvidePresenter
import dagger.Subcomponent

@MainScope
@Subcomponent(modules = [(ClickerModule::class)])
interface ClickerComponent: ProvidePresenter<IClickerPresenter> {
    fun inject(clickerFragment: ClickerFragment)
}