package com.github.sergiords.ignite.data

import java.time.LocalDate
import java.time.LocalTime

internal abstract class AbstractIterator<T : Comparable<T>>(private var next: T, private val endInclusive: T) : Iterator<T> {

    private var hasNext = true

    abstract fun T.step(): T

    override fun hasNext() = hasNext && next <= endInclusive

    override fun next(): T {
        val current = next
        next = next.step()
        hasNext = (current != endInclusive)
        return current
    }

}

operator fun LocalDate.rangeTo(end: LocalDate) = object : Iterable<LocalDate> {
    override fun iterator() = object : AbstractIterator<LocalDate>(this@rangeTo, end) {
        override fun LocalDate.step() = plusDays(1)
    }
}

operator fun LocalTime.rangeTo(end: LocalTime) = object : Iterable<LocalTime> {
    override fun iterator() = object : AbstractIterator<LocalTime>(this@rangeTo, end) {
        override fun LocalTime.step() = plusHours(1)
    }
}
