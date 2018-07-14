package com.awsm_guys.mobileclicker.clicker.model.controller.lan

import com.awsm_guys.mobileclicker.clicker.model.controller.DesktopController
import com.awsm_guys.mobileclicker.clicker.model.controller.DesktopControllerFactory
import com.awsm_guys.mobileclicker.primitivestore.PrimitiveStore

class LanDesktopControllerFactory(
        val desktopIp: String? = null,
        val desktopPort: Int? = null,
        val sessionId: String? = null,
        val maxPage: Int? = null
) : DesktopControllerFactory() {


    override fun create(primitiveStore: PrimitiveStore): DesktopController =
            LanDesktopController(
                    desktopIp, desktopPort, sessionId, primitiveStore, maxPage
            )

    override fun toString(): String {
        return "LanDesktopControllerFactory(desktopIp=$desktopIp, desktopPort=$desktopPort, sessionId=$sessionId, maxPage=$maxPage)"
    }


}