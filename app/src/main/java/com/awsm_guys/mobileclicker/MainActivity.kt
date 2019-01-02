package com.awsm_guys.mobileclicker

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.KeyEvent
import com.awsm_guys.mobileclicker.clicker.view.ClickerFragment
import com.awsm_guys.mobileclicker.clicker.view.VolumeButtonBroadcastDelegate
import com.awsm_guys.mobileclicker.connection.view.ConnectionActivity
import com.kannabi.simplelifecycleapilibrary.lifecycleapi.activity.ComponentStoreActivity
import io.reactivex.Observable

class MainActivity :
    ComponentStoreActivity(),
    VolumeButtonBroadcastDelegate.VolumeButtonsObservable {

    private val volumeButtonBroadcastDelegate = VolumeButtonBroadcastDelegate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            placeFragment(ClickerFragment())
        }
    }

    override fun onBackPressed() {
        startActivity(
            Intent(this, ConnectionActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    private fun placeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()
    }

    override fun getObservable(): Observable<Int> =
        volumeButtonBroadcastDelegate.getPressObservable()

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean =
        volumeButtonBroadcastDelegate.onKeyPressed(keyCode) || super.onKeyDown(keyCode, event)

    override fun onDestroy() {
        volumeButtonBroadcastDelegate.onDestroy()
        super.onDestroy()
    }
}
