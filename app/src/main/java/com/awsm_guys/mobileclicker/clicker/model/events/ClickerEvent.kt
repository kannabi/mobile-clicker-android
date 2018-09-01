package com.awsm_guys.mobileclicker.clicker.model.events

import com.awsm_guys.mobileclicker.clicker.model.controller.poko.Meta

sealed class ClickerEvent

class ConnectionOpen: ClickerEvent()

class ConnectionClose: ClickerEvent()

class ClickerBroken: ClickerEvent()

data class PageSwitch(val page: Int): ClickerEvent()

data class MetaUpdate(val meta: Meta): ClickerEvent()