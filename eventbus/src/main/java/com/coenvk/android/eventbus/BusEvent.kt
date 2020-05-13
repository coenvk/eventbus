package com.coenvk.android.eventbus

abstract class BusEvent<out D>(val payload: D? = null)