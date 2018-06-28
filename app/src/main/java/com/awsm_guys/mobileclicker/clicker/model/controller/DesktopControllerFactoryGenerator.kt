package com.awsm_guys.mobileclicker.clicker.model.controller

import com.awsm_guys.mobileclicker.clicker.model.controller.lan.LanDesktopController
import com.awsm_guys.mobileclicker.primitivestore.PrimitiveStore

class DesktopControllerFactoryGenerator {
    fun generate(tag: String): DesktopControllerFactory? =
            when(tag){
                LAN_TAG -> object : DesktopControllerFactory() {
                    override fun create(primitiveStore: PrimitiveStore, sessionId: String): DesktopController =
                        LanDesktopController(primitiveStore = primitiveStore, sessionId = sessionId)
                }
                else -> null
            }
}