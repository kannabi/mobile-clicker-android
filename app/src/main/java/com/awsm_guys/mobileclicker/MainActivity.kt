package com.awsm_guys.mobileclicker

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import com.awsm_guys.mobileclicker.clicker.view.ClickerFragment
import com.awsm_guys.mobileclicker.connection.view.ConnectionActivity
import com.kannabi.simplelifecycleapilibrary.lifecycleapi.activity.ComponentStoreActivity

class MainActivity: ComponentStoreActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        placeFragment(ClickerFragment())
    }

    override fun onBackPressed() {
        startActivity(
                Intent(this, ConnectionActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    private fun placeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit()
    }
}
