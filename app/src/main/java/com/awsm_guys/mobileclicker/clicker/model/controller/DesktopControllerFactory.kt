package com.awsm_guys.mobileclicker.clicker.model.controller

import com.awsm_guys.mobileclicker.primitivestore.PrimitiveStore

abstract class DesktopControllerFactory {
    abstract fun create(primitiveStore: PrimitiveStore): DesktopController
}