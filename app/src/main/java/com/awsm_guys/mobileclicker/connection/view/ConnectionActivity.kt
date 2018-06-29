package com.awsm_guys.mobileclicker.connection.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.util.Log
import com.awsm_guys.mobileclicker.App
import com.awsm_guys.mobileclicker.R
import com.awsm_guys.mobileclicker.connection.IConnectionPresenter
import com.awsm_guys.mobileclicker.connection.IConnectionView
import com.awsm_guys.mobileclicker.di.ConnectionComponent
import com.awsm_guys.mobileclicker.utils.LoggingMixin
import com.kannabi.simplelifecycleapilibrary.lifecycleapi.activity.PresenterActivity

class ConnectionActivity :
        PresenterActivity<IConnectionView, ConnectionComponent, IConnectionPresenter>(),
        IConnectionView, LoggingMixin {

    companion object {
        const val ENTER_NAME_FRAGMENT_TAG = "ENTER_NAME_FRAGMENT_TAG"
        const val SEARCH_FRAGMENT_TAG = "SEARCH_FRAGMENT_TAG"
    }

    private var currentFragmentTag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getComponent().inject(this)
        setContentView(R.layout.activity_connection)
    }

    override fun provideComponent(): ConnectionComponent =
            (application as App).componentProvider.getConnectionComponent()

    override fun onBackPressed() {
        if (currentFragmentTag == SEARCH_FRAGMENT_TAG) {
             getPresenter().onCancelSearch()
        }
        super.onBackPressed()
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        currentFragmentTag = when (fragment) {
            is SearchConnectionFragment -> SEARCH_FRAGMENT_TAG
            is EnterNameFragment -> {
                fragment.enterListener = ::onUsernameEnter
                ENTER_NAME_FRAGMENT_TAG
            }
            else -> null
        }
    }

    override fun showAskNameWindow(currentName: String) {
        if (currentFragmentTag != ENTER_NAME_FRAGMENT_TAG) {
            replaceContent(
                    EnterNameFragment()
                            .apply {
                                enterListener = ::onUsernameEnter
                                this@apply.currentName = currentName
                            }
                    , ENTER_NAME_FRAGMENT_TAG
            )
        }
    }

    private fun onUsernameEnter(username: String) = getPresenter().onUsernameEnter(username)

    override fun showSearch() {
        if (currentFragmentTag != SEARCH_FRAGMENT_TAG) {
            replaceContent(SearchConnectionFragment(), SEARCH_FRAGMENT_TAG)
        }
    }

    override fun showSuccess() {

    }

    override fun showConnectionError() {
    }

    private fun replaceContent(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit()
    }
}
