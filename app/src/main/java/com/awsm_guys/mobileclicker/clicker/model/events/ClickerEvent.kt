package com.awsm_guys.mobileclicker.clicker.model.events

sealed class ClickerEvent

data class ConnectionOpen(
        val currentPage: Int,
        val maxPage: Int
): ClickerEvent()

class ConnectionClose: ClickerEvent()

class ClickerBroken: ClickerEvent()

data class PageSwitch(val page: Int): ClickerEvent()