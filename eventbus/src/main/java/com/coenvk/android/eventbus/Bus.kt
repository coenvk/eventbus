package com.coenvk.android.eventbus

abstract class Bus<E : BusEvent<*>> : IBus<E> {

    inline fun <reified T : E> subscribe(noinline consumer: (event: T) -> Unit) {
        subscribe(T::class.java, consumer)
    }

    inline fun <reified T : E> unsubscribe() {
        unsubscribe(T::class.java)
    }

}