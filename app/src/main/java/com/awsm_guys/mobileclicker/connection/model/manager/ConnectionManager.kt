package com.awsm_guys.mobileclicker.connection.model.manager

import com.awsm_guys.mobileclicker.clicker.model.controller.DesktopControllerFactory
import io.reactivex.Observable

interface ConnectionManager {

    fun startListening(): Observable<DesktopControllerFactory>

    fun stopListening()
}