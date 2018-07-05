package com.awsm_guys.mobileclicker.di

import com.awsm_guys.mobileclicker.connection.IConnectionPresenter
import com.awsm_guys.mobileclicker.connection.view.ConnectionActivity
import com.awsm_guys.mobileclicker.di.scopes.ConnectionScope
import com.kannabi.simplelifecycleapilibrary.lifecycleapi.ProvidePresenter
import dagger.Subcomponent

@ConnectionScope
@Subcomponent(modules = [(ConnectionModule::class)])
interface ConnectionComponent  : ProvidePresenter<IConnectionPresenter> {
    fun inject(connectionFragment: ConnectionActivity)
}