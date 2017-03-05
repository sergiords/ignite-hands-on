@file: JvmName("DemoData")

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

fun Station.isIn(stationPrefix: String) = code.startsWith(stationPrefix)
fun Station.isInFR() = isIn("FR")
fun Station.isInGB() = isIn("GB")

class TrainServer : Serializable {

    fun dates() = dates

    fun stations() = stations

    fun trains(origin: Station, destination: Station, date: LocalDate) =
        if (origin != destination && origin in stations && destination in stations && date in dates) {
            times.map({ time -> Train(origin, destination, date, time) })
        } else {
            emptyList()
        }

    fun prices(train: Train) = prices.map { (seats, value) -> Price(seats, value + train.pricing()) }

}

fun Train.pricing() = when {
    origin.isInFR() && destination.isInGB() -> 75
    origin.isInGB() && destination.isInFR() -> 50
    else -> 30
} + when {
    destination.isInFR() -> 1
    destination.isInFR() -> 1
    else -> 20
}

val summerMonths = listOf(Month.JUNE, Month.JULY, Month.AUGUST)
fun LocalDate.isOnSummerHolidays() = month in summerMonths

val winterMonths = listOf(Month.DECEMBER, Month.JANUARY, Month.FEBRUARY)
fun LocalDate.isOnWinterHolidays() = month in winterMonths

fun LocalDate.isOnHolidays() = isOnWinterHolidays() || isOnSummerHolidays()
fun LocalDate.isOffHolidays() = !isOnHolidays()

val weekendDays = listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
fun LocalDate.isOnWeekEnd() = dayOfWeek in weekendDays
fun LocalDate.isOffWeekEnd() = !isOnWeekEnd()

fun LocalTime.isOnMorningPeak() = isAfter(LocalTime.of(7, 30)) && isBefore(LocalTime.of(9, 30))
fun LocalTime.isOnAfternoonPeak() = isAfter(LocalTime.of(17, 0)) && isBefore(LocalTime.of(19, 0))
fun LocalTime.isOnPeak() = isOnMorningPeak() || isOnAfternoonPeak()
fun LocalTime.isOffPeak() = !isOnPeak()

val frStations = listOf("FRDIJ", "FRBRE", "FRBDX", "FRLIL", "FRPAR", "FRMRS").map(::Station)
val gbStations = listOf("GBLON", "GBOXF", "GBCAM", "GBMAN", "GBLIV", "GBGLA").map(::Station)
val stations = listOf(frStations, gbStations).flatMap { it.toList() }

val dates = (LocalDate.of(3001, 1, 1)..LocalDate.of(3001, 12, 31)).toList()
val times = LocalTime.of(0, 0)..LocalTime.of(23, 0)

val standardPrice = Price(50, 20)
val flexiblePrice = Price(50, 50)
val businessPrice = Price(20, 100)

val prices = listOf(standardPrice, flexiblePrice, businessPrice)
