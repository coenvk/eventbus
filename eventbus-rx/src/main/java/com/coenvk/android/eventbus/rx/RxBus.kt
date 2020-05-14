package com.coenvk.android.eventbus.rx

import com.coenvk.android.eventbus.Bus
import com.coenvk.android.eventbus.BusEvent
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.Subject
import java.util.concurrent.ConcurrentHashMap

class RxBus<E : BusEvent<*>>(
    subject: Subject<E>,
    private val schedulerProvider: SchedulerProvider = DefaultSchedulerProvider
) : Bus<E>() {

    private val serializedSubject = subject.toSerialized()

    private var subscribers: ConcurrentHashMap<Class<*>, Disposable> = ConcurrentHashMap()

    override fun <T : E> subscribe(eventType: Class<T>, consumer: (event: T) -> Unit) {
        subscribers[eventType]?.dispose()
        subscribers[eventType] = serializedSubject.filter { eventType.isInstance(it) }
            .observeOn(schedulerProvider.main)
            .subscribe { Consumer<T> { consumer(it) } }
    }

    override fun <T : E> unsubscribe(eventType: Class<T>) {
        subscribers.remove(eventType)?.dispose()
    }

    override fun unsubscribeAll() {
        subscribers.forEach { it.value.dispose() }
        subscribers.clear()
    }

    override fun publish(event: E) {
        serializedSubject.onNext(event)
    }

}