package com.awsm_guys.mobileclicker.connection.view


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.awsm_guys.mobileclicker.R
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_enter_name.*
import java.util.concurrent.TimeUnit

class EnterNameFragment : Fragment() {

    companion object {
        const val EDIT_TEXT_FOCUS_STATE = "EDIT_TEXT_FOCUS_STATE"
        const val CURRENT_NAME = "CURRENT_NAME"
    }

    private val compositeDisposable = CompositeDisposable()
    var enterListener: (String) -> Unit = { }
    var currentName: String = ""
    set(value) {
        usernameEditText?.setText(value)
        usernameEditText?.setSelection(value.length)
        field = value
    }

    private var usernameEditText: EditText? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_enter_name, container, false)
                    .also {
                        val focusCandidateId =
                                if (savedInstanceState?.getBoolean(EDIT_TEXT_FOCUS_STATE) == true) {
                                    R.id.name_edittext
                                } else {
                                    R.id.focus_workaround
                                }
                        it.findViewById<View>(focusCandidateId).requestFocus()
                        usernameEditText = it.findViewById(R.id.name_edittext)
                        currentName = savedInstanceState?.getString(CURRENT_NAME) ?: currentName
                    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(EDIT_TEXT_FOCUS_STATE, usernameEditText?.hasFocus() ?: false)
        outState.putString(CURRENT_NAME, currentName)
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

        if (currentName.isNotBlank()) {
            enter_button.isEnabled = true
        }

        name_edittext.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                with(name_edittext.text.toString().trim()) {
                    val isAppropriateLength = (length in 3..10)

                    if (!isAppropriateLength) {
                        showError()
                    } else {
                        hideError()
                    }

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

    fun showError() {
        error_text.let {
            it.visibility = View.VISIBLE
            YoYo.with(Techniques.FadeIn)
                    .duration(200)
                    .playOn(it)
        }
    }

    fun hideError() {
        error_text.let {
            YoYo.with(Techniques.FadeOut)
                    .duration(200)
                    .onEnd { _ -> it.visibility = View.INVISIBLE }
                    .playOn(it)
        }
    }
}
