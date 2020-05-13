package com.coenvk.android.eventbus.coroutines

import com.coenvk.android.eventbus.Bus
import com.coenvk.android.eventbus.BusEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consume
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext

class KtBus<E : BusEvent<*>> @ExperimentalCoroutinesApi constructor(
    private val channel: BroadcastChannel<E> = BroadcastChannel(1),
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider
) :
    Bus<E>(), CoroutineScope {

    override val coroutineContext: CoroutineContext = dispatcherProvider.io + SupervisorJob()

    private val subscribers: ConcurrentHashMap<Class<*>, ReceiveChannel<E>> = ConcurrentHashMap()

    @Suppress("UNCHECKED_CAST")
    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    override fun <T : E> subscribe(eventType: Class<T>, consumer: (event: T) -> Unit) {
        launch {
//            channel.consume {
//                (consumeAsFlow().filter { it as? T != null } as Flow<T>).collect { consumer(it) }
//            }
            channel.consume {
                subscribers[eventType]?.cancel()
                subscribers[eventType] = this
                for (event in this) {
                    if (eventType.isInstance(event)) {
                        withContext(dispatcherProvider.main) { consumer(event as T) }
                    }
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    override fun <T : E> unsubscribe(eventType: Class<T>) {
        subscribers[eventType]?.cancel()
    }

    override fun unsubscribeAll() {
        subscribers.forEach { it.value.cancel() }
    }

    @ExperimentalCoroutinesApi
    override fun publish(event: E) {
        launch {
            channel.send(event)
        }
    }

}