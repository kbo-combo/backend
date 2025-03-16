package com.example.kbocombo.mock.event

import org.springframework.context.ApplicationEvent
import org.springframework.context.PayloadApplicationEvent
import org.springframework.context.event.EventListener
import org.springframework.test.context.event.ApplicationEvents
import java.util.concurrent.CopyOnWriteArrayList
import java.util.stream.Stream

class CustomDefaultApplicationEvents : ApplicationEvents {

    private val events: MutableList<ApplicationEvent> = CopyOnWriteArrayList()

    @EventListener
    fun onApplicationEvent(event: ApplicationEvent) {
        events.add(event)
    }

    override fun stream(): Stream<ApplicationEvent> {
        return events.stream()
    }

    override fun <T> stream(type: Class<T>): Stream<T> {
        val var10000: Stream<Any> = events.stream().map { source: ApplicationEvent -> this.unwrapPayloadEvent(source) }
        return var10000.filter { obj: Any? -> type.isInstance(obj) }.map { obj: Any? -> type.cast(obj) }
    }

    override fun clear() {
        events.clear()
    }

    private fun unwrapPayloadEvent(source: Any): Any {
        return if (source is PayloadApplicationEvent<*>) {
            source.payload
        } else {
            source
        }
    }
}
