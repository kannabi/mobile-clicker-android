package com.awsm_guys.mobileclicker.clicker.model.controller

import com.awsm_guys.mobileclicker.clicker.model.controller.lan.LanDesktopControllerFactory

class DesktopControllerFactoryGenerator {
    fun generate(tag: String): DesktopControllerFactory? =
            when(tag){
                LAN_TAG -> LanDesktopControllerFactory()
                else -> null
            }
}