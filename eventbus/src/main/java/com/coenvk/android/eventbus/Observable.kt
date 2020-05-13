package com.coenvk.android.eventbus

interface Observable<E : BusEvent<*>> {

    fun subscribe(consumer: (event: E) -> Unit)

}