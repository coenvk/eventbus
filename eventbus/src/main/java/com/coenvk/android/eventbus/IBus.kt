package com.coenvk.android.eventbus

internal interface IBus<E : BusEvent<*>> {

    fun <T : E> subscribe(eventType: Class<T>, consumer: (event: T) -> Unit)

    fun <T : E> unsubscribe(eventType: Class<T>)

    fun unsubscribeAll()

    fun publish(event: E)

}