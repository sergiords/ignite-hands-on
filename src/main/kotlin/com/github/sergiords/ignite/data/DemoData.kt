@file: JvmName("DemoData")
@file:Suppress("unused")

package com.github.sergiords.ignite.data

import java.io.Serializable
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.format.DateTimeFormatter.ISO_DATE
import java.time.format.DateTimeFormatter.ISO_TIME

data class Station(val code: String) : Serializable {

    override fun toString() = code

}

data class Train(val origin: Station, val destination: Station, val date: LocalDate, val time: LocalTime) : Serializable {

    override fun toString() = "/$origin/$destination/${date.format(ISO_DATE)}/${time.format(ISO_TIME)}"

}

data class Price(val seats: Int, val value: Int) : Serializable {

    override fun toString() = "$seats => $value€"

}

class TrainServer : Serializable {

    fun dates() = dates

    fun stations() = stations

    fun trains(origin: Station, destination: Station, date: LocalDate) =
        if (origin != destination && origin in stations && destination in stations && date in dates) {
            times.map({ time -> Train(origin, destination, date, time) })
        } else {
            emptyList()
        }

    fun prices(train: Train) = prices.map { (seats, value) -> Price(train.seats(seats), train.pricing(value)) }

}

fun Station.isIn(stationPrefix: String) = code.startsWith(stationPrefix)

fun Train.pricing(base: Int) =
    base + when {
        origin.isIn("FR") -> 75
        else -> 50
    } + when {
        date.isOnHolidays() -> 42
        else -> 0
    } + when {
        date.isOnWeekEnd() -> 31
        else -> ((date.dayOfWeek.value * date.dayOfWeek.value) % 6) * 5
    } + when {
        time.isOnPeak() -> 15
        else -> (time.hour * time.hour / 8) - (3 * time.hour) + 18
    }

fun Train.seats(base: Int) = when (base) {

    cheapPrice.seats -> when {
        date.isOnHolidays() || date.isOnWeekEnd() || time.isOnPeak() -> 0
        !date.isOnHolidays() && !date.isOnWeekEnd() && !time.isOnPeak() -> 2
        else -> base
    }

    standardPrice.seats -> when {
        date.isOnWeekEnd() || time.isOnPeak() -> base
        !date.isOnWeekEnd() && !time.isOnPeak() -> base + 2
        else -> base
    }

    flexiblePrice.seats -> when {
        date.isOnHolidays() || date.isOnWeekEnd() -> base + 2
        !date.isOnHolidays() && !date.isOnWeekEnd() -> base
        else -> base
    }

    businessPrice.seats -> when {
        date.isOnHolidays() || date.isOnWeekEnd() || time.isOnPeak() -> base + 5
        !date.isOnHolidays() && !date.isOnWeekEnd() && !time.isOnPeak() -> base + 2
        else -> base
    }

    else -> base
}

val summerMonths = listOf(Month.JUNE, Month.JULY, Month.AUGUST)
fun LocalDate.isOnSummerHolidays() = month in summerMonths

val winterMonths = listOf(Month.DECEMBER, Month.JANUARY, Month.FEBRUARY)
fun LocalDate.isOnWinterHolidays() = month in winterMonths

fun LocalDate.isOnHolidays() = isOnWinterHolidays() || isOnSummerHolidays()

val weekendDays = listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
fun LocalDate.isOnWeekEnd() = dayOfWeek in weekendDays

fun LocalTime.isOnMorningPeak() = isAfter(LocalTime.of(7, 30)) && isBefore(LocalTime.of(9, 30))
fun LocalTime.isOnAfternoonPeak() = isAfter(LocalTime.of(17, 0)) && isBefore(LocalTime.of(19, 0))
fun LocalTime.isOnPeak() = isOnMorningPeak() || isOnAfternoonPeak()

val frStations = listOf("FRDIJ", "FRBRE", "FRBDX", "FRLIL", "FRPAR", "FRMRS").map(::Station)
val gbStations = listOf("GBLON", "GBOXF", "GBCAM", "GBMAN", "GBLIV", "GBGLA").map(::Station)
val stations = listOf(frStations, gbStations).flatMap { it.toList() }

val dates = (LocalDate.of(3001, 1, 1)..LocalDate.of(3001, 12, 31)).toList()
val times = (LocalTime.of(6, 0)..LocalTime.of(23, 0)).toList()

val cheapPrice = Price(0, 10) // make this one hard to get
val standardPrice = Price(1, 20)
val flexiblePrice = Price(2, 50)
val businessPrice = Price(5, 100)
val prices = listOf(cheapPrice, standardPrice, flexiblePrice, businessPrice)
