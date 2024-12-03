package com.devian.adventofcode.`2023`.days

import com.devian.adventofcode.AdventOfCodeTest
import kotlin.math.roundToLong

class Day06 : AdventOfCodeTest(6) {

    override fun part1() {
//        val product = getWinnersProduct(buildRaces1(TEST_INPUT))
        val product = getWinnersProduct(buildRaces1(input))
        println("PRODUCT: $product")
    }

    override fun part2() {
//        val product = getWinnersProduct(buildRaces2(TEST_INPUT))
        val product = getWinnersProduct(buildRaces2(input))
        println("PRODUCT: $product")
    }

    private fun getWinnersProduct(races: List<Race>): Long {
        var product = 0L

        races.forEach { race ->
            var waysToWin = 0L
            val isOdd = race.timeInMillis % 2 > 0
            val start = (race.timeInMillis / 2.0).roundToLong()

            if (race.checkTime(start)) waysToWin += if (isOdd) 2 else 1
            for (i in start + 1 until race.timeInMillis) {
                if (!race.checkTime(i)) break
                waysToWin += 2
            }

            if (waysToWin > 0) {
                if (product == 0L) {
                    product = waysToWin
                } else {
                    product *= waysToWin
                }
            }
        }

        return product
    }

    private fun buildRaces1(input: List<String>): List<Race> =
        input.map {
            it.split(Regex(" +")).mapNotNull { s -> s.toLongOrNull() }
        }.let { (times, distance) ->
            times.mapIndexed { i, t ->
                Race(t, distance[i])
            }
        }

    private fun buildRaces2(input: List<String>) =
        input.map {
            it.filterNot { c -> c.isWhitespace() }
                .split(':')[1]
                .toLong()
        }.let { (time, distance) ->
            listOf(Race(time, distance))
        }

    data class Race(
        val timeInMillis: Long,
        val recordInMillimeters: Long,
    ) {
        fun checkTime(time: Long): Boolean = time * (timeInMillis - time) > recordInMillimeters
    }

    companion object {
        private val TEST_INPUT = """
            Time:      7  15   30
            Distance:  9  40  200
        """.trimIndent().split('\n').map { it.trim() }
    }
}