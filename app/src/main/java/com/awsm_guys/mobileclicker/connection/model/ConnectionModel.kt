package com.awsm_guys.mobileclicker.connection.model

import com.awsm_guys.mobileclicker.connection.IConnectionModel
import com.awsm_guys.mobileclicker.primitivestore.PrimitiveStore

class ConnectionModel(
        private var primitiveStore: PrimitiveStore
) : IConnectionModel {
}