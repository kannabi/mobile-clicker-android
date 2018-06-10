package com.awsm_guys.mobileclicker

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.awsm_guys.mobileclicker.udp.BroadcastSender
import io.reactivex.disposables.CompositeDisposable



class MainActivity : AppCompatActivity() {
    private val compositeDisposable by lazy { CompositeDisposable() }
    private val broadcastSender = BroadcastSender()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        broadcastSender.runBroadcast()
    }

    override fun onStop() {
        super.onStop()
        broadcastSender.onDestroy()
        compositeDisposable.clear()
    }
}
