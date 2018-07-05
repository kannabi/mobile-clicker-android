package com.awsm_guys.mobileclicker.clicker.model.controller.lan

import com.awsm_guys.mobileclicker.clicker.model.controller.DesktopController
import com.awsm_guys.mobileclicker.clicker.model.controller.DesktopControllerFactory
import com.awsm_guys.mobileclicker.primitivestore.PrimitiveStore

class LanDesktopControllerFactory(
        private val desktopIp: String? = null,
        private val desktopPort: Int? = null,
        private val sessionId: String? = null
) : DesktopControllerFactory() {
    override fun create(primitiveStore: PrimitiveStore): DesktopController =
            LanDesktopController(
                    desktopIp, desktopPort, sessionId, primitiveStore
            )
}