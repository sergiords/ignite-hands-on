@file: JvmName("AffinityData")

package com.github.sergiords.ignite.data

import java.io.Serializable

data class Team(val name: String, val country: String) : Serializable {

    override fun toString() = "$name from $country"

}

data class User(val name: String, val team: Team, val commits: Int) : Serializable {

    override fun toString() = "$name with $commits commits from $team"

}

fun teams() = (1..20).map { id -> Team("Team$id", countries[id % 4]) }

fun users(team: Team) = (1..100).map { id -> User("User$id", team, id.derivedValue()) }

/**
 * @return a pseudo-random-repeatable-easily-generated value for this int value
 */
fun Int.derivedValue() = (this * this) % 1234

/*
 * ==== Hard Coded Data ====
 */

val countries = listOf("France", "Great Britain", "Belgium", "Netherlands")
