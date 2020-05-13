package com.coenvk.android.eventbus.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatcherProvider {

    val io: CoroutineDispatcher
        get() = Dispatchers.IO

    val main: CoroutineDispatcher
        get() = Dispatchers.Main

    val ui: CoroutineDispatcher
        get() = Dispatchers.Main

}

internal object DefaultDispatcherProvider : DispatcherProvider