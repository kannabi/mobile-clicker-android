package com.awsm_guys.mobileclicker.connection.view


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.awsm_guys.mobileclicker.R
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers

import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_connection.*
import java.util.concurrent.TimeUnit

class ConnectionFragment : Fragment() {

    private val compositeDisposable = CompositeDisposable()
    lateinit var enterListener: (String) -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_connection, container, false)
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable.add(
            RxView.clicks(enter_button)
                    .throttleWithTimeout(300, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { name_edittext.text.toString() }
                    .subscribe(enterListener::invoke)
        )

        name_edittext.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                with(name_edittext.text) {
                    val isAppropriateLength = (length in 3..10)

                    if (!isAppropriateLength) { showError() }

                    enter_button.isEnabled = isAppropriateLength
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    fun showError(){
        error_text.let {
            YoYo.with(Techniques.FadeIn)
                    .duration(200)
                    .playOn(it)
        }
    }
}
