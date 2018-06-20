package com.awsm_guys.mobileclicker.connection.view

import android.os.Bundle
import com.awsm_guys.mobileclicker.App
import com.awsm_guys.mobileclicker.R
import com.awsm_guys.mobileclicker.connection.IConnectionPresenter
import com.awsm_guys.mobileclicker.connection.IConnectionView
import com.awsm_guys.mobileclicker.di.ConnectionComponent
import com.kannabi.simplelifecycleapilibrary.lifecycleapi.activity.PresenterActivity

class ConnectionActivity :
        PresenterActivity<IConnectionView, ConnectionComponent, IConnectionPresenter>(),
        IConnectionView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getComponent().inject(this)
        setContentView(R.layout.activity_connection)
    }

    override fun provideComponent(): ConnectionComponent =
            (application as App).componentProvider.getConnectionComponent()

    override fun showAskNameWindow() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSearch() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSuccess() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
